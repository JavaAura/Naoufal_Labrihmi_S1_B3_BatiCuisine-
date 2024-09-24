package view;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

import controller.ClientController;
import controller.ComposantController;
import controller.DevisController;
import controller.ProjetController;
import entity.Client;
import entity.Devis;
import entity.Projet;
import entity.enums.EtatProjet;
import utilitaire.DateUtils;
import utilitaire.InputValidator;

public class ConsoleUI {
    private final ProjetController projetController;
    private final DevisController devisController;
    private final ClientController clientController;
    private final ComposantController composantController;
    private final InputValidator inputValidator;

    public ConsoleUI(ProjetController projetController, DevisController devisController,
            ClientController clientController, ComposantController composant) {
        this.projetController = projetController;
        this.devisController = devisController;
        this.clientController = clientController;
        this.composantController = composant;
        Scanner scanner = new Scanner(System.in);
        this.inputValidator = new InputValidator(scanner);

    }

    public void start() {
        System.out.println("=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===");
        boolean running = true;

        while (running) {
            afficherMenuPrincipal();
            int choix = inputValidator.promptInt("Choisissez une option : ");

            switch (choix) {
                case 1:
                    createProject();
                    break;
                case 2:
                    projetController.afficherProjetsExistants();
                    break;
                case 3:
                    calculateProjectCost();
                    break;
                case 4:
                    changeDevisAcceptanceStatus();
                    break;
                case 5:
                    updateProjectState();
                    break;
                case 6:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Option invalide.");
            }
        }
    }

