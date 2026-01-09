package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Carburant_Mouvement_Taux")
public class CarburantMouvementTaux implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_carburant_type", nullable = false)
    private CarburantType carburantType;

    @Column(name = "ancien_taux", nullable = false, precision = 15, scale = 2)
    private BigDecimal ancienTaux;

    @Column(name = "nouveau_taux", nullable = false, precision = 15, scale = 2)
    private BigDecimal nouveauTaux;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;
}