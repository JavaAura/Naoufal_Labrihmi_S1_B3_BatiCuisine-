package repository.memory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    public Client getClientById(Long id) {
        Client client = null;
        String sql = "SELECT * FROM Client WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client = new Client();
                client.setId(rs.getLong("id")); // Assuming Client class has an id field
                client.setNom(rs.getString("nom"));
                client.setAdresse(rs.getString("adresse"));
                client.setTelephone(rs.getString("telephone"));
                client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Client getClientByName(String name) {
        Client client = null;
        String sql = "SELECT * FROM Client WHERE nom = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client = new Client();
                client.setId(rs.getLong("id")); // Assuming Client class has an id field
                client.setNom(rs.getString("nom"));
                client.setAdresse(rs.getString("adresse"));
                client.setTelephone(rs.getString("telephone"));
                client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getLong("id"));
                client.setNom(rs.getString("nom"));
                client.setAdresse(rs.getString("adresse"));
                client.setTelephone(rs.getString("telephone"));
                client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
}
