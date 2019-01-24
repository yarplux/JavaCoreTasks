package hometasks.task180212;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;

import static hometasks.task180212.FileOperations.*;
import static hometasks.task180212.MyConsole.*;

public class ArchOperations {

    private static final int BUFFER_SIZE = 1024;
    private static final Charset winCharset = Charset.forName("CP866");

    static boolean zipCompress(String sourceFolder, String archiveName, String target) {

        boolean error = false;
        Path sourcePath = normalizePath(null, sourceFolder, false);
        if (sourcePath == null)
            error = true;

        File archive = new File(sourceFolder + File.separator + archiveName);

        if (!error && Files.exists(archive.toPath()) && !Files.notExists(archive.toPath())) {
            System.out.println("There is such archive in this folder!");
            if (!answer()) {
                return false;
            }
        } else {
            try {
                if (!archive.createNewFile()) {
                    System.out.println("Program can't create such file");
                    error = true;
                }
            } catch (IOException e) {
                System.out.println("Program can't create such file");
                error = true;
            }
        }

        Path targetPath = normalizePath(null, target, false);
        if (targetPath == null)
            error = true;

        if (!error && !Files.exists(targetPath)) {
            System.out.println("No such file to archive!");
            error = true;
        }

        if (!error) {
            try {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archive));
                doZip(targetPath.toFile(), out);

            } catch (IOException e) {
                System.out.println("Program can't write to created archive");
                error = true;
            } catch (NullPointerException e) {
                System.out.println("Something is wrong with this archiving file/folder structure!" + NL + e.getMessage());
                error = true;
            }
        }

        if (error) {
            remove(archive.toString(),true,false);
            return error;
        }

        return true;
    }

    private static void doZip(File dir, ZipOutputStream out) throws IOException, NullPointerException {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                if (f.list().length == 0) {
                    out.putNextEntry(new ZipEntry(f.getPath() + File.separator));
                }
                doZip(f, out);
            } else {
                out.putNextEntry(new ZipEntry(f.getPath()));
                FileInputStream tmpInputStream = new FileInputStream(f);
                byte[] buffer = new byte[BUFFER_SIZE];
                int len;
                while ((len = tmpInputStream.read(buffer)) >= 0)
                    out.write(buffer, 0, len);
                tmpInputStream.close();
            }
        }
    }

    static boolean zipDecompress(String sourceArchive, String target) {
        File sourceFile = new File(sourceArchive);
        if (!sourceFile.exists() || !sourceFile.canRead()) {
            System.out.println("File cannot be read");
            return false;
        }

        Path targetPath = normalizePath(null, target, false);
        if (targetPath == null) return false;
        if (!targetPath.toFile().exists()) {
            if (!targetPath.toFile().mkdir()) {
                System.out.println("Program can't create folder to this archive");
                return false;
            }
        } else {
            //TO DO обработать ситуацию
            System.out.println("There is such folder.");
        }

        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(sourceArchive), winCharset);
            ZipEntry entry;

            while ((entry = zip.getNextEntry()) != null) {
                File newFile = new File(target + File.separator + entry.getName());
                System.out.println("Unzipping to " + newFile.getAbsolutePath());

                if (entry.isDirectory()) {
                    if (!new File(newFile.getAbsolutePath()).mkdirs()) {
                        System.out.println("Something is wrong! Program can't create " + newFile.getAbsolutePath() + " directory");
                        zip.closeEntry();
                        zip.close();
                        return false;
                    }
                } else {
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((len = zip.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    zip.closeEntry();
                }
            }
            zip.close();
        } catch (IOException e) {
            System.out.println("Program can't write to the target folder");
            e.printStackTrace();
        }
        return true;
    }
}
