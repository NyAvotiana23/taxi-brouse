package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "VM_Chauffeur_Activite")
public class VmChauffeurActivite implements Serializable {
    @Id
    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "nombre_trajets")
    private Long nombreTrajets;

    @Column(name = "trajets_termines")
    private Long trajetsTermines;

    @Column(name = "dernier_trajet")
    private LocalDateTime dernierTrajet;

    @Column(name = "premier_trajet")
    private LocalDateTime premierTrajet;
}