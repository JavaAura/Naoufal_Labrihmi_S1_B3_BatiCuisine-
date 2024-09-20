package service.iter;

import entity.Composant;

public interface IComposantService<T extends Composant> {
    void addComposant(T composant);
}
