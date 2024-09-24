package service.impl;

import java.util.List;

import entity.Projet;
import entity.enums.EtatProjet;
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

    @Override
    public void updateProjectCost(Long projectId, double totalCost, double profitMargin) {
        projetRepository.updateProjectCost(projectId, totalCost, profitMargin);
    }

    @Override
    public double getTotalCost(Long projectId) {
        return projetRepository.getProjectById(projectId).getCoutTotal();
    }

    @Override
    public List<Projet> getProjetsWithClients() {
        return projetRepository.getProjetsWithClients();
    }

    @Override
    public void updateProjectStateByName(String projectName, EtatProjet newState) {
        projetRepository.updateProjectStateByName(projectName, newState);
    }

}
