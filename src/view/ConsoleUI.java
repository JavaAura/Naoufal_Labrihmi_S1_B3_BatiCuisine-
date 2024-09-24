package view;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    private final Map<Long, Client> clientCache = new HashMap<>();

    public ConsoleUI(ProjetController projetController, DevisController devisController,
            ClientController clientController, ComposantController composant) {
        this.projetController = projetController;
        this.devisController = devisController;
        this.clientController = clientController;
        this.composantController = composant;
        this.inputValidator = new InputValidator(new Scanner(System.in));
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
                    break;
            }

        }
    }

    private void updateProjectState() {
        String projectName = inputValidator.promptString("Nom du projet : ");
        String newStateStr = inputValidator.promptString("État du projet (EN_COURS, TERMINE, ANNULE) : ");

        EtatProjet newState = EtatProjet.valueOf(newStateStr);
        projetController.updateProjectStateByName(projectName, newState);
        System.out.println("État du projet '" + projectName + "' mis à jour avec succès.");
    }

    private void afficherMenuPrincipal() {
        System.out.println("=== Menu Principal ===");
        System.out.println("1. Créer un nouveau projet");
        System.out.println("2. Afficher les projets existants");
        System.out.println("3. Calculer le coût d'un projet");
        System.out.println("4. Changer le statut d'acceptation d'un devis");
        System.out.println("5. Mettre à jour l'état d'un projet");
        System.out.println("6. Quitter");
    }

    private void createProject() {
        System.out.println("--- Recherche de client ---");
        Client client = getClient();

        if (client != null) {
            System.out.println("--- Création d'un Nouveau Projet ---");
            String projectName = inputValidator.promptString("Entrez le nom du projet : ");
            double surface = inputValidator.promptInt("Entrez la surface de la cuisine (en m²) : ");

            // Create the project without final cost calculation
            Projet newProject = projetController.createProject(projectName, 0, client.getNom(),
                    client.getAdresse(), client.getTelephone(), client.isEstProfessionnel(), surface);

            if (newProject.getId() != null) {
                System.out.println("Projet créé avec succès! ID du projet : " + newProject.getId());
                addComponents(newProject.getId());
                finalizeAndCreateQuote(newProject.getId(), client.isEstProfessionnel());
            } else {
                System.out.println("Erreur lors de la création du projet. ID non généré.");
            }
        }
    }

    private Client getClient() {
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        int clientOption = inputValidator
                .promptInt("1. Chercher un client existant\n2. Ajouter un nouveau client\nChoisissez une option : ");

        return clientOption == 1 ? searchExistingClient() : addNewClient();
    }

    private Client searchExistingClient() {
        String clientName = inputValidator.promptString("Entrez le nom du client : ");
        Optional<Client> optionalClient = clientController.getClientByName(clientName);

        return optionalClient.map(client -> clientCache.computeIfAbsent(client.getId(), id -> client))
                .orElseGet(() -> handleClientNotFound(clientName));
    }

    private Client handleClientNotFound(String clientName) {
        System.out.println("Aucun client trouvé avec le nom : " + clientName);
        return null;
    }

    private Client addNewClient() {
        System.out.println("--- Ajouter un nouveau client ---");
        String clientName = inputValidator.promptString("Entrez le nom du client : ");

        Optional<Client> optionalExistingClient = clientController.getClientByName(clientName);
        if (optionalExistingClient.isPresent()) {
            System.out.println("Un client avec ce nom existe déjà : " + optionalExistingClient.get().getNom());
            return optionalExistingClient.get();
        }

        String clientAddress = inputValidator.promptString("Entrez l'adresse du client : ");
        String clientPhone = inputValidator.promptValidPhoneNumber("Entrez le numéro de téléphone du client : ");
        boolean isProfessional = inputValidator
                .promptProfessionalStatus("Le client est-il un professionnel ? (1 pour Oui, 2 pour Non) : ");

        Client newClient = clientController.registerClient(clientName, clientAddress, clientPhone, isProfessional);
        clientCache.put(newClient.getId(), newClient);
        return newClient;
    }

    private void addComponents(Long projectId) {
        System.out.println("--- Ajout des composants ---");
        addMaterials(projectId);
        addLaborComponents(projectId);
        ;
    }

    private void addMaterials(Long projectId) {
        System.out.println("--- Ajout des matériaux ---");
        addComponents(projectId, "matériaux", this::addMaterial);
    }

    private void addLaborComponents(Long projectId) {
        System.out.println("--- Ajout de la main-d'œuvre ---");
        addComponents(projectId, "main-d'œuvre", this::addLaborDetails); // Renamed method reference to avoid conflict
    }

    private void addComponents(Long projectId, String componentType, Consumer<Long> addComponent) {
        boolean addAnother;
        do {
            addComponent.accept(projectId);
            addAnother = inputValidator
                    .getBooleanInput("Voulez-vous ajouter un autre " + componentType + " ? (true/false) : ");
        } while (addAnother);
    }

    private void addMaterial(Long projectId) {
        String materialName = inputValidator.promptString("Entrez le nom du matériau : ");
        double quantity = inputValidator.promptDouble("Entrez la quantité de ce matériau (en m²) : ");
        double unitCost = inputValidator.promptDouble("Entrez le coût unitaire de ce matériau (€/m²) : ");
        double transportCost = inputValidator.promptDouble("Entrez le coût de transport de ce matériau (€) : ");
        double qualityCoefficient = inputValidator.promptDouble(
                "Entrez le coefficient de qualité du matériau (1.0 = standard, > 1.0 = haute qualité) : ");

        composantController.addMateriel(materialName, unitCost, quantity, "Matériau", 0.2, transportCost,
                qualityCoefficient, projectId);
        System.out.println("Matériau ajouté avec succès !");
    }

    private void addLaborDetails(Long projectId) {
        String laborType = inputValidator
                .promptString("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
        double hourlyRate = inputValidator.promptDouble("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
        double hoursWorked = inputValidator.promptDouble("Entrez le nombre d'heures travaillées : ");
        double productivityFactor = inputValidator
                .promptDouble("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");

        composantController.addMainOeuvre(laborType, hourlyRate, hoursWorked, productivityFactor, "Main-d'œuvre", 0.2,
                projectId);
        System.out.println("Main-d'œuvre ajoutée avec succès !");
    }

    private void finalizeAndCreateQuote(Long projectId, boolean isProfessional) {
        System.out.println("--- Finalisation du coût du projet et création d'un devis ---");
        double marginPercentage = inputValidator.promptDouble("Entrez la marge bénéficiaire (%) : ");
        boolean applyVAT = inputValidator.getBooleanInput("Appliquer la TVA ? (true/false) : ");
        double vatPercentage = applyVAT ? inputValidator.promptDouble("Entrez le pourcentage de TVA (%) : ") : 0;

        double totalCostBeforeMargin = composantController.calculateTotalMaterialsCost(projectId) +
                composantController.calculateTotalLaborCost(projectId);

        // Apply discount if the client is professional
        if (isProfessional) {
            double discountPercentage = 10.0;
            totalCostBeforeMargin -= totalCostBeforeMargin * (discountPercentage / 100);
            System.out.println("Un rabais de " + discountPercentage + "% a été appliqué pour le client professionnel.");
        }

        double finalTotalCost = applyVAT ? totalCostBeforeMargin * (1 + vatPercentage / 100) : totalCostBeforeMargin;

        projetController.updateProjectCost(projectId, totalCostBeforeMargin, marginPercentage);

        // Fetch the project using the projectId
        Projet projet = projetController.getProjectById(projectId);

        if (projet != null) {
            // Create the quote
            devisController.createDevis(projet, finalTotalCost, LocalDate.now(),
                    LocalDate.now().plusDays(30), false);
            System.out.println("Devis créé avec succès! Montant estimé : " + finalTotalCost + " €");
        } else {
            System.out.println("Projet introuvable avec l'ID : " + projectId);
        }
    }

    private void calculateProjectCost() {
        System.out.println("--- Calculer le coût d'un projet ---");
        String projectName = inputValidator.promptString("Entrez le nom du projet : ");
        Projet projet = projetController.getProjetByName(projectName);

        if (projet != null) {
            double totalCost = composantController.calculateTotalMaterialsCost(projet.getId()) +
                    composantController.calculateTotalLaborCost(projet.getId());
            System.out.println("Le coût total du projet '" + projet.getNomProjet() + "' est : " + totalCost + " $");
        } else {
            System.out.println("Projet non trouvé.");
        }
    }

    private void changeDevisAcceptanceStatus() {
        String projetNom = inputValidator.promptString("Entrez le nom du projet : ");

        // Fetch the project using the project name
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
