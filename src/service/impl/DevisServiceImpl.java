package service.impl;

import entity.Devis;
import repository.inter.IDevisRepository;
import service.iter.IDevisService;

public class DevisServiceImpl implements IDevisService {
    private final IDevisRepository devisRepository;

    public DevisServiceImpl(IDevisRepository devisRepository) {
        this.devisRepository = devisRepository;
    }

    @Override
    public void addDevis(Devis devis) {
        devisRepository.addDevis(devis);
    }

    @Override
    public Devis getDevisById(int id) {
        return devisRepository.getDevisById(id);
    }

    @Override
    public void updateAccDevis(Devis devis) {
        devisRepository.updateAccDevis(devis);
    }

    @Override
    public Devis findByProjetId(Long projetId) {
        return devisRepository.findByProjetId(projetId);
    }

}
