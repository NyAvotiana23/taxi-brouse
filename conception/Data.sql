-- ============================================
-- Complete Database INSERT Script
-- Taxi Management System (MDG Taxi)
-- Corrigé pour PostgreSQL
-- ============================================

-- ============================================
-- CARBURANT (Fuel Types and Rates)
-- ============================================

-- Carburant_Type
INSERT INTO Carburant_Type (libelle, dernier_taux)
VALUES ('Essence', 1.5),
       ('Diesel', 1.2),
       ('Gaz Naturel', 0.8);

-- Carburant_Mouvement_Taux
INSERT INTO Carburant_Mouvement_Taux (id_carburant_type, ancien_taux, nouveau_taux, date_mouvement)
VALUES (1, 1.4, 1.5, '2026-01-01 00:00:00'),
       (2, 1.1, 1.2, '2026-01-01 00:00:00'),
       (3, 0.7, 0.8, '2026-01-01 00:00:00');

-- Additional Carburant_Type
INSERT INTO Carburant_Type (libelle, dernier_taux)
VALUES ('Ethanol', 1.1),
       ('Hybride', 1.3),
       ('Electrique', 0.0);

-- Additional Carburant_Mouvement_Taux
INSERT INTO Carburant_Mouvement_Taux (id_carburant_type, ancien_taux, nouveau_taux, date_mouvement)
VALUES (4, 1.0, 1.1, '2026-02-01 00:00:00'),
       (5, 1.2, 1.3, '2026-02-01 00:00:00'),
       (6, 0.0, 0.0, '2026-02-01 00:00:00');

INSERT INTO Caisse_Type (libelle)
VALUES ('Caisse Principale'),
       ('Caisse Secondaire');

INSERT INTO Caisse (id_caisse_type, nom, solde_initial)
VALUES (1, 'Caisse 1', 1000000.0),
       (2, 'Caisse 2', 500000.0);

-- ============================================
-- DEVISE (Currency and Exchange Rates)
-- ============================================

-- Devise
INSERT INTO Devise (libelle, dernier_taux)
VALUES ('USD', 1.0),
       ('EUR', 0.9),
       ('MGA', 4000.0);

-- Devise_Mouvement_Taux
INSERT INTO Devise_Mouvement_Taux (id_devise, ancien_taux, nouveau_taux, date_mouvement)
VALUES (1, 0.9, 1.0, '2026-01-01 00:00:00'),
       (2, 0.8, 0.9, '2026-01-01 00:00:00'),
       (3, 3500.0, 4000.0, '2026-01-01 00:00:00');


-- ============================================

-- Donnees CategoriePersonne

INSERT INTO Categorie_Personne (libelle)
VALUES ('Adulte'),
       ('Enfant'),
       ('Senior');

--Donnees

-- ============================================
-- TYPE_PLACE (New: Added as per request)
-- ============================================

-- Type_Place
INSERT INTO Type_Place (nom_type_place, description)
VALUES ('Premium', 'Siege premium avec confort accru et espace supplementaire'),
       ('Standard', 'Siege standard pour un voyage economique'),
       ('VIP', 'Siege VIP avec services exclusifs'),
       ('Economique', 'Siege basique pour budgets limites');


-- Additional Devise
INSERT INTO Devise (libelle, dernier_taux)
VALUES ('GBP', 0.75),
       ('JPY', 110.0),
       ('CHF', 0.92);

-- Additional Devise_Mouvement_Taux
INSERT INTO Devise_Mouvement_Taux (id_devise, ancien_taux, nouveau_taux, date_mouvement)
VALUES (4, 0.7, 0.75, '2026-02-01 00:00:00'),
       (5, 100.0, 110.0, '2026-02-01 00:00:00'),
       (6, 0.9, 0.92, '2026-02-01 00:00:00');

-- ============================================
-- VEHICULE (Vehicles)
-- ============================================

-- Vehicule_Type
INSERT INTO Vehicule_Type (libelle)
VALUES ('Voiture'),
       ('Bus'),
       ('Camion');

-- Vehicule_Statut
INSERT INTO Vehicule_Statut (libelle, score, span_html)
VALUES ('Disponible', 10, '<span class="badge bg-success">Disponible</span>'),
       ('En maintenance', 5, '<span class="badge bg-warning">En maintenance</span>'),
       ('Hors service', 0, '<span class="badge bg-danger">Hors service</span>');

