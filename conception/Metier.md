# Logiques métier & transactions automatiques
## Système de Gestion Taxi-Brousse

---

## 1. Création d'un Trajet

### Déclencheur
- Création d'un enregistrement dans `Trajet`

### Logiques automatiques
1. Vérification préalable :
   - Le véhicule doit avoir le statut **Disponible** (via `VM_Vehicule_Statut_Actuel`)
   - Le chauffeur doit avoir le statut **Actif** (via `VM_Chauffeur_Statut_Actuel`)
   - Pas de conflit de planning pour le véhicule et le chauffeur

2. Création automatique d'une **Prévision de Trajet**
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

3. Création automatique d'une **Prévision Financière**
   - Insertion dans `Prevision_Finance`
   - **id_entite_origine** = id du trajet créé
   - **table_origine** = 'Trajet'
   - montant = nombre_passager × frais_unitaire
   - id_type_mouvement = Recette
   - description = "Prévision initiale du trajet"
   - date = datetime_depart

4. Initialisation du statut du trajet
   - Enregistrement dans `Trajet_Mouvement_Statut`
   - Statut initial = "Prévu"
   - id_ancien_statut = NULL
   - id_nouveau_statut = ID du statut "Prévu"

5. Rafraîchissement des vues matérialisées
   - `VM_Trajet_Detail`
   - `VM_Prevision_vs_Reel`

### Transaction
- Toutes les opérations sont exécutées dans **une seule transaction**
- En cas d'échec → rollback global

---

## 2. Ouverture d'un Trajet (statut : Ouvert)

### Déclencheur
- Changement de statut du trajet vers "Ouvert"

### Logiques automatiques
1. Vérification finale :
   - Véhicule = Disponible (via `VM_Vehicule_Statut_Actuel`)
   - Chauffeur = Actif (via `VM_Chauffeur_Statut_Actuel`)

2. Enregistrement du changement de statut
   - Insertion dans `Trajet_Mouvement_Statut`
   - id_ancien_statut = statut précédent
   - id_nouveau_statut = ID du statut "Ouvert"

3. Verrouillage temporaire du véhicule et du chauffeur
   - Empêche leur affectation à d'autres trajets au même créneau

4. Rafraîchissement des vues matérialisées
   - `VM_Trajet_Detail`

---

## 3. Démarrage d'un Trajet (statut : En cours)

### Déclencheur
- Changement de statut vers "En cours"

### Logiques automatiques
1. Mise à jour du statut du véhicule
   - Insertion dans `Vehicule_Mouvement_Statut`
   - id_nouveau_statut → "En circulation"

2. Mise à jour du statut du chauffeur (optionnel selon implémentation)
   - Insertion dans `Chauffeur_Mouvement_Statut`
   - id_nouveau_statut → "En mission" (si ce statut existe)

3. Enregistrement du changement de statut trajet
   - Insertion dans `Trajet_Mouvement_Statut`

4. Rafraîchissement des vues matérialisées
   - `VM_Vehicule_Statut_Actuel`
   - `VM_Chauffeur_Statut_Actuel`
   - `VM_Trajet_Detail`

---

## 4. Réservation sur un Trajet

### Déclencheur
- Création d'une `Trajet_Reservation`

### Logiques automatiques
1. Vérification de la capacité
   - Places déjà réservées (via `VM_Trajet_Remplissage`)
   - `places_reservees + nombre_place_reservation <= maximum_passager`

2. Attribution du siège
   - Vérification de l'unicité du `numero_siege` pour ce trajet

3. Initialisation du statut de réservation
   - Insertion dans `Trajet_Reservation_Mouvement_Status`
   - Statut initial = "Réservé"

4. **Création d'une prévision financière de recette**
   - Insertion dans `Prevision_Finance`
   - **id_entite_origine** = id de la réservation
   - **table_origine** = 'Trajet_Reservation'
   - montant = montant de la réservation × nombre_place_reservation
   - id_type_mouvement = Recette
   - date = date_reservation
   - description = "Prévision basée sur réservation"

5. Vérification de la capacité complète
   - Si `places_reservees >= maximum_passager` :
      - Passage automatique du trajet à "Complet" (optionnel)

6. Rafraîchissement des vues matérialisées
   - `VM_Trajet_Remplissage`
   - `VM_Reservation_Detail`
   - `VM_Prevision_vs_Reel`

### Transaction
- Réservation + Prévision financière + Statut dans une seule transaction

---

## 5. Paiement d'un Trajet

