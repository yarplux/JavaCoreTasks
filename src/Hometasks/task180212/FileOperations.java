package Hometasks.task180212;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FileUtils;


import static java.nio.file.FileVisitResult.*;
import static Hometasks.task180212.MyConsole.*;

public class FileOperations {

    static final String FS = File.separator;
    static final int TABULATION = 1;

    enum OPERATIONS {PRINT, COPY, MOVE, REMOVE}

    enum ATTRIBUTES {LIST, TABLE, TREE, PRINT, FORCE}

    // Set fields to table type of print files
    private enum TABLE_FIELDS {
        NAME, TYPE, SIZE, CREATED
    }

    private static class ProcessFiles extends SimpleFileVisitor<Path> {

        // PREPARATION fields and methods ______________________________________________________________________________

        private List<TABLE_FIELDS> FIELDS = clearFieldsToDefault();              // Set settings for printing as a table
        private OPERATIONS currentOperation;
        private EnumSet<ATTRIBUTES> currentAttributes;
        private Path currentDestination;
        private Path currentRoot;
        private Console currentConsole;

        static ArrayList<TABLE_FIELDS> clearFieldsToDefault() {
            ArrayList<TABLE_FIELDS> out = new ArrayList<>();
            out.add(TABLE_FIELDS.NAME);
            out.add(TABLE_FIELDS.TYPE);
            return out;
        }

        String printFields() {
            return Arrays.toString(FIELDS.toArray());
        }

        void addField(TABLE_FIELDS field) {
            FIELDS.add(field);
        }

        void deleteField(TABLE_FIELDS field) {
            FIELDS.remove(field);
            if (FIELDS.size() < 1) {
                currentConsole.printf(NL+"You can't remove last field from table view!"+NL+NL+"(It was restored to default)");
            }
        }


        static ProcessFiles Create(OPERATIONS operation, EnumSet<ATTRIBUTES> attributes, Console console, String root) {
            int restriction = 0;
            if (attributes.contains(ATTRIBUTES.LIST)) restriction++;
            if (attributes.contains(ATTRIBUTES.TABLE)) restriction++;
            if (attributes.contains(ATTRIBUTES.TREE)) restriction++;

            if (restriction > 1) {
                console.printf(NL + "You can use only one type of printing!");
                return null;
            }

            Path testPath = normalizePath(null, root, console);
            if (testPath == null) return null;


            return new ProcessFiles(operation, attributes, console, testPath);
        }

        static ProcessFiles Create(OPERATIONS operation, EnumSet<ATTRIBUTES> attributes, Console console, String root, String destination) {
            ProcessFiles out = Create(operation, attributes, console, root);
            if (out == null) return null;

            out.currentDestination = normalizePath(null, destination, console);
            if (out.currentDestination == null) return null;

            return out;
        }

        private ProcessFiles(OPERATIONS operation, EnumSet<ATTRIBUTES> attributes, Console console, Path root) {
            currentOperation = operation;
            currentAttributes = EnumSet.copyOf(attributes);
            currentConsole = console;
            currentRoot = root;
        }


        // OPERATION fields and methods ________________________________________________________________________________

        private ArrayList<FileEntry> entriesInFolder = new ArrayList<>();
        private class FileEntry {
            private String FileName;
            private String Type;
            private String Size;
            private String Created;

            FileEntry(Path file, BasicFileAttributes attr) {
                FileName = file.getFileName().toString();

                if (attr.isDirectory()) {
                    Type = "Folder";
                }
                else if (attr.isRegularFile()) {
                    Type = "Regular File";
                }
                else if (attr.isSymbolicLink()) {
                    Type = "Symbolic link";
                }
                else if (attr.isOther()) {
                    Type = "Other";
                }
                else {
                    Type = "What is it?";
                }

                Size = (attr.isDirectory())?"-":FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file.toFile()));

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Created = format.format(new Date(attr.creationTime().toMillis()));
            }