-- Vehicule
INSERT INTO Vehicule (id_type, id_type_carburant, marque, modele, maximum_passager, immatriculation, capacite_carburant,
                      depense_carburant_100km)
VALUES (1, 1, 'Toyota', 'Corolla', 5, '1244 TBK', 50.0, 5.0),
       (2, 2, 'Mercedes', 'Sprinter', 20, 'DEF456', 100.0, 10.0),
       (3, 2, 'Volvo', 'FH12', 2, 'GHI789', 200.0, 20.0);

-- Vehicule_Mouvement_Statut
INSERT INTO Vehicule_Mouvement_Statut (id_vehicule, date_mouvement, id_nouveau_statut, observation)
VALUES (1, '2026-01-01 00:00:00', 1, 'Initial status'),
       (2, '2026-01-01 00:00:00', 1, 'Initial status'),
       (3, '2026-01-01 00:00:00', 1, 'Initial status');

-- Vehicule_Entretien
INSERT INTO Vehicule_Entretien (id_vehicule, motif, date_debut_entretien, date_fin_entretien, montant_depense)
VALUES (1, 'Maintenance reguliere', '2026-01-01 00:00:00', '2026-01-02 00:00:00', 100.0),
       (2, 'Maintenance reguliere', '2026-01-01 00:00:00', '2026-01-02 00:00:00', 200.0),
       (3, 'Maintenance reguliere', '2026-01-01 00:00:00', '2026-01-02 00:00:00', 300.0);

-- Additional Vehicule_Type
INSERT INTO Vehicule_Type (libelle)
VALUES ('Moto'),
       ('Van'),
       ('SUV');


-- Additional Vehicule_Statut
INSERT INTO Vehicule_Statut (libelle, score, span_html)
VALUES ('En transit', 8, '<span class="badge bg-primary">En transit</span>'),
       ('Reserve', 7, '<span class="badge bg-info">Reserve</span>'),
       ('En test', 6, '<span class="badge bg-secondary">En test</span>');

-- Additional Vehicule
INSERT INTO Vehicule (id_type, id_type_carburant, marque, modele, maximum_passager, immatriculation, capacite_carburant,
                      depense_carburant_100km)
VALUES (4, 1, 'Honda', 'CB500', 2, 'JKL012', 20.0, 3.0),
       (5, 2, 'Ford', 'Transit', 15, 'MNO345', 80.0, 8.0),
       (6, 1, 'Jeep', 'Wrangler', 5, 'PQR678', 60.0, 12.0);

-- Additional Vehicule_Mouvement_Statut
INSERT INTO Vehicule_Mouvement_Statut (id_vehicule, date_mouvement, id_nouveau_statut, observation)
VALUES (4, '2026-02-01 00:00:00', 4, 'Changed to transit'),
       (5, '2026-02-01 00:00:00', 5, 'Reserved for trip'),
       (6, '2026-02-01 00:00:00', 6, 'Testing new vehicle');

-- Additional Vehicule_Entretien
INSERT INTO Vehicule_Entretien (id_vehicule, motif, date_debut_entretien, date_fin_entretien, montant_depense)
VALUES (4, 'Changement d''huile', '2026-02-01 00:00:00', '2026-02-02 00:00:00', 50.0),
       (5, 'Reparation freins', '2026-02-01 00:00:00', '2026-02-03 00:00:00', 150.0),
       (6, 'Verification batterie', '2026-02-01 00:00:00', '2026-02-02 00:00:00', 80.0);

-- ============================================
-- CHAUFFEUR (Drivers)
-- ============================================

-- Chauffeur_Statut
INSERT INTO Chauffeur_Statut (libelle, score, span_html)
VALUES ('Actif', 10, '<span class="badge bg-success">Actif</span>'),
       ('En conge', 5, '<span class="badge bg-warning">En conge</span>'),
       ('Suspendu', 0, '<span class="badge bg-danger">Suspendu</span>');

-- Chauffeur
INSERT INTO Chauffeur (nom, prenom, date_naissance, numero_permis)
VALUES ('Rakoto', 'Jean', '1980-01-01', 'PERMIS1'),
       ('Rabe', 'Pierre', '1985-01-01', 'PERMIS2'),
       ('Ranaivo', 'Paul', '1990-01-01', 'PERMIS3');

