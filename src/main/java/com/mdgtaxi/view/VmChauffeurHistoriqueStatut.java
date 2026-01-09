package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "VM_Chauffeur_Historique_Statut")
public class VmChauffeurHistoriqueStatut implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "chauffeur")
    private String chauffeur;

    @Column(name = "ancien_statut")
    private String ancienStatut;

    @Column(name = "nouveau_statut")
    private String nouveauStatut;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "observation")
    private String observation;
}