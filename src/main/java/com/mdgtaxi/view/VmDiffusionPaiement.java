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
        d.id AS id,
        s.id AS id_societe,
        s.nom AS nom_societe,
        d.id AS id_diffusion,
        COALESCE(SUM(CAST(dd.montant_unitaire AS DECIMAL(15,2)) * CAST(dd.nombre_repetition AS INTEGER)), 0) AS montant_a_payer,
        COALESCE(SUM(CAST(p.montant_paye AS DECIMAL(15,2))), 0) AS montant_paye,
        (COALESCE(SUM(CAST(dd.montant_unitaire AS DECIMAL(15,2)) * CAST(dd.nombre_repetition AS INTEGER)), 0) - 
         COALESCE(SUM(CAST(p.montant_paye AS DECIMAL(15,2))), 0)) AS montant_reste,
        d.date_creation
    FROM Diffusion d
    INNER JOIN Societe s ON d.id_societe = s.id
    LEFT JOIN Diffusion_Detail dd ON dd.id_diffusion = d.id
    LEFT JOIN Diffusion_Paiement p ON p.id_diffusion = d.id
    GROUP BY d.id, s.id, s.nom, d.date_creation
""")
@Synchronize({"Diffusion", "Diffusion_Detail", "Societe", "Diffusion_Paiement"})
public class VmDiffusionPaiement implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_societe")
    private Long idSociete;

    @Column(name = "nom_societe")
    private String nomSociete;

    @Column(name = "id_diffusion")
    private Long idDiffusion;

    @Column(name = "montant_a_payer")
    private BigDecimal montantAPayer;

    @Column(name = "montant_paye")
    private BigDecimal montantPaye;

    @Column(name = "montant_reste")
    private BigDecimal montantReste;

    @Column(name = "date_creation")
    private java.time.LocalDateTime dateCreation;
}