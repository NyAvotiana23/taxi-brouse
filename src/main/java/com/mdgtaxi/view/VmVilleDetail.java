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
        v.id AS id_ville,
        v.nom AS nom_ville,
        r.nom AS nom_region,
        p.nom AS nom_province,
        r.id AS id_region,
        p.id AS id_province
    FROM Ville v
             INNER JOIN Region r ON v.id_region = r.id
             INNER JOIN Province p ON r.id_province = p.id
""")
@Synchronize({"Ville", "Region", "Province"})
public class VmVilleDetail implements Serializable {
    @Id
    @Column(name = "id_ville")
    private Long idVille;

    @Column(name = "nom_ville")
    private String nomVille;

    @Column(name = "nom_region")
    private String nomRegion;

    @Column(name = "nom_province")
    private String nomProvince;

    @Column(name = "id_region")
    private Long idRegion;

    @Column(name = "id_province")
    private Long idProvince;
}