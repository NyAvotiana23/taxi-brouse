package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Diffusion_Detail")
public class DiffusionDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_publicite", nullable = false)
    private Publicite publicite;

    @ManyToOne
    @JoinColumn(name = "id_diffusion", nullable = false)
    private Diffusion diffusion;

    @ManyToOne
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;

    @Column(name = "nombre_repetition")
    private Integer nombreRepetition;

    @Column(name = "montant_unitaire")
    private BigDecimal montantUnitaire;
}
