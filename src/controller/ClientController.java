package controller;

import java.util.List;
import entity.Client;
import service.iter.IClientService;

public class ClientController {
    private final IClientService clientService;

    // Constructor with Dependency Injection (DI)
    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    public void registerClient(String name, String address, String phone, boolean isProfessional) {
        Client client = new Client();
        client.setNom(name);
        client.setAdresse(address);
        client.setTelephone(phone);
        client.setEstProfessionnel(isProfessional);

        // Calculate discount or special tax conditions based on client type
        double discount = 0.0;
        if (isProfessional) {
            discount = calculateProfessionalDiscount(client);
            System.out.println("This is a professional client. Discount applied: " + discount + "%");
        } else {
            System.out.println("This is a regular client.");
        }

        clientService.addClient(client);
        System.out.println("Client registered successfully: " + client.getNom());
    }

    // Method to calculate discount for professional clients
    private double calculateProfessionalDiscount(Client client) {
        return 10.0;
    }

    public Client getClientById(Long id) {
        Client client = clientService.getClientById(id);
        if (client != null) {
            System.out.println("Client found: " + client.getNom());
        } else {
            System.out.println("No client found with ID: " + id);
        }
        return client;
    }

    public Client getClientByName(String name) {
        Client client = clientService.getClientByName(name);
        if (client != null) {
            System.out.println("Client found: " + client.getNom());
        } else {
            System.out.println("No client found with name: " + name);
        }
        return client;
    }

}
