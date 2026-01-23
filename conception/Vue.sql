-- ============================================
-- VUES - SYSTÈME TAXI-BROUSSE
-- ============================================

-- ============================================
-- 1. VUES VÉHICULES
-- ============================================

-- Vue : Statut actuel des véhicules
CREATE VIEW VM_Vehicule_Statut_Actuel AS
SELECT DISTINCT ON (vms.id_vehicule)
    vms.id_vehicule,
    vs.libelle AS libelle_statut,
    vms.date_mouvement
FROM Vehicule_Mouvement_Statut vms
         INNER JOIN Vehicule_Statut vs ON vms.id_nouveau_statut = vs.id
ORDER BY vms.id_vehicule, vms.date_mouvement DESC;

-- Vue matérialisée : Détails complets des véhicules
CREATE MATERIALIZED VIEW VM_Vehicule_Detail AS
SELECT
    v.id AS id_vehicule,
    v.immatriculation,
    v.marque,
    v.modele,
    v.maximum_passager,
    v.capacite_carburant,
    v.depense_carburant_100km,
    vt.libelle AS libelle_type,
    ct.libelle AS type_carburant,
    vsa.libelle_statut,
    vsa.date_mouvement AS date_dernier_statut
FROM Vehicule v
         INNER JOIN Vehicule_Type vt ON v.id_type = vt.id
         INNER JOIN Carburant_Type ct ON v.id_type_carburant = ct.id
         LEFT JOIN VM_Vehicule_Statut_Actuel vsa ON v.id = vsa.id_vehicule;

CREATE UNIQUE INDEX idx_vm_vehicule_detail_id ON VM_Vehicule_Detail (id_vehicule);

-- Vue matérialisée : Coût d'entretien par véhicule
CREATE MATERIALIZED VIEW VM_Vehicule_Cout_Entretien AS
SELECT
    v.id AS id_vehicule,
    COALESCE(SUM(ve.montant_depense), 0) AS total_depense_entretien,
    COUNT(ve.id) AS nombre_entretiens,
    MAX(ve.date_debut_entretien) AS dernier_entretien
FROM Vehicule v
         LEFT JOIN Vehicule_Entretien ve ON v.id = ve.id_vehicule
GROUP BY v.id;

CREATE UNIQUE INDEX idx_vm_vehicule_cout_id ON VM_Vehicule_Cout_Entretien (id_vehicule);

-- Vue : Historique complet des statuts véhicules
CREATE VIEW VM_Vehicule_Historique_Statut AS
SELECT
    vms.id,
    vms.id_vehicule,
    v.immatriculation,
    vs_ancien.libelle AS ancien_statut,
    vs_nouveau.libelle AS nouveau_statut,
    vms.date_mouvement,
    vms.observation
FROM Vehicule_Mouvement_Statut vms
         INNER JOIN Vehicule v ON vms.id_vehicule = v.id
         LEFT JOIN Vehicule_Statut vs_ancien ON vms.id_ancien_statut = vs_ancien.id
         INNER JOIN Vehicule_Statut vs_nouveau ON vms.id_nouveau_statut = vs_nouveau.id
ORDER BY vms.date_mouvement DESC;

-- ============================================
-- 2. VUES CHAUFFEURS
-- ============================================

-- Vue : Statut actuel des chauffeurs
CREATE VIEW VM_Chauffeur_Statut_Actuel AS
SELECT DISTINCT ON (cms.id_chauffeur)
    cms.id_chauffeur,
    cs.libelle AS libelle_statut,
    cms.date_mouvement
FROM Chauffeur_Mouvement_Statut cms
         INNER JOIN Chauffeur_Statut cs ON cms.id_nouveau_statut = cs.id
ORDER BY cms.id_chauffeur, cms.date_mouvement DESC;

