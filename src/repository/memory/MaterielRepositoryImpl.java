package repository.memory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
