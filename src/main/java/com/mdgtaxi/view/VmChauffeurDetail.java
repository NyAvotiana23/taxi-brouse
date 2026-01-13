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
        c.nom || ' ' || c.prenom AS nom_complet,
        c.date_naissance,
        EXTRACT(YEAR FROM AGE(c.date_naissance)) AS age,
        c.numero_permis,
        csa.libelle_statut,
        csa.date_mouvement AS date_dernier_statut
    FROM Chauffeur c
             LEFT JOIN VM_Chauffeur_Statut_Actuel csa ON c.id = csa.id_chauffeur
""")
@Synchronize({"Chauffeur", "VM_Chauffeur_Statut_Actuel"})
public class VmChauffeurDetail implements Serializable {
    @Id
    @Column(name = "id_chauffeur")
    private Long idChauffeur;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom_complet")
    private String nomComplet;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "age")
    private Integer age;

    @Column(name = "numero_permis")
    private String numeroPermis;

    @Column(name = "libelle_statut")
    private String libelleStatut;

    @Column(name = "date_dernier_statut")
    private LocalDateTime dateDernierStatut;
}