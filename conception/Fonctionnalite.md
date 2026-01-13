# 📌 Fonctionnalités — Système de Gestion Taxi-Brousse (Interne)

## 1. Gestion des Carburants

### Fonctionnalités

- Création et gestion des types de carburant
- Suivi des taux actuels
- Historique des changements de taux
- Consultation via `Carburant_Mouvement_Taux`

### Informations suivies

- Libellé du carburant (Essence, Diesel, etc.)
- Dernier taux appliqué
- Historique complet des variations

---

## 2. Gestion des Devises

### Fonctionnalités

- Création et gestion des devises
- Suivi des taux de change
- Historique des fluctuations
- Consultation via `Devise_Mouvement_Taux`

### Informations suivies

- Libellé de la devise
- Dernier taux de change
- Historique des variations

---

## 3. Gestion des Véhicules

### Fonctionnalités

- Création et mise à jour des véhicules
- Affectation d'un type de véhicule et type de carburant
- Gestion de la capacité maximale de passagers
- Gestion de la capacité du réservoir
- Suivi de la consommation aux 100km
- Suivi de l'immatriculation
- Consultation de l'historique des statuts via `VM_Vehicule_Historique_Statut`
- Gestion des entretiens et dépenses liées via `VM_Vehicule_Cout_Entretien`
- Vue détaillée consolidée via `VM_Vehicule_Detail`

### Statuts possibles (Vehicule_Statut)

- Disponible
- En circulation
- En maintenance
- Immobilisé
- Hors service

### Flux de statut

- Changement de statut tracé dans `Vehicule_Mouvement_Statut`
- Historique consultable par véhicule
- Statut actuel accessible via `VM_Vehicule_Statut_Actuel`

---

## 4. Gestion des Chauffeurs

### Fonctionnalités

- Création et mise à jour des fiches chauffeurs
- Gestion des informations personnelles (nom, prénom, date de naissance)
- Gestion des permis de conduire
- Affectation aux trajets
- Suivi d'activité via `VM_Chauffeur_Activite` (nombre de trajets effectués, dates)
- Historique des changements de statut via `VM_Chauffeur_Historique_Statut`
- Vue détaillée avec calcul d'âge via `VM_Chauffeur_Detail`

### Statuts possibles (Chauffeur_Statut)

- Actif
- Inactif
- Suspendu
- En congé

### Flux de statut

- Tout changement est enregistré dans `Chauffeur_Mouvement_Statut`
- Statut actuel accessible via `VM_Chauffeur_Statut_Actuel`

---

## 5. Gestion Géographique

### Fonctionnalités

- Gestion des provinces
- Gestion des régions rattachées aux provinces
- Gestion des villes rattachées aux régions
- Consultation rapide via `VM_Ville_Detail` (avec région et province)
- Navigation hiérarchique Province → Région → Ville

---

## 6. Gestion des Lignes et Itinéraires

### Fonctionnalités

- Création des lignes (ville départ → ville arrivée)
- Définition de la distance en kilomètres
- Définition des arrêts intermédiaires via `Ligne_Arret`
- Ordonnancement des arrêts via `Ligne_Detail`
- Visualisation complète de l'itinéraire via `VM_Ligne_Itineraire`
- Vue consolidée des lignes via `VM_Ligne_Detail` (avec comptage des arrêts)

---

## 7. Gestion des Trajets

### Fonctionnalités

- Planification des trajets
- Affectation du chauffeur et du véhicule
- Définition du tarif unitaire
- Suivi du nombre de passagers
- Gestion des heures réelles de départ et d'arrivée
- Calcul automatique de la durée
- Consultation de l'historique des statuts via `Trajet_Mouvement_Statut`
- Vue détaillée consolidée via `VM_Trajet_Detail`
- Suivi du taux de remplissage via `VM_Trajet_Remplissage`

### Statuts possibles (Trajet_Statut)

- Prévu
- Ouvert
- En cours
- Suspendu
- Terminé
- Annulé

### Flux de statut

