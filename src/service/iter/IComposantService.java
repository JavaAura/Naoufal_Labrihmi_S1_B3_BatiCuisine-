package service.iter;

import java.util.List;

import entity.Composant;

public interface IComposantService<T extends Composant> {
    void addComposant(T composant);

    List<T> getAllComposantsByProject(Long projectId);

}
