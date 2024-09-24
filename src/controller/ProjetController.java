package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import entity.Client;
import entity.Projet;
import entity.enums.EtatProjet;
import service.iter.IClientService;
import service.iter.IProjetService;

public class ProjetController {
    private final IProjetService projetService;
    private final ClientController clientController;
    private final Map<String, Client> clientCache = new HashMap<>();

    public ProjetController(IProjetService projetService, ClientController clientController) {
        this.projetService = projetService;
        this.clientController = clientController;
    }

    public Projet createProject(String projectName, double profitMargin, String clientName, String clientAddress,
            String clientPhone, boolean isProfessional, double surface) {
        validateProjectInputs(projectName, profitMargin, surface);

        Client existingClient = clientCache.computeIfAbsent(clientName, name -> {
            Optional<Client> optionalClient = clientController.getClientByName(name);
            // Check if the client exists
            if (!optionalClient.isPresent()) {
                // Register the client if it does not exist
                clientController.registerClient(name, clientAddress, clientPhone, isProfessional);
                optionalClient = clientController.getClientByName(name); // Re-fetch the client after registration
            }
            // Return the client or null if not found
            return optionalClient.orElse(null);
        });

        if (existingClient == null) {
            System.out.println("Error: Client creation failed. Cannot associate client with the project.");
            return null;
        }

        Projet projet = new Projet();
        projet.setNomProjet(projectName);
        projet.setMargeBeneficiaire(profitMargin);
        projet.setEtatProjet(EtatProjet.EN_COURS);
        projet.setClient(existingClient);
        projet.setSurface(surface);

        projetService.addProject(projet);
        return projet;
    }

    private void validateProjectInputs(String projectName, double profitMargin, double surface) {
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        if (profitMargin < 0 || surface <= 0) {
            throw new IllegalArgumentException("Profit margin and surface must be positive");
        }
    }

    public void addComponentToProject(Long projectId, String componentName, double unitCost, int quantity) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            System.out.println("Adding component " + componentName + " to project " + projet.getNomProjet());
            // Logic to update total costs here
        } else {
            System.out.println("Project not found.");
        }
    }

    // public void calculateTotalCost(Long projectId) {
    // Projet projet = projetService.getProjectById(projectId);
    // if (projet != null) {
    // double totalCost = calculateProjectCosts(projet); // Assuming you have a
    // method for this
    // projet.setCoutTotal(totalCost + projet.getMargeBeneficiaire());
    // System.out.println("Total cost for project " + projet.getNomProjet() + " is:
    // " + projet.getCoutTotal());
    // } else {
    // System.out.println("Project not found.");
    // }
    // }

    // private double calculateProjectCosts(Projet projet) {
    // // Implement actual logic for calculating costs, e.g., summing components
    // costs
    // return 0.0; // Placeholder
    // }

    public void generateEstimate(Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet != null) {
            System.out.println("Generating estimate for project " + projet.getNomProjet());
            // Logic to generate an estimate
        } else {
            System.out.println("Project not found.");
        }
    }

    public Projet getProjetByName(String projectName) {
        return Optional.ofNullable(projetService.getProjectByName(projectName))
                .map(projet -> {
                    System.out.println("Project found: " + projet.getNomProjet());
                    return projet;
                })
                .orElseGet(() -> {
                    System.out.println("Project not found.");
                    return null;
                });
    }

    public Projet getProjectById(Long projectId) {
        return Optional.ofNullable(projetService.getProjectById(projectId))
                .map(projet -> {
                    System.out.println("Project found: " + projet.getNomProjet());
                    return projet;
                })
                .orElseGet(() -> {
                    System.out.println("Project not found.");
                    return null;
                });
    }

    public void updateProjectCost(Long projectId, double totalCost, double marginPercentage) {
        if (totalCost < 0 || marginPercentage < 0) {
            throw new IllegalArgumentException("Total cost and margin percentage must be non-negative");
        }

        projetService.updateProjectCost(projectId, totalCost, marginPercentage);
        System.out.println("Updated project cost for project ID " + projectId);
    }

    public void afficherProjetsExistants() {
        List<Projet> projets = projetService.getProjetsWithClients();

        if (projets.isEmpty()) {
            System.out.println("\u001B[31mNo projects found.\u001B[0m");
        } else {
            projets.stream().forEach(projet -> {
                Client client = projet.getClient();
                System.out.printf("\u001B[34mProject Name:\u001B[0m %s%n" +
                        "\u001B[32mProfit Margin:\u001B[0m %.2f%n" +
                        "\u001B[32mTotal Cost:\u001B[0m %.2f%n" +
                        "\u001B[32mSurface:\u001B[0m %.2f%n" +
                        "\u001B[33mProject State:\u001B[0m %s%n" +
                        "\u001B[34mClient Name:\u001B[0m %s%n" +
                        "\u001B[34mClient Address:\u001B[0m %s%n" +
                        "\u001B[34mClient Phone:\u001B[0m %s%n" +
                        "\u001B[34mIs Professional:\u001B[0m %b%n" +
                        "========================================%n",
                        projet.getNomProjet(),
                        projet.getMargeBeneficiaire(),
                        projet.getCoutTotal(),
                        projet.getSurface(),
                        projet.getEtatProjet(),
                        client.getNom(),
                        client.getAdresse(),
                        client.getTelephone(),
                        client.isEstProfessionnel());
            });
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
