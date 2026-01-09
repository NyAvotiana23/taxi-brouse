package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "VM_Vehicule_Detail")
public class VmVehiculeDetail implements Serializable {
    @Id
    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "marque")
    private String marque;

    @Column(name = "modele")
    private String modele;

    @Column(name = "maximum_passager")
    private Integer maximumPassager;

    @Column(name = "capacite_carburant")
    private BigDecimal capaciteCarburant;

    @Column(name = "depense_carburant_100km")
    private BigDecimal depenseCarburant100km;

    @Column(name = "libelle_type")
    private String libelleType;

    @Column(name = "type_carburant")
    private String typeCarburant;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_dernier_statut")
    private LocalDateTime dateDernierStatut;
}