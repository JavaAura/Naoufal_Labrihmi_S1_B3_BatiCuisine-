package repository.memory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entity.Client;
import repository.inter.IClientRepository;

public class ClientRepositoryImpl implements IClientRepository {
    private Connection connection;

    public ClientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addClient(Client client) {
        String sql = "INSERT INTO Client (nom, adresse, telephone, estProfessionnel) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getAdresse());
            stmt.setString(3, client.getTelephone());
            stmt.setBoolean(4, client.isEstProfessionnel());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        String sql = "SELECT * FROM Client WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty(); // Return an empty Optional if no client is found
    }

    @Override
    public Optional<Client> getClientByName(String name) {
        String sql = "SELECT * FROM Client WHERE nom = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty(); // Return an empty Optional if no client is found
    }

    @Override
    public List<Client> getAllClients() {
        String sql = "SELECT * FROM Client";
        List<Client> clients = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            clients = mapResultSetToClientList(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    // Helper method to map ResultSet to a Client object
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setNom(rs.getString("nom"));
        client.setAdresse(rs.getString("adresse"));
        client.setTelephone(rs.getString("telephone"));
        client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));
        return client;
    }

    // Helper method to map ResultSet to a List of Client objects using Streams
    private List<Client> mapResultSetToClientList(ResultSet rs) throws SQLException {
        List<Client> clients = new ArrayList<>();

        while (rs.next()) {
            clients.add(mapResultSetToClient(rs));
        }

        return clients.stream().collect(Collectors.toList()); // Use Streams to collect the clients
    }
}