-- Vue matérialisée : Détails complets des chauffeurs
CREATE MATERIALIZED VIEW VM_Chauffeur_Detail AS
SELECT
    c.id AS id_chauffeur,
    c.nom,
    c.prenom,
    c.nom || ' ' || c.prenom AS nom_complet,
    c.date_naissance,
    EXTRACT(YEAR FROM AGE(c.date_naissance)) AS age,
    c.numero_permis,
    csa.libelle_statut,
    csa.date_mouvement AS date_dernier_statut
FROM Chauffeur c
         LEFT JOIN VM_Chauffeur_Statut_Actuel csa ON c.id = csa.id_chauffeur;

CREATE UNIQUE INDEX idx_vm_chauffeur_detail_id ON VM_Chauffeur_Detail (id_chauffeur);

-- Vue matérialisée : Activité des chauffeurs
CREATE MATERIALIZED VIEW VM_Chauffeur_Activite AS
SELECT
    c.id AS id_chauffeur,
    COUNT(t.id) AS nombre_trajets,
    COUNT(CASE WHEN t.id_trajet_statut IN (
        SELECT id FROM Trajet_Statut WHERE libelle = 'Terminé'
    ) THEN 1 END) AS trajets_termines,
    MAX(t.datetime_depart) AS dernier_trajet,
    MIN(t.datetime_depart) AS premier_trajet
FROM Chauffeur c
         LEFT JOIN Trajet t ON c.id = t.id_chauffeur
GROUP BY c.id;

CREATE UNIQUE INDEX idx_vm_chauffeur_activite_id ON VM_Chauffeur_Activite (id_chauffeur);

-- Vue : Historique des statuts chauffeurs
CREATE VIEW VM_Chauffeur_Historique_Statut AS
SELECT
    cms.id,
    cms.id_chauffeur,
    c.nom || ' ' || c.prenom AS chauffeur,
    cs_ancien.libelle AS ancien_statut,
    cs_nouveau.libelle AS nouveau_statut,
    cms.date_mouvement,
    cms.observation
FROM Chauffeur_Mouvement_Statut cms
         INNER JOIN Chauffeur c ON cms.id_chauffeur = c.id
         LEFT JOIN Chauffeur_Statut cs_ancien ON cms.id_ancien_statut = cs_ancien.id
         INNER JOIN Chauffeur_Statut cs_nouveau ON cms.id_nouveau_statut = cs_nouveau.id
ORDER BY cms.date_mouvement DESC;

-- ============================================
-- 3. VUES GÉOGRAPHIQUES
-- ============================================

-- Vue matérialisée : Détails complets des villes
CREATE MATERIALIZED VIEW VM_Ville_Detail AS
SELECT
    v.id AS id_ville,
    v.nom AS nom_ville,
    r.nom AS nom_region,
    p.nom AS nom_province,
    r.id AS id_region,
    p.id AS id_province
FROM Ville v
         INNER JOIN Region r ON v.id_region = r.id
         INNER JOIN Province p ON r.id_province = p.id;

CREATE UNIQUE INDEX idx_vm_ville_detail_id ON VM_Ville_Detail (id_ville);

-- ============================================
-- 4. VUES LIGNES
-- ============================================

-- Vue matérialisée : Détails des lignes
CREATE MATERIALIZED VIEW VM_Ligne_Detail AS
SELECT
    l.id AS id_ligne,
    vd.nom_ville AS ville_depart,
    vd.nom_region AS region_depart,
    va.nom_ville AS ville_arrivee,
    va.nom_region AS region_arrivee,
    l.distance_km,
    COUNT(ld.id) AS nombre_arrets
FROM Ligne l
         INNER JOIN VM_Ville_Detail vd ON l.id_ville_depart = vd.id_ville
         INNER JOIN VM_Ville_Detail va ON l.id_ville_arrivee = va.id_ville
         LEFT JOIN Ligne_Detail ld ON l.id = ld.id_ligne
GROUP BY l.id, vd.nom_ville, vd.nom_region, va.nom_ville, va.nom_region, l.distance_km;

