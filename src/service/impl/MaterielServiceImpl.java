package service.impl;

import java.util.List;

import entity.Materiel;
import repository.inter.IComposantRepository;
import service.iter.IComposantService;

public class MaterielServiceImpl implements IComposantService<Materiel> {
    private final IComposantRepository<Materiel> materielRepository;

    public MaterielServiceImpl(IComposantRepository<Materiel> materielRepository) {
        this.materielRepository = materielRepository;
    }

    @Override
    public void addComposant(Materiel materiel) {
        materielRepository.addComposant(materiel);
    }

    @Override
    public List<Materiel> getAllComposantsByProject(Long projectId) {
        return materielRepository.getAllComposantsByProject(projectId);
    }
}