package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        t.id AS id_trajet,
        vd.nom AS nom_ville_depart,
        va.nom AS nom_ville_arrive,
        v.immatriculation AS immatriculation_vehicule,
        c.nom AS nom_chauffeur,
        c.prenom AS prenom_chauffeur,
        CAST(t.datetime_depart AS DATE) AS date_depart,
        CAST(t.datetime_depart AS TIME) AS heure_depart,
        COALESCE(SUM(trp.montant), 0) AS montant_reel_ticket,
        COALESCE(SUM(dpr.montant_paye), 0) AS montant_reel_diffusion,
        COALESCE(SUM(trp.montant), 0) + COALESCE(SUM(dpr.montant_paye), 0) AS ca_reel,
        COALESCE(SUM(trp.montant), 0) AS montant_paye,
        COALESCE(SUM(trd.nombre_places * COALESCE(tttpcr.tarif_unitaire_avec_remise, vttp.tarif_unitaire)), 0) - COALESCE(SUM(trp.montant), 0) AS montant_restant
    FROM Trajet t
    INNER JOIN Ligne l ON t.id_ligne = l.id
    INNER JOIN Ville vd ON l.id_ville_depart = vd.id
    INNER JOIN Ville va ON l.id_ville_arrivee = va.id
    INNER JOIN Vehicule v ON t.id_vehicule = v.id
    INNER JOIN Chauffeur c ON t.id_chauffeur = c.id
    LEFT JOIN Trajet_Reservation tr ON tr.id_trajet = t.id
    LEFT JOIN Trajet_Reservation_Details trd ON trd.id_trajet_reservation = tr.id
    LEFT JOIN Trajet_Tarif_Type_Place_Categorie_Remise tttpcr 
        ON tttpcr.id_trajet = t.id 
        AND tttpcr.id_type_place = trd.id_type_place 
        AND tttpcr.id_categorie_personne = trd.id_categorie_personne
    LEFT JOIN Vehicule_Tarif_Type_Place vttp 
        ON vttp.id_vehicule = t.id_vehicule 
        AND vttp.id_type_place = trd.id_type_place
    LEFT JOIN Trajet_Reservation_Paiement trp ON trp.id_trajet_reservation = tr.id
    LEFT JOIN Diffusion_Detail dd ON dd.id_trajet = t.id
    LEFT JOIN Diffusion_Paiement_Repartition dpr ON dpr.id_diffusion_detail = dd.id
    GROUP BY t.id, vd.nom, va.nom, v.immatriculation, c.nom, c.prenom, t.datetime_depart
""")
@Synchronize({"Trajet", "Ligne", "Ville", "Vehicule", "Chauffeur", "Trajet_Reservation",
        "Trajet_Reservation_Details", "Trajet_Tarif_Type_Place_Categorie_Remise",
        "Vehicule_Tarif_Type_Place", "Trajet_Reservation_Paiement", "Diffusion_Detail",
        "Diffusion_Paiement_Repartition"})
public class VmTrajetCaReel implements Serializable {

    @Id
    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "nom_ville_depart")
    private String nomVilleDepart;

    @Column(name = "nom_ville_arrive")
    private String nomVilleArrive;

    @Column(name = "immatriculation_vehicule")
    private String immatriculationVehicule;

    @Column(name = "nom_chauffeur")
    private String nomChauffeur;

    @Column(name = "prenom_chauffeur")
    private String prenomChauffeur;

    @Column(name = "date_depart")
    private LocalDate dateDepart;

    @Column(name = "heure_depart")
    private LocalTime heureDepart;

    @Column(name = "montant_reel_ticket")
    private BigDecimal montantReelTicket;

    @Column(name = "montant_reel_diffusion")
    private BigDecimal montantReelDiffusion;

    @Column(name = "ca_reel")
    private BigDecimal caReel;

    @Column(name = "montant_paye")
    private BigDecimal montantPaye;

    @Column(name = "montant_restant")
    private BigDecimal montantRestant;

    // Alias getter for compatibility
    public BigDecimal getMontantChiffreAffaireReel() {
        return caReel;
    }
}