package controller;

import entity.MainOeuvre;
import entity.Materiel;
import entity.Projet;
import service.iter.IComposantService;
import service.iter.IProjetService;

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
}
