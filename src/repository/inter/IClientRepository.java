package repository.inter;

import java.util.List;
import java.util.Optional;

import entity.Client;

public interface IClientRepository {
    void addClient(Client client);

    Optional<Client> getClientById(Long id);

    Optional<Client> getClientByName(String name);

    List<Client> getAllClients();
}
