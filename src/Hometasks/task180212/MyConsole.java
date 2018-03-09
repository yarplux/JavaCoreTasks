package Hometasks.task180212;

import java.io.Console;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import Hometasks.task180126.GeksField;

import static Hometasks.task180212.FileOperations.*;

public class MyConsole {

    static final String NL = System.lineSeparator();

    /*
    HashMap - define commands, their attributes and help for them.
    "key" - commands
    "value" - HashMap of attributes and their description

    TO DO: reorganize it to an xml file
     */

    private static final Map<String, Map<String, String>> COMMANDS = new HashMap<>();

    static {
        String[] tempArrayOfCommands = {"help", "cls", "exit", "dir", "cd", "createGeksField", "printGeksFiels", "findMinPath"};
        String[][] tempArrayOfAttributes = {
                {"", "[command] - Print all commands or help about [command]"},
                {"", "- Clear screen of MyConsole"},
                {"", "- Exit from this program"},
                {"", "[attributes] - Print files/folders from the current directory" +
                        NL + NL + "   (by default as list of names (without any differences between files and folders)" +
                        "-t", "- as a table name, type, size, creation time",
                        "-r", "- as tree of files (only names)"+
                        NL + NL + "   (Do not use -t and -r at the same time!)",
                        "-p [path]", "- print files/folders from the path"},
                {"", "[path] - Change directory (supports absolute and relative paths)" +
                        NL + NL + "   You can use \" \" if the path contains space or any other special characters" +
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
                        NL + NL + "   x2, y2 - coordinates of the final cell of the path"}

        };


        for (int i = 0; i < tempArrayOfAttributes.length; i++) {
            COMMANDS.put(tempArrayOfCommands[i], new HashMap<String, String>());
            for (int j = 0; j < tempArrayOfAttributes[i].length - 1; j += 2) {
                COMMANDS.get(tempArrayOfCommands[i]).put(tempArrayOfAttributes[i][j], tempArrayOfAttributes[i][j + 1]);
            }
        }
    }

    private static final String WELCOME_TEXT = "" +
            NL + "Welcome to the MyConsole program!" + NL + NL;

    static void clrscr() {
        if (System.getProperty("os.name").contains("Windows")) {
            ProcessBuilder cls = new ProcessBuilder("cmd", "/c", "cls");
            cls.inheritIO();

            try {
                cls.start().waitFor();
            } catch (InterruptedException | IOException e) {
                System.out.println(NL + NL + "You have a problem with clear screen on your Windows system!" + NL);
                e.printStackTrace();
                System.out.println(NL);
            }
        } else {
            try {
                Runtime.getRuntime().exec("clear");
            } catch (IOException e) {
                System.out.println(NL + NL + "You have a problem with clear screen on your non-Windows system!" + NL);
                e.printStackTrace();
                System.out.println(NL);
            }
        }
    }

    public static void main(String[] args) {

        Console c = System.console();
        if (c == null) {
            System.out.println("Can't get access to the system console" + NL + "Finishing...");
            return;
        }

        clrscr();
        c.printf(WELCOME_TEXT);
        c.printf("Commands: " + NL);
        for (Map.Entry<String, Map<String, String>> entry : COMMANDS.entrySet()) {
            c.printf(entry.getKey() + " ");
        }

        Path tempPath = Paths.get(System.getProperty("user.dir"));
        while (true) {

            c.flush();
            String command = c.readLine(NL + NL + tempPath.toString() + " > ");
            String[] tokens = command.split("[ ]+");

            if (tokens.length > 0 && COMMANDS.containsKey(tokens[0])) {

                // parse help __________________________________________________________________________________________
                if (tokens[0].equals("help")) {
                    if (tokens.length == 1) {
                        for (Map.Entry<String, Map<String, String>> commandEntry : COMMANDS.entrySet()) {
                            for (Map.Entry<String, String> attributeEntry : commandEntry.getValue().entrySet()) {
                                if (attributeEntry.getKey().equals("")) {
                                    c.printf(NL + NL + commandEntry.getKey() + " " + attributeEntry.getValue());
                                } else {
                                    c.printf(NL + NL + "   " + attributeEntry.getKey() + " " + attributeEntry.getValue());
                                }
                            }
                        }
                    } else if (COMMANDS.containsKey(tokens[1])) {
                        for (Map.Entry<String, String> attributeEntry : COMMANDS.get(tokens[1]).entrySet()) {
                            if (attributeEntry.getKey().equals("")) {
                                c.printf(NL + NL + tokens[1] + " " + attributeEntry.getValue());
                            } else {
                                c.printf(NL + NL + "   " + attributeEntry.getKey() + " " + attributeEntry.getValue());
                            }
                        }
                    } else {
                        c.printf(NL + "Use help with commands: " + NL);
                        for (Map.Entry<String, Map<String, String>> entry : COMMANDS.entrySet()) {
                            c.printf(entry.getKey() + " ");
                        }
                    }

                    // parse cls __________________________________________________________________________________________
                } else if (tokens[0].equals("cls")) {
                    clrscr();
                    c.printf(NL + WELCOME_TEXT);

                    // parse exit __________________________________________________________________________________________
                } else if (tokens[0].equals("exit")) {
                    break;

                    // parse cd ____________________________________________________________________________________________
                } else if (tokens[0].equals("cd")) {
                    Path testPath;
                    if (tokens.length > 1) {
                        testPath = normalizePath(tempPath, command.substring(command.indexOf(tokens[1])), c);
                        if (testPath != null) {
                            tempPath = testPath;
                        }
                    }
                } else if (tokens[0].equals("dir")) {
                    List<String> tmpTokens = Arrays.asList(tokens);
                    ATTRIBUTES view = ATTRIBUTES.LIST;

                    if (tmpTokens.contains("-r")) view = ATTRIBUTES.TREE;
                    if (tmpTokens.contains("-t")) view = ATTRIBUTES.TABLE;

                    if (!tmpTokens.contains("-p")) {
                        PrintFiles(tempPath.toString(), c, view);
                    } else {
                        Integer index = tmpTokens.indexOf("-p");
                        if (index < tmpTokens.size()-1) {
                            PrintFiles(tmpTokens.get(index+1), c, view);
                        } else {
                            c.printf("Try to add your path after -p");
                        }
                    }
                }
                // parse Geks functions ________________________________________________________________________________



            } else {
                c.printf(NL + "Unknown command! Commands: " + NL);
                for (Map.Entry<String, Map<String, String>> entry : COMMANDS.entrySet()) {
                    c.printf(entry.getKey() + " ");
                }
            }
        }
        System.out.println(NL + NL + "Finishing...");
    }
}