### Déclencheur
- Insertion dans `Trajet_Reservation_Paiement`

### Logiques automatiques
1. Vérification préalable
   - La réservation n'est pas annulée
   - Le trajet n'est pas annulé
   - Montant total payé ≤ montant réservation

2. Création d'un mouvement financier
   - Insertion dans `Trajet_Finance`
   - id_type_mouvement = Recette
   - montant = montant du paiement

3. **Création d'un mouvement de caisse avec traçabilité**
   - Insertion dans `Caisse_Mouvement`
   - **id_entite_origine** = id du paiement
   - **table_origine** = 'Trajet_Reservation_Paiement'
   - id_type_mouvement = Recette
   - montant = montant du paiement
   - motif = "Paiement réservation #[id] - Trajet #[id_trajet]"
   - date_mouvement = date_paiement

4. Mise à jour du statut de réservation (optionnel)
   - Si paiement complet → statut "Confirmé"

5. Rafraîchissement des vues matérialisées
   - `VM_Paiement_Trajet`
   - `VM_Trajet_Finance`
   - `VM_Caisse_Solde_Actuel`
   - `VM_Finance_Journaliere`

### Transaction
- Paiement + Finance + Caisse dans une seule transaction atomique

---

## 6. Incident ou Arrêt de Trajet

### Déclencheur
- Création d'un `Trajet_Arret_Detail`

### Logiques automatiques
1. Vérification préalable
   - Le trajet doit être en statut "En cours"

2. Enregistrement de la dépense dans Finance Trajet
   - Insertion dans `Trajet_Finance`
   - id_type_mouvement = Dépense
   - montant = montant_depense

3. **Création d'un mouvement de caisse avec traçabilité** (si montant > 0)
   - Insertion dans `Caisse_Mouvement`
   - **id_entite_origine** = id du détail de trajet
   - **table_origine** = 'Trajet_Arret_Detail'
   - id_type_mouvement = Dépense
   - montant = montant_depense
   - motif = libellé du motif d'arrêt + " - Trajet #[id_trajet]"
   - date_mouvement = datetime_debut

4. **Création d'une prévision financière de dépense** (si incident planifié)
   - Insertion dans `Prevision_Finance`
   - **id_entite_origine** = id du détail de trajet
   - **table_origine** = 'Trajet_Arret_Detail'
   - montant = montant_depense
   - id_type_mouvement = Dépense
   - date = datetime_debut
   - description = "Dépense incident : [motif]"

5. Rafraîchissement des vues matérialisées
   - `VM_Trajet_Finance`
   - `VM_Caisse_Solde_Actuel`
   - `VM_Finance_Journaliere`
   - `VM_Prevision_vs_Reel`

### Transaction
- Incident + Finance + Prévision + Caisse dans une seule transaction

---

## 7. Ravitaillement en Carburant

### Déclencheur
- Création d'un `Trajet_Carburant_Detail`

### Logiques automatiques
1. Vérification préalable
   - Le trajet doit être en statut "En cours"
   - Quantité ajoutée ≤ capacité réservoir du véhicule

2. Calcul du coût
   - montant = quantite_carburant_ajoute × taux_carburant

3. Enregistrement de la dépense dans Finance Trajet
   - Insertion dans `Trajet_Finance`
   - id_type_mouvement = Dépense
   - montant = coût calculé

4. **Création d'un mouvement de caisse avec traçabilité**
   - Insertion dans `Caisse_Mouvement`
   - **id_entite_origine** = id du ravitaillement
   - **table_origine** = 'Trajet_Carburant_Detail'
   - id_type_mouvement = Dépense
   - montant = coût calculé
   - motif = "Carburant [quantité]L - Trajet #[id_trajet]"
   - date_mouvement = datetime

5. **Création d'une prévision financière** (si ravitaillement planifié)
   - Insertion dans `Prevision_Finance`
   - **id_entite_origine** = id du ravitaillement
   - **table_origine** = 'Trajet_Carburant_Detail'
   - montant = coût calculé
   - id_type_mouvement = Dépense
   - description = "Carburant prévu : [quantité]L"

6. Rafraîchissement des vues matérialisées
   - `VM_Trajet_Finance`
   - `VM_Caisse_Solde_Actuel`
   - `VM_Finance_Journaliere`

### Transaction
- Ravitaillement + Finance + Prévision + Caisse dans une seule transaction

---

## 8. Clôture d'un Trajet (statut : Terminé)

### Déclencheur
- Changement de statut vers "Terminé"

