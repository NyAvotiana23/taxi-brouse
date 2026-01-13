# Règles de contrôle avant création / modification des entités

## Système de Gestion Taxi-Brousse

---

## 1. Contrôles Généraux (Applicables à toutes les entités)

- Les champs obligatoires ne doivent pas être NULL
- Les identifiants de référence doivent exister (clé étrangère valide)
- Les valeurs numériques doivent être positives si applicables
- Les dates ne doivent pas être incohérentes (fin < début interdit)
- Les libellés doivent être uniques si nécessaire (statuts, types, modes)

---

## 2. Carburant

### Avant création d'un type de carburant

- `libelle` non vide et unique
- `dernier_taux > 0`

### Avant changement de taux carburant

- `nouveau_taux > 0`
- Le changement doit être enregistré dans `Carburant_Mouvement_Taux`

---

## 3. Devise

### Avant création d'une devise

- `libelle` non vide et unique
- `dernier_taux > 0`

### Avant changement de taux devise

- `nouveau_taux > 0`
- Le changement doit être enregistré dans `Devise_Mouvement_Taux`

---

## 4. Véhicule

### Avant création d'un véhicule

- `id_type` doit exister dans `Vehicule_Type`
- `id_type_carburant` doit exister dans `Carburant_Type`
- `maximum_passager > 0`
- `immatriculation` non vide et unique
- `capacite_carburant > 0` (si défini)
- `depense_carburant_100km > 0` (si défini)

### Avant changement de statut véhicule

- Le véhicule ne doit pas être affecté à un trajet **en cours**
- `id_nouveau_statut` doit exister dans `Vehicule_Statut`
- Le changement doit être enregistré dans `Vehicule_Mouvement_Statut`

---

## 5. Entretien Véhicule

### Avant création d'un entretien

- `id_vehicule` doit exister
- Le statut actuel du véhicule ne doit pas être **Hors service**
- `motif` non vide
- `montant_depense >= 0`
- `date_debut_entretien <= date_du_jour`
- `date_fin_entretien >= date_debut_entretien` (si définie)

---

## 6. Chauffeur

### Avant création d'un chauffeur

- `nom` et `prenom` non vides
- `date_naissance < date_du_jour`
- `numero_permis` non vide et unique

### Avant changement de statut chauffeur

- Le chauffeur ne doit pas être affecté à un trajet **en cours**
- `id_nouveau_statut` doit exister dans `Chauffeur_Statut`
- Le mouvement doit être enregistré dans `Chauffeur_Mouvement_Statut`

---

## 7. Localisation (Province / Région / Ville)

### Avant création d'une province

- `nom` non vide et unique

### Avant création d'une région

- `id_province` doit exister dans `Province`
- `nom` non vide
- Combinaison (`id_province`, `nom`) unique

### Avant création d'une ville

- `id_region` doit exister dans `Region`
- `nom` non vide
- Combinaison (`id_region`, `nom`) unique

---

## 8. Ligne & Itinéraire

### Avant création d'une ligne

- `id_ville_depart` doit exister dans `Ville`
- `id_ville_arrivee` doit exister dans `Ville`
- `id_ville_depart ≠ id_ville_arrivee`
- `distance_km > 0` (si définie)
- Combinaison (`id_ville_depart`, `id_ville_arrivee`) unique

### Avant création d'un arrêt de ligne

- `id_ville` doit exister dans `Ville`
- `nom_arret` non vide
- Combinaison (`id_ville`, `nom_arret`) unique

### Avant ajout d'un détail de ligne

- `id_ligne` doit exister dans `Ligne`
- `id_ligne_arret` doit exister dans `Ligne_Arret`
- `ordre > 0`
- Combinaison (`id_ligne`, `ordre`) unique
- Combinaison (`id_ligne`, `id_ligne_arret`) unique

---

## 9. Trajet (RÈGLE CRITIQUE)

### Avant création d'un trajet

- `datetime_depart > date_du_jour`
- `datetime_arrivee > datetime_depart` (si définie)
- `id_ligne` doit exister dans `Ligne`
- `id_vehicule` doit exister dans `Vehicule`
- `id_chauffeur` doit exister dans `Chauffeur`
- `id_trajet_statut` doit exister dans `Trajet_Statut`
- `frais_unitaire >= 0`
- `nombre_passager >= 0`

