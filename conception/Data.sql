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
VALUES (1, 1, 'Toyota', 'Corolla', 5, 'ABC123', 50.0, 5.0),
       (2, 2, 'Mercedes', 'Sprinter', 20, 'DEF456', 100.0, 10.0),
       (3, 2, 'Volvo', 'FH12', 2, 'GHI789', 200.0, 20.0);

-- Vehicule_Mouvement_Statut
INSERT INTO Vehicule_Mouvement_Statut (id_vehicule, date_mouvement, id_nouveau_statut, observation)
VALUES (1, '2026-01-01 00:00:00', 1, 'Initial status'),
       (2, '2026-01-01 00:00:00', 1, 'Initial status'),
       (3, '2026-01-01 00:00:00', 1, 'Initial status');

-- Vehicule_Entretien
INSERT INTO Vehicule_Entretien (id_vehicule, motif, date_debut_entretien, date_fin_entretien, montant_depense)
VALUES (1, 'Maintenance régulière', '2026-01-01 00:00:00', '2026-01-02 00:00:00', 100.0),
       (2, 'Maintenance régulière', '2026-01-01 00:00:00', '2026-01-02 00:00:00', 200.0),
       (3, 'Maintenance régulière', '2026-01-01 00:00:00', '2026-01-02 00:00:00', 300.0);

-- Chauffeur_Statut
INSERT INTO Chauffeur_Statut (libelle, score, span_html)
VALUES ('Actif', 10, '<span class="badge bg-success">Actif</span>'),
       ('En congé', 5, '<span class="badge bg-warning">En congé</span>'),
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

-- Ligne
INSERT INTO Ligne (id_ville_depart, id_ville_arrivee, distance_km)
VALUES (1, 2, 300.0),
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

-- Trajet_Statut
INSERT INTO Trajet_Statut (libelle, score, span_html)
VALUES ('En cours', 10, '<span class="badge bg-info">En cours</span>'),
       ('Terminé', 20, '<span class="badge bg-success">Terminé</span>'),
       ('Annulé', 0, '<span class="badge bg-danger">Annulé</span>');

-- Trajet
INSERT INTO Trajet (id_ligne, id_chauffeur, id_vehicule, nombre_passager, id_trajet_statut, datetime_depart,
                    datetime_arrivee, frais_unitaire)
VALUES (1, 1, 1, 5, 1, '2026-01-01 00:00:00', '2026-01-01 10:00:00', 10.0),
       (2, 2, 2, 20, 1, '2026-01-01 00:00:00', '2026-01-01 12:00:00', 20.0),
       (3, 3, 3, 2, 1, '2026-01-01 00:00:00', '2026-01-01 14:00:00', 30.0);

-- Trajet_Mouvement_Statut
INSERT INTO Trajet_Mouvement_Statut (id_trajet, date_mouvement, id_nouveau_statut, observation)
VALUES (1, '2026-01-01 00:00:00', 1, 'Initial status'),
       (2, '2026-01-01 00:00:00', 1, 'Initial status'),
       (3, '2026-01-01 00:00:00', 1, 'Initial status');

-- Trajet_Motif_Arret
INSERT INTO Trajet_Motif_Arret (libelle)
VALUES ('Panne'),
       ('Repos'),
       ('Ravitaillement');

-- Trajet_Arret_Detail
INSERT INTO Trajet_Arret_Detail (id_trajet, id_caisse, id_ville, id_trajet_motif_arret, montant_depense, datetime_debut,
                                 datetime_fin)
VALUES (1, NULL, 1, 1, 50.0, '2026-01-01 05:00:00', '2026-01-01 06:00:00'),
       (2, NULL, 2, 2, 100.0, '2026-01-01 06:00:00', '2026-01-01 07:00:00'),
       (3, NULL, 3, 3, 150.0, '2026-01-01 07:00:00', '2026-01-01 08:00:00');

-- Trajet_Carburant_Detail
INSERT INTO Trajet_Carburant_Detail (id_trajet, id_caisse, id_ville, id_carburant_type, datetime,
                                     quantite_carburant_ajoute, taux_carburant)
VALUES (1, NULL, 1, 1, '2026-01-01 05:00:00', 10.0, 1.5),
       (2, NULL, 2, 2, '2026-01-01 06:00:00', 20.0, 1.2),
       (3, NULL, 3, 2, '2026-01-01 07:00:00', 30.0, 1.2);

-- Type_Client
INSERT INTO Type_Client (libelle)
VALUES ('Particulier'),
       ('Entreprise');

