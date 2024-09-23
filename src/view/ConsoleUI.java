package view;

import java.time.LocalDate;
import java.util.Scanner;

import controller.ClientController;
import controller.ComposantController;
import controller.DevisController;
import controller.ProjetController;
import entity.Client;
import entity.Devis;
import entity.Projet;

public class ConsoleUI {
    private final ProjetController projetController;
    private final DevisController devisController;
    private final ClientController clientController;
    private final ComposantController composantController;
    private final Scanner scanner;

    public ConsoleUI(ProjetController projetController, DevisController devisController,
            ClientController clientController, ComposantController composant) {
        this.projetController = projetController;
        this.devisController = devisController;
        this.clientController = clientController;
        this.composantController = composant;
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
                    calculateProjectCost();
                    break;
                case 4:
                    changeDevisAcceptanceStatus();
                    break;
                case 5:
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
        System.out.println("4. Changer le statut d'acceptation d'un devis");
        System.out.println("5. Quitter");
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

        if (client != null) {
            System.out.println("--- Création d'un Nouveau Projet ---");
            System.out.print("Entrez le nom du projet : ");
            String projectName = scanner.nextLine();

            System.out.print("Entrez la surface de la cuisine (en m²) : ");
            double surface = Double.parseDouble(scanner.nextLine());

            // Create the project
            Projet newProject = projetController.createProject(
                    projectName,
                    0, // profit margin can be entered later if needed
                    client.getNom(),
                    client.getAdresse(),
                    client.getTelephone(),
                    client.isEstProfessionnel());

            Long projectId = newProject.getId();
            if (projectId != null) {
                System.out.println("Projet créé avec succès! ID du projet : " + projectId);
                addMaterials(projectId);
                addLabor(projectId);
                System.out.println("Tous les composants ont été ajoutés avec succès.");
            } else {
                System.out.println("Erreur lors de la création du projet. ID non généré.");
            }
        }
    }

    private void addMaterials(Long projectId) {
        System.out.println("--- Ajout des matériaux ---");
        boolean addAnotherMaterial = true;
        while (addAnotherMaterial) {
            System.out.print("Entrez le nom du matériau : ");
            String materialName = scanner.nextLine();

            System.out.print("Entrez la quantité de ce matériau (en m²) : ");
            double quantity = scanner.nextDouble();

            System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
            double unitCost = scanner.nextDouble();

            System.out.print("Entrez le coût de transport de ce matériau (€) : ");
            double transportCost = scanner.nextDouble();

            System.out.print("Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");
            double qualityCoefficient = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            composantController.addMateriel(materialName, unitCost, quantity, "Matériau", 0.2, transportCost,
                    qualityCoefficient, projectId);
            System.out.println("Matériau ajouté avec succès !");
            System.out.print("Voulez-vous ajouter un autre matériau ? (y/n) : ");
            String continueAdding = scanner.nextLine();
            addAnotherMaterial = continueAdding.equalsIgnoreCase("y");
        }
    }

    private void addLabor(Long projectId) {
        System.out.println("--- Ajout de la main-d'œuvre ---");
        boolean addAnotherLabor = true;
        while (addAnotherLabor) {
            System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
            String laborType = scanner.nextLine();

            System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
            double hourlyRate = scanner.nextDouble();

            System.out.print("Entrez le nombre d'heures travaillées : ");
            double hoursWorked = scanner.nextDouble();

            System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
            double productivityFactor = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            composantController.addMainOeuvre(laborType, hourlyRate, hoursWorked, productivityFactor, "Main-d'œuvre",
                    0.2, projectId);
            System.out.println("Main-d'œuvre ajoutée avec succès !");
            System.out.print("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ");
            String continueAddingLabor = scanner.nextLine();
            addAnotherLabor = continueAddingLabor.equalsIgnoreCase("y");
        }
    }

    private void calculateProjectCost() {
        System.out.println("--- Calcul du coût d'un projet ---");
        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();

        // Fetch the project by name
        Projet project = projetController.getProjetByName(projectName);

        if (project != null) {
            // If the project exists, calculate the total cost
            calculateTotalCost(project.getId());
        } else {
            System.out.println("Projet non trouvé avec le nom : " + projectName);
        }
    }

    private void calculateTotalCost(Long projectId) {
        System.out.println("--- Calcul du coût total ---");

        // Fetch the project object
        Projet project = projetController.getProjectById(projectId);
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }

        // Continue with VAT and margin input
        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        boolean applyVAT = scanner.nextLine().equalsIgnoreCase("y");
        double vatPercentage = 0;
        if (applyVAT) {
            System.out.print("Entrez le pourcentage de TVA (%) : ");
            vatPercentage = scanner.nextDouble();
            scanner.nextLine(); // consume newline
        }

        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        boolean applyMargin = scanner.nextLine().equalsIgnoreCase("y");
        double marginPercentage = 0;
        if (applyMargin) {
            System.out.print("Entrez le pourcentage de marge bénéficiaire (%) : ");
            marginPercentage = scanner.nextDouble();
            scanner.nextLine(); // consume newline
        }

        // Calculate costs
        double materialsCostBeforeVAT = composantController.calculateTotalMaterialsCost(projectId);
        double laborCostBeforeVAT = composantController.calculateTotalLaborCost(projectId);

        // VAT Calculation
        double materialsCostWithVAT = applyVAT ? materialsCostBeforeVAT * (1 + vatPercentage / 100)
                : materialsCostBeforeVAT;
        double laborCostWithVAT = applyVAT ? laborCostBeforeVAT * (1 + vatPercentage / 100) : laborCostBeforeVAT;

        // Total Costs
        double totalCostBeforeMargin = materialsCostWithVAT + laborCostWithVAT;
        double totalCost = totalCostBeforeMargin * (1 + marginPercentage / 100);

        System.out.println("Coût total des matériaux : " + materialsCostBeforeVAT + " €");
        System.out.println("Coût total de la main-d'œuvre : " + laborCostBeforeVAT + " €");
        System.out.println("Coût total avant marge : " + totalCostBeforeMargin + " €");
        System.out.println("Coût total après application de la marge : " + totalCost + " €");

        // Create Devis with the project object
        LocalDate emissionDate = LocalDate.now();
        LocalDate validityDate = emissionDate.plusMonths(1);

        devisController.createDevis(project, totalCost, emissionDate, validityDate, false, vatPercentage,
                marginPercentage);
    }

    private void changeDevisAcceptanceStatus() {
        System.out.print("Entrez le nom du projet : ");
        String projetNom = scanner.nextLine();

        // Fetch the projet using the project name
        Projet projet = projetController.getProjetByName(projetNom); // Use projetController instead

        if (projet != null) {
            // Fetch devis associated with the project
            Devis devis = devisController.findDevisByProjetId(projet.getId()); // Ensure this method exists in
                                                                               // DevisController

            if (devis != null) {
                System.out.println(
                        "Devis trouvé : " + devis.getId() + " avec statut d'acceptation : " + devis.isAccepte());

                System.out.print("Souhaitez-vous accepter ce devis ? (y/n) : ");
                String choix = scanner.nextLine();

                // Set the acceptance status based on user input
                boolean newAcceptanceStatus = choix.equalsIgnoreCase("y");
                devis.setAccepte(newAcceptanceStatus);

                // Update the status in the controller
                devisController.updateAccDevis(devis.getId(), newAcceptanceStatus); // Ensure this method exists in
                                                                                    // DevisController
                System.out.println("Statut d'acceptation du devis mis à jour avec succès !");
            } else {
                System.out.println("Aucun devis trouvé pour ce projet.");
            }
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

}
