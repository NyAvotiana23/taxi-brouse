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
                EXTRACT(MONTH FROM t.datetime_depart)::INTEGER AS mois,
                EXTRACT(YEAR FROM t.datetime_depart)::INTEGER AS annee,
        
                -- CA PRÉVISIONNEL
                -- Montant prévisionnel tickets par mois
                COALESCE(SUM(COALESCE(prevision_tickets.montant_tickets, 0)), 0) AS caPrevisionTicket,
        
                -- Montant prévisionnel diffusions par mois
                COALESCE(SUM(COALESCE(prevision_diffusion.montant_diffusion, 0)), 0) AS caPrevisionDiffusion,
        
                -- Montant prévisionnel produits (ventes de produits supplémentaires)
                COALESCE(prevision_produits.montant_produits, 0) AS caPrevisionProduit,
        
                -- CA prévisionnel total
                COALESCE(SUM(COALESCE(prevision_tickets.montant_tickets, 0)), 0) + 
                COALESCE(SUM(COALESCE(prevision_diffusion.montant_diffusion, 0)), 0) +
                COALESCE(prevision_produits.montant_produits, 0) AS caPrevisionTotal,
        
                -- CA RÉEL
                -- Montant réel tickets (paiements effectués)
                COALESCE(SUM(COALESCE(paiements_tickets.montant_reel_tickets, 0)), 0) AS caReelTicket,
        
                -- Montant réel diffusions (paiements effectués)
                COALESCE(SUM(COALESCE(paiements_diffusion.montant_reel_diffusion, 0)), 0) AS caReelDiffusion,
        
                -- Montant réel produits (paiements effectués)
                COALESCE(paiements_produits.montant_reel_produits, 0) AS caReelProduit,
        
                -- CA réel total
                COALESCE(SUM(COALESCE(paiements_tickets.montant_reel_tickets, 0)), 0) + 
                COALESCE(SUM(COALESCE(paiements_diffusion.montant_reel_diffusion, 0)), 0) +
                COALESCE(paiements_produits.montant_reel_produits, 0) AS caReelTotal
        
            FROM Trajet t
            INNER JOIN Ligne l ON t.id_ligne = l.id
        
            -- Sous-requête pour le montant prévisionnel des tickets
            LEFT JOIN (
                SELECT 
                    tr.id_trajet,
                    SUM(COALESCE(trd.nombre_places, 0) * COALESCE(trd.tarif_unitaire, 0)) AS montant_tickets
                FROM Trajet_Reservation tr
                INNER JOIN Trajet_Reservation_Details trd ON trd.id_trajet_reservation = tr.id
                GROUP BY tr.id_trajet
            ) prevision_tickets ON prevision_tickets.id_trajet = t.id
        
            -- Sous-requête pour le montant prévisionnel des diffusions
            LEFT JOIN (
                SELECT 
                    dd.id_trajet,
                    SUM(COALESCE(dd.nombre_repetition, 0) * COALESCE(dd.montant_unitaire, 0)) AS montant_diffusion
                FROM Diffusion_Detail dd
                GROUP BY dd.id_trajet
            ) prevision_diffusion ON prevision_diffusion.id_trajet = t.id
        
            -- Sous-requête pour le montant prévisionnel des produits supplémentaires par mois/année
            LEFT JOIN (
                SELECT 
                    EXTRACT(MONTH FROM pev.date)::INTEGER AS mois,
                    EXTRACT(YEAR FROM pev.date)::INTEGER AS annee,
                    SUM(COALESCE(pev.quantite, 0) * COALESCE(pev.prix_unitaire, 0)) AS montant_produits
                FROM Produit_Extra_Vente pev
                GROUP BY EXTRACT(MONTH FROM pev.date), EXTRACT(YEAR FROM pev.date)
            ) prevision_produits ON prevision_produits.mois = EXTRACT(MONTH FROM t.datetime_depart)::INTEGER 
                                 AND prevision_produits.annee = EXTRACT(YEAR FROM t.datetime_depart)::INTEGER
        
            -- Sous-requête pour les paiements réels des tickets
            LEFT JOIN (
                SELECT 
                    tr.id_trajet,
                    SUM(COALESCE(trp.montant, 0)) AS montant_reel_tickets
                FROM Trajet_Reservation tr
                INNER JOIN Trajet_Reservation_Paiement trp ON trp.id_trajet_reservation = tr.id
                GROUP BY tr.id_trajet
            ) paiements_tickets ON paiements_tickets.id_trajet = t.id
        
            -- Sous-requête pour les paiements réels des diffusions
            LEFT JOIN (
                SELECT 
                    dd.id_trajet,
                    SUM(COALESCE(dpr.montant_paye, 0)) AS montant_reel_diffusion
                FROM Diffusion_Detail dd
                INNER JOIN Diffusion_Paiement_Repartition dpr ON dpr.id_diffusion_detail = dd.id
                GROUP BY dd.id_trajet
            ) paiements_diffusion ON paiements_diffusion.id_trajet = t.id
        
            -- Sous-requête pour les paiements réels des produits supplémentaires par mois/année
            LEFT JOIN (
                SELECT 
                    EXTRACT(MONTH FROM pevp.date_payement)::INTEGER AS mois,
                    EXTRACT(YEAR FROM pevp.date_payement)::INTEGER AS annee,
                    SUM(COALESCE(pevp.montant, 0)) AS montant_reel_produits
                FROM Produit_Extra_Vente_Payement pevp
                GROUP BY EXTRACT(MONTH FROM pevp.date_payement), EXTRACT(YEAR FROM pevp.date_payement)
            ) paiements_produits ON paiements_produits.mois = EXTRACT(MONTH FROM t.datetime_depart)::INTEGER 
                                 AND paiements_produits.annee = EXTRACT(YEAR FROM t.datetime_depart)::INTEGER
        
            GROUP BY EXTRACT(MONTH FROM t.datetime_depart), EXTRACT(YEAR FROM t.datetime_depart)
            ORDER BY annee DESC, mois DESC
        """)
@Synchronize({"Trajet", "Ligne",
        "Trajet_Reservation", "Trajet_Reservation_Details", "Trajet_Reservation_Paiement",
        "Diffusion_Detail", "Diffusion_Paiement_Repartition",
        "Produit_Extra_Vente", "Produit_Extra_Vente_Payement"})
public class VmCAComplet implements Serializable {

    @Id
    @Column(name = "mois")
    private Integer mois;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "caPrevisionTicket", precision = 15, scale = 2)
    private BigDecimal caPrevisionTicket;

    @Column(name = "caPrevisionDiffusion", precision = 15, scale = 2)
    private BigDecimal caPrevisionDiffusion;

    @Column(name = "caPrevisionProduit", precision = 15, scale = 2)
    private BigDecimal caPrevisionProduit;

    @Column(name = "caPrevisionTotal", precision = 15, scale = 2)
    private BigDecimal caPrevisionTotal;

    @Column(name = "caReelTicket", precision = 15, scale = 2)
    private BigDecimal caReelTicket;

    @Column(name = "caReelDiffusion", precision = 15, scale = 2)
    private BigDecimal caReelDiffusion;

    @Column(name = "caReelProduit", precision = 15, scale = 2)
    private BigDecimal caReelProduit;

    @Column(name = "caReelTotal", precision = 15, scale = 2)
    private BigDecimal caReelTotal;
}
