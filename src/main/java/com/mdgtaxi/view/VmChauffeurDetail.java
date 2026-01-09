package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "VM_Chauffeur_Detail")
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