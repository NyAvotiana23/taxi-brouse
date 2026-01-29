package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Produit_Extra_Vente_Payement")
public class ProduitExtraVentePayement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_produit_extra_vente", nullable = false)
    private ProduitExtraVente produitExtraVente;

    @Column(name = "date_payement")
    private LocalDateTime datePayement;

    @Column(name = "montant")
    private BigDecimal montant;
}