-- Chauffeur_Mouvement_Statut
INSERT INTO Chauffeur_Mouvement_Statut (id_chauffeur, date_mouvement, id_nouveau_statut, observation)
VALUES (1, '2026-01-01 00:00:00', 1, 'Initial status'),
       (2, '2026-01-01 00:00:00', 1, 'Initial status'),
       (3, '2026-01-01 00:00:00', 1, 'Initial status');

-- Additional Chauffeur_Statut
INSERT INTO Chauffeur_Statut (libelle, score, span_html)
VALUES ('En formation', 8, '<span class="badge bg-primary">En formation</span>'),
       ('Disponible', 9, '<span class="badge bg-success">Disponible</span>'),
       ('Retraite', 1, '<span class="badge bg-secondary">Retraite</span>');

-- Additional Chauffeur
INSERT INTO Chauffeur (nom, prenom, date_naissance, numero_permis)
VALUES ('Razafy', 'Marie', '1995-01-01', 'PERMIS4'),
       ('Andriana', 'Luc', '1975-01-01', 'PERMIS5'),
       ('Rakotomalala', 'Sophie', '1988-01-01', 'PERMIS6');

-- Additional Chauffeur_Mouvement_Statut
INSERT INTO Chauffeur_Mouvement_Statut (id_chauffeur, date_mouvement, id_nouveau_statut, observation)
VALUES (4, '2026-02-01 00:00:00', 4, 'Started training'),
       (5, '2026-02-01 00:00:00', 5, 'Now available'),
       (6, '2026-02-01 00:00:00', 6, 'Retired');

-- ============================================
-- GEOGRAPHIE (Geography: Province, Region, Ville)
-- ============================================

-- Province
INSERT INTO Province (nom)
VALUES ('Antananarivo'),
       ('Toamasina'),
       ('Fianarantsoa');

-- Region
INSERT INTO Region (id_province, nom)
VALUES (1, 'Analamanga'),
       (2, 'Atsinanana'),
       (3, 'Haute Matsiatra');

-- Ville
INSERT INTO Ville (id_region, nom)
VALUES (1, 'Antananarivo'),
       (2, 'Toamasina'),
       (3, 'Fianarantsoa');

-- Additional Province
INSERT INTO Province (nom)
VALUES ('Mahajanga'),
       ('Antsiranana'),
       ('Toliara');

-- Additional Region
INSERT INTO Region (id_province, nom)
VALUES (4, 'Boeny'),
       (5, 'Diana'),
       (6, 'Atsimo-Andrefana');

-- Additional Ville
INSERT INTO Ville (id_region, nom)
VALUES (4, 'Mahajanga'),
       (5, 'Antsiranana'),
       (6, 'Toliara');

-- ============================================
-- LIGNE (Routes and Stops)
-- ============================================

-- Ligne
INSERT INTO Ligne (id_ville_depart, id_ville_arrivee, distance_km)
VALUES (1, 4, 300.0),
       (2, 3, 400.0),
       (3, 1, 500.0);

-- Ligne_Arret
INSERT INTO Ligne_Arret (id_ville, nom_arret)
VALUES (1, 'Arret1'),
       (2, 'Arret2'),
       (3, 'Arret3');

-- Ligne_Detail
INSERT INTO Ligne_Detail (id_ligne, ordre, id_ligne_arret)
VALUES (1, 1, 1),
       (1, 2, 2),
       (2, 1, 2),
       (2, 2, 3),
       (3, 1, 3),
       (3, 2, 1);

-- Additional Ligne
INSERT INTO Ligne (id_ville_depart, id_ville_arrivee, distance_km)
VALUES (4, 5, 600.0),
       (5, 6, 700.0),
       (6, 4, 800.0);

-- Additional Ligne_Arret
INSERT INTO Ligne_Arret (id_ville, nom_arret)
VALUES (4, 'Arret4'),
       (5, 'Arret5'),
       (6, 'Arret6');

-- Additional Ligne_Detail
INSERT INTO Ligne_Detail (id_ligne, ordre, id_ligne_arret)
VALUES (4, 1, 4),
       (4, 2, 5),
       (5, 1, 5),
       (5, 2, 6),
       (6, 1, 6),
       (6, 2, 4);

-- ============================================
-- TRAJET (Trips)
-- ============================================

-- Trajet_Statut
INSERT INTO Trajet_Statut (libelle, score, span_html)
VALUES ('En cours', 10, '<span class="badge bg-info">En cours</span>'),
       ('Termine', 20, '<span class="badge bg-success">Termine</span>'),
       ('Annule', 0, '<span class="badge bg-danger">Annule</span>');

