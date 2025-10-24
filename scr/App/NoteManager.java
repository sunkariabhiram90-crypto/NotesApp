package scr.App;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class NoteManager {
    private final Scanner scanner = new Scanner(System.in);

    public void createNote() {
        System.out.print("Enter note title: ");
        String title = scanner.nextLine().trim();
        if (!ValidationUtils.isValidTitle(title)) {
            System.out.println("Invalid title name.");
            return;
        }

        Path file = Paths.get(Constants.NOTES_DIR, ValidationUtils.sanitizeFileName(title) + ".txt");
        if (Files.exists(file)) {
            System.out.print("File exists. Overwrite? (y/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("y")) return;
        }

        System.out.println("Enter content (type '" + Constants.SAVE_SENTINEL + "' to save):");
        String content = readMultiline();

        try {
            FileUtils.writeToFile(file, content, false);
            System.out.println("✅ Note saved.");
        } catch (IOException e) {
            FileUtils.logException(e);
        }
    }

    public void readNote() {
        Path file = chooseNote();
        if (file == null) return;
        try {
            System.out.println("\n--- " + ValidationUtils.stripTxtExtension(file.getFileName().toString()) + " ---");
            System.out.println(FileUtils.readFile(file));
        } catch (IOException e) {
            FileUtils.logException(e);
        }
    }

    public void editNote() {
        Path file = chooseNote();
        if (file == null) return;

        System.out.println("1. Overwrite\n2. Append\n3. Rename");
        String opt = scanner.nextLine().trim();

        switch (opt) {
            case "1": editContent(file, false); break;
            case "2": editContent(file, true); break;
            case "3": renameNote(file); break;
            default: System.out.println("Invalid option.");
        }
    }

    private void editContent(Path file, boolean append) {
        try {
            if (append) {
                System.out.println("--- Existing content ---");
                String existing = FileUtils.readFile(file);
                System.out.println(existing);
                System.out.println("--- Add new content below ---");
            } else {
                System.out.println("--- Overwriting note ---");
            }

            System.out.println("Enter content (type '" + Constants.SAVE_SENTINEL + "' to save):");
            String text = readMultiline();

            FileUtils.writeToFile(file, text, append);
            System.out.println("✅ Note updated.");

        } catch (IOException e) {
            FileUtils.logException(e);
        }
    }

    private void renameNote(Path oldFile) {
        System.out.print("New title: ");
        String newTitle = scanner.nextLine().trim();
        if (!ValidationUtils.isValidTitle(newTitle)) {
            System.out.println("Invalid title.");
            return;
        }

        Path newFile = Paths.get(Constants.NOTES_DIR, ValidationUtils.sanitizeFileName(newTitle) + ".txt");
        try {
            Files.move(oldFile, newFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ Note renamed.");
        } catch (IOException e) {
            FileUtils.logException(e);
        }
    }

    public void deleteNote() {
        Path file = chooseNote();
        if (file == null) return;
        System.out.print("Delete this note? (y/N): ");
        if (!scanner.nextLine().equalsIgnoreCase("y")) return;
        try {
            Files.delete(file);
            System.out.println("✅ Note deleted.");
        } catch (IOException e) {
            FileUtils.logException(e);
        }
    }

    public void searchNote() {
        System.out.print("Enter keyword to search: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        try {
            List<Path> files = FileUtils.listNotes();
            List<Path> matches = new ArrayList<>();

            for (Path file : files) {
                String title = ValidationUtils.stripTxtExtension(file.getFileName().toString()).toLowerCase();
                if (title.contains(keyword)) matches.add(file);
            }

            if (matches.isEmpty()) {
                System.out.println("No notes found containing \"" + keyword + "\".");
                return;
            }

            System.out.println("Found notes:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.printf("%d. %s%n", i + 1,
                        ValidationUtils.stripTxtExtension(matches.get(i).getFileName().toString()));
            }

            System.out.print("Enter number to read or press Enter to cancel: ");
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                try {
                    int choice = Integer.parseInt(input);
                    if (choice >= 1 && choice <= matches.size()) {
                        Path selected = matches.get(choice - 1);
                        System.out.println(FileUtils.readFile(selected));
                    } else System.out.println("Invalid selection.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error searching notes.");
            FileUtils.logException(e);
        }
    }

    private Path chooseNote() {
        try {
            List<Path> files = FileUtils.listNotes();
            if (files.isEmpty()) {
                System.out.println("No notes found.");
                return null;
            }
            for (int i = 0; i < files.size(); i++)
                System.out.println((i + 1) + ". " + ValidationUtils.stripTxtExtension(files.get(i).getFileName().toString()));

            System.out.print("Select number: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return (choice >= 1 && choice <= files.size()) ? files.get(choice - 1) : null;
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return null;
        }
    }

    private String readMultiline() {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if (line.equals(Constants.SAVE_SENTINEL)) break;
            sb.append(line).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
