package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "VM_Vehicule_Statut_Actuel")
public class VmVehiculeStatutActuel implements Serializable {
    @Id
    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;
}