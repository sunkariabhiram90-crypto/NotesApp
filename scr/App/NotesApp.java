package scr.App;

import java.nio.file.Paths;
import java.util.Scanner;

public class NotesApp {
    private static final Scanner sc = new Scanner(System.in);
    private static final NoteManager manager = new NoteManager();

    public static void main(String[] args) {
        FileUtils.ensureNotesDirectory();

        while (true) {
            System.out.println("\n=== NOTES APP ===");
            System.out.println("1. Create Note");
            System.out.println("2. Read Note");
            System.out.println("3. Edit Note");
            System.out.println("4. Delete Note");
            System.out.println("5. Search Note");
            System.out.println("6. Load Notes from File");
            System.out.println("7. Save Notes & Exit");
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1": manager.createNote(); break;
                case "2": manager.readNote(); break;
                case "3": manager.editNote(); break;
                case "4": manager.deleteNote(); break;
                case "5": manager.searchNote(); break;
                case "6": loadNotes(); break;
                case "7": saveAndExit(); break;
                default: System.out.println("Invalid input.");
            }
        }
    }

    private static void loadNotes() {
        System.out.print("Enter filename to load: ");
        String file = sc.nextLine().trim();

        try {
            var loadedNotes = NoteFileUtils.loadNotesFromFile(file);
            System.out.print("Merge with current notes? (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();

            if (ans.equals("y")) {
                NoteFileUtils.mergeNotes(loadedNotes, Paths.get(Constants.NOTES_DIR));
                System.out.println("✅ Notes merged from " + file);
            } else {
                NoteFileUtils.replaceNotes(loadedNotes, Paths.get(Constants.NOTES_DIR));
                System.out.println("✅ Notes replaced with " + file);
            }
        } catch (Exception e) {
            System.out.println("❌ Error loading notes: " + e.getMessage());
            FileUtils.logException(e);
        }
    }

    private static void saveAndExit() {
        System.out.print("Enter filename to save: ");
        String file = sc.nextLine().trim();

        try {
            var f = new java.io.File(file);
            if (f.exists()) {
                System.out.print("File exists. Merge with previous data? (y/n): ");
                String ans = sc.nextLine().trim().toLowerCase();

                if (ans.equals("y")) {
                    var prevNotes = NoteFileUtils.loadNotesFromFile(file);
                    NoteFileUtils.mergeNotes(prevNotes, Paths.get(Constants.NOTES_DIR));
                    NoteFileUtils.saveNotesToFile(Paths.get(Constants.NOTES_DIR), file);
                    System.out.println("✅ Notes merged and saved to " + file);
                } else {
                    NoteFileUtils.saveNotesToFile(Paths.get(Constants.NOTES_DIR), file);
                    System.out.println("✅ Notes saved to " + file);
                }
            } else {
                NoteFileUtils.saveNotesToFile(Paths.get(Constants.NOTES_DIR), file);
                System.out.println("✅ Notes saved to " + file);
            }
        } catch (Exception e) {
            System.out.println("❌ Error saving notes: " + e.getMessage());
            FileUtils.logException(e);
        }

        System.out.println("Exiting...");
        System.exit(0);
    }
}
