package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "VM_Chauffeur_Statut_Actuel")
public class VmChauffeurStatutActuel implements Serializable {
    @Id
    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;
}