-- Trajet (CORRIGÉ: format de date PostgreSQL YYYY-MM-DD au lieu de YYYY-DD-MM)
INSERT INTO Trajet (id_ligne, id_chauffeur, id_vehicule, nombre_passager, id_trajet_statut, datetime_depart,
                    datetime_arrivee)
VALUES (1, 1, 1, 50, 2, '2026-01-20 10:00:00', '2026-01-20 12:00:00'),
       (1, 2, 1, 50, 2, '2026-01-21 10:00:00', '2026-01-21 12:00:00'),
       (1, 3, 1, 50, 2, '2026-01-21 15:00:00', '2026-01-21 17:00:00');

-- Trajet_Mouvement_Statut
INSERT INTO Trajet_Mouvement_Statut (id_trajet, date_mouvement, id_nouveau_statut, observation)
VALUES (1, '2026-01-20 12:00:00', 2, 'Trajet terminé'),
       (2, '2026-01-21 12:00:00', 2, 'Trajet terminé'),
       (3, '2026-01-21 17:00:00', 2, 'Trajet terminé');

-- Trajet_Motif_Arret
INSERT INTO Trajet_Motif_Arret (libelle)
VALUES ('Panne'),
       ('Repos'),
       ('Ravitaillement');

-- Trajet_Arret_Detail
INSERT INTO Trajet_Arret_Detail (id_trajet, id_caisse, id_ville, id_trajet_motif_arret, montant_depense, datetime_debut,
                                 datetime_fin)
VALUES (1, NULL, 1, 1, 50.0, '2026-01-20 05:00:00', '2026-01-20 06:00:00'),
       (2, NULL, 2, 2, 100.0, '2026-01-21 06:00:00', '2026-01-21 07:00:00'),
       (3, NULL, 3, 3, 150.0, '2026-01-21 07:00:00', '2026-01-21 08:00:00');

-- Trajet_Carburant_Detail
INSERT INTO Trajet_Carburant_Detail (id_trajet, id_caisse, id_ville, id_carburant_type, datetime,
                                     quantite_carburant_ajoute, taux_carburant)
VALUES (1, NULL, 1, 1, '2026-01-20 05:00:00', 10.0, 1.5),
       (2, NULL, 2, 2, '2026-01-21 06:00:00', 20.0, 1.2),
       (3, NULL, 3, 2, '2026-01-21 07:00:00', 30.0, 1.2);

-- Additional Trajet_Statut
INSERT INTO Trajet_Statut (libelle, score, span_html)
VALUES ('Prevu', 5, '<span class="badge bg-warning">Prevu</span>'),
       ('En retard', 15, '<span class="badge bg-danger">En retard</span>'),
       ('Avance', 25, '<span class="badge bg-primary">Avance</span>');

-- Additional Trajet (avec frais_unitaire en Ariary)
INSERT INTO Trajet (id_ligne, id_chauffeur, id_vehicule, nombre_passager, id_trajet_statut, datetime_depart,
                    datetime_arrivee)
VALUES (4, 4, 4, 2, 4, '2026-02-01 08:00:00', '2026-02-01 18:00:00'),
       (5, 5, 5, 15, 5, '2026-02-15 06:00:00', '2026-02-15 18:00:00'),
       (6, 6, 6, 5, 6, '2026-02-20 07:00:00', '2026-02-20 15:00:00');

-- Additional Trajet_Mouvement_Statut
INSERT INTO Trajet_Mouvement_Statut (id_trajet, date_mouvement, id_nouveau_statut, observation)
VALUES (4, '2026-02-01 00:00:00', 4, 'Scheduled'),
       (5, '2026-02-01 00:00:00', 5, 'Delayed'),
       (6, '2026-02-01 00:00:00', 6, 'Advanced');

-- Additional Trajet_Motif_Arret
INSERT INTO Trajet_Motif_Arret (libelle)
VALUES ('Controle police'),
       ('Pause repas'),
       ('Changement pneu');

-- Additional Trajet_Arret_Detail
INSERT INTO Trajet_Arret_Detail (id_trajet, id_caisse, id_ville, id_trajet_motif_arret, montant_depense, datetime_debut,
                                 datetime_fin)