CREATE UNIQUE INDEX idx_vm_ligne_detail_id ON VM_Ligne_Detail (id_ligne);

-- Vue : Itinéraire complet des lignes
CREATE VIEW VM_Ligne_Itineraire AS
SELECT
    ld.id_ligne,
    ld.ordre,
    la.nom_arret,
    v.nom AS nom_ville,
    r.nom AS nom_region
FROM Ligne_Detail ld
         INNER JOIN Ligne_Arret la ON ld.id_ligne_arret = la.id
         INNER JOIN Ville v ON la.id_ville = v.id
         INNER JOIN Region r ON v.id_region = r.id
ORDER BY ld.id_ligne, ld.ordre;

-- ============================================
-- 5. VUES TRAJETS
-- ============================================

-- Vue : Statut actuel des trajets
CREATE VIEW VM_Trajet_Statut_Actuel AS
SELECT DISTINCT ON (tms.id_trajet)
    tms.id_trajet,
    ts.libelle AS libelle_statut,
    tms.date_mouvement
FROM Trajet_Mouvement_Statut tms
         INNER JOIN Trajet_Statut ts ON tms.id_nouveau_statut = ts.id
ORDER BY tms.id_trajet, tms.date_mouvement DESC;

-- Vue matérialisée : Détails complets des trajets
CREATE MATERIALIZED VIEW VM_Trajet_Detail AS
SELECT
    t.id AS id_trajet,
    t.id_ligne,
    ld.ville_depart,
    ld.ville_arrivee,
    c.nom || ' ' || c.prenom AS chauffeur,
    t.id_chauffeur,
    v.immatriculation AS vehicule,
    v.marque || ' ' || v.modele AS modele_vehicule,
    t.id_vehicule,
    tsa.libelle_statut AS statut_trajet,
    t.datetime_depart,
    t.datetime_arrivee,
    t.nombre_passager,
    v.maximum_passager,
    t.frais_unitaire,
    CASE
        WHEN t.datetime_arrivee IS NOT NULL
            THEN EXTRACT(EPOCH FROM (t.datetime_arrivee - t.datetime_depart))/3600
        END AS duree_heures
FROM Trajet t
         INNER JOIN VM_Ligne_Detail ld ON t.id_ligne = ld.id_ligne
         INNER JOIN Chauffeur c ON t.id_chauffeur = c.id
         INNER JOIN Vehicule v ON t.id_vehicule = v.id
         LEFT JOIN VM_Trajet_Statut_Actuel tsa ON t.id = tsa.id_trajet;

CREATE UNIQUE INDEX idx_vm_trajet_detail_id ON VM_Trajet_Detail (id_trajet);

-- Vue matérialisée : Taux de remplissage des trajets
CREATE MATERIALIZED VIEW VM_Trajet_Remplissage AS
SELECT
    t.id AS id_trajet,
    t.nombre_passager AS capacite_prevue,
    v.maximum_passager AS capacite_max,
    COALESCE(SUM(tr.nombre_place_reservation), 0) AS places_reservees,
    v.maximum_passager - COALESCE(SUM(tr.nombre_place_reservation), 0) AS places_restantes,
    ROUND(
            COALESCE(SUM(tr.nombre_place_reservation), 0)::NUMERIC /
            NULLIF(v.maximum_passager, 0) * 100,
            2
    ) AS taux_remplissage_pct
FROM Trajet t
         INNER JOIN Vehicule v ON t.id_vehicule = v.id
         LEFT JOIN Trajet_Reservation tr ON t.id = tr.id_trajet
    AND tr.id_reservation_statut NOT IN (
        SELECT id FROM Reservation_Statut WHERE libelle = 'Annulé'
    )
GROUP BY t.id, v.maximum_passager;

CREATE UNIQUE INDEX idx_vm_trajet_remplissage_id ON VM_Trajet_Remplissage (id_trajet);

