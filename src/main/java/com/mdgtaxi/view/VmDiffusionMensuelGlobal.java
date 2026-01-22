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
        ROW_NUMBER() OVER (ORDER BY YEAR(t.datetime_depart), MONTH(t.datetime_depart)) AS id,
        MONTH(t.datetime_depart) AS mois,
        YEAR(t.datetime_depart) AS annee,
        SUM(CAST(d.montant_unite AS DECIMAL(10,2))) AS montant_total,
        SUM(CAST(d.nombre AS INTEGER)) AS nombre_total
    FROM Diffusion d
    INNER JOIN Trajet t ON d.id_trajet = t.id
    GROUP BY YEAR(t.datetime_depart), MONTH(t.datetime_depart)
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