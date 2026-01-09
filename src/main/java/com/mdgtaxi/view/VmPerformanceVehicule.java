package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Immutable
@Table(name = "VM_Performance_Vehicule")
public class VmPerformanceVehicule implements Serializable {
    @Id
    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "marque")
    private String marque;

    @Column(name = "modele")
    private String modele;

    @Column(name = "nombre_trajets")
    private Long nombreTrajets;

    @Column(name = "cout_entretien_total")
    private BigDecimal coutEntretienTotal;

    @Column(name = "recette_totale")
    private BigDecimal recetteTotale;

    @Column(name = "benefice_total")
    private BigDecimal beneficeTotal;

    @Column(name = "duree_moyenne_trajet_heures")
    private BigDecimal dureeMoyenneTrajetHeures;
}