-- Vue : Incidents de trajets
CREATE VIEW VM_Trajet_Incident AS
SELECT
    tad.id,
    tad.id_trajet,
    v.nom AS ville,
    tma.libelle AS motif_arret,
    tad.montant_depense,
    tad.datetime_debut,
    tad.datetime_fin,
    CASE
        WHEN tad.datetime_fin IS NOT NULL
            THEN EXTRACT(EPOCH FROM (tad.datetime_fin - tad.datetime_debut))/3600
        END AS duree_arret_heures
FROM Trajet_Arret_Detail tad
         INNER JOIN Ville v ON tad.id_ville = v.id
         INNER JOIN Trajet_Motif_Arret tma ON tad.id_trajet_motif_arret = tma.id
ORDER BY tad.datetime_debut DESC;

-- Vue : Consommation carburant par trajet
CREATE VIEW VM_Trajet_Carburant AS
SELECT
    tcd.id_trajet,
    COUNT(tcd.id) AS nombre_ravitaillements,
    SUM(tcd.quantite_carburant_ajoute) AS total_litres,
    SUM(tcd.quantite_carburant_ajoute * tcd.taux_carburant) AS cout_total_carburant,
    AVG(tcd.taux_carburant) AS taux_moyen
FROM Trajet_Carburant_Detail tcd
GROUP BY tcd.id_trajet;

-- ============================================
-- 6. VUES RÉSERVATIONS
-- ============================================

-- Vue : Statut actuel des réservations
CREATE VIEW VM_Reservation_Statut_Actuel AS
SELECT DISTINCT ON (trms.id_trajet_reservation)
    trms.id_trajet_reservation,
    rs.libelle AS libelle_statut,
    trms.date_mouvement
FROM Trajet_Reservation_Mouvement_Status trms
         INNER JOIN Reservation_Statut rs ON trms.id_nouveau_statut = rs.id
ORDER BY trms.id_trajet_reservation, trms.date_mouvement DESC;

-- Vue matérialisée : Détails des réservations
CREATE MATERIALIZED VIEW VM_Reservation_Detail AS
SELECT
    tr.id AS id_reservation,
    tr.id_trajet,
    c.nom_client,
    tc.libelle AS type_client,
    tr.nom_passager,
    tr.numero_siege,
    COALESCE(rsa.libelle_statut, rs.libelle) AS statut_reservation,
    tr.montant,
    tr.nombre_place_reservation,
    tr.date_reservation
FROM Trajet_Reservation tr
         INNER JOIN Client c ON tr.id_client = c.id
         INNER JOIN Type_Client tc ON c.id_type_client = tc.id
         INNER JOIN Reservation_Statut rs ON tr.id_reservation_statut = rs.id
         LEFT JOIN VM_Reservation_Statut_Actuel rsa ON tr.id = rsa.id_trajet_reservation;

CREATE UNIQUE INDEX idx_vm_reservation_detail_id ON VM_Reservation_Detail (id_reservation);

-- Vue matérialisée : Paiements par trajet
CREATE MATERIALIZED VIEW VM_Paiement_Trajet AS
SELECT
    tr.id_trajet,
    COUNT(DISTINCT trp.id) AS nombre_paiements,
    COALESCE(SUM(trp.montant), 0) AS total_paye,
    MAX(trp.date_paiement) AS dernier_paiement,
    STRING_AGG(DISTINCT mp.libelle, ', ') AS modes_paiement
FROM Trajet_Reservation tr
         LEFT JOIN Trajet_Reservation_Paiement trp ON tr.id = trp.id_trajet_reservation
         LEFT JOIN Mode_Paiement mp ON trp.id_mode_paiement = mp.id
GROUP BY tr.id_trajet;

CREATE UNIQUE INDEX idx_vm_paiement_trajet_id ON VM_Paiement_Trajet (id_trajet);

-- ============================================
-- 7. VUES FINANCIÈRES
-- ============================================

