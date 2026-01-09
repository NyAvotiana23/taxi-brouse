
# Tables : Système de Gestion Taxi-Brousse

## 1. GESTION DES CARBURANTS

**Carburant_Type** : id, libelle, dernier_taux

**Carburant_Mouvement_Taux** : id, id_carburant_type, ancien_taux, nouveau_taux, date_mouvement

## 2. GESTION DES DEVISES

**Devise** : id, libelle, dernier_taux

**Devise_Mouvement_Taux** : id, id_devise, ancien_taux, nouveau_taux, date_mouvement

## 3. GESTION DES VÉHICULES

**Vehicule_Type** : id, libelle

**Vehicule_Statut** : id, libelle

**Vehicule** : id, id_type, id_type_carburant, marque, modele, maximum_passager, immatriculation, capacite_carburant, depense_carburant_100km

**Vehicule_Mouvement_Statut** : id, id_vehicule, date_mouvement, id_ancien_statut, id_nouveau_statut, observation

**Vehicule_Entretien** : id, id_vehicule, motif, date_debut_entretien, date_fin_entretien, montant_depense

## 4. GESTION DES CHAUFFEURS

**Chauffeur_Statut** : id, libelle

**Chauffeur** : id, nom, prenom, date_naissance, numero_permis

**Chauffeur_Mouvement_Statut** : id, id_chauffeur, date_mouvement, id_ancien_statut, id_nouveau_statut, observation

## 5. GESTION GÉOGRAPHIQUE

**Province** : id, nom

**Region** : id, id_province, nom

**Ville** : id, id_region, nom

## 6. GESTION DES LIGNES

**Ligne** : id, id_ville_depart, id_ville_arrivee, distance_km

**Ligne_Arret** : id, id_ville, nom_arret

**Ligne_Detail** : id, id_ligne, ordre, id_ligne_arret

## 7. GESTION DES TRAJETS

**Trajet_Statut** : id, libelle

**Trajet** : id, id_ligne, id_chauffeur, id_vehicule, nombre_passager, id_trajet_statut, datetime_depart, datetime_arrivee, frais_unitaire

**Trajet_Mouvement_Statut** : id, id_trajet, date_mouvement, id_ancien_statut, id_nouveau_statut, observation

**Trajet_Motif_Arret** : id, libelle

**Trajet_Arret_Detail** : id, id_trajet, id_caisse, id_ville, id_trajet_motif_arret, montant_depense, datetime_debut, datetime_fin

**Trajet_Carburant_Detail** : id, id_trajet, id_caisse, id_ville, id_carburant_type, datetime, quantite_carburant_ajoute, taux_carburant

## 8. GESTION DES CLIENTS ET RÉSERVATIONS

**Type_Client** : id, libelle

**Client** : id, id_type_client, nom_client, telephone, email

**Type_Mouvement** : id, libelle

**Mode_Paiement** : id, libelle

**Reservation_Status** : id, libelle

**Trajet_Reservation** : id, id_client, id_trajet, id_reservation_statut, numero_siege, nom_passager, montant, date_reservation, nombre_place_reservation

**Trajet_Reservation_Mouvement_Status** : id, id_trajet_reservation, date_mouvement, id_ancien_statut, id_nouveau_statut, observation

**Trajet_Reservation_Paiement** : id, id_client, id_trajet_reservation, id_caisse, montant, id_mode_paiement, date_paiement

## 9. GESTION FINANCIÈRE

**Trajet_Finance** : id, id_trajet, montant, id_type_mouvement, date_mouvement

**Prevision_Finance** : id, id_trajet, id_entite_origine (pas FK), table_origine, montant, id_type_mouvement, date, description

**Prevision_Trajet** : id, id_ligne, id_chauffeur, id_vehicule, nombre_passager, id_trajet_statut, datetime_depart, datetime_arrivee, frais_unitaire

## 10. GESTION DE LA CAISSE

**Caisse_Type** : id, libelle

**Caisse** : id, id_caisse_type, nom, solde_initial

**Caisse_Mouvement** : id, id_caisse, id_entite_origine (pas FK), table_origine, id_type_mouvement, montant, motif, date_mouvement

---

# Vues : Système de Gestion Taxi-Brousse

## VUES VÉHICULES

**VM_Vehicule_Statut_Actuel** : id_vehicule, libelle_statut, date_mouvement

**VM_Vehicule_Detail** : id_vehicule, immatriculation, marque, modele, maximum_passager, capacite_carburant, depense_carburant_100km, libelle_type, type_carburant, libelle_statut, date_dernier_statut

