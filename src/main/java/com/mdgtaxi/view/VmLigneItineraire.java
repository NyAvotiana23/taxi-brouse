package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        ld.id_ligne,
        ld.ordre,
        la.nom_arret,
        v.nom AS nom_ville,
        r.nom AS nom_region
    FROM Ligne_Detail ld
             INNER JOIN Ligne_Arret la ON ld.id_ligne_arret = la.id
             INNER JOIN Ville v ON la.id_ville = v.id
             INNER JOIN Region r ON v.id_region = r.id
    ORDER BY ld.id_ligne, ld.ordre
""")
@Synchronize({"Ligne_Detail", "Ligne_Arret", "Ville", "Region"})
public class VmLigneItineraire implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "id_ligne")
    private Long idLigne;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "nom_arret")
    private String nomArret;

    @Column(name = "nom_ville")
    private String nomVille;

    @Column(name = "nom_region")
    private String nomRegion;
}