-- Vue matérialisée : Finance par trajet
CREATE MATERIALIZED VIEW VM_Trajet_Finance AS
SELECT
    tf.id_trajet,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Recette' THEN tf.montant ELSE 0 END), 0) AS total_recette,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Dépense' THEN tf.montant ELSE 0 END), 0) AS total_depense,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Recette' THEN tf.montant ELSE -tf.montant END), 0) AS benefice
FROM Trajet_Finance tf
         INNER JOIN Type_Mouvement tm ON tf.id_type_mouvement = tm.id
GROUP BY tf.id_trajet;

CREATE UNIQUE INDEX idx_vm_trajet_finance_id ON VM_Trajet_Finance (id_trajet);

-- Vue matérialisée : Finance journalière
CREATE MATERIALIZED VIEW VM_Finance_Journaliere AS
SELECT
    DATE(tf.date_mouvement) AS date_jour,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Recette' THEN tf.montant ELSE 0 END), 0) AS total_recette,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Dépense' THEN tf.montant ELSE 0 END), 0) AS total_depense,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Recette' THEN tf.montant ELSE -tf.montant END), 0) AS benefice,
    COUNT(DISTINCT tf.id_trajet) AS nombre_trajets
FROM Trajet_Finance tf
         INNER JOIN Type_Mouvement tm ON tf.id_type_mouvement = tm.id
GROUP BY DATE(tf.date_mouvement)
ORDER BY date_jour DESC;

CREATE UNIQUE INDEX idx_vm_finance_jour ON VM_Finance_Journaliere (date_jour);

-- Vue matérialisée : Finance mensuelle
CREATE MATERIALIZED VIEW VM_Finance_Mensuelle AS
SELECT
    DATE_TRUNC('month', tf.date_mouvement) AS mois,
    EXTRACT(YEAR FROM tf.date_mouvement) AS annee,
    EXTRACT(MONTH FROM tf.date_mouvement) AS numero_mois,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Recette' THEN tf.montant ELSE 0 END), 0) AS total_recette,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Dépense' THEN tf.montant ELSE 0 END), 0) AS total_depense,
    COALESCE(SUM(CASE WHEN tm.libelle = 'Recette' THEN tf.montant ELSE -tf.montant END), 0) AS benefice,
    COUNT(DISTINCT tf.id_trajet) AS nombre_trajets
FROM Trajet_Finance tf
         INNER JOIN Type_Mouvement tm ON tf.id_type_mouvement = tm.id
GROUP BY DATE_TRUNC('month', tf.date_mouvement), EXTRACT(YEAR FROM tf.date_mouvement), EXTRACT(MONTH FROM tf.date_mouvement)
ORDER BY mois DESC;

CREATE UNIQUE INDEX idx_vm_finance_mois ON VM_Finance_Mensuelle (mois);

-- Vue matérialisée : Prévision vs Réel
CREATE MATERIALIZED VIEW VM_Prevision_vs_Reel AS
SELECT
    t.id AS id_trajet,
    COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Recette')
                          THEN pf.montant ELSE 0 END), 0) AS recette_prevue,
    COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Dépense')
                          THEN pf.montant ELSE 0 END), 0) AS depense_prevue,
    COALESCE(vtf.total_recette, 0) AS recette_reelle,
    COALESCE(vtf.total_depense, 0) AS depense_reelle,
    COALESCE(vtf.total_recette, 0) - COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Recette')
                                                           THEN pf.montant ELSE 0 END), 0) AS ecart_recette,
    COALESCE(vtf.total_depense, 0) - COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Dépense')
                                                           THEN pf.montant ELSE 0 END), 0) AS ecart_depense,
    COALESCE(vtf.benefice, 0) - (
        COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Recette')
                              THEN pf.montant ELSE 0 END), 0) -
        COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Dépense')
                              THEN pf.montant ELSE 0 END), 0)
        ) AS ecart_benefice
