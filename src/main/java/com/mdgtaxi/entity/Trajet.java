package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Trajet")
public class Trajet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ligne", nullable = false)
    private Ligne ligne;

    @ManyToOne
    @JoinColumn(name = "id_chauffeur", nullable = false)
    private Chauffeur chauffeur;

    @ManyToOne
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @Column(name = "nombre_passager", nullable = false)
    private Integer nombrePassager;

    @ManyToOne
    @JoinColumn(name = "id_trajet_statut", nullable = false)
    private TrajetStatut trajetStatut;

    @Column(name = "datetime_depart", nullable = false)
    private LocalDateTime datetimeDepart;

    @Column(name = "datetime_arrivee")
    private LocalDateTime datetimeArrivee;


}