- Tous les changements sont enregistrés dans `Trajet_Mouvement_Statut`
- Statut actuel accessible via `VM_Trajet_Statut_Actuel`

---

## 8. Gestion des Incidents et Arrêts

### Fonctionnalités

- Déclaration d'arrêts imprévus via `Trajet_Arret_Detail`
- Association d'un motif d'arrêt
- Localisation de l'arrêt (ville)
- Suivi des dépenses liées aux incidents
- Analyse de la durée des arrêts
- Association à une caisse pour traçabilité financière
- Historique des incidents par trajet via `VM_Trajet_Incident`

### Motifs possibles (Trajet_Motif_Arret)

- Panne mécanique
- Accident
- Contrôle
- Pause prolongée
- Autre

---

## 9. Gestion du Carburant

### Fonctionnalités

- Enregistrement des ravitaillements via `Trajet_Carburant_Detail`
- Suivi de la quantité ajoutée
- Enregistrement du taux au moment du ravitaillement
- Localisation du ravitaillement (ville)
- Association à une caisse pour traçabilité financière
- Analyse de la consommation via `VM_Trajet_Carburant`
- Calcul du coût total par trajet

---

## 10. Gestion des Clients

### Fonctionnalités

- Création et gestion des clients
- Classification par type (Entreprise, Client Simple)
- Gestion des coordonnées (téléphone, email)
- Client par défaut pour réservations sans client spécifique

### Types de client (Type_Client)

- Client Simple (par défaut)
- Entreprise

---

## 11. Gestion des Réservations (Interne)

### Fonctionnalités

- Enregistrement manuel des réservations
- Association au client
- Attribution des sièges
- Gestion du nombre de places réservées
- Suivi du statut de chaque réservation
- Historique des changements de statut via `Trajet_Reservation_Mouvement_Status`
- Vue détaillée via `VM_Reservation_Detail`

### Statuts possibles (Reservation_Statut)

- Réservé
- Confirmé
- Annulé
- Utilisé

---

## 12. Gestion des Paiements

### Fonctionnalités

- Enregistrement des paiements liés aux réservations via `Trajet_Reservation_Paiement`
- Association d'un mode de paiement
- Association à une caisse pour traçabilité
- Historique des paiements par trajet via `VM_Paiement_Trajet`
- Suivi des montants encaissés
- Identification du dernier paiement

### Modes de paiement (Mode_Paiement)

- Espèces
- Mobile Money
- Carte bancaire
- Virement
- Autre

---

## 13. Gestion Financière des Trajets

### Fonctionnalités

- Enregistrement des mouvements financiers via `Trajet_Finance`
- Distinction recette / dépense
- Calcul automatique du bénéfice par trajet via `VM_Trajet_Finance`
- Analyse financière journalière via `VM_Finance_Journaliere`
- Analyse financière mensuelle via `VM_Finance_Mensuelle`
- Suivi des tendances

### Types de mouvement (Type_Mouvement)

- Recette
- Dépense

---

## 14. Prévisions et Planification

### Fonctionnalités

- Prévision des recettes et dépenses par trajet via `Prevision_Finance`
- Traçabilité de l'origine des prévisions via `id_entite_origine` et `table_origine`
- Association des prévisions aux entités sources :
  - Réservations anticipées (recettes prévues)
  - Incidents potentiels (dépenses prévues)
  - Entretiens planifiés (dépenses prévues)
  - Ravitaillements prévus (dépenses carburant)
- Planification de trajets futurs via `Prevision_Trajet`
- Comparaison prévision vs réel après clôture via `VM_Prevision_vs_Reel`
- Analyse de rentabilité anticipée
- Suivi de l'écart entre prévisions et réalisations
- Vue détaillée des prévisions via `VM_Prevision_Detail`

### Sources de prévisions

- **Trajet** : prévision initiale automatique à la création
- **Trajet_Reservation** : prévision de recettes basée sur les réservations
- **Trajet_Arret_Detail** : prévision de dépenses basée sur incidents planifiés
- **Vehicule_Entretien** : prévision de dépenses d'entretien
- **Trajet_Carburant_Detail** : prévision de dépenses carburant

