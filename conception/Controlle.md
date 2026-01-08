## Règles de contrôle avant création / modification des entités
Système de Gestion Taxi-Brousse

---

## 1. Contrôles Généraux (Applicables à toutes les entités)
- Les champs obligatoires ne doivent pas être NULL
- Les identifiants de référence doivent exister (clé étrangère valide)
- Les valeurs numériques doivent être positives si applicables
- Les dates ne doivent pas être incohérentes (fin < début interdit)
- Les libellés doivent être uniques si nécessaire (statuts, types, modes)

---

## 2. Véhicule

### Avant création d'un véhicule
- `id_type` doit exister dans `Vehicule_Type`
- `id_vehicule_statut` doit exister dans `Vehicule_Statut`
- `maximum_passager > 0`
- `immatriculation` non vide et unique

### Avant changement de statut véhicule
- Le véhicule ne doit pas être affecté à un trajet **en cours**
- Le changement doit être enregistré dans `Vehicule_Mouvement_Statut`

---

## 3. Entretien Véhicule

### Avant création d'un entretien
- `id_vehicule` doit exister
- Le véhicule ne doit pas être **hors service**
- `montant_depense >= 0`
- `date_entretien <= date_du_jour`

---

## 4. Chauffeur

### Avant création d'un chauffeur
- `id_chauffeur_statut` doit exister dans `Chauffeur_Statut`
- `date_naissance < date_du_jour`
- `numero_permis` non vide et unique

### Avant changement de statut chauffeur
- Le chauffeur ne doit pas être affecté à un trajet **en cours**
- Le mouvement doit être enregistré dans `Chauffeur_Mouvement_Statut`

---

## 5. Localisation (Province / Région / Ville)

### Avant création d'une région
- `id_province` doit exister dans `Province`

### Avant création d'une ville
- `id_region` doit exister dans `Region`

---

## 6. Ligne & Itinéraire

### Avant création d'une ligne
- `id_ville_depart` doit exister
- `id_ville_arrivee` doit exister
- `id_ville_depart ≠ id_ville_arrivee`

### Avant ajout d'un arrêt de ligne
- `id_ligne` doit exister
- `ordre` doit être unique par ligne
- `id_ligne_arret` doit exister

---

## 7. Trajet (RÈGLE CRITIQUE)

### Avant création d'un trajet
- `datetime_depart > date_du_jour`
- `datetime_arrivee > datetime_depart`
- `id_ligne` doit exister
- `id_vehicule` doit exister
- `id_chauffeur` doit exister
- `frais_unitaire >= 0`

### Dépendances obligatoires
- Véhicule doit avoir le statut **Disponible**
- Chauffeur doit avoir le statut **Actif**
- Véhicule et chauffeur ne doivent pas être déjà affectés à un trajet **au même créneau**
- `nombre_passager <= vehicule.maximum_passager`

### Avant changement de statut trajet
- Le changement doit être enregistré dans `Trajet_Mouvement_Statut`
- Impossible de passer à **Terminé** si aucun départ enregistré

---

## 8. Arrêts & Incidents de Trajet

### Avant création d'un arrêt (Trajet_Detail)
- `id_trajet` doit exister
- Le trajet doit être **En cours**
- `id_motif` doit exister dans `Trajet_Motif_Arret`
- `montant_depense >= 0`
- `datetime_fin >= datetime_debut`

---

## 9. Réservation

### Avant création d'une réservation
- `id_trajet` doit exister
- Le trajet doit être **Ouvert**
- `id_status_reservation` doit exister
- `nombre_place_reservation > 0`
- Le nombre total réservé ne doit pas dépasser `vehicule.maximum_passager`
- `numero_siege` doit être unique par trajet

### Avant annulation d'une réservation
- Le trajet ne doit pas être **Terminé**

---

## 10. Paiement

### Avant création d'un paiement
- `id_trajet` doit exister
- `id_mode_paiement` doit exister
- `montant > 0`
- Le trajet ne doit pas être **Annulé**

---

## 11. Finance Trajet

### Avant création d'un mouvement financier
- `id_trajet` doit exister
- `id_type_mouvement` doit exister
- `montant >= 0`
- Les dépenses doivent être justifiées (incident ou entretien)

---

## 12. Prévision Financière

### Avant création d'une prévision
- `id_trajet` doit exister
- `id_entite_origine` doit être un identifiant valide
- `table_origine` doit être une table existante dans le système
- Les combinaisons valides pour `table_origine` sont :
    - `Trajet_Reservation` (pour prévision de recettes)
    - `Trajet_Detail` (pour prévision de dépenses d'incident)
    - `Vehicule_Entretien` (pour prévision de dépenses d'entretien)
    - `Trajet` (pour prévision initiale automatique)
- `id_type_mouvement` doit exister dans `Type_Mouvement`
- `montant >= 0`
- `date >= date_du_jour` (prévisions futures uniquement)
- La cohérence `table_origine` / `id_type_mouvement` doit être respectée :
    - Si `table_origine = 'Trajet_Reservation'` alors `id_type_mouvement = Recette`
    - Si `table_origine = 'Trajet_Detail'` ou `'Vehicule_Entretien'` alors `id_type_mouvement = Dépense`

---

## 13. Prévision Trajet

### Avant création d'un trajet prévisionnel
- `datetime_depart > date_du_jour`
- Véhicule et chauffeur doivent être **Disponibles**
- Pas de conflit avec trajets existants

---

## 14. Caisse

### Avant création d'une caisse
- `id_caisse_type` doit exister
- `solde_initial >= 0`

### Avant création d'un mouvement de caisse
- `id_caisse` doit exister
- `id_entite_origine` doit être un identifiant valide
- `table_origine` doit être une table existante dans le système
- Les combinaisons valides pour `table_origine` sont :
    - `Trajet_Paiement` (pour entrées d'argent)
    - `Trajet_Detail` (pour sorties liées aux incidents)
    - `Vehicule_Entretien` (pour sorties liées à l'entretien)
    - `Trajet_Finance` (pour mouvements financiers globaux)
- `id_type_mouvement` doit exister dans `Type_Mouvement`
- `montant > 0`
- Le solde ne doit pas devenir négatif après l'opération
- La cohérence `table_origine` / `id_type_mouvement` doit être respectée :
    - Si `table_origine = 'Trajet_Paiement'` alors `id_type_mouvement = Recette`
    - Si `table_origine = 'Trajet_Detail'` ou `'Vehicule_Entretien'` alors `id_type_mouvement = Dépense`
- L'entité référencée par `id_entite_origine` doit exister dans `table_origine`

---

## 15. Cohérence Globale & Sécurité
- Tous les changements de statut doivent être historisés
- Les suppressions physiques sont interdites (soft delete recommandé)
- Les données financières ne sont jamais modifiables après validation
- La traçabilité des mouvements financiers est obligatoire via `id_entite_origine` et `table_origine`
- Les vues matérialisées doivent être rafraîchies après :
    - Clôture de trajet
    - Validation de caisse
    - Fin de journée

---