    private void updateProjectState() {
        // Ask for project name
        System.out.print("Mettre à jour etat de projet :\n ");
        String projectName = inputValidator.getValidatedString("nom du projet");

        // Ask for project state
        System.out.print("Entrez le nouvel état du projet (EN_COURS, TERMINE, ANNULE) : ");
        String newStateInput = inputValidator.getValidatedString("état du projet",
                Arrays.asList("EN_COURS", "TERMINE", "ANNULE"));

        // Validate and convert the project state
        EtatProjet newState;
        try {
            newState = EtatProjet.valueOf(newStateInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("État de projet invalide. Veuillez utiliser EN_COURS, TERMINE ou ANNULE.");
            return;
        }

        // Update project state
        projetController.updateProjectStateByName(projectName, newState);
        System.out.println("État du projet '" + projectName + "' mis à jour avec succès.");
    }

    private void afficherMenuPrincipal() {
        System.out.println("=== Menu Principal ===");
        System.out.println("1. Créer un nouveau projet");
        System.out.println("2. Afficher les projets existants");
        System.out.println("3. Calculer le coût d'un projet");
        System.out.println("4. Changer le statut d'acceptation d'un devis");
        System.out.println("5. Mettre à jour l'état d'un projet"); // New option
        System.out.println("6. Quitter");
    }

    private void createProject() {
        System.out.println("--- Recherche de client ---");
        Client client = getClient();

        if (client != null) {
            System.out.println("--- Création d'un Nouveau Projet ---");
            String projectName = inputValidator.promptString("Entrez le nom du projet : ");
            double surface = inputValidator.promptInt("Entrez la surface de la cuisine (en m²) : ");

            Projet newProject = projetController.createProject(
                    projectName,
                    0, // Initial profit margin
                    client.getNom(),
                    client.getAdresse(),
                    client.getTelephone(),
                    client.isEstProfessionnel(),
                    surface);

            Long projectId = newProject.getId();
            if (projectId != null) {
                System.out.println("Projet créé avec succès! ID du projet : " + projectId);
                addMaterials(projectId);
                addLabor(projectId);

                // Finalize cost and create quote
                finalizeAndCreateQuote(projectId);
            } else {
                System.out.println("Erreur lors de la création du projet. ID non généré.");
            }
        }
    }

    private Client getClient() {
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant");
        System.out.println("2. Ajouter un nouveau client");
        int clientOption = inputValidator.promptInt("Choisissez une option : ");

        if (clientOption == 1) {
            String clientName = inputValidator.promptString("Entrez le nom du client : ");
            Client client = clientController.getClientByName(clientName);
            if (client != null) {
                System.out.println("Client trouvé !");
                return client;
            } else {
                System.out.println("Client non trouvé.");
            }
        } else if (clientOption == 2) {
            return addNewClient();
        }
        return null;
    }

    private Client addNewClient() {
        System.out.println("--- Ajouter un nouveau client ---");
        String clientName = inputValidator.promptString("Entrez le nom du client : ");
        String clientAddress = inputValidator.promptString("Entrez l'adresse du client : ");
        String clientPhone = inputValidator.promptString("Entrez le numéro de téléphone du client : ");
        boolean isProfessional = inputValidator.getBooleanInput("Le client est-il un professionnel ? (true/false) : ");

        clientController.registerClient(clientName, clientAddress, clientPhone, isProfessional);
        return clientController.getClientByName(clientName);
    }

    private void addMaterials(Long projectId) {
        System.out.println("--- Ajout des matériaux ---");
        boolean addAnotherMaterial = true;
        while (addAnotherMaterial) {
            String materialName = inputValidator.promptString("Entrez le nom du matériau : ");
            double quantity = inputValidator.promptInt("Entrez la quantité de ce matériau (en m²) : ");
            double unitCost = inputValidator.promptInt("Entrez le coût unitaire de ce matériau (€/m²) : ");
            double transportCost = inputValidator.promptInt("Entrez le coût de transport de ce matériau (€) : ");
            double qualityCoefficient = inputValidator.promptInt(
                    "Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");

            composantController.addMateriel(materialName, unitCost, quantity, "Matériau", 0.2, transportCost,
                    qualityCoefficient, projectId);
            System.out.println("Matériau ajouté avec succès !");
            addAnotherMaterial = inputValidator.promptString("Voulez-vous ajouter un autre matériau ? (y/n) : ")
                    .equalsIgnoreCase("y");
        }
    }

    private void addLabor(Long projectId) {
        System.out.println("--- Ajout de la main-d'œuvre ---");
        boolean addAnotherLabor = true;
        while (addAnotherLabor) {
            String laborType = inputValidator
                    .promptString("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
            double hourlyRate = inputValidator.promptInt("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
            double hoursWorked = inputValidator.promptInt("Entrez le nombre d'heures travaillées : ");
            double productivityFactor = inputValidator
                    .promptInt("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");

            composantController.addMainOeuvre(laborType, hourlyRate, hoursWorked, productivityFactor, "Main-d'œuvre",
                    0.2, projectId);
            System.out.println("Main-d'œuvre ajoutée avec succès !");
            addAnotherLabor = inputValidator
                    .promptString("Voulez-vous ajouter un autre type de main-d'œuvre ? (y/n) : ").equalsIgnoreCase("y");
        }
    }

    private void finalizeAndCreateQuote(Long projectId) {
        System.out.println("--- Finalisation du coût du projet et création d'un devis ---");

        // Prompt for profit margin
        double marginPercentage = inputValidator.promptInt("Entrez la marge bénéficiaire (%) : ");
        boolean applyVAT = inputValidator.getBooleanInput("Appliquer la TVA ? (true/false) : ");
        double vatPercentage = 0;
        if (applyVAT) {
            vatPercentage = inputValidator.promptDouble("Entrez le pourcentage de TVA (%) : ");

        }

        // Calculate costs
        double materialsCostBeforeVAT = composantController.calculateTotalMaterialsCost(projectId);
        double laborCostBeforeVAT = composantController.calculateTotalLaborCost(projectId);

        double totalCostBeforeMargin = materialsCostBeforeVAT + laborCostBeforeVAT;

        // Apply margin
        double totalCostWithMargin = totalCostBeforeMargin * (1 + marginPercentage / 100);

        // Apply VAT if applicable
        double finalTotalCost = applyVAT ? totalCostWithMargin * (1 + vatPercentage / 100) : totalCostWithMargin;

        // Store the profit margin and total cost in the database
        projetController.updateProjectCost(projectId, totalCostBeforeMargin, marginPercentage);

        // Create the quote
        Projet projet = projetController.getProjectById(projectId);
        if (projet != null) {
            System.out.println("Montant estimé calculé : " + finalTotalCost + " €");

            LocalDate emissionDate = inputValidator.getValidatedDate("Entrez la date d'émission (yyyy-MM-dd) : ");
            LocalDate validityDate = inputValidator.getValidatedDate("Entrez la date de validité (yyyy-MM-dd) : ");

            boolean isAccepted = inputValidator.getBooleanInput("Le devis est-il accepté ? (true/false) : ");

            devisController.createDevis(projet, finalTotalCost, emissionDate, validityDate, isAccepted);
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

    private void calculateProjectCost() {
        System.out.println("--- Calculer le coût d'un projet ---");
        String projectName = inputValidator.promptString("Entrez le nom du projet : ");

        // Fetch the project using the provided name
        Projet projet = projetController.getProjetByName(projectName);

        if (projet != null) {
            System.out.println("Projet trouvé : " + projet.nomProjet);

            // Check if a quote already exists for the project
            Devis devis = devisController.findDevisByProjetId(projet.getId());
            if (devis != null) {
                System.out.println("Devis déjà existant trouvé : " + devis.getId() + " avec montant estimé : "
                        + devis.getMontantEstime() + " $");
                return; // Exit since we already have a quote
            }

            // Calculate costs
            double materialsCostBeforeVAT = composantController.calculateTotalMaterialsCost(projet.getId());
            double laborCostBeforeVAT = composantController.calculateTotalLaborCost(projet.getId());

            double totalCostBeforeMargin = materialsCostBeforeVAT + laborCostBeforeVAT;

            // Ask if the user wants to apply a profit margin
            boolean applyMargin = inputValidator
                    .getBooleanInput("Souhaitez-vous appliquer une marge bénéficiaire ? (true/false) : ");
            double totalCost = totalCostBeforeMargin;

            double marginPercentage = 0;
            if (applyMargin) {
                marginPercentage = inputValidator.promptDouble("Entrez la marge bénéficiaire (%) : ");
                totalCost *= (1 + marginPercentage / 100);
                System.out.println("Coût total après application de la marge bénéficiaire : " + totalCost + " €");
            } else {
                System.out.println("Coût total sans marge bénéficiaire : " + totalCost + " €");
            }

            // Option to apply VAT
            boolean applyVAT = inputValidator.getBooleanInput("Souhaitez-vous appliquer une TVA ? (true/false) : ");
            if (applyVAT) {
                double vatPercentage = inputValidator.promptDouble("Entrez le pourcentage de TVA (%) : ");
                totalCost *= (1 + vatPercentage / 100);
                System.out.println("Coût total après application de la TVA : " + totalCost + " €");
            }

            // Store the calculated cost and margin in the database
            projetController.updateProjectCost(projet.getId(), totalCost, marginPercentage); // Fixed order
            System.out.println("Coût du projet mis à jour avec succès.");
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

    private void changeDevisAcceptanceStatus() {
        String projetNom = inputValidator.promptString("Entrez le nom du projet : ");

        // Fetch the projet using the project name
        Projet projet = projetController.getProjetByName(projetNom);

        if (projet != null) {
            // Fetch devis associated with the project
            Devis devis = devisController.findDevisByProjetId(projet.getId());

            if (devis != null) {
                System.out.println(
                        "Devis trouvé : " + devis.getId() + " avec statut d'acceptation : " + devis.isAccepte());

                boolean newAcceptanceStatus = inputValidator
                        .getBooleanInput("Souhaitez-vous accepter ce devis ? (true/false) : ");
                devis.setAccepte(newAcceptanceStatus);

                // Update the status in the controller
                devisController.updateAccDevis(devis.getId(), newAcceptanceStatus);
                System.out.println("Statut d'acceptation du devis mis à jour avec succès !");
            } else {
                System.out.println("Aucun devis trouvé pour ce projet.");
            }
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

}
