package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Chauffeur_Mouvement_Statut")
public class ChauffeurMouvementStatut implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_chauffeur", nullable = false)
    private Chauffeur chauffeur;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;

    @ManyToOne
    @JoinColumn(name = "id_ancien_statut")
    private ChauffeurStatut ancienStatut;

    @ManyToOne
    @JoinColumn(name = "id_nouveau_statut", nullable = false)
    private ChauffeurStatut nouveauStatut;

    @Column(name = "observation")
    private String observation;
}