### Dépendances obligatoires

- Le statut actuel du véhicule doit être **Disponible** (via `VM_Vehicule_Statut_Actuel`)
- Le statut actuel du chauffeur doit être **Actif** (via `VM_Chauffeur_Statut_Actuel`)
- Véhicule et chauffeur ne doivent pas être déjà affectés à un trajet **au même créneau**
- `nombre_passager <= vehicule.maximum_passager`

### Avant changement de statut trajet

- `id_nouveau_statut` doit exister dans `Trajet_Statut`
- Le changement doit être enregistré dans `Trajet_Mouvement_Statut`
- Impossible de passer à **Terminé** si aucune `datetime_arrivee` enregistrée

---

## 10. Arrêts & Incidents de Trajet

### Avant création d'un arrêt (Trajet_Arret_Detail)

- `id_trajet` doit exister dans `Trajet`
- Le statut actuel du trajet doit être **En cours**
- `id_ville` doit exister dans `Ville`
- `id_trajet_motif_arret` doit exister dans `Trajet_Motif_Arret`
- `id_caisse` doit exister dans `Caisse` (si défini)
- `montant_depense >= 0` (si défini)
- `datetime_debut` non null
- `datetime_fin >= datetime_debut` (si définie)

---

## 11. Ravitaillement Carburant

### Avant création d'un ravitaillement (Trajet_Carburant_Detail)

- `id_trajet` doit exister dans `Trajet`
- Le statut actuel du trajet doit être **En cours**
- `id_ville` doit exister dans `Ville`
- `id_carburant_type` doit exister dans `Carburant_Type`
- `id_caisse` doit exister dans `Caisse` (si défini)
- `quantite_carburant_ajoute > 0`
- `taux_carburant > 0`
- La quantité totale ajoutée ne doit pas dépasser la `capacite_carburant` du véhicule

---

## 12. Client

### Avant création d'un client

- `id_type_client` doit exister dans `Type_Client`
- `nom_client` non vide
- Si `email` fourni, doit être au format valide
- Si `telephone` fourni, doit être au format valide

---

## 13. Réservation

### Avant création d'une réservation

- `id_client` doit exister dans `Client`
- `id_trajet` doit exister dans `Trajet`
- Le statut actuel du trajet doit être **Ouvert** ou **Prévu**
- `id_reservation_statut` doit exister dans `Reservation_Statut`
- `nom_passager` non vide
- `nombre_place_reservation > 0`
- `montant >= 0`
- Le nombre total réservé (incluant cette réservation) ne doit pas dépasser `vehicule.maximum_passager`
- `numero_siege` doit être unique par trajet (si défini)

### Avant changement de statut réservation

- `id_nouveau_statut` doit exister dans `Reservation_Statut`
- Le mouvement doit être enregistré dans `Trajet_Reservation_Mouvement_Status`

### Avant annulation d'une réservation

- Le statut actuel du trajet ne doit pas être **Terminé**

---

## 14. Paiement Réservation

### Avant création d'un paiement

- `id_client` doit exister dans `Client`
- `id_trajet_reservation` doit exister dans `Trajet_Reservation`
- `id_mode_paiement` doit exister dans `Mode_Paiement`
- `id_caisse` doit exister dans `Caisse` (si défini)
- `montant > 0`
- Le trajet lié ne doit pas être **Annulé**
- Le montant total payé ne doit pas dépasser le montant de la réservation

---

## 15. Finance Trajet

### Avant création d'un mouvement financier

- `id_trajet` doit exister dans `Trajet`
- `id_type_mouvement` doit exister dans `Type_Mouvement`
- `montant >= 0`
- Les dépenses doivent être justifiées (incident, entretien ou carburant)

---

## 16. Prévision Financière

### Avant création d'une prévision

