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
        ROW_NUMBER() OVER (ORDER BY annee, mois) AS id,
        mois,
        annee,
        montant_total,
        nombre_total
    FROM (
        SELECT
            CAST(EXTRACT(MONTH FROM t.datetime_depart) AS integer) AS mois,
            CAST(EXTRACT(YEAR FROM t.datetime_depart) AS integer) AS annee,
            SUM(dd.nombre_repetition * dd.montant_unitaire) AS montant_total,
            SUM(dd.nombre_repetition) AS nombre_total
        FROM Diffusion_Detail dd
        INNER JOIN Trajet t ON dd.id_trajet = t.id
        GROUP BY EXTRACT(YEAR FROM t.datetime_depart), EXTRACT(MONTH FROM t.datetime_depart)
        
        
    ) subquery
""")
@Synchronize({"Diffusion_Detail", "Trajet"})
public class VmDiffusionMensuelGlobal implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "mois")
    private Integer mois;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "montant_total")
    private BigDecimal montantTotal;

    @Column(name = "nombre_total")
    private Integer nombreTotal;
}