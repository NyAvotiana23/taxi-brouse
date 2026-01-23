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
        
                -- Montant réel des tickets (via paiements effectués)
                COALESCE(paiements_tickets.montant_reel_tickets, 0) AS montant_reel_ticket,
        
                -- Montant réel des diffusions (via paiements effectués)
                COALESCE(paiements_diffusion.montant_reel_diffusion, 0) AS montant_reel_diffusion,
        
                -- CA réel total
                COALESCE(paiements_tickets.montant_reel_tickets, 0) + 
                COALESCE(paiements_diffusion.montant_reel_diffusion, 0) AS montant_chiffre_affaire_reel
        
            FROM Trajet t
            INNER JOIN Ligne l ON t.id_ligne = l.id
            INNER JOIN Ville vd ON l.id_ville_depart = vd.id
            INNER JOIN Ville va ON l.id_ville_arrivee = va.id
            INNER JOIN Vehicule v ON t.id_vehicule = v.id
            INNER JOIN Chauffeur c ON t.id_chauffeur = c.id
        
            -- Sous-requête pour les paiements des réservations (tickets)
            LEFT JOIN (
                SELECT 
                    tr.id_trajet,
                    SUM(trp.montant) AS montant_reel_tickets
                FROM Trajet_Reservation tr
                INNER JOIN Trajet_Reservation_Paiement trp ON trp.id_trajet_reservation = tr.id
                GROUP BY tr.id_trajet
            ) paiements_tickets ON paiements_tickets.id_trajet = t.id
        
            -- Sous-requête pour les paiements des diffusions publicitaires
            LEFT JOIN (
                SELECT 
                    dd.id_trajet,
                    SUM(dpr.montant_paye) AS montant_reel_diffusion
                FROM Diffusion_Detail dd
                INNER JOIN Diffusion_Paiement_Repartition dpr ON dpr.id_diffusion_detail = dd.id
                GROUP BY dd.id_trajet
            ) paiements_diffusion ON paiements_diffusion.id_trajet = t.id
        
            ORDER BY t.id ASC
        """)
@Synchronize({"Trajet", "Ligne", "Ville", "Vehicule", "Chauffeur",
        "Trajet_Reservation", "Trajet_Reservation_Paiement",
        "Diffusion_Detail", "Diffusion_Paiement_Repartition"})
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

    @Column(name = "montant_chiffre_affaire_reel")
    private BigDecimal montantChiffreAffaireReel;
}