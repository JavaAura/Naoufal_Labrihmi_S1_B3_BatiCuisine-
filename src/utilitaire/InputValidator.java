package utilitaire;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

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
                System.out.println(RED + "Entrée invalide. Veuillez entrer un nombre décimal." + RESET);
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
                System.out.println(RED + "Entrée invalide. Veuillez entrer 'true' ou 'false'." + RESET);
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
                System.out.println(RED + "Date invalide. Veuillez entrer une date au format 'yyyy-MM-dd'." + RESET);
            }
        }
    }

    public String getValidatedString(String fieldName) {
        String input;
        do {
            System.out.print("Entrez " + fieldName + " : ");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(RED + fieldName + " ne peut pas être vide." + RESET);
            }
        } while (input.isEmpty());
        return input;
    }

    public String getValidatedString(String fieldName, List<String> allowedValues) {
        String input;
        do {
            System.out.print("Entrez " + fieldName + " : ");
            input = scanner.nextLine().trim().toUpperCase();
            if (!allowedValues.contains(input)) {
                System.out.println(
                        RED + "Valeur invalide pour " + fieldName + ". Valeurs acceptées : " + allowedValues + RESET);
            }
        } while (!allowedValues.contains(input));
        return input;
    }

    public LocalDate promptValidDate(String message, boolean allowEmpty) {
        while (true) {
            String dateStr = promptString(message);
            if (allowEmpty && dateStr.isEmpty()) {
                logger.info(GREEN + "No date provided, returning null." + RESET);
                return null;
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
        scanner.nextLine();
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
        return name != null && name.matches("[a-zA-Z\\s]+");
    }

    public Long promptLong(String message) {
        System.out.print(message);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid long value: ");
            }
        }
    }

    public boolean isValidPhoneNumber(String phone) {
        String regex = "^\\+?[0-9]{10,15}$"; // Example: allows optional '+' followed by 10 to 15 digits
        return phone.matches(regex);
    }

    public String promptValidPhoneNumber(String message) {
        String phone;
        while (true) {
            phone = promptString(message);
            if (isValidPhoneNumber(phone)) {
                return phone; // Return the valid phone number
            } else {
                System.out.println(RED + "Numéro de téléphone invalide. Veuillez entrer un numéro valide." + RESET);
            }
        }
    }

    public boolean promptProfessionalStatus(String message) {
        while (true) {
            int input = promptInt(message);
            if (input == 1) {
                return true; // Professional
            } else if (input == 2) {
                return false; // Not professional
            } else {
                System.out.println(RED + "Entrée invalide. Veuillez entrer 1 pour Oui ou 2 pour Non." + RESET);
            }
        }
    }

}
