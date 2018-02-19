package Hometasks.task180212;

import java.util.*;
import java.io.*;

public class CopyMedia {

    public static void main(String[] args) {

        File folder;
        //folder = new File("C:\\Users\\user\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Media Cache");
        // For MozillaFirefox
        folder = new File("C:\\Users\\user\\AppData\\Local\\Mozilla\\Firefox\\Profiles\\anemv05s.default\\cache2\\entries");

        long minSize = 500;
        if (args.length > 0) {
            folder = new File(args[0]);
            if (!folder.isDirectory()) {
                System.out.println("Input correct path!");
                return;
            }
        }
        if (args.length > 1) {
            try {
                minSize = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        ArrayList<File> files = new ArrayList<>();

        try{
            files = listMP3(folder, minSize);
        } catch (NullPointerException e){
            System.out.println("Cash folder was empty!");
            e.printStackTrace();
        }

            for (File tempFile : files) {
                FileOperations.copyFile1(tempFile, new File("D:\\"));
                String name = tempFile.getName();
                if (name.length() < 5 || !name.substring(name.length()-4).equals(".mp3")) {
                    FileOperations.rename1(tempFile, tempFile.getName() + ".mp3");
                }
            }
    }

    private static ArrayList<File> listMP3(File folder, long minSize) throws NullPointerException {

        ArrayList<File> files = new ArrayList<>();

        for (File tempFile : folder.listFiles() ) {
            if (!tempFile.isDirectory()) {

                long tempSize = tempFile.length() / 1024;
                if (tempSize < minSize){
                    continue;
                }

                InputStream is = null;
                DataInputStream dis = null;

                try {

                    is = new FileInputStream(tempFile.getPath());
                    dis = new DataInputStream(is);

                    byte[] bs = new byte[3];

                    if (is.available()>2) {
                        int success = dis.read(bs);

                        if (success < 3) {
                            System.out.println("Something was wrong. It's impossible to read "+tempFile.getName()+" file!");
                            continue;
                        }

                        // According to signatures of different types of mp3 files from Wiki
                        if ((char)bs[0] == 0xFF || ((char)bs[0] == 'I' && (char)bs[1] == 'D' && (char)bs[2] == '3')){
                            files.add(tempFile);
                        }
                    }
                } catch(IOException e) {

                    // if any I/O error occurs
                    e.printStackTrace();
                } finally {

                    // releases any associated system files with this stream
                    try {
                        if (is != null)
                            is.close();
                        if (dis != null)
                            dis.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return files;
    }
}