VALUES (4, NULL, 4, 4, 200.0, '2026-02-01 06:00:00', '2026-02-01 07:00:00'),
       (5, NULL, 5, 5, 250.0, '2026-02-01 07:00:00', '2026-02-01 08:00:00'),
       (6, NULL, 6, 6, 300.0, '2026-02-01 08:00:00', '2026-02-01 09:00:00');

-- Additional Trajet_Carburant_Detail
INSERT INTO Trajet_Carburant_Detail (id_trajet, id_caisse, id_ville, id_carburant_type, datetime,
                                     quantite_carburant_ajoute, taux_carburant)
VALUES (4, NULL, 4, 4, '2026-02-01 06:00:00', 15.0, 1.1),
       (5, NULL, 5, 5, '2026-02-01 07:00:00', 25.0, 1.3),
       (6, NULL, 6, 6, '2026-02-01 08:00:00', 35.0, 0.0);

-- ============================================
-- CLIENT (Clients and Reservations)
-- ============================================

-- Type_Client
INSERT INTO Type_Client (libelle)
VALUES ('Particulier'),
       ('Entreprise');

-- Client
INSERT INTO Client (id_type_client, nom_client, telephone, email)
VALUES (1, 'ClientDefault', '0341234567', 'default@example.com'),
       (2, 'Client2', '0342345678', 'client2@example.com'),
       (1, 'Client3', '0343456789', 'client3@example.com');

-- Additional Type_Client
INSERT INTO Type_Client (libelle)
VALUES ('Groupe'),
       ('VIP'),
       ('Etudiant');

-- Additional Client
INSERT INTO Client (id_type_client, nom_client, telephone, email)
VALUES (3, 'Client4', '0344567890', 'client4@example.com'),
       (4, 'Client5', '0345678901', 'client5@example.com'),
       (5, 'Client6', '0346789012', 'client6@example.com');

-- ============================================
-- PAIEMENT (Payment Methods)
-- ============================================

-- Type_Mouvement
INSERT INTO Type_Mouvement (libelle)
VALUES ('Entree'),
       ('Sortie');

-- Mode_Paiement
INSERT INTO Mode_Paiement (libelle)
VALUES ('Cash'),
       ('Carte'),
       ('Virement');

-- Additional Type_Mouvement
INSERT INTO Type_Mouvement (libelle)
VALUES ('Ajustement'),
       ('Remboursement'),
       ('Transfert');

-- Additional Mode_Paiement
INSERT INTO Mode_Paiement (libelle)
VALUES ('Mobile Money'),
       ('Cheque'),
       ('Crypto');

-- ============================================
-- RESERVATION (Reservations)
-- ============================================

-- Reservation_Statut
INSERT INTO Reservation_Statut (libelle, score, span_html)
VALUES ('Confirmee', 10, '<span class="badge bg-success">Confirmee</span>'),
       ('Annulee', 0, '<span class="badge bg-danger">Annulee</span>'),
       ('En attente', 5, '<span class="badge bg-warning">En attente</span>');

;

-- Additional Reservation_Statut
INSERT INTO Reservation_Statut (libelle, score, span_html)
VALUES ('Payee', 15, '<span class="badge bg-primary">Payee</span>'),
       ('Remboursee', 2, '<span class="badge bg-info">Remboursee</span>'),
       ('Expiree', 0, '<span class="badge bg-secondary">Expiree</span>');


-- ============================================
-- FINANCE (Financial Tracking)
-- ============================================

-- Trajet_Finance
INSERT INTO Trajet_Finance (id_trajet, montant, id_type_mouvement, date_mouvement)
VALUES (1, 10.0, 1, '2026-01-01 00:00:00'),
       (2, 20.0, 1, '2026-01-01 00:00:00'),
       (3, 30.0, 1, '2026-01-01 00:00:00');

-- Prevision_Finance
INSERT INTO Prevision_Finance (id_trajet, id_entite_origine, table_origine, montant, id_type_mouvement, date,
                               description)
VALUES (1, NULL, 'Trajet', 10.0, 1, '2026-01-01 00:00:00', 'Prevision pour trajet 1'),
       (2, NULL, 'Trajet', 20.0, 1, '2026-01-01 00:00:00', 'Prevision pour trajet 2'),
       (3, NULL, 'Trajet', 30.0, 1, '2026-01-01 00:00:00', 'Prevision pour trajet 3');

-- Prevision_Trajet
INSERT INTO Prevision_Trajet (id_ligne, id_chauffeur, id_vehicule, nombre_passager, id_trajet_statut, datetime_depart,
                              datetime_arrivee, frais_unitaire)
