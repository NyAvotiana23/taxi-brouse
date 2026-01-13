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
                cs_nouveau.libelle AS nouveau_statut,
                cms.date_mouvement,
                cms.observation
            FROM Chauffeur_Mouvement_Statut cms
                     INNER JOIN Chauffeur c ON cms.id_chauffeur = c.id
                     INNER JOIN Chauffeur_Statut cs_nouveau ON cms.id_nouveau_statut = cs_nouveau.id
            ORDER BY cms.date_mouvement DESC
        """)
@Synchronize({ "Chauffeur_Mouvement_Statut", "Chauffeur", "Chauffeur_Statut" })
public class VmChauffeurHistoriqueStatut implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "chauffeur")
    private String chauffeur;

    @Column(name = "nouveau_statut")
    private String nouveauStatut;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "observation")
    private String observation;
}