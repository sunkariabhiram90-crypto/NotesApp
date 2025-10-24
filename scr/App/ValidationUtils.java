package scr.App;

public class ValidationUtils {

    public static boolean isValidTitle(String title) {
        if (title == null || title.trim().isEmpty()) return false;
        String illegal = "[\\\\/:*?\"<>|]";
        return !title.matches(".*" + illegal + ".*");
    }

    public static String sanitizeFileName(String raw) {
        if (raw == null) return "untitled";
        return raw.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
    }

    public static String stripTxtExtension(String filename) {
        if (filename == null) return "";
        return filename.replaceFirst("(?i)\\.txt$", "");
    }
}
