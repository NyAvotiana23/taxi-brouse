package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Caisse_Mouvement")
public class CaisseMouvement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_caisse", nullable = false)
    private Caisse caisse;

    @Column(name = "id_entite_origine")
    private Long idEntiteOrigine;

    @Column(name = "table_origine")
    private String tableOrigine;

    @ManyToOne
    @JoinColumn(name = "id_type_mouvement", nullable = false)
    private TypeMouvement typeMouvement;

    @Column(name = "montant", nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(name = "motif")
    private String motif;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;
}