package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Devise_Mouvement_Taux")
public class DeviseMouvementTaux implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_devise", nullable = false)
    private Devise devise;

    @Column(name = "ancien_taux", nullable = false, precision = 15, scale = 4)
    private BigDecimal ancienTaux;

    @Column(name = "nouveau_taux", nullable = false, precision = 15, scale = 4)
    private BigDecimal nouveauTaux;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;
}