### Logiques automatiques
1. Vérification préalable
   - `datetime_arrivee` doit être renseignée
   - `datetime_arrivee > datetime_depart`

2. Calcul final financier
   - total_recette (via `VM_Trajet_Finance`)
   - total_depense (via `VM_Trajet_Finance`)
   - benefice = total_recette - total_depense

3. Mise à jour des statuts
   - Véhicule → Disponible
      - Insertion dans `Vehicule_Mouvement_Statut`
   - Chauffeur → Actif
      - Insertion dans `Chauffeur_Mouvement_Statut`
   - Trajet → Terminé
      - Insertion dans `Trajet_Mouvement_Statut`

4. **Comparaison prévision vs réel**
   - Récupération des prévisions via `Prevision_Finance` WHERE id_trajet
   - Calcul des écarts :
      - ecart_recette = recette_reelle - recette_prevue
      - ecart_depense = depense_reelle - depense_prevue
      - ecart_benefice = benefice_reel - benefice_prevu
   - Mise à jour de `VM_Prevision_vs_Reel`

5. Mise à jour des réservations
   - Passage des réservations confirmées à "Utilisé"

6. Rafraîchissement des vues matérialisées
   - `VM_Trajet_Detail`
   - `VM_Trajet_Finance`
   - `VM_Vehicule_Statut_Actuel`
   - `VM_Chauffeur_Statut_Actuel`
   - `VM_Prevision_vs_Reel`
   - `VM_Finance_Journaliere`
   - `VM_Finance_Mensuelle`
   - `VM_Performance_Ligne`
   - `VM_Performance_Vehicule`
   - `VM_Chauffeur_Activite`

### Transaction
- Toutes les opérations dans une transaction unique

---

## 9. Annulation d'un Trajet

### Déclencheur
- Statut du trajet → Annulé

### Logiques automatiques
1. Libération du véhicule et du chauffeur
   - Véhicule → Disponible
   - Chauffeur → Actif
   - Insertions dans les tables de mouvement de statut

2. Annulation des réservations non utilisées
   - Passage à statut "Annulé"
   - Insertions dans `Trajet_Reservation_Mouvement_Status`

3. Gestion des prévisions financières
   - **Les prévisions restent en base pour audit**
   - Marquage implicite comme "annulées" via le statut du trajet

4. Blocage des écritures financières
   - Aucune nouvelle écriture financière autorisée après annulation
   - Validation au niveau applicatif

5. Rafraîchissement des vues matérialisées
   - `VM_Trajet_Detail`
   - `VM_Vehicule_Statut_Actuel`
   - `VM_Chauffeur_Statut_Actuel`
   - `VM_Reservation_Detail`

---

## 10. Entretien Véhicule

### Déclencheur
- Insertion dans `Vehicule_Entretien`

### Logiques automatiques
1. Vérification préalable
   - Le véhicule ne doit pas être en statut "Hors service"

2. Changement de statut du véhicule (optionnel)
   - Véhicule → "En maintenance" pendant l'entretien

3. Création d'un mouvement financier (optionnel, selon contexte)
   - Insertion dans table financière globale si nécessaire
   - Type = Dépense

4. **Création d'un mouvement de caisse avec traçabilité**
   - Insertion dans `Caisse_Mouvement`
   - **id_entite_origine** = id de l'entretien
   - **table_origine** = 'Vehicule_Entretien'
   - id_type_mouvement = Dépense
   - montant = montant_depense
   - motif = "Entretien véhicule [immat] : [motif]"
   - date_mouvement = date_debut_entretien

5. **Création d'une prévision financière** (si entretien planifié)
   - Insertion dans `Prevision_Finance`
   - **id_entite_origine** = id de l'entretien
   - **table_origine** = 'Vehicule_Entretien'
   - montant = montant_depense
   - id_type_mouvement = Dépense
   - date = date_debut_entretien
   - description = "Entretien planifié : [motif]"

6. Mise à jour des indicateurs de coût véhicule
   - Recalcul via `VM_Vehicule_Cout_Entretien`

7. Rafraîchissement des vues matérialisées
   - `VM_Vehicule_Cout_Entretien`
   - `VM_Caisse_Solde_Actuel`
   - `VM_Performance_Vehicule`

### Transaction
- Entretien + Finance + Prévision + Caisse dans une seule transaction

---

## 11. Mouvements de Caisse (RÈGLE CRITIQUE)

### Logique centrale
- **Aucun mouvement de caisse ne peut exister seul**
- Chaque mouvement DOIT être lié à une entité source via :
   - **id_entite_origine** : ID de l'enregistrement source
   - **table_origine** : Nom de la table source

