package repository.inter;

import java.util.List;

import entity.Devis;
import entity.Projet;
import entity.enums.EtatProjet;

public interface IProjetRepository {

    void addProject(Projet projet);

    List<Projet> getAllProjets();

    Projet getProjectById(Long id);

    Projet getProjectByName(String name);

    void updateProjectCost(Long projectId, double totalCost, double profitMargin);

    double getTotalCost(Long projectId);

    List<Projet> getProjetsWithClients();

    void updateProjectStateByName(String projectName, EtatProjet newState);

}