            @Override
            public String toString() {
                String out="";

                if (currentAttributes.contains(ATTRIBUTES.TABLE)) {
                    String viewedName = (FileName.length() > 20) ? FileName.substring(0, 20) : FileName;
                    if (FIELDS.contains(TABLE_FIELDS.NAME)) out = out + String.format("%1$-20s", viewedName) + "\t";
                    if (FIELDS.contains(TABLE_FIELDS.TYPE)) out = out + String.format("%1$-13s", Type) + "\t";
                    if (FIELDS.contains(TABLE_FIELDS.SIZE)) out = out + String.format("%1$-12s", Size) + "\t";
                    if (FIELDS.contains(TABLE_FIELDS.CREATED)) out = out + Created + "\t";

                    if (FIELDS.contains(TABLE_FIELDS.NAME) && FileName.length() > 20) {
                        String tail = FileName.substring(20);
                        while (tail.length() > 20) {
                            out += tail.substring(0, 20);
                            tail = tail.substring(20);
                        }
                        out += tail;
                    }
                }
                else {
                    out += FileName;
                }
                return out;
            }
        }


        String depthSpace = "";

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
            if (currentOperation == OPERATIONS.PRINT) {
                if (currentAttributes.contains(ATTRIBUTES.TABLE)) {
                    entriesInFolder.add(new FileEntry(file, attr));
                }
                else if (currentAttributes.contains(ATTRIBUTES.LIST)) {
                    currentConsole.printf(file.getFileName().toString()+" ");
                }
                else if (currentAttributes.contains((ATTRIBUTES.TREE))) {
                    if (!attr.isDirectory()) {
                        currentConsole.printf(depthSpace + file.getFileName().toString() + NL);
                    }
                }
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) {
            if (currentAttributes.contains(ATTRIBUTES.TREE)) {
                currentConsole.printf(depthSpace + dir.getFileName().toString()+NL);
                String space = new String(new char[TABULATION]).replace('\0', ' ');
                depthSpace += "|"+space+"> ";
            }
            else if (!dir.equals(currentRoot)) {
                    if (!currentAttributes.contains(ATTRIBUTES.FORCE)) {
                        return SKIP_SUBTREE;
                    }
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            if (currentOperation == OPERATIONS.PRINT) {
                if (currentAttributes.contains(ATTRIBUTES.TABLE)) {
                    for (FileEntry current : entriesInFolder) {
                        currentConsole.printf(NL+current.toString());
                    }
                } else if (currentAttributes.contains(ATTRIBUTES.TREE)) {
                    depthSpace = depthSpace.substring(0,depthSpace.length()-2);
                    depthSpace = depthSpace.substring(0,depthSpace.length()-TABULATION);
                }
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            currentConsole.printf(NL+"Cannot Access: "+exc.toString());
            return TERMINATE;
        }
    }

    static void PrintFiles(String path, Console c, ATTRIBUTES view) {

        Path tmpPath = normalizePath(null, path, c);
        if (tmpPath == null) return;

        if (c == null) {
            System.out.println("ERROR. Cannot find the console!");
            return;
        }

        if (view != ATTRIBUTES.LIST && view != ATTRIBUTES.TABLE && view != ATTRIBUTES.TREE) {
            c.printf("Invalid type of view. Use LIST/TABLE/TREE attributes only.");
        }

        ProcessFiles processFilesObject = ProcessFiles.Create(OPERATIONS.PRINT, EnumSet.of(view), c, tmpPath.toString());
        if (processFilesObject == null) return;

        processFilesObject.addField(TABLE_FIELDS.SIZE);
        processFilesObject.addField(TABLE_FIELDS.CREATED);

        try {
            if (view == ATTRIBUTES.TREE) {
                Files.walkFileTree(tmpPath, processFilesObject);
            } else {
                Files.walkFileTree(tmpPath, EnumSet.noneOf(FileVisitOption.class), 1, processFilesObject);
            }
        } catch (IOException e) {
            c.printf(NL + "There is a problem with this path: " + NL + e.getMessage());
        }
    }

    static Path normalizePath(Path current, String testPath, Console c) {

        File path;
        Path out = null;
        boolean error = false; // use flag to organise only one exit from the function
        String errorMessage = NL;

        if (testPath.length() == 0) {
            error = true;
            errorMessage += "Your path is empty";
        }

        if (!error && (testPath.charAt(0) == '"' || testPath.charAt(testPath.length() - 1) == '"')) {
            if (testPath.length() < 3) {
                error = true;
                errorMessage += "Your path is empty or invalid";
            } else {
                testPath = testPath.substring(1, testPath.length() - 1);
            }
        }

        if (!error) {
            if (testPath.charAt(0) == '.') {
                if (current == null) {
                    path = new File(testPath);
                } else {
                    path = new File(current.toString() + FS + testPath);
                }
            } else {
                // In another case .getCannonicalPath return the path to default user folder
                if (testPath.toLowerCase().equals(System.getenv("SystemDrive").toLowerCase())) {
                    testPath += FS;
                }
                path = new File(testPath);
            }
            try {
                path = new File(path.getCanonicalPath());
                if (path.isDirectory()) {
                    out = path.toPath();
                } else {
                    out = null;
                    error = true;
                    errorMessage += "There isn't such path";
                }
            } catch (IOException e) {
                error = true;
                errorMessage += "It's impossible to parse your path! It's invalid" + NL + e.toString();
            }
        }

        if (error) {
            if (c == null) {
                System.out.println(errorMessage);
            } else {
                c.printf(errorMessage);
            }
        }
        return out;
    }

    static boolean rename1(File oldFile, String name) {
        if (!oldFile.exists()) {
            System.out.println("There isn't such file or folder to rename");
            return false;
        }
        String newName = oldFile.getPath().substring(0, oldFile.getPath().length() - oldFile.getName().length()) + name;
        File newFile = new File(newName);
        if (newFile.exists()) {
            System.out.println("Impossible operation. File or folder '" + newName + "' already exists!");
            return false;
        }

        boolean success = oldFile.renameTo(newFile);
        if (success) {
            System.out.println("File: " + oldFile.getName() + "\nWas renamed to: " + name + "\nSuccessfully");
        } else {
            System.out.println("Impossible operation. File " + oldFile.getName() + " wasn't renamed");
            return false;
        }
        return true;
    }

    static boolean copyFile1(File file, File folder) {

        boolean success = false;
        if (!folder.isDirectory()) {
            System.out.println("There isn't such folder to copy");
            return false;
        }
        if (!file.exists() || file.isDirectory()) {
            System.out.println("There isn't such file to copy");
            return false;
        }

        File newFile = new File(folder + file.getName());

        try {
            success = newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (success) {
            System.out.println("File " + file.getName() + " copied to " + folder.getPath() + " successfully");
            return true;
        } else {
            System.out.println("It's impossible to create new '" + file.getName() + "' file there");
            return false;
        }

    }
}
