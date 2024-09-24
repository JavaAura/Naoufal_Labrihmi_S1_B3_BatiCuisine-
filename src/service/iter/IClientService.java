package service.iter;

import java.util.List;
import java.util.Optional;

import entity.Client;

public interface IClientService {
    void addClient(Client client);

    Optional<Client> getClientById(Long id);

    Optional<Client> getClientByName(String name);

    List<Client> getAllClients();
}
