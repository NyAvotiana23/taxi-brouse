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
        ROW_NUMBER() OVER (ORDER BY s.id, d.id) AS id,
        s.id AS id_societe,
        d.id AS id_diffusion,
        pub.id AS id_publicite,
        t.id AS id_trajet,
        CAST(EXTRACT(MONTH FROM t.datetime_depart) AS integer) AS mois,
        CAST(EXTRACT(YEAR FROM t.datetime_depart) AS integer) AS annee,
        CAST(d.nombre AS INTEGER) AS nombre,
        (CAST(d.montant_unite AS DECIMAL(15,2)) * CAST(d.nombre AS INTEGER)) AS montant_a_paye_total,
        COALESCE(SUM(CAST(p.montant AS DECIMAL(15,2))), 0) AS montant_paye_total,
        ((CAST(d.montant_unite AS DECIMAL(15,2)) * CAST(d.nombre AS INTEGER)) - COALESCE(SUM(CAST(p.montant AS DECIMAL(15,2))), 0)) AS montant_reste_total
    FROM Diffusion d
    INNER JOIN Publicite pub ON d.id_publicite = pub.id
    INNER JOIN Societe s ON pub.id_societe = s.id
    INNER JOIN Trajet t ON d.id_trajet = t.id
    LEFT JOIN Diffusion_Paiement p ON p.id_diffusion = d.id AND p.id_societe = s.id
    GROUP BY s.id, d.id, pub.id, t.id, t.datetime_depart, d.nombre, d.montant_unite
""")
@Synchronize({"Diffusion", "Publicite", "Societe", "Trajet", "Diffusion_Paiement"})
public class VmDiffusionPaiement implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_societe")
    private Long idSociete;

    @Column(name = "id_diffusion")
    private Long idDiffusion;

    @Column(name = "id_publicite")
    private Long idPublicite;

    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "mois")
    private Integer mois;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "nombre")
    private Integer nombre;

    @Column(name = "montant_a_paye_total")
    private BigDecimal montantAPayeTotal;

    @Column(name = "montant_paye_total")
    private BigDecimal montantPayeTotal;

    @Column(name = "montant_reste_total")
    private BigDecimal montantResteTotal;
}