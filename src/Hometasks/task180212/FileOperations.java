package Hometasks.task180212;

import java.io.*;
import java.nio.file.*;
//import java.nio.file.attribute.*;

import static Hometasks.task180212.MyConsole.*;

public class FileOperations {

    static final String FS = File.separator;

    // Java <7
/*
   static boolean delete1(File file){
        if (!file.exists()){
            System.out.println("There isn't such file or folder to rename");
            return false;
        }

        boolean success =

   }
*/
    static Path normalizePath(Path current, String testPath, Console c) {

        File path;
        Path out = null;
        boolean error = false; // use flag to organise only one exit from the function
        String errorMessage = NL;

        if (testPath.length() == 0) {
            error = true;
            errorMessage += "Your path is empty";
        }

        if (!error && (testPath.charAt(0) == '"' || testPath.charAt(testPath.length()-1) == '"')) {
            if (testPath.length() < 3) {
                error = true;
                errorMessage += "Your path is empty or invalid";
            } else {
                testPath = testPath.substring(1,testPath.length()-1);
            }
        }

        if (!error) {
            if (testPath.charAt(0) == '.') {
                path = new File(current.toString() + FS + testPath);
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
                errorMessage += "It's impossible to parse your path! It's invalid" +  NL + e.toString();
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

// Java 7
    //static boolean rename2()
/*        try {
            FileWriter out = new FileWriter(newFile, true);

        } catch (IOException e) {
            System.out.println(e);
            return false;
        }*/

}
