package controller;

import java.util.Optional;

import entity.Client;
import service.iter.IClientService;

public class ClientController {
    private final IClientService clientService;

    // Constructor with Dependency Injection (DI)
    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    public Client registerClient(String name, String address, String phone, boolean isProfessional) {
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
        return client; // Return the created Client object
    }

    // Method to calculate discount for professional clients
    private double calculateProfessionalDiscount(Client client) {
        return 10.0;
    }

    public Optional<Client> getClientById(Long id) {
        Optional<Client> client = clientService.getClientById(id);
        if (client.isPresent()) {
            System.out.println("Client found: " + client.get().getNom());
        } else {
            System.out.println("No client found with ID: " + id);
        }
        return client;
    }
    
    public Optional<Client> getClientByName(String name) {
        Optional<Client> client = clientService.getClientByName(name);
        if (client.isPresent()) {
            System.out.println("Client found: " + client.get().getNom());
        } else {
            System.out.println("No client found with name: " + name);
        }
        return client;
    }
    
    public boolean isUserProfessional(Long clientId) {
        Optional<Client> client = clientService.getClientById(clientId);
        if (!client.isPresent()) {
            System.out.println("No client found for ID: " + clientId);
            return false; // or throw an exception, based on your design
        }
        return client.get().isEstProfessionnel();
    }
    

}
