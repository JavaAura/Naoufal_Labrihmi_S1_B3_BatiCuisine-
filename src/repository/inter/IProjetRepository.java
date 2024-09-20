package repository.inter;

import java.util.List;

import entity.Projet;

public interface IProjetRepository {

    void addProject(Projet projet);

    List<Projet> getAllProjets();

    Projet getProjectById(Long id);
}
