package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT DISTINCT ON (vms.id_vehicule)
        vms.id_vehicule,
        vs.libelle AS libelle_statut,
        vms.date_mouvement
    FROM Vehicule_Mouvement_Statut vms
             INNER JOIN Vehicule_Statut vs ON vms.id_nouveau_statut = vs.id
    ORDER BY vms.id_vehicule, vms.date_mouvement DESC
""")
@Synchronize({"Vehicule_Mouvement_Statut", "Vehicule_Statut"})
public class VmVehiculeStatutActuel implements Serializable {
    @Id
    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;
}