**VM_Vehicule_Cout_Entretien** : id_vehicule, total_depense_entretien, nombre_entretiens, dernier_entretien

**VM_Vehicule_Historique_Statut** : id, id_vehicule, immatriculation, ancien_statut, nouveau_statut, date_mouvement, observation

## VUES CHAUFFEURS

**VM_Chauffeur_Statut_Actuel** : id_chauffeur, libelle_statut, date_mouvement

**VM_Chauffeur_Detail** : id_chauffeur, nom, prenom, nom_complet, date_naissance, age, numero_permis, libelle_statut, date_dernier_statut

**VM_Chauffeur_Activite** : id_chauffeur, nombre_trajets, trajets_termines, dernier_trajet, premier_trajet

**VM_Chauffeur_Historique_Statut** : id, id_chauffeur, chauffeur, ancien_statut, nouveau_statut, date_mouvement, observation

## VUES GÉOGRAPHIQUES

**VM_Ville_Detail** : id_ville, nom_ville, nom_region, nom_province, id_region, id_province

## VUES LIGNES

**VM_Ligne_Detail** : id_ligne, ville_depart, region_depart, ville_arrivee, region_arrivee, distance_km, nombre_arrets

**VM_Ligne_Itineraire** : id_ligne, ordre, nom_arret, nom_ville, nom_region

## VUES TRAJETS

**VM_Trajet_Statut_Actuel** : id_trajet, libelle_statut, date_mouvement

**VM_Trajet_Detail** : id_trajet, id_ligne, ville_depart, ville_arrivee, chauffeur, id_chauffeur, vehicule, modele_vehicule, id_vehicule, statut_trajet, datetime_depart, datetime_arrivee, nombre_passager, maximum_passager, frais_unitaire, duree_heures

**VM_Trajet_Remplissage** : id_trajet, capacite_prevue, capacite_max, places_reservees, places_restantes, taux_remplissage_pct

**VM_Trajet_Incident** : id, id_trajet, ville, motif_arret, montant_depense, datetime_debut, datetime_fin, duree_arret_heures

**VM_Trajet_Carburant** : id_trajet, nombre_ravitaillements, total_litres, cout_total_carburant, taux_moyen

## VUES RÉSERVATIONS

**VM_Reservation_Statut_Actuel** : id_trajet_reservation, libelle_statut, date_mouvement

**VM_Reservation_Detail** : id_reservation, id_trajet, nom_client, type_client, nom_passager, numero_siege, statut_reservation, montant, nombre_place_reservation, date_reservation

**VM_Paiement_Trajet** : id_trajet, nombre_paiements, total_paye, dernier_paiement, modes_paiement

## VUES FINANCIÈRES

**VM_Trajet_Finance** : id_trajet, total_recette, total_depense, benefice

**VM_Finance_Journaliere** : date_jour, total_recette, total_depense, benefice, nombre_trajets

**VM_Finance_Mensuelle** : mois, annee, numero_mois, total_recette, total_depense, benefice, nombre_trajets

**VM_Prevision_vs_Reel** : id_trajet, recette_prevue, depense_prevue, recette_reelle, depense_reelle, ecart_recette, ecart_depense, ecart_benefice

**VM_Prevision_Detail** : id, id_trajet, table_origine, id_entite_origine, type_mouvement, montant, date, description

## VUES CAISSE

**VM_Caisse_Detail** : id_caisse, libelle_type, nom, solde_initial

**VM_Caisse_Solde_Actuel** : id_caisse, solde_actuel, nombre_mouvements, dernier_mouvement

**VM_Caisse_Mouvement_Historique** : id, id_caisse, nom_caisse, date_mouvement, libelle_type_mouvement, montant, motif, table_origine, id_entite_origine

**VM_Caisse_Tracabilite** : id_mouvement, id_caisse, caisse, table_origine, id_entite_origine, type_mouvement, montant, date_mouvement, description_origine

## VUES ANALYTIQUES AVANCÉES

**VM_Performance_Ligne** : id_ligne, ville_depart, ville_arrivee, nombre_trajets, taux_remplissage_moyen, recette_totale, benefice_total, benefice_moyen_par_trajet

**VM_Performance_Vehicule** : id_vehicule, immatriculation, marque, modele, nombre_trajets, cout_entretien_total, recette_totale, benefice_total, duree_moyenne_trajet_heures