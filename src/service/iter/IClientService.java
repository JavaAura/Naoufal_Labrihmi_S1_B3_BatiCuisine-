package service.iter;

import java.util.List;

import entity.Client;

public interface IClientService {
    void addClient(Client client);

    Client getClientById(Long id);

    Client getClientByName(String name);

    List<Client> getAllClients();
}
