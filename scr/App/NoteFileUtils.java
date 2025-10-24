package scr.App;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class NoteFileUtils {

    public static void saveNotesToFile(Path notesDir, String zipFileName) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            if (Files.exists(notesDir)) {
                DirectoryStream<Path> stream = Files.newDirectoryStream(notesDir, "*.txt");
                for (Path file : stream) {
                    ZipEntry entry = new ZipEntry(file.getFileName().toString());
                    zos.putNextEntry(entry);
                    byte[] bytes = Files.readAllBytes(file);
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
            }
        }
    }

    public static List<Path> loadNotesFromFile(String zipFileName) throws IOException {
        List<Path> tempFiles = new ArrayList<>();
        Path tempDir = Files.createTempDirectory("notes_temp");

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFileName))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path tempFile = tempDir.resolve(entry.getName());
                Files.copy(zis, tempFile, StandardCopyOption.REPLACE_EXISTING);
                tempFiles.add(tempFile);
                zis.closeEntry();
            }
        }
        return tempFiles;
    }

    public static void mergeNotes(List<Path> loadedNotes, Path notesDir) throws IOException {
        if (!Files.exists(notesDir)) Files.createDirectories(notesDir);

        for (Path file : loadedNotes) {
            Path target = notesDir.resolve(file.getFileName());
            if (Files.exists(target)) {
                String name = file.getFileName().toString();
                String base = name.replaceFirst("(?i)\\.txt$", "");
                String ext = ".txt";
                int count = 1;
                while (Files.exists(target)) {
                    target = notesDir.resolve(base + "_" + count + ext);
                    count++;
                }
            }
            Files.copy(file, target);
        }
    }

    public static void replaceNotes(List<Path> loadedNotes, Path notesDir) throws IOException {
        if (Files.exists(notesDir)) {
            DirectoryStream<Path> stream = Files.newDirectoryStream(notesDir, "*.txt");
            for (Path file : stream) Files.delete(file);
        } else {
            Files.createDirectories(notesDir);
        }

        for (Path file : loadedNotes) {
            Path target = notesDir.resolve(file.getFileName());
            Files.copy(file, target);
        }
    }
}
