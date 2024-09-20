package service.impl;

import entity.MainOeuvre;
import repository.inter.IComposantRepository;
import service.iter.IComposantService;

public class MainOeuvreServiceImpl implements IComposantService<MainOeuvre> {
    private final IComposantRepository<MainOeuvre> mainOeuvreRepository;

    public MainOeuvreServiceImpl(IComposantRepository<MainOeuvre> mainOeuvreRepository) {
        this.mainOeuvreRepository = mainOeuvreRepository;
    }

    @Override
    public void addComposant(MainOeuvre mainOeuvre) {
        mainOeuvreRepository.addComposant(mainOeuvre);
    }
}