VALUES (1, 1, 1, 5, 1, '2026-01-01 00:00:00', '2026-01-01 10:00:00', 10.0),
       (2, 2, 2, 20, 1, '2026-01-01 00:00:00', '2026-01-01 12:00:00', 20.0),
       (3, 3, 3, 2, 1, '2026-01-01 00:00:00', '2026-01-01 14:00:00', 30.0);

-- Additional Trajet_Finance
INSERT INTO Trajet_Finance (id_trajet, montant, id_type_mouvement, date_mouvement)
VALUES (4, 40.0, 3, '2026-02-01 00:00:00'),
       (5, 50.0, 4, '2026-02-01 00:00:00'),
       (6, 60.0, 5, '2026-02-01 00:00:00');

-- Additional Prevision_Finance
INSERT INTO Prevision_Finance (id_trajet, id_entite_origine, table_origine, montant, id_type_mouvement, date,
                               description)
VALUES (4, NULL, 'Trajet', 40.0, 3, '2026-02-01 00:00:00', 'Prevision pour trajet 4'),
       (5, NULL, 'Trajet', 50.0, 4, '2026-02-01 00:00:00', 'Prevision pour trajet 5'),
       (6, NULL, 'Trajet', 60.0, 5, '2026-02-01 00:00:00', 'Prevision pour trajet 6');

-- Additional Prevision_Trajet
INSERT INTO Prevision_Trajet (id_ligne, id_chauffeur, id_vehicule, nombre_passager, id_trajet_statut, datetime_depart,
                              datetime_arrivee, frais_unitaire)
VALUES (4, 4, 4, 2, 4, '2026-02-01 00:00:00', '2026-02-01 12:00:00', 40.0),
       (5, 5, 5, 15, 5, '2026-02-01 00:00:00', '2026-02-01 14:00:00', 50.0),
       (6, 6, 6, 5, 6, '2026-02-01 00:00:00', '2026-02-01 16:00:00', 60.0);

-- ============================================
-- VEHICULE_TARIF_TYPE_PLACE
-- ============================================

INSERT INTO Vehicule_Tarif_Type_Place (id_vehicule, id_type_place, tarif_unitaire, nombre_place)
VALUES (1, 3, 65000.0, 20.0),
       (1, 1, 50000.0, 20.0),
       (1, 4, 40000.0, 20.0);

-- ============================================
-- TRAJET_TARIF_TYPE_PLACE_CATEGORIE_REMISE
-- ============================================

-- Pour le trajet 1 avec catégorie Enfant (id=2)
INSERT INTO Trajet_Tarif_Type_Place_Categorie_Remise (id_type_place, id_trajet, id_categorie_personne,
                                                      tarif_unitaire_avec_remise)
VALUES (3, 1, 2, 65000), -- VIP pour Enfant
       (1, 1, 2, 50000), -- Premium pour Enfant
       (4, 1, 2, 40000); -- Economique pour Enfant

-- Pour le trajet 1 avec catégorie Adulte (id=1)
INSERT INTO Trajet_Tarif_Type_Place_Categorie_Remise (id_type_place, id_trajet, id_categorie_personne,
                                                      tarif_unitaire_avec_remise)
VALUES (3, 1, 1, 70000), -- VIP pour Adulte
       (1, 1, 1, 60000), -- Premium pour Adulte
       (4, 1, 1, 50000); -- Economique pour Adulte

-- ============================================
-- TRAJET_REMISE_POURCENTAGE
-- ============================================

INSERT INTO Trajet_Remise_Pourcentage (id_trajet, categorie_application, categorie_par_rapport, remisePourcent)
VALUES (1, 3, 2, -20);

-- ============================================
-- UNITE (Unités de mesure / configuration)
-- ============================================
INSERT INTO Unite (libelle)
VALUES ('Ariary'),
       ('Diffusion'),
       ('Mois'),
       ('Pourcentage');

-- ============================================
-- SOCIETE (Sociétés / annonceurs)
-- ============================================
INSERT INTO Societe (nom, description)
VALUES ('VaniAla', 'Entreprise de télécommunications - publicité taxi'),
       ('Lewis', 'Marque de boissons énergisantes - campagnes publicitaires'),
       ('Socobis', 'Societe biscuit'),
       ('Jejoo', 'Entreprise Magazine');

