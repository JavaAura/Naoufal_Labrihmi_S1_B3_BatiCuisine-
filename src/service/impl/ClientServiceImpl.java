package service.impl;

import java.util.List;
import entity.Client;
import repository.inter.IClientRepository;
import service.iter.IClientService;

public class ClientServiceImpl implements IClientService {

    // using Dependency Injection (DI)

    private final IClientRepository clientRepository;

    public ClientServiceImpl(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void addClient(Client client) {
        clientRepository.addClient(client);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.getClientById(id);
    }

    @Override
    public Client getClientByName(String name) {
        return clientRepository.getClientByName(name);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }
}
