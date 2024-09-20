package controller;

import entity.Devis;
import entity.Projet;
import service.iter.IDevisService;
import service.iter.IProjetService;

import java.time.LocalDate;

public class DevisController {
    private final IDevisService devisService;
    private final IProjetService projetService;

    public DevisController(IDevisService devisService, IProjetService projetService) {
        this.devisService = devisService;
        this.projetService = projetService;
    }

    public void createDevis(Long projectId, double estimatedAmount, LocalDate emissionDate, LocalDate validityDate,
            boolean isAccepted) {
        Projet projet = projetService.getProjectById(projectId);
        if (projet == null) {
            System.out.println("Project not found with ID: " + projectId);
            return;
        }

        Devis devis = new Devis();
        devis.setMontantEstime(estimatedAmount);
        devis.setDateEmission(java.sql.Date.valueOf(emissionDate));
        devis.setDateValidite(java.sql.Date.valueOf(validityDate));
        devis.setAccepte(isAccepted);
        devis.setProjet(projet); // Associate the project with the estimate

        devisService.addDevis(devis);
        System.out.println("Estimate created successfully for project ID: " + projectId);
    }
}
