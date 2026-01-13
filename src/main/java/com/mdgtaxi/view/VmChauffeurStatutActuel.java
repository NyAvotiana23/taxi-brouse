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
    SELECT DISTINCT ON (cms.id_chauffeur)
        cms.id_chauffeur,
        cs.libelle AS libelle_statut,
        cms.date_mouvement
    FROM Chauffeur_Mouvement_Statut cms
             INNER JOIN Chauffeur_Statut cs ON cms.id_nouveau_statut = cs.id
    ORDER BY cms.id_chauffeur, cms.date_mouvement DESC
""")
@Synchronize({"Chauffeur_Mouvement_Statut", "Chauffeur_Statut"})
public class VmChauffeurStatutActuel implements Serializable {
    @Id
    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;
}