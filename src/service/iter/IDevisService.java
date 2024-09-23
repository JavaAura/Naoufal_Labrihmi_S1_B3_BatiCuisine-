package service.iter;

import entity.Devis;

public interface IDevisService {
    void addDevis(Devis devis);

    Devis getDevisById(int id);

    void updateAccDevis(Devis devis);

    Devis findByProjetId(Long projetId);
}
