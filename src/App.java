import java.sql.Connection;

import database.DatabaseConnection;
import controller.ClientController;
import controller.DevisController;
import controller.ProjetController;
import repository.inter.IClientRepository;
import repository.inter.IProjetRepository;
import repository.inter.IDevisRepository;
import repository.memory.ClientRepositoryImpl;
import repository.memory.ProjetRepositoryImpl;
import repository.memory.DevisRepositoryImpl;
import service.impl.ClientServiceImpl;
import service.impl.DevisServiceImpl;
import service.impl.ProjetServiceImpl;
import view.ConsoleUI;

public class App {
    public static void main(String[] args) {
        try {
            // Initialize the database connection (Singleton)
            DatabaseConnection connectionInstance = DatabaseConnection.getInstance();
            Connection connection = connectionInstance.getConnection();
            System.out.println("Database connection initialized!");

            // Initialize the repositories
            IClientRepository clientRepository = new ClientRepositoryImpl(connection);
            IProjetRepository projetRepository = new ProjetRepositoryImpl(connection);
            IDevisRepository devisRepository = new DevisRepositoryImpl(connection);

            // Initialize the service implementations with repositories
            ClientServiceImpl clientService = new ClientServiceImpl(clientRepository);
            ProjetServiceImpl projetService = new ProjetServiceImpl(projetRepository);
            DevisServiceImpl devisService = new DevisServiceImpl(devisRepository);

            // Initialize the controllers with the injected services
            ClientController clientController = new ClientController(clientService);
            ProjetController projetController = new ProjetController(projetService, clientController);
            DevisController devisController = new DevisController(devisService, projetService);

            // Initialize the ConsoleUI with the controllers
            ConsoleUI consoleUI = new ConsoleUI(projetController, devisController, clientController);
            consoleUI.start();

        } catch (Exception e) {
            System.err.println("Error initializing the database connection: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Application is running...");
    }
}