-- ============================================
-- CONFIGURATION (exemples de paramètres utiles)
-- ============================================
-- Coût unitaire de diffusion publicitaire (en Ariary)
INSERT INTO Configuration (libelle, valeur, id_unite, code)
VALUES ('Cout diffusion publicitaire taxi', '100000', 1, 'COUT_DIFFUSION_TAXI');

-- Exemple de configuration supplémentaire (optionnel)
INSERT INTO Configuration (libelle, valeur, id_unite, code)
VALUES ('Taux TVA publicité', '20', 4, 'TAUX_TVA_PUBLICITE');

-- ============================================
-- PUBLICITE (Campagnes publicitaires)
-- ============================================
INSERT INTO Publicite (id_societe, description, duree)
VALUES (1, 'Campagne publicitaire janvier 2026 - VaniAla', 'Janvier 2026'),
       (2, 'Campagne publicitaire janvier 2026 - Lewis', 'Janvier 2026'),
       (3, 'Campagne publicitaire janvier 2026 - Socobis', 'Janvier 2026'),
       (4, 'Campagne publicitaire janvier 2026 - Jejoo', 'Janvier 2026');

-- ============================================
-- DIFFUSION (Diffusions publicitaires sur trajets)
-- ============================================

INSERT INTO Diffusion (id_societe, date_creation) 
VALUES (1, '2026-01-01 00:00:00'),
       (2, '2026-01-01 00:00:00'),
       (3, '2026-01-01 00:00:00'),
       (4, '2026-01-01 00:00:00');

-- ============================================
-- DIFFUSION_DETAIL (Détails des diffusions par trajet)
-- ============================================

INSERT INTO Diffusion_Detail (id_publicite, id_diffusion, id_trajet, nombre_repetition, montant_unitaire)
VALUES (1, 1, 1, 1, 100000),
       (2, 2, 1, 1, 100000),
       (3, 3, 2, 2, 100000),
       (4, 4, 2, 1, 100000);

-- ============================================
-- DIFFUSION_PAIEMENT (Paiements des sociétés pour les diffusions)
-- ============================================
-- Les paiements peuvent être ajoutés ici si nécessaire
-- Exemple commenté:
-- INSERT INTO Diffusion_Paiement (id_diffusion, id_societe, date_paiement, montant_paye)
-- VALUES (1, 1, '2026-01-20 10:00:00', 50000);

-- 1. Trajet 1 – 40 adultes
INSERT INTO Trajet_Reservation (id_client, id_trajet, id_reservation_statut, nom_passager, date_reservation)
VALUES (1, 1, 1, 'Groupe 40 adultes T1', CURRENT_TIMESTAMP);

INSERT INTO Trajet_Reservation_Details (id_trajet_reservation, id_categorie_personne, id_type_place, nombre_places, tarif_unitaire)
VALUES (1, 1, 2, 40.0, 50000);


-- 2. Trajet 2 – 30 adultes
INSERT INTO Trajet_Reservation (id_client, id_trajet, id_reservation_statut, nom_passager, date_reservation)
VALUES (1, 2, 1, 'Groupe 30 adultes T2', CURRENT_TIMESTAMP);

INSERT INTO Trajet_Reservation_Details (id_trajet_reservation, id_categorie_personne, id_type_place, nombre_places, tarif_unitaire)
VALUES (2, 1, 2, 30.0, 50000);


-- 3. Trajet 3 – 50 adultes
INSERT INTO Trajet_Reservation (id_client, id_trajet, id_reservation_statut, nom_passager, date_reservation)
VALUES (1, 3, 1, 'Groupe 50 adultes T3', CURRENT_TIMESTAMP);

INSERT INTO Trajet_Reservation_Details (id_trajet_reservation, id_categorie_personne, id_type_place, nombre_places, tarif_unitaire)
VALUES (3, 1, 2, 50.0, 50000);


--Donnees Produit Categorie
INSERT INTO Produit_Categorie (libelle)
VALUES ('Boisson');

--Donnees Produit extra
INSERT INTO Produit_Extra (nom, prix_unitaire, id_categorie, id_societe)
VALUES ('Eau', 5000, 1, 1);

--Donnees Produit extra vente
INSERT INTO Produit_Extra_Vente (id_Produit, date, quantite, remise, prix_unitaire, id_client)
VALUES (1, '2026-01-20 10:00:00', 10, 0, 5000, 1),
(1, '2026-01-20 10:00:00', 10, 0, 5000, 1);

