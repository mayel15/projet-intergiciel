-- creation des tables à partir des données recupérer depuis mirth 

CREATE TABLE IF NOT EXISTS PersonSimple (
    person_id VARCHAR(255),
    nom VARCHAR(255),
    prenom VARCHAR(255),
    naissance VARCHAR(255),
    sexe VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Address (
    person_id VARCHAR(255),
    address VARCHAR(255),
    ville VARCHAR(255),
    code_postale VARCHAR(10),
    pays VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Movements (
    person_id VARCHAR(255),
    numero_sejour VARCHAR(255),
    service VARCHAR(255),
    date_mouvement VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Stay (
    person_id VARCHAR(255),
    numero_sejour VARCHAR(255),
    date_debut VARCHAR(255),
    date_fin VARCHAR(255)
);

-- creation des tables à partir des données recupérées depuis le topic 1 à partir du consumer1
 
CREATE TABLE IF NOT EXISTS PersonSimpleConsom (
    person_id VARCHAR(255),
    nom VARCHAR(255),
    prenom VARCHAR(255),
    naissance VARCHAR(255),
    sexe VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS AddressConsom (
    person_id VARCHAR(255),
    address VARCHAR(255),
    ville VARCHAR(255),
    code_postale VARCHAR(10),
    pays VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS MovementsConsom (
    person_id VARCHAR(255),
    numero_sejour VARCHAR(255),
    service VARCHAR(255),
    date_mouvement VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS StayConsom (
    person_id VARCHAR(255),
    numero_sejour VARCHAR(255),
    date_debut VARCHAR(255),
    date_fin VARCHAR(255)
);

-- peupler la database
-- si vous voulez lancer enchanges entre consumer2-producer3 & producer2-consumer3-cs
-- sans indépendemment des donnees recupérées depuis mirth
/*

INSERT INTO PersonSimpleConsom (person_id, nom, prenom, naissance, sexe) VALUES
('123456789', 'Dupont', 'Jean', '1990-05-15', 'M'),
('987654321', 'Durand', 'Marie', '1985-08-22', 'F'),
('456789123', 'Martin', 'Pierre', '1978-12-03', 'M'),
('654321987', 'Dubois', 'Sophie', '1995-04-27', 'F'),
('789123456', 'Lefevre', 'Anne', '1982-07-11', 'F');

INSERT INTO AddressConsom (person_id, address, ville, code_postale, pays) VALUES
('123456789', '123 Rue de la Paix', 'Paris', '75001', 'France'),
('987654321', '456 Avenue des Lilas', 'Lyon', '69002', 'France'),
('456789123', '789 Boulevard Voltaire', 'Marseille', '13001', 'France'),
('654321987', '987 Rue du Commerce', 'Lille', '59000', 'France'),
('789123456', '456 Avenue Victor Hugo', 'Bordeaux', '33000', 'France');

INSERT INTO MovementsConsom (person_id, numero_sejour, service, date_mouvement) VALUES
('123456789', '12345', 'Cardiologie', '2024-03-01'),
('987654321', '54321', 'Pédiatrie', '2024-02-15'),
('456789123', '67890', 'Chirurgie', '2024-02-28'),
('654321987', '09876', 'Dermatologie', '2024-03-10'),
('789123456', '56789', 'Neurologie', '2024-03-05');

INSERT INTO StayConsom (person_id, numero_sejour, date_debut, date_fin) VALUES
('123456789', '12345', '2024-02-28', '2024-03-10'),
('987654321', '54321', '2024-02-10', '2024-02-20'),
('456789123', '67890', '2024-02-20', '2024-03-05'),
('654321987', '09876', '2024-03-01', '2024-03-15'),
('789123456', '56789', '2024-02-15', '2024-02-25');

*/