# 📌 Fonctionnalités – Système de Gestion Taxi-Brousse (Interne)

## 1. Gestion des Véhicules
### Fonctionnalités
- Création et mise à jour des véhicules
- Affectation d'un type de véhicule
- Gestion de la capacité maximale
- Suivi de l'immatriculation
- Consultation de l'historique des statuts
- Gestion des entretiens et dépenses liées

### Statuts possibles (Vehicule_Statut)
- Disponible
- En circulation
- En maintenance
- Immobilisé
- Hors service

### Flux de statut
- Changement de statut tracé dans `Vehicule_Mouvement_Statut`
- Historique consultable par véhicule

---

## 2. Gestion des Chauffeurs
### Fonctionnalités
- Création et mise à jour des fiches chauffeurs
- Gestion des permis de conduire
- Affectation aux trajets
- Suivi d'activité (nombre de trajets effectués)
- Historique des changements de statut

### Statuts possibles (Chauffeur_Statut)
- Actif
- Inactif
- Suspendu
- En congé

### Flux de statut
- Tout changement est enregistré dans `Chauffeur_Mouvement_Statut`

---

## 3. Gestion Géographique
### Fonctionnalités
- Gestion des provinces
- Gestion des régions rattachées aux provinces
- Gestion des villes rattachées aux régions
- Consultation rapide via vues (Ville, Région, Province)

---

## 4. Gestion des Lignes et Itinéraires
### Fonctionnalités
- Création des lignes (ville départ → ville arrivée)
- Définition des arrêts intermédiaires
- Ordonnancement des arrêts
- Visualisation complète de l'itinéraire

---

## 5. Gestion des Trajets
### Fonctionnalités
- Planification des trajets
- Affectation du chauffeur et du véhicule
- Définition du tarif unitaire
- Suivi du nombre de passagers
- Gestion des heures réelles de départ et d'arrivée
- Consultation de l'historique des statuts

### Statuts possibles (Trajet_Statut)
- Prévu
- Ouvert
- En cours
- Suspendu
- Terminé
- Annulé

### Flux de statut
- Tous les changements sont enregistrés dans `Trajet_Mouvement_Statut`

---

## 6. Gestion des Incidents et Arrêts
### Fonctionnalités
- Déclaration d'arrêts imprévus
- Association d'un motif d'arrêt
- Suivi des dépenses liées aux incidents
- Analyse de la durée des arrêts
- Historique des incidents par trajet

### Motifs possibles (Trajet_Motif_Arret)
- Panne mécanique
- Accident
- Contrôle
- Pause prolongée
- Autre

---

## 7. Gestion des Réservations (Interne)
### Fonctionnalités
- Enregistrement manuel des réservations
- Attribution des sièges
- Gestion du nombre de places réservées
- Suivi du statut de chaque réservation

### Statuts possibles (Reservation_Status)
- Réservé
- Confirmé
- Annulé
- Utilisé

---

## 8. Gestion des Paiements
### Fonctionnalités
- Enregistrement des paiements liés aux trajets
- Association d'un mode de paiement
- Historique des paiements par trajet
- Suivi des montants encaissés

### Modes de paiement (Mode_Paiement)
- Espèces
- Mobile Money
- Autre

---

## 9. Gestion Financière des Trajets
### Fonctionnalités
- Enregistrement des mouvements financiers
- Distinction recette / dépense
- Calcul automatique du bénéfice par trajet
- Analyse financière par période

### Types de mouvement (Type_Mouvement)
- Recette
- Dépense

---

## 10. Prévisions et Planification
### Fonctionnalités
- Prévision des recettes et dépenses par trajet
- Traçabilité de l'origine des prévisions via `id_entite_origine` et `table_origine`
- Association des prévisions aux entités sources :
    - Réservations anticipées (recettes prévues)
    - Incidents potentiels (dépenses prévues)
    - Entretiens planifiés (dépenses prévues)
- Comparaison prévision vs réel après clôture du trajet
- Planification de trajets futurs
- Analyse de rentabilité anticipée
- Suivi de l'écart entre prévisions et réalisations

### Sources de prévisions
- **Trajet** : prévision initiale automatique à la création
- **Trajet_Reservation** : prévision de recettes basée sur les réservations
- **Trajet_Detail** : prévision de dépenses basée sur incidents planifiés
- **Vehicule_Entretien** : prévision de dépenses d'entretien

---

## 11. Gestion de la Caisse
### Fonctionnalités
- Création de caisses (principale, secondaire, gare)
- Définition du solde initial
- Enregistrement des mouvements de caisse avec traçabilité complète
- Association automatique aux opérations sources via `id_entite_origine` et `table_origine`
- Calcul du solde actuel
- Historique complet des entrées et sorties
- Audit des mouvements par origine

### Types de caisse (Caisse_Type)
- Caisse principale
- Caisse secondaire
- Caisse de gare

### Traçabilité des mouvements
Chaque mouvement de caisse est lié à son origine :
- **Trajet_Paiement** : entrée d'argent suite à un paiement
- **Trajet_Detail** : sortie d'argent pour incident/arrêt
- **Vehicule_Entretien** : sortie d'argent pour entretien
- **Trajet_Finance** : mouvements financiers globaux

---

## 12. Tableaux de Bord & Supervision
### Fonctionnalités
- Tableau de bord exploitation
- Tableau de bord financier
- Suivi du taux de remplissage
- Détection d'anomalies (retards, surcoûts)
- Analyse par véhicule, chauffeur, ligne
- Analyse de l'origine des mouvements financiers
- Traçabilité complète des flux de trésorerie

---

## 13. Traçabilité & Audit
### Fonctionnalités
- Historique des statuts (véhicules, chauffeurs, trajets)
- Journal des mouvements financiers
- **Traçabilité complète de l'origine des transactions** via `id_entite_origine` et `table_origine`
- Consultation des actions passées
- Audit des mouvements de caisse par source
- Support pour audit interne
- Vérification de la cohérence entre prévisions et réalisations

### Capacités d'audit renforcées
- Identification de l'origine de chaque mouvement financier
- Suivi des enchaînements d'événements (réservation → paiement → caisse)
- Détection des incohérences dans les flux financiers
- Traçabilité des dépenses par type d'événement

---

## 14. Performance & Accès Rapide
### Fonctionnalités
- Utilisation de vues matérialisées
- Lecture rapide sans jointures complexes
- Rafraîchissement contrôlé des données
- Optimisation pour zones à faible connectivité