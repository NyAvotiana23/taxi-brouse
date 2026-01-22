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
        ROW_NUMBER() OVER (ORDER BY EXTRACT(YEAR FROM t.datetime_depart), EXTRACT(MONTH FROM t.datetime_depart)) AS id,
        CAST(EXTRACT(MONTH FROM t.datetime_depart) AS integer) AS mois,
        CAST(EXTRACT(YEAR FROM t.datetime_depart) AS integer) AS annee,
        d.montant_unite AS montant_total,
        SUM(CAST(d.nombre AS INTEGER))          AS nombre_total
    FROM Diffusion d
    INNER JOIN Trajet t ON d.id_trajet = t.id
    GROUP BY EXTRACT(YEAR FROM t.datetime_depart), EXTRACT(MONTH FROM t.datetime_depart)
""")
@Synchronize({"Diffusion", "Trajet"})
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