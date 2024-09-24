package controller;

import java.util.List;

import entity.Client;
import entity.Projet;
import entity.enums.EtatProjet;
import service.iter.IClientService;
import service.iter.IProjetService;

public class ProjetController {
    private final IProjetService projetService;
    private final ClientController clientController;

    public ProjetController(IProjetService projetService, ClientController clientController) {
        this.projetService = projetService;
        this.clientController = clientController;
    }

    public Projet createProject(String projectName, double profitMargin, String clientName, String clientAddress,
            String clientPhone, boolean isProfessional, double surface) {
        // Validate inputs
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        if (profitMargin < 0 || surface <= 0) {
            throw new IllegalArgumentException("Profit margin and surface must be positive");
        }

        // Check if the client exists by name
        Client existingClient = clientController.getClientByName(clientName);
        if (existingClient == null) {
            clientController.registerClient(clientName, clientAddress, clientPhone, isProfessional);
            existingClient = clientController.getClientByName(clientName);
            if (existingClient == null) {
                System.out.println("Error: Client creation failed. Cannot associate client with the project.");
                return null;
            }
        }

        // Create and save the project
        Projet projet = new Projet();
        projet.setNomProjet(projectName);
        projet.setMargeBeneficiaire(profitMargin);
        projet.setEtatProjet(EtatProjet.EN_COURS);
        projet.setClient(existingClient);
        projet.setSurface(surface);

        projetService.addProject(projet);
        return projet;
    }

    public void addComponentToProject(Long projectId, String componentName, double unitCost, int quantity) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            // Logic to add components to the project
            System.out.println("Adding component " + componentName + " to project " + projet.getNomProjet());
            // Update total costs here
        } else {
            System.out.println("Project not found.");
        }
    }

    public void calculateTotalCost(Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            // Logic to calculate total cost
            double totalCost = 0.0; // Replace with actual calculation logic
            projet.setCoutTotal(totalCost + projet.getMargeBeneficiaire());
            System.out.println("Total cost for project " + projet.getNomProjet() + " is: " + projet.getCoutTotal());
        } else {
            System.out.println("Project not found.");
        }
    }

    public void generateEstimate(Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            // Logic to generate an estimate
            System.out.println("Generating estimate for project " + projet.getNomProjet());
        } else {
            System.out.println("Project not found.");
        }
    }

    public Projet getProjetByName(String projectName) {
        Projet projet = projetService.getProjectByName(projectName);
        if (projet != null) {
            System.out.println("Project found: " + projet.getNomProjet());
        } else {
            System.out.println("Project not found.");
        }
        return projet;
    }

    public Projet getProjectById(Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            System.out.println("Project found: " + projet.getNomProjet());
        } else {
            System.out.println("Project not found.");
        }
        return projet;
    }

    // New method to update project costs
    public void updateProjectCost(Long projectId, double totalCost, double marginPercentage) {
        if (totalCost < 0 || marginPercentage < 0) {
            throw new IllegalArgumentException("Total cost and margin percentage must be non-negative");
        }

        projetService.updateProjectCost(projectId, totalCost, marginPercentage);
        System.out.println("Updated project cost for project ID " + projectId);
    }

    public void getTotalCost(Long projectId) {
        double totalCost = projetService.getTotalCost(projectId);
        System.out.println("Total cost for project ID " + projectId + " is: " + totalCost);
    }

    public void afficherProjetsExistants() {
        List<Projet> projets = projetService.getProjetsWithClients();

        if (projets.isEmpty()) {
            System.out.println("\u001B[31mNo projects found.\u001B[0m");
        } else {
            for (Projet projet : projets) {
                Client client = projet.getClient();
                System.out.println("\u001B[34mProject Name:\u001B[0m " + projet.getNomProjet());
                System.out.println("\u001B[32mProfit Margin:\u001B[0m " + projet.getMargeBeneficiaire());
                System.out.println("\u001B[32mTotal Cost:\u001B[0m " + projet.getCoutTotal());
                System.out.println("\u001B[32mSurface:\u001B[0m " + projet.getSurface());
                System.out.println("\u001B[33mProject State:\u001B[0m " + projet.getEtatProjet());
                System.out.println("\u001B[34mClient Name:\u001B[0m " + client.getNom());
                System.out.println("\u001B[34mClient Address:\u001B[0m " + client.getAdresse());
                System.out.println("\u001B[34mClient Phone:\u001B[0m " + client.getTelephone());
                System.out.println("\u001B[34mIs Professional:\u001B[0m " + client.isEstProfessionnel());
                System.out.println("========================================");
            }
        }
    }

    public void updateProjectStateByName(String projectName, EtatProjet newState) {
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }

        projetService.updateProjectStateByName(projectName, newState);
        System.out.println("Updated project state for project: " + projectName);
    }

}
