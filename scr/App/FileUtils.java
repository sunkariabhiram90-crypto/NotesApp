package scr.App;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileUtils {

    public static void ensureNotesDirectory() {
        Path dir = Paths.get(Constants.NOTES_DIR);
        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
                System.out.println("Created notes directory: " + Constants.NOTES_DIR);
            } catch (IOException e) {
                logException(e);
            }
        }
    }

    public static void writeToFile(Path path, String content, boolean append) throws IOException {
        try (FileWriter fw = new FileWriter(path.toFile(), append);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(content);
        }
    }

    public static String readFile(Path path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (FileReader fr = new FileReader(path.toFile());
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static void logException(Exception e) {
        e.printStackTrace();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        try (FileWriter fw = new FileWriter(Constants.ERROR_LOG, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {

            pw.println("=== " + timestamp + " ===");
            pw.println("Exception: " + e);
            e.printStackTrace(pw);
            pw.println();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Path> listNotes() throws IOException {
        List<Path> notes = new ArrayList<>();
        Path dir = Paths.get(Constants.NOTES_DIR);
        if (Files.exists(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
                for (Path file : stream) notes.add(file);
            }
        }
        notes.sort(Comparator.comparing(p -> p.getFileName().toString().toLowerCase()));
        return notes;
    }
}
