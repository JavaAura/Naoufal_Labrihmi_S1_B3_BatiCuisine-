package repository.inter;

import java.util.List;

import entity.Client;

public interface IClientRepository {
    void addClient(Client client);

    Client getClientById(Long id);

    Client getClientByName(String name);

    List<Client> getAllClients();
}
