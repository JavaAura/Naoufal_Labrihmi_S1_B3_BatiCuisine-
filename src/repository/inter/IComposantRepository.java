package repository.inter;

import entity.Composant;

public interface IComposantRepository<T extends Composant> {
    void addComposant(T composant);

    // Composant getComposantById(int id);

    // List<Composant> getAllComposants();
}
