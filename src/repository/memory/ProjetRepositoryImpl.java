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
        String sql = "INSERT INTO Projet (nomProjet, margeBeneficiaire, coutTotal, etatProjet, client_id) "
                + "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projet.getNomProjet());
            stmt.setDouble(2, projet.getMargeBeneficiaire());
            stmt.setDouble(3, projet.getCoutTotal());

            // Set the etatProjet using the enum's name
            stmt.setObject(4, projet.getEtatProjet().name(), java.sql.Types.OTHER);

            stmt.setLong(5, projet.getClient().getId());

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
        String sql = "SELECT p.id, p.nomProjet, p.margeBeneficiaire, p.coutTotal, p.etatProjet, c.nom "
                + "FROM Projet p JOIN Client c ON p.client_id = c.id";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getLong("clientId"));
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

                projet.setClient(client);

                projets.add(projet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return projets;
    }

    @Override
    public Projet getProjectById(Long id) {
        Projet projet = null;
        String sql = "SELECT p.id, p.nomProjet, p.margeBeneficiaire, p.coutTotal, p.etatProjet, "
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

                // Create the client object
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
        String sql = "SELECT p.id, p.nomProjet, p.margeBeneficiaire, p.coutTotal, p.etatProjet, "
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

}
