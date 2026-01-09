package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Vehicule_Entretien")
public class VehiculeEntretien implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @Column(name = "motif", nullable = false)
    private String motif;

    @Column(name = "date_debut_entretien", nullable = false)
    private LocalDateTime dateDebutEntretien;

    @Column(name = "date_fin_entretien")
    private LocalDateTime dateFinEntretien;

    @Column(name = "montant_depense", nullable = false, precision = 15, scale = 2)
    private BigDecimal montantDepense;
}