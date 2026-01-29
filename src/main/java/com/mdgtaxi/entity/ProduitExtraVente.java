package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Produit_Extra_Vente")
public class ProduitExtraVente implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_Produit", nullable = false)
    private ProduitExtra produitExtra;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "remise")
    private Double remise;

    @Column(name = "prix_unitaire")
    private BigDecimal prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;
}
