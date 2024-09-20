package repository.memory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import entity.MainOeuvre;
import repository.inter.IComposantRepository;

public class MainOeuvreRepositoryImpl implements IComposantRepository<MainOeuvre> {
    private Connection connection;

    public MainOeuvreRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addComposant(MainOeuvre mainOeuvre) {
        String sql = "INSERT INTO MainOeuvre (nom, typeComposant, tauxTVA, projet_id, tauxHoraire, heuresTravail, productiviteOuvrier) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mainOeuvre.getNom());
            stmt.setString(2, mainOeuvre.getTypeComposant());
            stmt.setDouble(3, mainOeuvre.getTauxTVA());
            stmt.setLong(4, mainOeuvre.getProjet().getId());
            stmt.setDouble(5, mainOeuvre.getTauxHoraire());
            stmt.setDouble(6, mainOeuvre.getHeuresTravail());
            stmt.setDouble(7, mainOeuvre.getProductiviteOuvrier());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL error while inserting MainOeuvre: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
