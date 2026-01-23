package com.mdgtaxi.entity;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Diffusion_Paiement")
public class DiffusionPaiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_diffusion", nullable = false)
    private Diffusion diffusion;

    @ManyToOne
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @Column(name = "date_paiement", nullable = false)
    private LocalDateTime datePaiement;

    @Column(name = "montant_paye", nullable = false, precision = 15, scale = 2)
    private BigDecimal montantPaye;

}
