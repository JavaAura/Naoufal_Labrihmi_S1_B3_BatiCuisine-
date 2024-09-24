package utilitaire;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import entity.enums.EtatProjet;

public class InputValidator {

    private static final Logger logger = Logger.getLogger(InputValidator.class.getName());
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";

    private Scanner scanner;

    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public double promptDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre décimal.");
            }
        }
    }

    public boolean getBooleanInput(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.out.println("Entrée invalide. Veuillez entrer 'true' ou 'false'.");
            }
        }
    }

    public LocalDate getValidatedDate(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Date invalide. Veuillez entrer une date au format 'yyyy-MM-dd'.");
            }
        }
    }

    // General string validation
    public String getValidatedString(String fieldName) {
        String input;
        do {
            System.out.print("Entrez " + fieldName + " : ");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(fieldName + " ne peut pas être vide.");
            }
        } while (input.isEmpty());
        return input;
    }

    // Validation against a list of allowed values
    public String getValidatedString(String fieldName, List<String> allowedValues) {
        String input;
        do {
            System.out.print("Entrez " + fieldName + " : ");
            input = scanner.nextLine().trim().toUpperCase();
            if (!allowedValues.contains(input)) {
                System.out.println("Valeur invalide pour " + fieldName + ". Valeurs acceptées : " + allowedValues);
            }
        } while (!allowedValues.contains(input));
        return input;
    }

    public LocalDate promptValidDate(String message, boolean allowEmpty) {
        while (true) {
            String dateStr = promptString(message);
            if (allowEmpty && dateStr.isEmpty()) {
                logger.info(GREEN + "No date provided, returning null." + RESET);
                return null; // No date provided
            }
            LocalDate datePublication = DateUtils.parseDate(dateStr);
            if (datePublication != null && DateUtils.isDateValid(datePublication)) {
                logger.info(GREEN + "Valid date provided: " + DateUtils.formatDate(datePublication) + RESET);
                return datePublication;
            }
            logger.warning(RED + "Invalid date format or out of range. Prompting user again." + RESET);
            System.out.println(RED + "Invalid date. Please try again." + RESET);
        }
    }

    public int promptInt(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.println(RED + "Invalid input. Please enter a valid integer." + RESET);
            scanner.next();
            System.out.print(message);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return value;
    }

    public String promptValidName(String message) {
        while (true) {
            String name = promptString(message);
            if (isValidName(name)) {
                return name;
            }
            System.out.println(RED + "Invalid name. Please enter a name containing only letters and spaces." + RESET);
        }
    }

    private boolean isValidName(String name) {
        // Check if name contains only letters and spaces
        return name != null && name.matches("[a-zA-Z\\s]+");
    }
    //////////////////////////////////////////

    private String getStringInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    private double getDoubleInput(String prompt) {
        double value = 0;
        boolean valid = false;
        do {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                value = Double.parseDouble(input);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (!valid);
        return value;
    }

    private EtatProjet getEtatProjetInput(String prompt) {
        EtatProjet etat = null;
        do {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                etat = EtatProjet.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid state. Please enter one of the following: EN_COURS, TERMINE, ANNULE.");
            }
        } while (etat == null);
        return etat;
    }

}
