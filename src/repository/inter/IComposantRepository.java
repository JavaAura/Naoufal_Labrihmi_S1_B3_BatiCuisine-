package repository.inter;

import java.util.List;

import entity.Composant;
import entity.Materiel;

public interface IComposantRepository<T extends Composant> {
    void addComposant(T composant);

    // Composant getComposantById(int id);

    // List<Composant> getAllComposants();
    List<T> getAllComposantsByProject(Long projectId);

}
