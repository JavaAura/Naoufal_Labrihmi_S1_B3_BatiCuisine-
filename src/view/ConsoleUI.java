package view;

import java.util.Scanner;

import controller.ClientController;
import controller.ComposantController;
import controller.DevisController;
import controller.ProjetController;
import entity.Client;
import service.impl.MainOeuvreServiceImpl;
import service.impl.MaterielServiceImpl;
import service.impl.ProjetServiceImpl;

public class ConsoleUI {
    private final ProjetController projetController;
    private final DevisController devisController;
    private final ClientController clientController; // Changed from static to instance
    private final Scanner scanner;

    public ConsoleUI(ProjetController projetController, DevisController devisController,
            ClientController clientController) {
        this.projetController = projetController;
        this.devisController = devisController;
        this.clientController = clientController; // Use the injected ClientController
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===");
        boolean running = true;

        while (running) {
            afficherMenuPrincipal();
            int choix = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choix) {
                case 1:
                    createProject();
                    break;
                case 2:
                    // afficherProjetsExistants();
                    break;
                case 3:
                    // calculerCoutProjet();
                    break;
                case 4:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void afficherMenuPrincipal() {
        System.out.println("=== Menu Principal ===");
        System.out.println("1. Créer un nouveau projet");
        System.out.println("2. Afficher les projets existants");
        System.out.println("3. Calculer le coût d'un projet");
        System.out.println("4. Quitter");
        System.out.print("Choisissez une option : ");
    }

    private void createProject() {
        System.out.println("--- Recherche de client ---");
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        System.out.print("Choisissez une option : ");
        int clientOption = Integer.parseInt(scanner.nextLine());

        Client client = null;

        if (clientOption == 1) {
            // Search for existing client
            System.out.println("--- Recherche de client existant ---");
            System.out.print("Entrez le nom du client : ");
            String clientName = scanner.nextLine();
            client = clientController.getClientByName(clientName);

            if (client != null) {
                System.out.println("Client trouvé !");
                System.out.println("Nom : " + client.getNom());
                System.out.println("Adresse : " + client.getAdresse());
                System.out.println("Numéro de téléphone : " + client.getTelephone());
                System.out.print("Souhaitez-vous continuer avec ce client ? (y/n) : ");
                String continueWithClient = scanner.nextLine();
                if (!continueWithClient.equalsIgnoreCase("y")) {
                    return;
                }
            } else {
                System.out.println("Client non trouvé.");
                return;
            }
        } else if (clientOption == 2) {
            // Add a new client
            System.out.println("--- Ajouter un nouveau client ---");
            System.out.print("Entrez le nom du client : ");
            String clientName = scanner.nextLine();
            System.out.print("Entrez l'adresse du client : ");
            String clientAddress = scanner.nextLine();
            System.out.print("Entrez le numéro de téléphone du client : ");
            String clientPhone = scanner.nextLine();
            System.out.print("Le client est-il un professionnel ? (true/false) : ");
            boolean isProfessional = Boolean.parseBoolean(scanner.nextLine());

            clientController.registerClient(clientName, clientAddress, clientPhone, isProfessional);
            client = clientController.getClientByName(clientName);
        }

        // if (client != null) {
        // // Create a new project with the selected or newly created client
        // System.out.println("--- Création d'un Nouveau Projet ---");
        // System.out.print("Entrez le nom du projet : ");
        // String projectName = scanner.nextLine();
        // System.out.print("Entrez la surface de la cuisine (en m²) : ");
        // double surface = Double.parseDouble(scanner.nextLine());

        // System.out.print("Entrez la marge bénéficiaire pour le projet : ");
        // double profitMargin = Double.parseDouble(scanner.nextLine());

        // // Create the project using the project controller
        // projetController.createProject(projectName, profitMargin, client.getNom(),
        // client.getAdresse(),
        // client.getTelephone(), client.isEstProfessionnel());

        // // After creating the project, add materials
        // System.out.println("--- Ajout des matériaux ---");
        // System.out.print("Entrez le nom du matériau : ");
        // String materialName = scanner.nextLine();
        // System.out.print("Entrez la quantité de ce matériau (en m²) : ");
        // double quantity = Double.parseDouble(scanner.nextLine());
        // System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
        // double unitCost = Double.parseDouble(scanner.nextLine());
        // System.out.print("Entrez le coût de transport de ce matériau (€) : ");
        // double transportCost = Double.parseDouble(scanner.nextLine());

        // // Add material to the project
        // ComposantController composantController = new ComposantController(new
        // MaterielServiceImpl(),
        // new MainOeuvreServiceImpl(), new ProjetServiceImpl());
        // composantController.addMateriel(materialName, unitCost, quantity, "Matériau",
        // 0.2, transportCost, 1.0, 1L);
        // } else {
        // System.out.println("Erreur lors de la création du projet.");
        // }
    }
}