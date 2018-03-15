package Hometasks.task180212;

import java.io.Console;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import Hometasks.task180126.GeksField;

import static Hometasks.task180212.FileOperations.*;
import static Hometasks.task180212.ArchOperations.*;

public class MyConsole {

    static final String NL = System.lineSeparator();
    static final Console c = System.console();

    /*
    HashMap - define commands, their attributes and help for them.
    "key" - commands
    "value" - HashMap of attributes and their description
     */

    private static final Map<String, Map<String, String>> COMMANDS = new TreeMap<>();

    static {
        String[] tempArrayOfCommands = {"help", "cls", "exit", "dir", "cd", "createGeksField", "printGeksFiels", "findMinPath", "zip", "unzip"};
        String[][] tempArrayOfAttributes = {
                {"", "[command] - Print all commands or help about [command]"},
                {"", "- Clear screen of MyConsole"},
                {"", "- Exit from this program"},
                {"", "[-r/-t] [-p <path>]- Print files/folders from the current directory" +
                        NL + NL + "   (by default as list of names (without any differences between files and folders)",
                        "-t", "- as a table name, type, size, creation time",
                        "-r", "- as tree of files (only names)" +
                        NL + NL + "   (Do not use -t and -r at the same time!)",
                        "-p", "[path] - print files/folders from the path"},
                {"", "[path] - Change directory (supports absolute and relative paths)",
                        "path", "- You can use \" \" if the path contains space or any other special characters" +
                        NL + NL + "   Start absolute path from '<DiskName>:' on Windows +" +
                        NL + NL + "   (or with '/' on other systems)" +
                        NL + NL + "   Start relative path from '.' or '..'"},
                {"", "<height> <width> - create the structure of geksagonal cells with such parameters"},
                {"", "- print created structure of geksagonal cells (if it was created)" +
                        NL + NL + "   EXPERIMENTAL FUNCTION - works well with small fields"},
                {"", "<x1> <y1> <x2> <y2> - find and print minimum path in created structure of geksagonal cells" +
                        NL + NL + "   (if it was created)" +
                        NL + NL + "   x - 'column' in the field, y - 'row' in the field" +
                        NL + NL + "   x1, y1 - coordinates of the beginning cell of the path" +
                        NL + NL + "   x2, y2 - coordinates of the final cell of the path"},
                {"", "-f <filename of archive> -s <folder to archive>" +
                        NL + NL + "   creates zip archive with <filename of archive> in the current folder" +
                        NL + NL + "   do not use spaces in the filename!"},
                {"", "-a <filename of archive> -t <folder>" +
                        NL + NL + "   unzip <filename of archive> in the <folder> or in current folder" +
                        NL + NL + "   do not use spaces in the filename!"},

        };


        for (int i = 0; i < tempArrayOfAttributes.length; i++) {
            COMMANDS.put(tempArrayOfCommands[i], new HashMap<String, String>());
            for (int j = 0; j < tempArrayOfAttributes[i].length - 1; j += 2) {
                COMMANDS.get(tempArrayOfCommands[i]).put(tempArrayOfAttributes[i][j], tempArrayOfAttributes[i][j + 1]);
            }
        }
    }

    private static final String WELCOME_TEXT = "" + NL + "Welcome to the MyConsole program!" + NL + NL;

    private static void clrscr() {
        String error = "";
        if (System.getProperty("os.name").contains("Windows")) {
            ProcessBuilder cls = new ProcessBuilder("cmd", "/c", "cls");
            cls.inheritIO();
            try {
                cls.start().waitFor();
            } catch (InterruptedException | IOException e) {
                error += NL + NL + "You have a problem with clear screen on your Windows system!" + NL + e.getMessage() + NL;
            }
        } else {
            try {
                Runtime.getRuntime().exec("clear");
            } catch (IOException e) {
                error += NL + NL + "You have a problem with clear screen on your non-Windows system!" + NL + e.getMessage() + NL;
            }
        }
        System.out.println(error);
    }

