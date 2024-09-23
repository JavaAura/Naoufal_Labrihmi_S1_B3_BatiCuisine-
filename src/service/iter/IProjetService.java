package service.iter;

import java.util.List;

import entity.Projet;

public interface IProjetService {
    void addProject(Projet projet);

    List<Projet> getAllProjets();

    Projet getProjectById(Long id);

    Projet getProjectByName(String name);
}
