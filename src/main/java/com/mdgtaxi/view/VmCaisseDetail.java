package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        c.id AS id_caisse,
        ct.libelle AS libelle_type,
        c.nom,
        c.solde_initial
    FROM Caisse c
             INNER JOIN Caisse_Type ct ON c.id_caisse_type = ct.id
""")
@Synchronize({"Caisse", "Caisse_Type"})
public class VmCaisseDetail implements Serializable {
    @Id
    @Column(name = "id_caisse")
    private Long idCaisse;

    @Column(name = "libelle_type")
    private String libelleType;

    @Column(name = "nom")
    private String nom;

    @Column(name = "solde_initial")
    private BigDecimal soldeInitial;
}