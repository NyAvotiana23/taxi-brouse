## Logiques métier & transactions automatiques
Système de Gestion Taxi-Brousse

---

## 1. Création d'un Trajet

### Déclencheur
- Création d'un enregistrement dans `Trajet`

### Logiques automatiques
1. Création automatique d'une **Prévision de Trajet**
    - Insertion dans `Prevision_Trajet`
    - Copie des champs :
        - id_ligne
        - id_chauffeur
        - id_vehicule
        - nombre_passager
        - id_trajet_statut
        - datetime_depart
        - datetime_arrivee
        - frais_unitaire

2. Création automatique d'une **Prévision Financière**
    - Insertion dans `Prevision_Finance`
    - **id_entite_origine** = id du trajet créé
    - **table_origine** = 'Trajet'
    - montant = nombre_passager × frais_unitaire
    - id_type_mouvement = Recette
    - description = "Prévision initiale du trajet"
    - date = datetime_depart

3. Initialisation du statut du trajet
    - Enregistrement dans `Trajet_Mouvement_Statut`
    - Statut initial = "Prévu"

### Transaction
- Toutes les opérations sont exécutées dans **une seule transaction**
- En cas d'échec → rollback global

---

## 2. Ouverture d'un Trajet (statut : Ouvert)

### Déclencheur
- Changement de statut du trajet vers "Ouvert"

### Logiques automatiques
- Vérification finale :
    - Véhicule = Disponible
    - Chauffeur = Actif
- Verrouillage temporaire du véhicule et du chauffeur
- Rafraîchissement des vues matérialisées liées

---

## 3. Démarrage d'un Trajet (statut : En cours)

### Déclencheur
- Changement de statut vers "En cours"

### Logiques automatiques
- Mise à jour du statut du véhicule → "En circulation"
- Mise à jour du statut du chauffeur → "En mission"
- Insertion dans :
    - `Vehicule_Mouvement_Statut`
    - `Chauffeur_Mouvement_Statut`

---

## 4. Réservation sur un Trajet

### Déclencheur
- Création d'une `Trajet_Reservation`

### Logiques automatiques
1. Incrémentation du nombre de places réservées
2. Vérification de la capacité restante
3. Mise à jour dynamique du taux de remplissage

4. **Création d'une prévision financière de recette**
    - Insertion dans `Prevision_Finance`
    - **id_entite_origine** = id de la réservation
    - **table_origine** = 'Trajet_Reservation'
    - montant = montant de la réservation × nombre_place_reservation
    - id_type_mouvement = Recette
    - date = date_reservation
    - description = "Prévision basée sur réservation"

5. Si capacité atteinte :
    - Passage automatique du trajet à "Complet"

### Transaction
- Réservation + Prévision financière dans une seule transaction

---

## 5. Paiement d'un Trajet

### Déclencheur
- Insertion dans `Trajet_Paiement`

### Logiques automatiques
1. Création d'un mouvement financier
    - Insertion dans `Trajet_Finance`
    - id_type_mouvement = Recette

2. **Création d'un mouvement de caisse avec traçabilité**
    - Insertion dans `Caisse_Mouvement`
    - **id_entite_origine** = id du paiement
    - **table_origine** = 'Trajet_Paiement'
    - id_type_mouvement = Recette
    - montant = montant du paiement
    - motif = "Paiement trajet #[id_trajet]"
    - date_mouvement = date_paiement

3. Mise à jour du solde de la caisse
    - solde_actuel = solde_actuel + montant

### Transaction
- Paiement + Finance + Caisse dans une seule transaction atomique

---

## 6. Incident ou Arrêt de Trajet

### Déclencheur
- Création d'un `Trajet_Detail`

### Logiques automatiques
1. Enregistrement de la dépense dans Finance Trajet
    - Insertion dans `Trajet_Finance`
    - id_type_mouvement = Dépense

2. **Création d'un mouvement de caisse avec traçabilité**
    - Insertion dans `Caisse_Mouvement`
    - **id_entite_origine** = id du détail de trajet
    - **table_origine** = 'Trajet_Detail'
    - id_type_mouvement = Dépense
    - montant = montant_depense
    - motif = libelle du motif d'arrêt
    - date_mouvement = datetime_debut

3. **Création d'une prévision financière de dépense** (si incident planifié)
    - Insertion dans `Prevision_Finance`
    - **id_entite_origine** = id du détail de trajet
    - **table_origine** = 'Trajet_Detail'
    - montant = montant_depense
    - id_type_mouvement = Dépense
    - date = datetime_debut
    - description = "Dépense incident : [motif]"

4. Mise à jour du solde de la caisse
    - solde_actuel = solde_actuel - montant_depense

### Transaction
- Incident + Finance + Prévision + Caisse dans une seule transaction

---

## 7. Clôture d'un Trajet (statut : Terminé)

### Déclencheur
- Changement de statut vers "Terminé"

### Logiques automatiques
1. Calcul final financier
    - total_recette
    - total_depense
    - benefice

2. Mise à jour des statuts
    - Véhicule → Disponible
    - Chauffeur → Actif

3. Enregistrement des mouvements de statut
    - Véhicule
    - Chauffeur
    - Trajet