FROM Trajet t
         LEFT JOIN Prevision_Finance pf ON t.id = pf.id_trajet
         LEFT JOIN VM_Trajet_Finance vtf ON t.id = vtf.id_trajet
GROUP BY t.id, vtf.total_recette, vtf.total_depense, vtf.benefice;

CREATE UNIQUE INDEX idx_vm_prevision_reel_id ON VM_Prevision_vs_Reel (id_trajet);

-- Vue : Détails des prévisions financières
CREATE VIEW VM_Prevision_Detail AS
SELECT
    pf.id,
    pf.id_trajet,
    pf.table_origine,
    pf.id_entite_origine,
    tm.libelle AS type_mouvement,
    pf.montant,
    pf.date,
    pf.description
FROM Prevision_Finance pf
         INNER JOIN Type_Mouvement tm ON pf.id_type_mouvement = tm.id
ORDER BY pf.date DESC;

-- ============================================
-- 8. VUES CAISSE
-- ============================================

-- Vue matérialisée : Détails des caisses
CREATE MATERIALIZED VIEW VM_Caisse_Detail AS
SELECT
    c.id AS id_caisse,
    ct.libelle AS libelle_type,
    c.nom,
    c.solde_initial
FROM Caisse c
         INNER JOIN Caisse_Type ct ON c.id_caisse_type = ct.id;

CREATE UNIQUE INDEX idx_vm_caisse_detail_id ON VM_Caisse_Detail (id_caisse);

-- Vue matérialisée : Solde actuel des caisses
CREATE MATERIALIZED VIEW VM_Caisse_Solde_Actuel AS
SELECT
    c.id AS id_caisse,
    c.solde_initial + COALESCE(SUM(
                                       CASE
                                           WHEN tm.libelle = 'Recette' THEN cm.montant
                                           WHEN tm.libelle = 'Dépense' THEN -cm.montant
                                           ELSE 0
                                           END
                               ), 0) AS solde_actuel,
    COUNT(cm.id) AS nombre_mouvements,
    MAX(cm.date_mouvement) AS dernier_mouvement
FROM Caisse c
         LEFT JOIN Caisse_Mouvement cm ON c.id = cm.id_caisse
         LEFT JOIN Type_Mouvement tm ON cm.id_type_mouvement = tm.id
GROUP BY c.id, c.solde_initial;

CREATE UNIQUE INDEX idx_vm_caisse_solde_id ON VM_Caisse_Solde_Actuel (id_caisse);

-- Vue : Historique des mouvements de caisse
CREATE VIEW VM_Caisse_Mouvement_Historique AS
SELECT
    cm.id,
    cm.id_caisse,
    c.nom AS nom_caisse,
    cm.date_mouvement,
    tm.libelle AS libelle_type_mouvement,
    cm.montant,
    cm.motif,
    cm.table_origine,
    cm.id_entite_origine
FROM Caisse_Mouvement cm
         INNER JOIN Caisse c ON cm.id_caisse = c.id
         INNER JOIN Type_Mouvement tm ON cm.id_type_mouvement = tm.id
ORDER BY cm.date_mouvement DESC;