---

## 15. Gestion de la Caisse

### Fonctionnalités

- Création de caisses (principale, secondaire, gare) via `Caisse`
- Définition du solde initial
- Enregistrement des mouvements de caisse avec traçabilité complète via `Caisse_Mouvement`
- Association automatique aux opérations sources via `id_entite_origine` et `table_origine`
- Calcul du solde actuel via `VM_Caisse_Solde_Actuel`
- Historique complet des entrées et sorties via `VM_Caisse_Mouvement_Historique`
- Audit des mouvements par origine via `VM_Caisse_Tracabilite`
- Vue détaillée des caisses via `VM_Caisse_Detail`

### Types de caisse (Caisse_Type)

- Caisse principale
- Caisse secondaire
- Caisse de gare

### Traçabilité des mouvements

Chaque mouvement de caisse est lié à son origine :

- **Trajet_Reservation_Paiement** : entrée d'argent suite à un paiement
- **Trajet_Arret_Detail** : sortie d'argent pour incident/arrêt
- **Vehicule_Entretien** : sortie d'argent pour entretien
- **Trajet_Carburant_Detail** : sortie d'argent pour ravitaillement
- **Trajet_Finance** : mouvements financiers globaux

---

## 16. Tableaux de Bord & Supervision

### Fonctionnalités

- Tableau de bord exploitation
- Tableau de bord financier
- Suivi du taux de remplissage par trajet
- Détection d'anomalies (retards, surcoûts)
- Analyse par véhicule via `VM_Performance_Vehicule`
- Analyse par ligne via `VM_Performance_Ligne`
- Analyse de l'origine des mouvements financiers
- Traçabilité complète des flux de trésorerie
- Suivi de la consommation carburant

### Indicateurs de performance

- Taux de remplissage moyen par ligne
- Bénéfice moyen par trajet
- Coût d'entretien par véhicule
- Durée moyenne des trajets
- Nombre de trajets terminés

---

## 17. Traçabilité & Audit

### Fonctionnalités

- Historique des statuts (véhicules, chauffeurs, trajets, réservations)
- Journal des mouvements financiers
- **Traçabilité complète de l'origine des transactions** via `id_entite_origine` et `table_origine`
- Consultation des actions passées
- Audit des mouvements de caisse par source via `VM_Caisse_Tracabilite`
- Support pour audit interne
- Vérification de la cohérence entre prévisions et réalisations
- Suivi des variations de taux (carburant, devises)

### Capacités d'audit renforcées

- Identification de l'origine de chaque mouvement financier
- Suivi des enchaînements d'événements (réservation → paiement → caisse)
- Détection des incohérences dans les flux financiers
- Traçabilité des dépenses par type d'événement
- Historique complet des changements de statut

---

## 18. Analyse et Reporting

### Fonctionnalités

- Analyse financière par période (jour, mois)
- Comparaison prévision vs réalisation
- Performance par ligne de transport
- Performance par véhicule
- Activité par chauffeur
- Suivi des coûts d'entretien
- Analyse de la consommation carburant
- Taux de remplissage par trajet/ligne

### Rapports disponibles

- Bilan financier journalier/mensuel
- Écarts prévision/réalisation
- Classement des lignes par rentabilité
- Coûts d'exploitation par véhicule
- Activité des chauffeurs

---

## 19. Performance & Accès Rapide

### Fonctionnalités

- Utilisation de vues matérialisées pour optimiser les performances
- Lecture rapide sans jointures complexes
- Rafraîchissement contrôlé des données
- Optimisation pour zones à faible connectivité
- Indexation appropriée sur les vues matérialisées
- Vues simples pour données en temps réel

### Vues matérialisées principales

- `VM_Vehicule_Detail`
- `VM_Chauffeur_Detail`
- `VM_Trajet_Detail`
- `VM_Finance_Journaliere`
- `VM_Finance_Mensuelle`
- `VM_Performance_Ligne`
- `VM_Performance_Vehicule`

---
