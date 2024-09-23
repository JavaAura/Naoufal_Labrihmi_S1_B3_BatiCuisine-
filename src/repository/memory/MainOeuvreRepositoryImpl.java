package repository.memory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<MainOeuvre> getAllComposantsByProject(Long projectId) {
        List<MainOeuvre> mainOeuvres = new ArrayList<>();
        String sql = "SELECT * FROM MainOeuvre WHERE projet_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MainOeuvre mainOeuvre = new MainOeuvre();
                mainOeuvre.setNom(rs.getString("nom"));
                mainOeuvre.setTypeComposant(rs.getString("typeComposant"));
                mainOeuvre.setTauxTVA(rs.getDouble("tauxTVA"));
                mainOeuvre.setTauxHoraire(rs.getDouble("tauxHoraire"));
                mainOeuvre.setHeuresTravail(rs.getDouble("heuresTravail"));
                mainOeuvre.setProductiviteOuvrier(rs.getDouble("productiviteOuvrier"));
                // Set the project based on projectId if necessary
                // mainOeuvre.setProjet(new Projet(projectId)); // Assuming you have a way to
                // set it

                mainOeuvres.add(mainOeuvre);
            }
        } catch (SQLException e) {
            System.err.println("SQL error while retrieving MainOeuvre by project: " + e.getMessage());
            e.printStackTrace();
        }

        return mainOeuvres;
    }
}
