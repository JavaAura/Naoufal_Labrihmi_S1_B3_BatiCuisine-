package controller;

import entity.MainOeuvre;
import entity.Materiel;
import entity.Projet;
import service.iter.IComposantService;
import service.iter.IProjetService;

import java.util.List;

public class ComposantController {
    private final IComposantService<Materiel> materielService;
    private final IComposantService<MainOeuvre> mainOeuvreService;
    private final IProjetService projetService;

    public ComposantController(IComposantService<Materiel> materielService,
            IComposantService<MainOeuvre> mainOeuvreService,
            IProjetService projetService) {
        this.materielService = materielService;
        this.mainOeuvreService = mainOeuvreService;
        this.projetService = projetService;
    }

    // Method to add a material component
    public void addMateriel(String name, double unitCost, double quantity,
            String typeComposant, double taxRate,
            double transportCost, double qualityCoefficient,
            Long projectId) {
        Projet projet = projetService.getProjectById(projectId); // Fetch the project
        if (projet == null) {
            System.out.println("Project not found with ID: " + projectId);
            return;
        }

        Materiel materiel = new Materiel();
        materiel.setNom(name);
        materiel.setCoutUnitaire(unitCost);
        materiel.setQuantite(quantity);
        materiel.setTypeComposant(typeComposant);
        materiel.setTauxTVA(taxRate);
        materiel.setCoutTransport(transportCost);
        materiel.setCoefficientQualite(qualityCoefficient);
        materiel.setProjet(projet);

        materielService.addComposant(materiel);
        System.out.println("Material " + name + " added to project ID: " + projectId);
    }

    // Method to add a MainOeuvre component
    public void addMainOeuvre(String name, double hourlyRate, double hoursWorked,
            double productivityFactor, String typeComposant,
            double taxRate, Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet == null) {
            System.out.println("Project not found with ID: " + projectId);
            return;
        }

        MainOeuvre mainOeuvre = new MainOeuvre();
        mainOeuvre.setNom(name);
        mainOeuvre.setTauxHoraire(hourlyRate);
        mainOeuvre.setHeuresTravail(hoursWorked);
        mainOeuvre.setProductiviteOuvrier(productivityFactor);
        mainOeuvre.setTypeComposant(typeComposant);
        mainOeuvre.setTauxTVA(taxRate);
        mainOeuvre.setProjet(projet);

        mainOeuvreService.addComposant(mainOeuvre);
        System.out.println("Labor " + name + " added to project ID: " + projectId);
    }

    // Method to calculate total materials cost for a project
    public double calculateTotalMaterialsCost(Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet == null) {
            System.out.println("Project not found.");
            return 0.0;
        }

        List<Materiel> materials = materielService.getAllComposantsByProject(projectId);
        double totalCost = 0.0;

        for (Materiel material : materials) {
            totalCost += material.getCoutUnitaire() * material.getQuantite();
            totalCost += totalCost * (material.getTauxTVA() / 100); // Apply VAT
            totalCost += material.getCoutTransport(); // Add transport cost
        }

        System.out.println("Total materials cost for project is: " + totalCost);
        return totalCost;
    }

    // Method to calculate total labor cost for a project
    public double calculateTotalLaborCost(Long projectId) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet == null) {
            System.out.println("Project not found with ID: " + projectId);
            return 0.0;
        }

        List<MainOeuvre> laborComponents = mainOeuvreService.getAllComposantsByProject(projectId);
        double totalCost = 0.0;

        for (MainOeuvre labor : laborComponents) {
            totalCost += labor.getTauxHoraire() * labor.getHeuresTravail();
            totalCost += totalCost * (labor.getTauxTVA() / 100); // Apply VAT
        }

        System.out.println("Total labor cost for project ID " + projectId + " is: " + totalCost);
        return totalCost;
    }
}
