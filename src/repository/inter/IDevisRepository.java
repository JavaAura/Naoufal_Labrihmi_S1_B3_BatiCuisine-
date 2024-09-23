package repository.inter;

import entity.Devis;

public interface IDevisRepository {
    void addDevis(Devis devis);

    void updateAccDevis(Devis devis);

    Devis getDevisById(int id);

    Devis findByProjetId(Long projetId);


    // List<Devis> findAll();

    // Devis findById(int id);

    // void deleteById(int id);
}
