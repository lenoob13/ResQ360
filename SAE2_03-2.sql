-- Nettoie les tables dans l'ordre
DELETE FROM EstAffecteA;
DELETE FROM Possede;
DELETE FROM Besoin;
DELETE FROM Concerne;
DELETE FROM DPS;
DELETE FROM Sport;
DELETE FROM Site;
DELETE FROM Journee;
DELETE FROM Competence;
DELETE FROM Secouriste;

/* 

Permet de supprimer les tables si elles existent

DROP TABLE IF EXISTS EstAffecteA;
DROP TABLE IF EXISTS Possede;
DROP TABLE IF EXISTS Besoin;
DROP TABLE IF EXISTS Concerne;
DROP TABLE IF EXISTS DPS;
DROP TABLE IF EXISTS Sport;
DROP TABLE IF EXISTS Site;
DROP TABLE IF EXISTS Journee;
DROP TABLE IF EXISTS Competence;
DROP TABLE IF EXISTS Secouriste;
*/

-- Table SECOURISTE
CREATE TABLE IF NOT EXISTS Secouriste (
    idSecouriste INT PRIMARY KEY,
    identifiant VARCHAR(50),
    email VARCHAR(100),
    motDePasse VARCHAR(50),
    admin BOOLEAN,
    nom VARCHAR(50),
    prenom VARCHAR(50)
);

-- Table COMPETENCE
CREATE TABLE IF NOT EXISTS Competence (
    idCompetence INT PRIMARY KEY,
    intitule VARCHAR(50) NOT NULL UNIQUE
);

-- Table POSSEDE
CREATE TABLE IF NOT EXISTS Possede (
    idSecouriste INT,
    idCompetence INT,
    CONSTRAINT pk_Possede PRIMARY KEY (idSecouriste, idCompetence),
    CONSTRAINT fk_Possede_Secouriste FOREIGN KEY (idSecouriste) REFERENCES Secouriste(idSecouriste),
    CONSTRAINT fk_Possede_Competence FOREIGN KEY (idCompetence) REFERENCES Competence(idCompetence)
);

-- Table JOURNEE
CREATE TABLE IF NOT EXISTS Journee (
    idJour INT PRIMARY KEY,
    jour INT NOT NULL,
    mois INT NOT NULL,
    annee INT NOT NULL,
    startHour INT NOT NULL,
    startMinute INT NOT NULL,
    endHour INT NOT NULL,
    endMinute INT NOT NULL
);


-- Table SITE
CREATE TABLE IF NOT EXISTS Site (
    idSite VARCHAR(50) PRIMARY KEY,
    nom VARCHAR(50),
    longitude FLOAT,
    latitude FLOAT
);

-- Table SPORT
CREATE TABLE IF NOT EXISTS Sport (
    idSport VARCHAR(50) PRIMARY KEY,
    nom VARCHAR(50)
);

-- Table DPS
CREATE TABLE IF NOT EXISTS DPS (
    idDPS INT PRIMARY KEY,
    idJournee INT NOT NULL,
    idSite VARCHAR(50) NOT NULL,
    note VARCHAR(500),
    CONSTRAINT fk_DPS_Journee FOREIGN KEY (idJournee) REFERENCES Journee(idJour),
    CONSTRAINT fk_DPS_Site FOREIGN KEY (idSite) REFERENCES Site(idSite)
);


-- Table CONCERNE
CREATE TABLE IF NOT EXISTS Concerne (
    idDPS INT,
    idSport VARCHAR(50),
    CONSTRAINT pk_Concerne PRIMARY KEY (idDPS, idSport),
    CONSTRAINT fk_Concerne_DPS FOREIGN KEY (idDPS) REFERENCES DPS(idDPS),
    CONSTRAINT fk_Concerne_Sport FOREIGN KEY (idSport) REFERENCES Sport(idSport)
);

-- Table BESOIN
CREATE TABLE IF NOT EXISTS Besoin (
    idDPS INT,
    idCompetence INT,
    nombre INT,
    CONSTRAINT pk_Besoin PRIMARY KEY (idDPS, idCompetence),
    CONSTRAINT fk_Besoin_DPS FOREIGN KEY (idDPS) REFERENCES DPS(idDPS),
    CONSTRAINT fk_Besoin_Competence FOREIGN KEY (idCompetence) REFERENCES Competence(idCompetence)
);

-- Table EST AFFECTE A
CREATE TABLE IF NOT EXISTS EstAffecteA (
    idSecouriste INT,
    idDPS INT,
    CONSTRAINT pk_EstAffecteA PRIMARY KEY (idSecouriste, idDPS),
    CONSTRAINT fk_EstAffecteA_Secouriste FOREIGN KEY (idSecouriste) REFERENCES Secouriste(idSecouriste),
    CONSTRAINT fk_EstAffecteA_DPS FOREIGN KEY (idDPS) REFERENCES DPS(idDPS)
);
-- A GARDER A LA CREATION DE LA BDD -----------------
INSERT INTO Competence (idCompetence, intitule)
VALUES
(0, 'CP'),
(1, 'CE'),
(2, 'CO'),
(3, 'PSE1'),
(4, 'PSE2'),
(5, 'SSA'),
(6, 'VPSP'),
(7, 'PBC'),
(8, 'PBF');

-- Tests --------------------------

INSERT INTO Secouriste (idSecouriste, identifiant, email, motDePasse, admin, nom, prenom)
VALUES
(1, 'user1', 'j.dupont@email.com', 'user', FALSE, 'Dupont', 'Julie'),
(2, 'user2', 'j.dupont@email.com', 'user', FALSE, 'Dupond', 'Juliette'),
(3, 'user3', 'j.dupont@email.com', 'user', FALSE, 'Dupon', 'Jul'),
(100, 'admin', 'admin@jaitoutlesdroits.tg', 'admin', TRUE, 'Admin', 'Admin');

INSERT INTO Possede (idSecouriste, idCompetence)
VALUES
(2, 2),
(1, 2),
(1, 3);

INSERT INTO Site (idSite, nom, longitude, latitude)
VALUES
('SITE001', 'Stade Municipal', 2.35, 48.86),
('SITE002', 'Velodrome', 9.81, 0.1);

INSERT INTO Journee (idJour, jour, mois, annee, startHour, startMinute, endHour, endMinute)
VALUES
(1, 21, 6, 2025, 9, 0, 18, 0),
(2, 28, 3, 2026, 12, 0, 19, 30);

INSERT INTO Sport (idSport, nom)
VALUES
('SP001', 'Football'),
('SP002', 'Basketball');

INSERT INTO DPS (idDPS, idJournee, idSite,note)
VALUES
(1, 1, 'SITE001','blablablablabla');

INSERT INTO Concerne (idDPS, idSport)
VALUES
(1, 'SP001'),
(1, 'SP002');

INSERT INTO Besoin (idDPS, idCompetence, nombre)
VALUES
(1, 2, 2),
(1, 3, 1);

INSERT INTO EstAffecteA (idSecouriste, idDPS)
VALUES
(1, 1),
(2, 1);
