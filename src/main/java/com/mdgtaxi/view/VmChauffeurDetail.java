package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        c.id AS id_chauffeur,
        c.nom,
        c.prenom,
        c.date_naissance,
        c.numero_permis,
        csa.libelle_statut,
        csa.date_mouvement AS date_dernier_statut
    FROM Chauffeur c
             LEFT JOIN (
                 SELECT DISTINCT ON (cms.id_chauffeur)
                     cms.id_chauffeur,
                     cs.libelle AS libelle_statut,
                     cms.date_mouvement
                 FROM Chauffeur_Mouvement_Statut cms
                          INNER JOIN Chauffeur_Statut cs ON cms.id_nouveau_statut = cs.id
                 ORDER BY cms.id_chauffeur, cms.date_mouvement DESC
             ) csa ON c.id = csa.id_chauffeur
""")
@Synchronize({"Chauffeur", "Chauffeur_Mouvement_Statut", "Chauffeur_Statut"})
public class VmChauffeurDetail implements Serializable {
    @Id
    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "numero_permis")
    private String numeroPermis;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_dernier_statut")
    private LocalDateTime dateDernierStatut;
}