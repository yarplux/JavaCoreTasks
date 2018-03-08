package Hometasks.task180212;

import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.*;

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
        String[] tempArrayOfCommands = {"help", "cls", "exit", "dir", "cd"};
        String[][] tempArrayOfAttributes = {
                {"", "[command] - Print all commands or help about [command]"},
                {"", "- Clear screen of MyConsole"},
                {"", "- Exit from this program"},
                {"", "-[attributes] - Print files/folders from the current directory",
                        "l", "- as list without attributes"},
                {"", "[path] - Change directory (supports absolute and relative paths)" +
                        NL + NL + "   You can use \" \" if the path contains space or any other special characters" +
                        NL + NL + "   Start absolute path from '<DiskName>:' on Windows +" +
                        NL + NL + "   (or with '/' on other systems)" +
                        NL + NL + "   Start relative path from '.' or '..'"},

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

                // parse cd __________________________________________________________________________________________
                } else if (tokens[0].equals("cd")) {
                    Path testPath;
                    if (tokens.length > 1) {
                        testPath = normalizePath(tempPath, command.substring(command.indexOf(tokens[1])), c);
                        if (testPath != null) {
                            tempPath = testPath;
                        }
                    }
                }
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