- `id_trajet` doit exister dans `Trajet` (si défini)
- `id_entite_origine` doit être un identifiant valide
- `table_origine` doit être une table existante dans le système
- Les combinaisons valides pour `table_origine` sont :
  - `Trajet_Reservation` (pour prévision de recettes)
  - `Trajet_Arret_Detail` (pour prévision de dépenses d'incident)
  - `Vehicule_Entretien` (pour prévision de dépenses d'entretien)
  - `Trajet_Carburant_Detail` (pour prévision de dépenses carburant)
  - `Trajet` (pour prévision initiale automatique)
- `id_type_mouvement` doit exister dans `Type_Mouvement`
- `montant >= 0`
- `date >= date_du_jour` (prévisions futures uniquement)
- La cohérence `table_origine` / `id_type_mouvement` doit être respectée :
  - Si `table_origine = 'Trajet_Reservation'` alors `id_type_mouvement = Recette`
  - Si `table_origine IN ('Trajet_Arret_Detail', 'Vehicule_Entretien', 'Trajet_Carburant_Detail')` alors `id_type_mouvement = Dépense`

---

## 17. Prévision Trajet

### Avant création d'un trajet prévisionnel

- `id_ligne` doit exister dans `Ligne`
- `id_chauffeur` doit exister dans `Chauffeur`
- `id_vehicule` doit exister dans `Vehicule`
- `id_trajet_statut` doit exister dans `Trajet_Statut`
- `datetime_depart > date_du_jour`
- `datetime_arrivee > datetime_depart` (si définie)
- Véhicule et chauffeur doivent être **Disponibles/Actifs**
- Pas de conflit avec trajets existants ou prévisionnels
- `nombre_passager >= 0`
- `frais_unitaire >= 0`

---

## 18. Caisse

### Avant création d'une caisse

- `id_caisse_type` doit exister dans `Caisse_Type`
- `nom` non vide et unique
- `solde_initial >= 0`

### Avant création d'un mouvement de caisse

- `id_caisse` doit exister dans `Caisse`
- `id_entite_origine` doit être un identifiant valide
- `table_origine` doit être une table existante dans le système
- Les combinaisons valides pour `table_origine` sont :
  - `Trajet_Reservation_Paiement` (pour entrées d'argent)
  - `Trajet_Arret_Detail` (pour sorties liées aux incidents)
  - `Vehicule_Entretien` (pour sorties liées à l'entretien)
  - `Trajet_Carburant_Detail` (pour sorties liées au carburant)
  - `Trajet_Finance` (pour mouvements financiers globaux)
- `id_type_mouvement` doit exister dans `Type_Mouvement`
- `montant > 0`
- Le solde ne doit pas devenir négatif après l'opération
- La cohérence `table_origine` / `id_type_mouvement` doit être respectée :
  - Si `table_origine = 'Trajet_Reservation_Paiement'` alors `id_type_mouvement = Recette`
  - Si `table_origine IN ('Trajet_Arret_Detail', 'Vehicule_Entretien', 'Trajet_Carburant_Detail')` alors `id_type_mouvement = Dépense`
- L'entité référencée par `id_entite_origine` doit exister dans `table_origine`

---

## 19. Cohérence Globale & Sécurité

- Tous les changements de statut doivent être historisés dans les tables `*_Mouvement_Statut`
- Les suppressions physiques sont interdites (soft delete recommandé)
- Les données financières ne sont jamais modifiables après validation
- La traçabilité des mouvements financiers est obligatoire via `id_entite_origine` et `table_origine`
- Les vues matérialisées doivent être rafraîchies après :
  - Clôture de trajet
  - Validation de caisse
  - Fin de journée
  - Changements de statut majeurs

---

## 20. Règles de Rafraîchissement des Vues Matérialisées

### Vues à rafraîchir après modification de véhicule

- `VM_Vehicule_Detail`
- `VM_Vehicule_Cout_Entretien`
- `VM_Performance_Vehicule`

### Vues à rafraîchir après modification de chauffeur

- `VM_Chauffeur_Detail`
- `VM_Chauffeur_Activite`

### Vues à rafraîchir après modification de trajet

- `VM_Trajet_Detail`
- `VM_Trajet_Remplissage`
- `VM_Trajet_Finance`
- `VM_Prevision_vs_Reel`
- `VM_Performance_Ligne`
- `VM_Performance_Vehicule`

### Vues à rafraîchir après modification financière

- `VM_Finance_Journaliere`
- `VM_Finance_Mensuelle`
- `VM_Trajet_Finance`
- `VM_Caisse_Solde_Actuel`

### Vues à rafraîchir après modification de réservation

- `VM_Reservation_Detail`
- `VM_Paiement_Trajet`
- `VM_Trajet_Remplissage`

---
