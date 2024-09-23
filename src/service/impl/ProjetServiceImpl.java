package service.impl;

import java.util.List;

import entity.Projet;
import repository.inter.IProjetRepository;
import service.iter.IProjetService;

public class ProjetServiceImpl implements IProjetService {
    private final IProjetRepository projetRepository;

    public ProjetServiceImpl(IProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    @Override
    public void addProject(Projet projet) {
        projetRepository.addProject(projet);
    }

    @Override
    public List<Projet> getAllProjets() {
        return projetRepository.getAllProjets();
    }

    @Override
    public Projet getProjectById(Long id) {
        return projetRepository.getProjectById(id);
    }

    @Override
    public Projet getProjectByName(String name) {
        return projetRepository.getProjectByName(name);
    }
}
