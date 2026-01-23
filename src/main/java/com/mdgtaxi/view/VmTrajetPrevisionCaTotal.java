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
        
                -- Nombre de places totales du véhicule
                COALESCE(places_totales.total_places, 0) AS nombre_places_totales,
        
                -- Nombre de places prises via réservations
                COALESCE(places_prises.total_prises, 0) AS nombre_places_prises,
        
                -- Places restantes
                COALESCE(places_totales.total_places, 0) - COALESCE(places_prises.total_prises, 0) AS nombre_places_restantes,
        
                -- Montant des tickets (réservations)
                COALESCE(revenus_tickets.montant_tickets, 0) AS montant_prevision_ticket,
        
                -- Montant des diffusions publicitaires
                COALESCE(revenus_diffusion.montant_diffusion, 0) AS montant_prevision_diffusion,
        
                -- CA total prévisionnel
                COALESCE(revenus_tickets.montant_tickets, 0) + COALESCE(revenus_diffusion.montant_diffusion, 0) AS ca_previsionnel
        
            FROM Trajet t
            INNER JOIN Ligne l ON t.id_ligne = l.id
            INNER JOIN Ville vd ON l.id_ville_depart = vd.id
            INNER JOIN Ville va ON l.id_ville_arrivee = va.id
            INNER JOIN Vehicule v ON t.id_vehicule = v.id
            INNER JOIN Chauffeur c ON t.id_chauffeur = c.id
        
            -- Sous-requête pour calculer le nombre total de places du véhicule
            LEFT JOIN (
                SELECT 
                    id_vehicule,
                    SUM(nombre_place) AS total_places
                FROM Vehicule_Tarif_Type_Place
                GROUP BY id_vehicule
            ) places_totales ON places_totales.id_vehicule = v.id
        
            -- Sous-requête pour calculer le nombre de places prises
            LEFT JOIN (
                SELECT 
                    tr.id_trajet,
                    SUM(trd.nombre_places) AS total_prises
                FROM Trajet_Reservation tr
                INNER JOIN Trajet_Reservation_Details trd ON trd.id_trajet_reservation = tr.id
                GROUP BY tr.id_trajet
            ) places_prises ON places_prises.id_trajet = t.id
        
            -- Sous-requête pour calculer le montant des tickets
            LEFT JOIN (
                SELECT 
                    tr.id_trajet,
                    SUM(trd.nombre_places * trd.tarif_unitaire) AS montant_tickets
                FROM Trajet_Reservation tr
                INNER JOIN Trajet_Reservation_Details trd ON trd.id_trajet_reservation = tr.id
                GROUP BY tr.id_trajet
            ) revenus_tickets ON revenus_tickets.id_trajet = t.id
        
            -- Sous-requête pour calculer le montant des diffusions
            LEFT JOIN (
                SELECT 
                    dd.id_trajet,
                    SUM(dd.nombre_repetition * dd.montant_unitaire) AS montant_diffusion
                FROM Diffusion_Detail dd
                GROUP BY dd.id_trajet
            ) revenus_diffusion ON revenus_diffusion.id_trajet = t.id
        
            ORDER BY t.id ASC
        """)
@Synchronize({"Trajet", "Ligne", "Ville", "Vehicule", "Chauffeur", "Vehicule_Tarif_Type_Place",
        "Trajet_Reservation", "Trajet_Reservation_Details", "Diffusion_Detail"})
public class VmTrajetPrevisionCaTotal implements Serializable {

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

    @Column(name = "nombre_places_totales")
    private BigDecimal nombrePlacesTotales;

    @Column(name = "nombre_places_prises")
    private BigDecimal nombrePlacesPrises;

    @Column(name = "nombre_places_restantes")
    private BigDecimal nombrePlacesRestantes;

    @Column(name = "montant_prevision_ticket")
    private BigDecimal montantPrevisionTicket;

    @Column(name = "montant_prevision_diffusion")
    private BigDecimal montantPrevisionDiffusion;

    @Column(name = "ca_previsionnel")
    private BigDecimal caPrevisionnel;

    // Alias getter for compatibility
    public BigDecimal getMontantChiffreAffairePrevision() {
        return caPrevisionnel;
    }
}