-- Vue : Traçabilité des mouvements de caisse par source
CREATE VIEW VM_Caisse_Tracabilite AS
SELECT
    cm.id AS id_mouvement,
    cm.id_caisse,
    c.nom AS caisse,
    cm.table_origine,
    cm.id_entite_origine,
    tm.libelle AS type_mouvement,
    cm.montant,
    cm.date_mouvement,
    CASE cm.table_origine
        WHEN 'Trajet_Reservation_Paiement' THEN
            (SELECT CONCAT('Paiement réservation #', trp.id, ' - ', tr.nom_passager)
             FROM Trajet_Reservation_Paiement trp
                      INNER JOIN Trajet_Reservation tr ON trp.id_trajet_reservation = tr.id
             WHERE trp.id = cm.id_entite_origine)
        WHEN 'Trajet_Arret_Detail' THEN
            (SELECT CONCAT('Incident trajet #', tad.id_trajet, ' - ', tma.libelle)
             FROM Trajet_Arret_Detail tad
                      INNER JOIN Trajet_Motif_Arret tma ON tad.id_trajet_motif_arret = tma.id
             WHERE tad.id = cm.id_entite_origine)
        WHEN 'Vehicule_Entretien' THEN
            (SELECT CONCAT('Entretien véhicule #', ve.id_vehicule, ' - ', ve.motif)
             FROM Vehicule_Entretien ve
             WHERE ve.id = cm.id_entite_origine)
        WHEN 'Trajet_Carburant_Detail' THEN
            (SELECT CONCAT('Carburant trajet #', tcd.id_trajet, ' - ', tcd.quantite_carburant_ajoute, 'L')
             FROM Trajet_Carburant_Detail tcd
             WHERE tcd.id = cm.id_entite_origine)
        ELSE 'Autre opération'
        END AS description_origine
FROM Caisse_Mouvement cm
         INNER JOIN Caisse c ON cm.id_caisse = c.id
         INNER JOIN Type_Mouvement tm ON cm.id_type_mouvement = tm.id
ORDER BY cm.date_mouvement DESC;

-- ============================================
-- 9. VUES ANALYTIQUES AVANCÉES
-- ============================================

-- Vue : Performance par ligne
CREATE MATERIALIZED VIEW VM_Performance_Ligne AS
SELECT
    l.id AS id_ligne,
    ld.ville_depart,
    ld.ville_arrivee,
    COUNT(DISTINCT t.id) AS nombre_trajets,
    COALESCE(AVG(vr.taux_remplissage_pct), 0) AS taux_remplissage_moyen,
    COALESCE(SUM(vtf.total_recette), 0) AS recette_totale,
    COALESCE(SUM(vtf.benefice), 0) AS benefice_total,
    COALESCE(AVG(vtf.benefice), 0) AS benefice_moyen_par_trajet
FROM Ligne l
         INNER JOIN VM_Ligne_Detail ld ON l.id = ld.id_ligne
         LEFT JOIN Trajet t ON l.id = t.id_ligne
         LEFT JOIN VM_Trajet_Remplissage vr ON t.id = vr.id_trajet
         LEFT JOIN VM_Trajet_Finance vtf ON t.id = vtf.id_trajet
GROUP BY l.id, ld.ville_depart, ld.ville_arrivee;

CREATE UNIQUE INDEX idx_vm_perf_ligne_id ON VM_Performance_Ligne (id_ligne);

-- Vue : Performance par véhicule
CREATE MATERIALIZED VIEW VM_Performance_Vehicule AS
SELECT
    v.id AS id_vehicule,
    v.immatriculation,
    v.marque,
    v.modele,
    COUNT(DISTINCT t.id) AS nombre_trajets,
    COALESCE(SUM(vce.total_depense_entretien), 0) AS cout_entretien_total,
    COALESCE(SUM(vtf.total_recette), 0) AS recette_totale,
    COALESCE(SUM(vtf.benefice), 0) AS benefice_total,
    COALESCE(AVG(EXTRACT(EPOCH FROM (t.datetime_arrivee - t.datetime_depart))/3600), 0) AS duree_moyenne_trajet_heures
FROM Vehicule v
         LEFT JOIN Trajet t ON v.id = t.id_vehicule
         LEFT JOIN VM_Vehicule_Cout_Entretien vce ON v.id = vce.id_vehicule
         LEFT JOIN VM_Trajet_Finance vtf ON t.id = vtf.id_trajet
GROUP BY v.id, v.immatriculation, v.marque, v.modele;

CREATE UNIQUE INDEX idx_vm_perf_vehicule_id ON VM_Performance_Vehicule (id_vehicule);

