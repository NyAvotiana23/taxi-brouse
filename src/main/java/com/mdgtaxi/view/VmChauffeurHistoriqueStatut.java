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
        cms.id,
        cms.id_chauffeur,
        c.nom || ' ' || c.prenom AS chauffeur,
        cs_ancien.libelle AS ancien_statut,
        cs_nouveau.libelle AS nouveau_statut,
        cms.date_mouvement,
        cms.observation
    FROM Chauffeur_Mouvement_Statut cms
             INNER JOIN Chauffeur c ON cms.id_chauffeur = c.id
             LEFT JOIN Chauffeur_Statut cs_ancien ON cms.id_ancien_statut = cs_ancien.id
             INNER JOIN Chauffeur_Statut cs_nouveau ON cms.id_nouveau_statut = cs_nouveau.id
    ORDER BY cms.date_mouvement DESC
""")
@Synchronize({"Chauffeur_Mouvement_Statut", "Chauffeur", "Chauffeur_Statut"})
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