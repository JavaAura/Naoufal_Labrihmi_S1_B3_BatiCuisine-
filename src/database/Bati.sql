CREATE TYPE EtatProjet AS ENUM( 'EN_COURS', 'TERMINE', 'ANNULE' );

-- Create Client table with unique 'nom', 'adresse', and 'telephone'
CREATE TABLE Client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL UNIQUE, -- Unique constraint on 'nom'
    adresse VARCHAR(255) NOT NULL UNIQUE, -- Unique constraint on 'adresse'
    telephone VARCHAR(20) NOT NULL UNIQUE, -- Unique constraint on 'telephone'
    estProfessionnel BOOLEAN NOT NULL
);
-- Create the Projet table
CREATE TABLE Projet (
    id SERIAL PRIMARY KEY,
    nomProjet VARCHAR(255) NOT NULL,
    margeBeneficiaire DOUBLE PRECISION,
    coutTotal DOUBLE PRECISION NOT NULL,
    etatProjet EtatProjet NOT NULL, -- Enum values can be stored as VARCHAR or use a specific type
    client_id INTEGER NOT NULL,
    -- Foreign Key Constraints
    FOREIGN KEY (client_id) REFERENCES Client (id) ON DELETE CASCADE,
    surface DOUBLE PRECISION NOT NULL
);
-- Create the Devis table
CREATE TABLE Devis (
    id SERIAL PRIMARY KEY,
    montantEstime DOUBLE PRECISION NOT NULL,
    dateEmission DATE NOT NULL,
    dateValidite DATE NOT NULL,
    accepte BOOLEAN NOT NULL,
    projet_id INTEGER NOT NULL, -- Foreign key to Projet
    -- Foreign Key Constraint
    FOREIGN KEY (projet_id) REFERENCES Projet (id) ON DELETE CASCADE
);
-- Create Composant table with foreign key to Projet
CREATE TABLE Composant (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    typeComposant VARCHAR(50) NOT NULL,
    tauxTVA DOUBLE PRECISION NOT NULL,
    projet_id INTEGER NOT NULL,  -- Foreign key to associate with Projet

-- Foreign Key Constraint
    
    FOREIGN KEY (projet_id) REFERENCES Projet(id) ON DELETE CASCADE
);
-- Create MainOeuvre table inheriting from Composant
CREATE TABLE MainOeuvre (
    tauxHoraire DOUBLE PRECISION NOT NULL,
    heuresTravail DOUBLE PRECISION NOT NULL,
    productiviteOuvrier DOUBLE PRECISION NOT NULL
) INHERITS (Composant);
-- Create Materiel table inheriting from Composant
CREATE TABLE Materiel (
    coutUnitaire DOUBLE PRECISION NOT NULL,
    quantite DOUBLE PRECISION NOT NULL,
    coutTransport DOUBLE PRECISION NOT NULL,
    coefficientQualite DOUBLE PRECISION NOT NULL
) INHERITS (Composant);