### Tables sources autorisées
- `Trajet_Reservation_Paiement` → Recettes
- `Trajet_Arret_Detail` → Dépenses (incidents)
- `Vehicule_Entretien` → Dépenses (entretien)
- `Trajet_Carburant_Detail` → Dépenses (carburant)
- `Trajet_Finance` → Mouvements globaux (optionnel)

### Contraintes
- Toute écriture = transaction atomique
- Le solde ne doit jamais devenir négatif
- **La cohérence entre `table_origine` et `id_type_mouvement` est obligatoire** :
   - `Trajet_Reservation_Paiement` → Recette uniquement
   - `Trajet_Arret_Detail` → Dépense uniquement
   - `Vehicule_Entretien` → Dépense uniquement
   - `Trajet_Carburant_Detail` → Dépense uniquement
- L'entité référencée doit exister dans la table source
- Validation applicative avant insertion

### Calcul du solde
- Récupération du solde actuel via `VM_Caisse_Solde_Actuel`
- Si Recette : `nouveau_solde = solde_actuel + montant`
- Si Dépense : `nouveau_solde = solde_actuel - montant`
- Vérification : `nouveau_solde >= 0`

### Traçabilité
- Permet de remonter de la caisse vers l'opération source
- Facilite l'audit et la réconciliation via `VM_Caisse_Tracabilite`
- Assure l'intégrité des flux financiers
- Description automatique de l'origine dans la vue de traçabilité

---

## 12. Prévision vs Réel

### Déclencheur
- Clôture d'un trajet (statut → Terminé)

### Logiques automatiques
1. Récupération de toutes les prévisions liées au trajet
   - Via `id_trajet` dans `Prevision_Finance`
   - Filtrage par `table_origine` pour analyse détaillée

2. Calcul des totaux prévus par type
   - Somme des recettes prévues (WHERE id_type_mouvement = Recette)
   - Somme des dépenses prévues (WHERE id_type_mouvement = Dépense)
   - Bénéfice prévu = recettes prévues - dépenses prévues

3. Récupération des totaux réels
   - Depuis `VM_Trajet_Finance`
   - total_recette
   - total_depense
   - benefice

4. Calcul de l'écart
   - ecart_recette = recette_reelle - recette_prevue
   - ecart_depense = depense_reelle - depense_prevue
   - ecart_benefice = benefice_reel - benefice_prevu

5. Mise à jour de la vue `VM_Prevision_vs_Reel`
   - Rafraîchissement de la vue matérialisée

### Analyse des écarts
- Identification des sources d'écart via `table_origine` dans `Prevision_Finance`
- Analyse par type d'événement :
   - Écarts sur réservations (table_origine = 'Trajet_Reservation')
   - Écarts sur incidents (table_origine = 'Trajet_Arret_Detail')
   - Écarts sur entretien (table_origine = 'Vehicule_Entretien')
   - Écarts sur carburant (table_origine = 'Trajet_Carburant_Detail')
- Aide à la décision pour futurs trajets
- Amélioration de la précision des prévisions

---

## 13. Changement de Taux Carburant

### Déclencheur
- Modification du `dernier_taux` dans `Carburant_Type`

### Logiques automatiques
1. Enregistrement du changement
   - Insertion dans `Carburant_Mouvement_Taux`
   - ancien_taux = valeur précédente
   - nouveau_taux = nouvelle valeur
   - date_mouvement = CURRENT_TIMESTAMP

2. Impact sur les prévisions futures
   - Les nouveaux ravitaillements utilisent le nouveau taux
   - Les prévisions existantes gardent leur taux historique

### Transaction
- Mise à jour + Historisation dans une seule transaction

---

## 14. Changement de Taux Devise

### Déclencheur
- Modification du `dernier_taux` dans `Devise`

### Logiques automatiques
1. Enregistrement du changement
   - Insertion dans `Devise_Mouvement_Taux`
   - ancien_taux = valeur précédente
   - nouveau_taux = nouvelle valeur
   - date_mouvement = CURRENT_TIMESTAMP

2. Impact sur les conversions futures
   - Les nouvelles opérations utilisent le nouveau taux
   - Les opérations existantes gardent leur taux historique

### Transaction
- Mise à jour + Historisation dans une seule transaction

---

## 15. Règles de Sécurité Métier

