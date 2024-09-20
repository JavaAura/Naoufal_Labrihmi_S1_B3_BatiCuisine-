package entity;

public abstract class Composant {
    private int id;
    private String nom;
    private String typeComposant;
    private double tauxTVA;
    private Projet projet;

    public Composant() {
    }

    public Composant(int id, String nom, String typeComposant, double tauxTVA, Projet projet) {
        this.id = id;
        this.nom = nom;
        this.typeComposant = typeComposant;
        this.tauxTVA = tauxTVA;
        this.projet = projet;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTypeComposant() {
        return typeComposant;
    } // Update return type

    public void setTypeComposant(String typeComposant) {
        this.typeComposant = typeComposant;
    }

    public double getTauxTVA() {
        return tauxTVA;
    }

    public void setTauxTVA(double tauxTVA) {
        this.tauxTVA = tauxTVA;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }
}
