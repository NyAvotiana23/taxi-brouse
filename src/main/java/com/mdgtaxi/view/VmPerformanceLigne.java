package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Immutable
@Table(name = "VM_Performance_Ligne")
public class VmPerformanceLigne implements Serializable {
    @Id
    @Column(name = "id_ligne")
    private Long idLigne;

    @Column(name = "ville_depart")
    private String villeDepart;

    @Column(name = "ville_arrivee")
    private String villeArrivee;

    @Column(name = "nombre_trajets")
    private Long nombreTrajets;

    @Column(name = "taux_remplissage_moyen")
    private BigDecimal tauxRemplissageMoyen;

    @Column(name = "recette_totale")
    private BigDecimal recetteTotale;

    @Column(name = "benefice_total")
    private BigDecimal beneficeTotal;

    @Column(name = "benefice_moyen_par_trajet")
    private BigDecimal beneficeMoyenParTrajet;
}