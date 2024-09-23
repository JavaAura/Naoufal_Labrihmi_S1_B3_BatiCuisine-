package repository.memory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Materiel;
import repository.inter.IComposantRepository;

public class MaterielRepositoryImpl implements IComposantRepository<Materiel> {
    private Connection connection;

    public MaterielRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addComposant(Materiel materiel) {
        String sql = "INSERT INTO Materiel (nom, typeComposant, tauxTVA, projet_id, coutUnitaire, quantite, coutTransport, coefficientQualite) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, materiel.getNom());
            stmt.setString(2, materiel.getTypeComposant());
            stmt.setDouble(3, materiel.getTauxTVA());
            stmt.setLong(4, materiel.getProjet().getId());
            stmt.setDouble(5, materiel.getCoutUnitaire());
            stmt.setDouble(6, materiel.getQuantite());
            stmt.setDouble(7, materiel.getCoutTransport());
            stmt.setDouble(8, materiel.getCoefficientQualite());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL error while inserting Materiel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Materiel> getAllComposantsByProject(Long projectId) {
        List<Materiel> materiels = new ArrayList<>();
        String sql = "SELECT * FROM Materiel WHERE projet_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Materiel materiel = new Materiel();
                materiel.setNom(rs.getString("nom"));
                materiel.setTypeComposant(rs.getString("typeComposant"));
                materiel.setTauxTVA(rs.getDouble("tauxTVA"));
                materiel.setCoutUnitaire(rs.getDouble("coutUnitaire"));
                materiel.setQuantite(rs.getDouble("quantite"));
                materiel.setCoutTransport(rs.getDouble("coutTransport"));
                materiel.setCoefficientQualite(rs.getDouble("coefficientQualite"));
                // Set the project based on projectId if necessary
                // materiel.setProjet(new Projet(projectId)); // Assuming you have a way to set
                // it

                materiels.add(materiel);
            }
        } catch (SQLException e) {
            System.err.println("SQL error while retrieving Materiel by project: " + e.getMessage());
            e.printStackTrace();
        }

        return materiels;
    }

}