    static void help(String command) {

        command = (COMMANDS.containsKey(command)) ? command : "";
        for (Map.Entry<String, Map<String, String>> commandEntry : COMMANDS.entrySet()) {
            if (command.equals("") || commandEntry.getKey().equals(command)) {
                for (Map.Entry<String, String> attributeEntry : commandEntry.getValue().entrySet()) {
                    if (attributeEntry.getKey().equals("")) {
                        c.printf(NL + NL + commandEntry.getKey() + " " + attributeEntry.getValue());
                    } else {
                        c.printf(NL + NL + "   " + attributeEntry.getKey() + " " + attributeEntry.getValue());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        if (c == null) {
            System.out.println("Can't get access to the system console" + NL + "Finishing...");
            return;
        }

        c.printf(WELCOME_TEXT);
        c.printf("Commands: " + NL);
        for (Map.Entry<String, Map<String, String>> entry : COMMANDS.entrySet()) System.out.println(entry.getKey());

        Path tempPath = Paths.get(System.getProperty("user.dir"));
        exit:
        while (true) {

            c.flush();
            String command = c.readLine(NL + NL + tempPath.toString() + " > ");
            List<String> tokens = Arrays.asList(command.split("[ ]+"));

            if (tokens.size() > 0 && COMMANDS.containsKey(tokens.get(0))) {
                Integer index;
                switch (tokens.get(0)) {
                    case "help":
                        if (tokens.size() == 1) {
                            command = "";
                        } else {
                            command = tokens.get(1);
                        }
                        help(command);
                        break;
                    case "cls":
                        clrscr();
                        System.out.println(WELCOME_TEXT);
                        break;
                    case "exit":
                        break exit;
                    case "cd":
                        if (tokens.size() > 1) {
                            Path testPath = normalizePath(tempPath, command.substring(command.indexOf(tokens.get(1))), true);
                            if (testPath != null) {
                                tempPath = testPath;
                            }
                        }
                        break;
                    case "dir":
                        ATTRIBUTES view = ATTRIBUTES.LIST;
                        if (tokens.contains("-r")) view = ATTRIBUTES.TREE;
                        if (tokens.contains("-t")) view = ATTRIBUTES.TABLE;
                        if (tokens.contains("-p")) {
                            index = tokens.indexOf("-p");
                            if (index < tokens.size() - 1) {
                                printFiles(command.substring(command.indexOf(tokens.get(index + 1))), c, view);
                            } else {
                                System.out.println("Try to add your path after -p");
                            }
                        } else {
                            printFiles(tempPath.toString(), c, view);
                        }
                        break;
                    case "zip": {
                        String archiveName = null;
                        if (tokens.contains("-f")) {
                            index = tokens.indexOf("-f");
                            if (index < tokens.size() - 1) {
                                archiveName = tokens.get(index + 1);
                            } else {
                                System.out.println("Try to add a filename after -f");
                                break;
                            }
                        }

                        index = tokens.indexOf("-p");
                        String target = null;
                        if (index < tokens.size() - 1) {
                            target = tokens.get(index + 1);
                        } else {
                            System.out.println("Try to add a path after -p");
                            break;
                        }
                        if (archiveName != null && target != null)
                            zipCompress(tempPath.toString(), archiveName, target);
                    }
                    break;
                    case "unzip": {
                        index = tokens.indexOf("-f");
                        String archiveName;
                        if (index < tokens.size() - 1) {
                            archiveName = tokens.get(index + 1);
                        } else {
                            System.out.println("Try to add a filename after -f");
                            break;
                        }

                        index = tokens.indexOf("-p");
                        String target;
                        if (index < tokens.size() - 1) {
                            target = tokens.get(index + 1);
                        } else {
                            System.out.println("Try to add a path after -p");
                            break;
                        }
                        zipCompress(tempPath.toString(), archiveName, target);
                    }
                }
            } else {
                System.out.println(NL + "Unknown command! Use help" + NL);
            }
        }
        System.out.println(NL + NL + "Finishing...");
    }
}
