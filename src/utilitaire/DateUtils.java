package utilitaire;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Method to parse a date from a string
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            return null; // Invalid format
        }
    }

    // Method to format a LocalDate object into a string
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(FORMATTER) : null;
    }

    // Method to validate that a parsed date is within a reasonable range (e.g.,
    // between 1500 and a future limit, or just allowing future dates)
    public static boolean isDateValid(LocalDate date) {
        LocalDate earliestDate = LocalDate.of(1500, 1, 1);

        // Option 1: Allow any date in the future, just check for the earliestDate
        return date != null && (date.isEqual(earliestDate) || date.isAfter(earliestDate));

        // Option 2: Set a limit for future dates (e.g., 2100-12-31)
        // LocalDate latestDate = LocalDate.of(2100, 12, 31);
        // return date != null && (date.isEqual(earliestDate) ||
        // date.isAfter(earliestDate))
        // && (date.isEqual(latestDate) || date.isBefore(latestDate));
    }
}