- Les données financières validées sont immuables
- Aucun trajet ne peut être supprimé (historique obligatoire)
- Les écritures comptables sont horodatées
- Toute action critique doit être journalisée
- **La traçabilité via `id_entite_origine` et `table_origine` est obligatoire**
- **Impossible de créer un mouvement de caisse sans référence source valide**
- Les prévisions ne peuvent être modifiées après création (nouvelles versions uniquement)
- Les changements de statut sont irréversibles (sauf exception documentée)
- Le solde de caisse ne peut jamais être négatif
- Les mouvements de taux (carburant, devise) sont historisés automatiquement

---

## 16. Rafraîchissement des Vues Matérialisées

### Déclencheurs
- Création / clôture de trajet
- Paiement
- Incident
- Ravitaillement carburant
- Mouvement de caisse
- Création de prévision
- Changement de statut (véhicule, chauffeur, trajet)
- Fin de journée (batch automatique)

### Stratégie
- Rafraîchissement ciblé par domaine après chaque opération
- Rafraîchissement global planifié (batch nocturne)
- Mise à jour prioritaire des vues de traçabilité
- Rafraîchissement concurrentiel si possible

### Vues affectées par domaine

**Domaine Véhicule :**
- `VM_Vehicule_Detail`
- `VM_Vehicule_Statut_Actuel` (vue simple, auto-actualisée)
- `VM_Vehicule_Cout_Entretien`
- `VM_Performance_Vehicule`

**Domaine Chauffeur :**
- `VM_Chauffeur_Detail`
- `VM_Chauffeur_Statut_Actuel` (vue simple, auto-actualisée)
- `VM_Chauffeur_Activite`

**Domaine Trajet :**
- `VM_Trajet_Detail`
- `VM_Trajet_Statut_Actuel` (vue simple, auto-actualisée)
- `VM_Trajet_Remplissage`

**Domaine Financier :**
- `VM_Trajet_Finance`
- `VM_Finance_Journaliere`
- `VM_Finance_Mensuelle`
- `VM_Prevision_vs_Reel`
- `VM_Paiement_Trajet`

**Domaine Caisse :**
- `VM_Caisse_Solde_Actuel`
- `VM_Caisse_Detail`
- `VM_Caisse_Mouvement_Historique` (vue simple, auto-actualisée)
- `VM_Caisse_Tracabilite` (vue simple, auto-actualisée)

**Domaine Analytique :**
- `VM_Performance_Ligne`
- `VM_Performance_Vehicule`

---

## 17. Intégrité Référentielle Logique

### Principe
Bien que `id_entite_origine` ne soit pas une clé étrangère au niveau base de données, l'application DOIT garantir que :

1. L'entité référencée existe dans `table_origine`
2. Le type de mouvement correspond à la table source
3. La suppression d'une entité source ne casse pas la traçabilité

### Implémentation
- **Validation applicative avant insertion** :
   - Vérifier que `id_entite_origine` existe dans `table_origine`
   - Vérifier la cohérence `table_origine` / `id_type_mouvement`

- **Triggers ou procédures stockées pour vérification** :
   - Trigger BEFORE INSERT sur `Caisse_Mouvement`
   - Trigger BEFORE INSERT sur `Prevision_Finance`

- **Soft delete recommandé pour les entités sources** :
   - Ajout d'une colonne `deleted_at` ou `is_deleted`
   - Ne jamais supprimer physiquement les enregistrements référencés

- **Log d'audit en cas d'incohérence détectée** :
   - Table `Audit_Log` pour tracer les tentatives invalides
   - Alertes automatiques pour les administrateurs

### Tables concernées
- `Caisse_Mouvement` : référence vers diverses tables via `table_origine`
- `Prevision_Finance` : référence vers diverses tables via `table_origine`

### Validation des combinaisons valides

**Pour Caisse_Mouvement :**
```
table_origine = 'Trajet_Reservation_Paiement' → id_type_mouvement = Recette
table_origine = 'Trajet_Arret_Detail' → id_type_mouvement = Dépense
table_origine = 'Vehicule_Entretien' → id_type_mouvement = Dépense
table_origine = 'Trajet_Carburant_Detail' → id_type_mouvement = Dépense
```

**Pour Prevision_Finance :**
```
table_origine = 'Trajet' → id_type_mouvement = Recette
table_origine = 'Trajet_Reservation' → id_type_mouvement = Recette
table_origine = 'Trajet_Arret_Detail' → id_type_mouvement = Dépense
table_origine = 'Vehicule_Entretien' → id_type_mouvement = Dépense
table_origine = 'Trajet_Carburant_Detail' → id_type_mouvement = Dépense
```

---