package com.mdgtaxi.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Diffusion_Paiement_Repartition")
public class DiffusionPaiementRepartition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_diffusion_detail", nullable = false)
    private DiffusionDetail diffusiondDetail;

    @ManyToOne
    @JoinColumn(name = "id_diffusion_paiement", nullable = false)
    private DiffusionPaiement diffusionPaiement;

    @Column(name = "montant_paye", nullable = false)
    private BigDecimal montantPaye;

    @Column(name = "pourcentage", nullable = false)
    private Double pourcentage;
}
