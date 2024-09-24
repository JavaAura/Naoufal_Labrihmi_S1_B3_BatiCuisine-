package repository.memory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Client;
import entity.Projet;
import entity.enums.EtatProjet;
import repository.inter.IProjetRepository;

public class ProjetRepositoryImpl implements IProjetRepository {
    private Connection connection;

    public ProjetRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addProject(Projet projet) {
        String sql = "INSERT INTO Projet (nomProjet, margeBeneficiaire, coutTotal, etatProjet, client_id, surface) "
                + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id"; // Added surface to query

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projet.getNomProjet());
            stmt.setDouble(2, projet.getMargeBeneficiaire());
            stmt.setDouble(3, projet.getCoutTotal());
            stmt.setObject(4, projet.getEtatProjet().name(), java.sql.Types.OTHER);
            stmt.setLong(5, projet.getClient().getId());
            stmt.setDouble(6, projet.getSurface()); // Set the surface value

            // Execute the query and get the generated ID
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Long generatedId = rs.getLong("id");
                projet.setId(generatedId); // Set the generated ID in the project object
                System.out.println("Project added successfully! ID: " + generatedId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Projet> getAllProjets() {
        List<Projet> projets = new ArrayList<>();
        String sql = "SELECT p.id, p.nomProjet, p.margeBeneficiaire, p.coutTotal, p.etatProjet, p.surface, "
                + "c.nom AS clientNom, c.adresse AS clientAdresse, c.telephone AS clientTelephone, c.estProfessionnel "
                + "FROM Projet p JOIN Client c ON p.client_id = c.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getLong("client_id"));
                client.setNom(rs.getString("clientNom"));
                client.setAdresse(rs.getString("clientAdresse"));
                client.setTelephone(rs.getString("clientTelephone"));
                client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));

                Projet projet = new Projet();
                projet.setId(rs.getLong("id"));
                projet.setNomProjet(rs.getString("nomProjet"));
                projet.setMargeBeneficiaire(rs.getDouble("margeBeneficiaire"));
                projet.setCoutTotal(rs.getDouble("coutTotal"));
                projet.setEtatProjet(EtatProjet.valueOf(rs.getString("etatProjet")));
                projet.setSurface(rs.getDouble("surface")); // Set surface value

                projet.setClient(client);
                projets.add(projet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projets;
    }

    @Override
    public Projet getProjectById(Long id) {
        Projet projet = null;
        String sql = "SELECT p.id, p.nomProjet, p.margeBeneficiaire, p.coutTotal, p.etatProjet, p.surface, "
                + "c.nom AS clientNom, c.adresse AS clientAdresse, c.telephone AS clientTelephone, c.estProfessionnel "
                + "FROM Projet p JOIN Client c ON p.client_id = c.id WHERE p.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                projet = new Projet();
                projet.setId(rs.getLong("id"));
                projet.setNomProjet(rs.getString("nomProjet"));
                projet.setMargeBeneficiaire(rs.getDouble("margeBeneficiaire"));
                projet.setCoutTotal(rs.getDouble("coutTotal"));
                projet.setEtatProjet(EtatProjet.valueOf(rs.getString("etatProjet")));
                projet.setSurface(rs.getDouble("surface")); // Set surface value

                Client client = new Client();
                client.setNom(rs.getString("clientNom"));
                client.setAdresse(rs.getString("clientAdresse"));
                client.setTelephone(rs.getString("clientTelephone"));
                client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));

                projet.setClient(client); // Set the client in the project
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projet;
    }

    @Override
    public Projet getProjectByName(String name) {
        Projet projet = null;
        String sql = "SELECT p.id, p.nomProjet, p.margeBeneficiaire, p.coutTotal, p.etatProjet, p.surface, "
                + "c.nom AS clientNom, c.adresse AS clientAdresse, c.telephone AS clientTelephone, c.estProfessionnel "
                + "FROM Projet p JOIN Client c ON p.client_id = c.id WHERE p.nomProjet = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                projet = new Projet();
                projet.setId(rs.getLong("id"));
                projet.setNomProjet(rs.getString("nomProjet"));
                projet.setMargeBeneficiaire(rs.getDouble("margeBeneficiaire"));
                projet.setCoutTotal(rs.getDouble("coutTotal"));
                projet.setEtatProjet(EtatProjet.valueOf(rs.getString("etatProjet")));
                projet.setSurface(rs.getDouble("surface")); // Set surface value

                Client client = new Client();
                client.setNom(rs.getString("clientNom"));
                client.setAdresse(rs.getString("clientAdresse"));
                client.setTelephone(rs.getString("clientTelephone"));
                client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));

                projet.setClient(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projet;
    }

    @Override
    public void updateProjectCost(Long projectId, double totalCost, double profitMargin) {
        String sql = "UPDATE Projet SET coutTotal = ?, margeBeneficiaire = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, totalCost);
            stmt.setDouble(2, profitMargin);
            stmt.setLong(3, projectId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Project cost updated successfully for project ID: " + projectId);
            } else {
                System.out.println("No project found with ID: " + projectId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getTotalCost(Long projectId) {
        double totalCost = 0.0;
        String sql = "SELECT coutTotal FROM Projet WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, projectId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalCost = rs.getDouble("coutTotal");
            } else {
                System.out.println("No project found with ID: " + projectId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCost;
    }

    @Override
    public List<Projet> getProjetsWithClients() {
        List<Projet> projets = new ArrayList<>();
        String sql = "SELECT p.id, p.nomProjet, p.margeBeneficiaire, p.coutTotal, p.etatProjet, p.surface, "
                + "p.client_id, c.nom AS clientNom, c.adresse AS clientAdresse, c.telephone AS clientTelephone, c.estProfessionnel "
                + "FROM Projet p JOIN Client c ON p.client_id = c.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getLong("client_id")); // Ensure this column name matches your query
                client.setNom(rs.getString("clientNom"));
                client.setAdresse(rs.getString("clientAdresse"));
                client.setTelephone(rs.getString("clientTelephone"));
                client.setEstProfessionnel(rs.getBoolean("estProfessionnel"));

                Projet projet = new Projet();
                projet.setId(rs.getLong("id"));
                projet.setNomProjet(rs.getString("nomProjet"));
                projet.setMargeBeneficiaire(rs.getDouble("margeBeneficiaire"));
                projet.setCoutTotal(rs.getDouble("coutTotal"));
                projet.setEtatProjet(EtatProjet.valueOf(rs.getString("etatProjet")));
                projet.setSurface(rs.getDouble("surface")); // Set surface value

                projet.setClient(client);
                projets.add(projet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projets;
    }

    @Override
    public void updateProjectStateByName(String projectName, EtatProjet newState) {
        String sql = "UPDATE Projet SET etatProjet = ?::etatprojet WHERE nomProjet = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newState.name()); // Correctly set the new state
            stmt.setString(2, projectName); // Use projectName here
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Project state updated successfully for project: " + projectName);
            } else {
                System.out.println("No project found with the name: " + projectName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
