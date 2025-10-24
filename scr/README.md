# Java Notes App

A **text-based Notes Manager** in Java using **File I/O** and **OOP principles**. This app allows users to create, read, edit, delete, search, and save notes with file persistence, including load/save functionality using zip files.

---

## **Project Structure**

NotesApp/
│
├── src/
│   └── app/
│       ├── NotesApp.java         ← main program + menu
│       ├── NoteManager.java      ← create, read, edit, delete, search
│       ├── FileUtils.java        ← reading/writing individual notes + logging
│       ├── NoteFileUtils.java    ← load/save all notes from/to zip
│       ├── ValidationUtils.java  ← filename validation
│       └── Constants.java        ← constants (folder names, sentinel)
│
├── Notes/                        ← folder for individual note files
└── errors.log                    ← logs any exceptions


---

## **Features**

- **Create Notes**: Supports multi-line notes with paragraphs.
- **Read Notes**: Display note content in console.
- **Edit Notes**:
  - Overwrite existing notes
  - Append to existing notes while viewing current content
  - Rename notes
- **Delete Notes**: Remove unwanted notes.
- **Search Notes**: Search notes by title keywords.
- **Load Notes**: Load notes from a `.zip` file with option to merge or replace.
- **Save Notes**: Save all notes in `Notes/` folder into a `.zip` file with merge or overwrite options.
- **File Persistence**: Notes are saved as `.txt` files inside the `Notes/` folder.
- **Error Handling**: Exceptions logged to `errors.log` with stack trace.

---

## **OOP Concepts Used**

- **Classes**:  
  - `NotesApp` – main program and menu  
  - `NoteManager` – encapsulates CRUD and search functionality  
  - `FileUtils` – handles single file read/write and exception logging  
  - `NoteFileUtils` – handles batch save/load of notes (zip-based)  
  - `ValidationUtils` – validates and sanitizes filenames  
  - `Constants` – stores app-wide constants  

- **Encapsulation**: Private methods for internal file operations.  
- **Abstraction**: Separate classes for note operations, file utilities, and validation.  
- **Exception Handling**: `try-with-resources`, checked and unchecked exceptions, logging with stack trace.  
- **File I/O**: `FileReader`, `BufferedReader`, `FileWriter`, `BufferedWriter`, `ZipInputStream`, `ZipOutputStream`.  

---

## **Usage**

1. **Run the app**
```bash
javac src/app/*.java
java -cp src app.NotesApp

## **Menu Option**

1. Create Note
2. Read Note
3. Edit Note
4. Delete Note
5. Search Note
6. Load Notes from File
7. Save Notes & Exit

## **Create/Edit Notes**

 - Type multiple lines.
 - Finish input with :wq to save.
 - Append mode shows existing content for reference.
 - Load/Save
 - Load/Save notes as .zip files.
 - Optionally merge with existing notes or replace all.


## **Sample Output**

=== NOTES APP ===
1. Create Note
2. Read Note
3. Edit Note
4. Delete Note
5. Search Note
6. Load Notes from File
7. Save Notes & Exit
Choice: 1

Enter note title: My First Note
Enter content (type ':wq' to save):
This is my first note.
It has multiple lines.
:wq
✅ Note saved.

Choice: 3
Select number: 1
1. Overwrite
2. Append
3. Rename
2
--- Existing content ---
This is my first note.
It has multiple lines.
--- Add new content below ---
Enter content (type ':wq' to save):
Adding a new paragraph.
:wq
✅ Note updated.

 ## **Notes**

 - All notes are stored in Notes/ folder.
 - Exceptions are logged in errors.log.
 - Filenames are sanitized to avoid invalid characters.