4. **Comparaison prévision vs réel**
    - Calcul des écarts :
        - recette_reelle - recette_prevue
        - depense_reelle - depense_prevue
    - Mise à jour de `VM_Prevision_vs_Reel`

5. Rafraîchissement des vues matérialisées
    - Trajet
    - Finance
    - Caisse
    - Prévisions

---

## 8. Annulation d'un Trajet

### Déclencheur
- Statut du trajet → Annulé

### Logiques automatiques
- Libération du véhicule et du chauffeur
- Annulation des réservations non utilisées
- **Les prévisions financières restent en base pour audit**
- Aucune écriture financière autorisée après annulation
- Marquage des prévisions comme "annulées" (via statut trajet)

---

## 9. Entretien Véhicule

### Déclencheur
- Insertion dans `Vehicule_Entretien`

### Logiques automatiques
1. Création d'un mouvement financier
    - Type = Dépense

2. **Création d'un mouvement de caisse avec traçabilité**
    - Insertion dans `Caisse_Mouvement`
    - **id_entite_origine** = id de l'entretien
    - **table_origine** = 'Vehicule_Entretien'
    - id_type_mouvement = Dépense
    - montant = montant_depense
    - motif = "Entretien véhicule : [motif]"
    - date_mouvement = date_entretien

3. **Création d'une prévision financière** (si entretien planifié)
    - Insertion dans `Prevision_Finance`
    - **id_entite_origine** = id de l'entretien
    - **table_origine** = 'Vehicule_Entretien'
    - montant = montant_depense
    - id_type_mouvement = Dépense
    - date = date_entretien
    - description = "Entretien planifié : [motif]"

4. Mise à jour des indicateurs de coût véhicule
5. Mise à jour du solde de la caisse

### Transaction
- Entretien + Finance + Prévision + Caisse dans une seule transaction

---

## 10. Mouvements de Caisse (RÈGLE CRITIQUE)

### Logique centrale
- **Aucun mouvement de caisse ne peut exister seul**
- Chaque mouvement DOIT être lié à une entité source via :
    - **id_entite_origine** : ID de l'enregistrement source
    - **table_origine** : Nom de la table source

### Tables sources autorisées
- `Trajet_Paiement` → Recettes
- `Trajet_Detail` → Dépenses (incidents)
- `Vehicule_Entretien` → Dépenses (entretien)
- `Trajet_Finance` → Mouvements globaux

### Contraintes
- Toute écriture = transaction atomique
- Le solde ne doit jamais devenir négatif
- **La cohérence entre `table_origine` et `id_type_mouvement` est obligatoire**
- L'entité référencée doit exister dans la table source

### Traçabilité
- Permet de remonter de la caisse vers l'opération source
- Facilite l'audit et la réconciliation
- Assure l'intégrité des flux financiers

---

## 11. Prévision vs Réel

### Déclencheur
- Clôture d'un trajet

### Logiques automatiques
1. Récupération de toutes les prévisions liées au trajet
    - Via `id_trajet` dans `Prevision_Finance`
    - Filtrage par `table_origine`

2. Calcul des totaux prévus par type
    - Somme des recettes prévues
    - Somme des dépenses prévues

3. Calcul des totaux réels
    - Depuis `Trajet_Finance`

4. Calcul de l'écart
    - recette_reelle - recette_prevue
    - depense_reelle - depense_prevue
    - benefice_reel - benefice_prevu

5. Mise à jour de la vue `VM_Prevision_vs_Reel`

### Analyse des écarts
- Identification des sources d'écart via `table_origine`
- Analyse par type d'événement
- Aide à la décision pour futurs trajets

---

## 12. Règles de Sécurité Métier

- Les données financières validées sont immuables
- Aucun trajet ne peut être supprimé (historique obligatoire)
- Les écritures comptables sont horodatées
- Toute action critique doit être journalisée
- **La traçabilité via `id_entite_origine` et `table_origine` est obligatoire**
- **Impossible de créer un mouvement de caisse sans référence source valide**
- Les prévisions ne peuvent être modifiées après création (nouvelles versions uniquement)

---

## 13. Rafraîchissement des Vues Matérialisées

### Déclencheurs
- Création / clôture de trajet
- Paiement
- Incident
- Mouvement de caisse
- Création de prévision
- Fin de journée

### Stratégie
- Rafraîchissement ciblé par domaine
- Rafraîchissement global planifié (batch nocturne)
- Mise à jour prioritaire des vues de traçabilité

### Vues affectées par la traçabilité
- `VM_Caisse_Mouvement_Historique` : inclut `table_origine`
- `VM_Prevision_vs_Reel` : analyse par source
- Nouvelles vues d'audit possibles pour suivre les flux par origine

---

## 14. Intégrité Référentielle Logique

### Principe
Bien que `id_entite_origine` ne soit pas une clé étrangère au niveau base de données, l'application DOIT garantir que :

1. L'entité référencée existe dans `table_origine`
2. Le type de mouvement correspond à la table source
3. La suppression d'une entité source ne casse pas la traçabilité

### Implémentation
- Validation applicative avant insertion
- Triggers ou procédures stockées pour vérification
- Soft delete recommandé pour les entités sources
- Log d'audit en cas d'incohérence détectée

---