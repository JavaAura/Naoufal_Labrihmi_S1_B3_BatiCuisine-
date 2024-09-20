package controller;

import entity.Client;
import entity.Projet;
import entity.enums.EtatProjet;
import service.iter.IClientService;
import service.iter.IProjetService;

public class ProjetController {
    private final IProjetService projetService;
    private final ClientController clientController;

    public ProjetController(IProjetService projetService, IClientService clientService) {
        this.projetService = projetService;
        this.clientController = new ClientController(clientService);
    }

    public void createProject(String projectName, double profitMargin, String clientName, String clientAddress,
            String clientPhone, boolean isProfessional) {
        // Check if the client exists by name
        Client existingClient = clientController.getClientByName(clientName);

        if (existingClient == null) {
            // If the client doesn't exist, create a new one
            clientController.registerClient(clientName, clientAddress, clientPhone, isProfessional);
            // Retrieve the newly created client
            existingClient = clientController.getClientByName(clientName);
            if (existingClient == null) {
                System.out.println("Error: Client creation failed. Cannot associate client with the project.");
                return;
            }
        }

        // Create the project and associate the existing client
        Projet projet = new Projet();
        projet.setNomProjet(projectName);
        projet.setMargeBeneficiaire(profitMargin);
        projet.setEtatProjet(EtatProjet.EN_COURS); // Assuming EtatProjet is an enum with values
        projet.setClient(existingClient); // Associate the client with the project

        projetService.addProject(projet);
        System.out.println(
                "Project created successfully: " + projet.getNomProjet() + " with client: " + existingClient.getNom());
    }

    public void addComponentToProject(Long projectId, String componentName, double unitCost, int quantity) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            // Logic to add components (materials, labor, etc.) to the project
            System.out.println("Adding component " + componentName + " to project " + projet.getNomProjet());
            // Update total costs here
        } else {
            System.out.println("Project not found.");
        }
    }

    public void calculateTotalCost(Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            // Logic to calculate total cost including materials, labor, taxes, etc.
            double totalCost = 0.0; // Replace with actual calculation
            projet.setCoutTotal(totalCost + projet.getMargeBeneficiaire());
            System.out.println("Total cost for project " + projet.getNomProjet() + " is: " + projet.getCoutTotal());
        } else {
            System.out.println("Project not found.");
        }
    }

    public void generateEstimate(Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            // Logic to generate an estimate based on project costs
            System.out.println("Generating estimate for project " + projet.getNomProjet());
        } else {
            System.out.println("Project not found.");
        }
    }

}