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

    public void createDevis(Projet projet, double estimatedAmount, LocalDate emissionDate, LocalDate validityDate,
            boolean isAccepted) {
        if (projet == null) {
            System.out.println("Project cannot be null.");
            return;
        }

        Devis devis = new Devis();
        devis.setMontantEstime(estimatedAmount);
        devis.setDateEmission(java.sql.Date.valueOf(emissionDate));
        devis.setDateValidite(java.sql.Date.valueOf(validityDate));
        devis.setAccepte(isAccepted);
        devis.setProjet(projet); // Associate the Devis with the Projet

        devisService.addDevis(devis);
        System.out.println("Devis created successfully for project: " + projet.getNomProjet());
    }

    public void updateAccDevis(int devisId, boolean isAccepted) {
        Devis devis = devisService.getDevisById(devisId);
        if (devis == null) {
            System.out.println("Devis not found with ID: " + devisId);
            return;
        }

        devis.setAccepte(isAccepted);
        devisService.updateAccDevis(devis);
        System.out.println("Devis acceptance status updated successfully for ID: " + devisId);
    }

    public Devis getDevisById(int devisId) {
        Devis devis = devisService.getDevisById(devisId);
        if (devis == null) {
            System.out.println("Devis not found with ID: " + devisId);
            return null;
        }
        System.out.println("Devis retrieved successfully for ID: " + devisId);
        return devis;
    }

    public Devis findDevisByProjetId(Long projetId) {
        Devis devis = devisService.findByProjetId(projetId);
        if (devis == null) {
            System.out.println("No Devis found for project ID: " + projetId);
            return null;
        }
        System.out.println("Devis retrieved successfully for project ID: " + projetId);
        return devis;
    }
}
