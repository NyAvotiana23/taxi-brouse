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
    SELECT DISTINCT ON (tms.id_trajet)
        tms.id_trajet,
        ts.libelle AS libelle_statut,
        tms.date_mouvement
    FROM Trajet_Mouvement_Statut tms
             INNER JOIN Trajet_Statut ts ON tms.id_nouveau_statut = ts.id
    ORDER BY tms.id_trajet, tms.date_mouvement DESC
""")
@Synchronize({"Trajet_Mouvement_Statut", "Trajet_Statut"})
public class VmTrajetStatutActuel implements Serializable {
    @Id
    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;
}