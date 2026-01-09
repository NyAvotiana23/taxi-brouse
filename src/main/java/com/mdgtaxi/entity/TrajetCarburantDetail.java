package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Trajet_Carburant_Detail")
public class TrajetCarburantDetail implements Serializable {
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
    @JoinColumn(name = "id_carburant_type", nullable = false)
    private CarburantType carburantType;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;

    @Column(name = "quantite_carburant_ajoute", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantiteCarburantAjoute;

    @Column(name = "taux_carburant", nullable = false, precision = 15, scale = 2)
    private BigDecimal tauxCarburant;
}