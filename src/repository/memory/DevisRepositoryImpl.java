package repository.memory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Devis;
import entity.Projet;
import repository.inter.IDevisRepository;

public class DevisRepositoryImpl implements IDevisRepository {
    private Connection connection;

    public DevisRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addDevis(Devis devis) {
        String sql = "INSERT INTO Devis (montantEstime, dateEmission, dateValidite, accepte, projet_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, devis.getMontantEstime());
            stmt.setDate(2, new java.sql.Date(devis.getDateEmission().getTime()));
            stmt.setDate(3, new java.sql.Date(devis.getDateValidite().getTime()));
            stmt.setBoolean(4, devis.isAccepte());
            stmt.setLong(5, devis.getProjet().getId()); // Assuming the Projet entity has getId() method

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Devis getDevisById(int id) {
        String sql = "SELECT * FROM Devis WHERE id = ?";
        Devis devis = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                devis = mapResultSetToDevis(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return devis;
    }

    @Override
    public void updateAccDevis(Devis devis) {
        String sql = "UPDATE Devis SET accepte = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, devis.isAccepte());
            stmt.setInt(2, devis.getId()); // Assuming Devis has a getId() method

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Devis mapResultSetToDevis(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        double montantEstime = rs.getDouble("montantEstime");
        Date dateEmission = rs.getDate("dateEmission");
        Date dateValidite = rs.getDate("dateValidite");
        boolean accepte = rs.getBoolean("accepte");

        // Retrieve Projet object (you might need to implement this correctly)
        int projetId = rs.getInt("projet_id");
        Projet projet = new Projet(); // Placeholder, implement the correct way to fetch Projet

        return new Devis(id, montantEstime, dateEmission, dateValidite, accepte, projet, 0, 0); // Replace 0s with
                                                                                                // actual values if
                                                                                                // needed
    }

    @Override
    public Devis findByProjetId(Long projetId) {
        String sql = "SELECT * FROM Devis WHERE projet_id = ?";
        Devis devis = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, projetId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                devis = mapResultSetToDevis(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return devis;
    }

    // @Override
    // public List<Devis> findAll() {
    // String sql = "SELECT * FROM Devis";
    // List<Devis> devisList = new ArrayList<>();

    // try (Statement stmt = connection.createStatement();
    // ResultSet rs = stmt.executeQuery(sql)) {

    // while (rs.next()) {
    // devisList.add(mapResultSetToDevis(rs));
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }

    // return devisList;
    // }
    // @Override
    // public Devis findById(int id) {
    // String sql = "SELECT * FROM Devis WHERE id = ?";
    // Devis devis = null;

    // try (PreparedStatement stmt = connection.prepareStatement(sql)) {
    // stmt.setInt(1, id);
    // ResultSet rs = stmt.executeQuery();

    // if (rs.next()) {
    // devis = mapResultSetToDevis(rs);
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }

    // return devis;
    // }

    // private Devis mapResultSetToDevis(ResultSet rs) throws SQLException {
    // int id = rs.getInt("id");
    // double montantEstime = rs.getDouble("montantEstime");
    // Date dateEmission = rs.getDate("dateEmission");
    // Date dateValidite = rs.getDate("dateValidite");
    // boolean accepte = rs.getBoolean("accepte");

    // // Assume there's a way to retrieve a Projet object by ID (using a repository
    // or
    // // similar)
    // // This is just a placeholder; you need to fetch the Projet object
    // appropriately
    // int projetId = rs.getInt("projet_id");
    // Projet projet = new ProjetRepositoryImpl(connection).findById(projetId);

    // return new Devis(id, montantEstime, dateEmission, dateValidite, accepte,
    // projet);
    // }

}
