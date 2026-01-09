package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
// Note: Fields are partial due to truncation in SQL
@Data
@Entity
@Immutable
@Table(name = "VM_Trajet_Detail")
public class VmTrajetDetail implements Serializable {
    @Id
    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "id_ligne")
    private Long idLigne;

    // Add other fields based on available info; assuming common ones
    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "nombre_passager")
    private Integer nombrePassager;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "datetime_depart")
    private LocalDateTime datetimeDepart;

    @Column(name = "datetime_arrivee")
    private LocalDateTime datetimeArrivee;

    @Column(name = "frais_unitaire")
    private BigDecimal fraisUnitaire;

    @Column(name = "ville_depart")
    private String villeDepart;

    @Column(name = "ville_arrivee")
    private String villeArrivee;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "nom_chauffeur")
    private String nomChauffeur;

}