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
    SELECT
        c.id AS id_chauffeur,
        COUNT(t.id) AS nombre_trajets,
        COUNT(CASE WHEN t.id_trajet_statut IN (
            SELECT id FROM Trajet_Statut WHERE libelle = 'Terminé'
        ) THEN 1 END) AS trajets_termines,
        MAX(t.datetime_depart) AS dernier_trajet,
        MIN(t.datetime_depart) AS premier_trajet
    FROM Chauffeur c
             LEFT JOIN Trajet t ON c.id = t.id_chauffeur
    GROUP BY c.id
""")
@Synchronize({"Chauffeur", "Trajet", "Trajet_Statut"})
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