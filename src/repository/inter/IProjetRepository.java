package repository.inter;

import java.util.List;

import entity.Devis;
import entity.Projet;

public interface IProjetRepository {

    void addProject(Projet projet);

    List<Projet> getAllProjets();

    Projet getProjectById(Long id);

    Projet getProjectByName(String name);

}