-- Client
INSERT INTO Client (id_type_client, nom_client, telephone, email)
VALUES (1, 'ClientDefault', '0341234567', 'default@example.com'),
       (2, 'Client2', '0342345678', 'client2@example.com'),
       (1, 'Client3', '0343456789', 'client3@example.com');

-- Type_Mouvement
INSERT INTO Type_Mouvement (libelle)
VALUES ('Entrée'),
       ('Sortie');

-- Mode_Paiement
INSERT INTO Mode_Paiement (libelle)
VALUES ('Cash'),
       ('Carte'),
       ('Virement');

-- Reservation_Statut
INSERT INTO Reservation_Statut (libelle, score)
VALUES ('Confirmée', 10),
       ('Annulée', 0),
       ('En attente', 5);

-- Trajet_Reservation
INSERT INTO Trajet_Reservation (id_client, id_trajet, id_reservation_statut, numero_siege, nom_passager,
                                date_reservation, nombre_place_reservation)
VALUES (1, 1, 1, 'A1', 'Passager1', '2026-01-01 00:00:00', 1),
       (2, 2, 1, 'A2', 'Passager2', '2026-01-01 00:00:00', 1),
       (3, 3, 1, 'A3', 'Passager3', '2026-01-01 00:00:00', 1)
;

-- Trajet_Reservation_Mouvement_Statut
INSERT INTO Trajet_Reservation_Mouvement_Statut (id_trajet_reservation, date_mouvement, id_nouveau_statut, observation)
VALUES (1, '2026-01-01 00:00:00', 1, 'Initial status'),
       (2, '2026-01-01 00:00:00', 1, 'Initial status'),
       (3, '2026-01-01 00:00:00', 1, 'Initial status');

-- Trajet_Reservation_Paiement
INSERT INTO Trajet_Reservation_Paiement (id_client, id_trajet_reservation, id_caisse, montant, id_mode_paiement,
                                         date_paiement)
VALUES (1, 1, NULL, 10.0, 1, '2026-01-01 00:00:00'),
       (2, 2, NULL, 20.0, 2, '2026-01-01 00:00:00'),
       (3, 3, NULL, 30.0, 3, '2026-01-01 00:00:00');

-- Trajet_Finance
INSERT INTO Trajet_Finance (id_trajet, montant, id_type_mouvement, date_mouvement)
VALUES (1, 10.0, 1, '2026-01-01 00:00:00'),
       (2, 20.0, 1, '2026-01-01 00:00:00'),
       (3, 30.0, 1, '2026-01-01 00:00:00');

-- Prevision_Finance
INSERT INTO Prevision_Finance (id_trajet, id_entite_origine, table_origine, montant, id_type_mouvement, date,
                               description)
VALUES (1, NULL, 'Trajet', 10.0, 1, '2026-01-01 00:00:00', 'Prévision pour trajet 1'),
       (2, NULL, 'Trajet', 20.0, 1, '2026-01-01 00:00:00', 'Prévision pour trajet 2'),
       (3, NULL, 'Trajet', 30.0, 1, '2026-01-01 00:00:00', 'Prévision pour trajet 3');

-- Prevision_Trajet
INSERT INTO Prevision_Trajet (id_ligne, id_chauffeur, id_vehicule, nombre_passager, id_trajet_statut, datetime_depart,
                              datetime_arrivee, frais_unitaire)
VALUES (1, 1, 1, 5, 1, '2026-01-01 00:00:00', '2026-01-01 10:00:00', 10.0),
       (2, 2, 2, 20, 1, '2026-01-01 00:00:00', '2026-01-01 12:00:00', 20.0),
       (3, 3, 3, 2, 1, '2026-01-01 00:00:00', '2026-01-01 14:00:00', 30.0);

-- Caisse_Type
INSERT INTO Caisse_Type (libelle)
VALUES ('Principale'),
       ('Secondaire');

-- Caisse
INSERT INTO Caisse (id_caisse_type, nom, solde_initial)
VALUES (1, 'Caisse1', 1000.0),
       (2, 'Caisse2', 2000.0);

-- Caisse_Mouvement
INSERT INTO Caisse_Mouvement (id_caisse, id_entite_origine, table_origine, id_type_mouvement, montant, motif,
                              date_mouvement)
VALUES (1, NULL, 'Trajet', 1, 10.0, 'Paiement trajet 1', '2026-01-01 00:00:00'),
       (2, NULL, 'Trajet', 1, 20.0, 'Paiement trajet 2', '2026-01-01 00:00:00'),
       (1, NULL, 'Trajet', 2, 5.0, 'Remboursement trajet 1', '2026-01-01 00:00:00');