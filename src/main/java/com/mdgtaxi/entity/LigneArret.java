package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Ligne_Arret")
public class LigneArret implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ville", nullable = false)
    private Ville ville;

    @Column(name = "nom_arret", nullable = false)
    private String nomArret;
}