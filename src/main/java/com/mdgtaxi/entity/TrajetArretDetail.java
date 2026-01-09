package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Trajet_Arret_Detail")
public class TrajetArretDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "id_caisse")
    private Caisse caisse;

    @ManyToOne
    @JoinColumn(name = "id_ville", nullable = false)
    private Ville ville;

    @ManyToOne
    @JoinColumn(name = "id_trajet_motif_arret", nullable = false)
    private TrajetMotifArret trajetMotifArret;

    @Column(name = "montant_depense", precision = 15, scale = 2)
    private BigDecimal montantDepense;

    @Column(name = "datetime_debut", nullable = false)
    private LocalDateTime datetimeDebut;

    @Column(name = "datetime_fin")
    private LocalDateTime datetimeFin;
}