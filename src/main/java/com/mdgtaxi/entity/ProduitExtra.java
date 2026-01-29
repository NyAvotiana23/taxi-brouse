package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Entity
@Table(name = "Produit_Extra")
public class ProduitExtra implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prix_unitaire")
    private BigDecimal prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "id_categorie", nullable = false)
    private ProduitCategorie produitCategorie;

    @ManyToOne
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;
}
