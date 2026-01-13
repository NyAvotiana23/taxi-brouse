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
        t.id AS id_trajet,
        COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Recette')
                              THEN pf.montant ELSE 0 END), 0) AS recette_prevue,
        COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Dépense')
                              THEN pf.montant ELSE 0 END), 0) AS depense_prevue,
        COALESCE(vtf.total_recette, 0) AS recette_reelle,
        COALESCE(vtf.total_depense, 0) AS depense_reelle,
        COALESCE(vtf.total_recette, 0) - COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Recette')
                                                               THEN pf.montant ELSE 0 END), 0) AS ecart_recette,
        COALESCE(vtf.total_depense, 0) - COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Dépense')
                                                               THEN pf.montant ELSE 0 END), 0) AS ecart_depense,
        COALESCE(vtf.benefice, 0) - (
            COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Recette')
                                  THEN pf.montant ELSE 0 END), 0) -
            COALESCE(SUM(CASE WHEN pf.id_type_mouvement = (SELECT id FROM Type_Mouvement WHERE libelle = 'Dépense')
                                  THEN pf.montant ELSE 0 END), 0)
            ) AS ecart_benefice
    FROM Trajet t
             LEFT JOIN Prevision_Finance pf ON t.id = pf.id_trajet
             LEFT JOIN VM_Trajet_Finance vtf ON t.id = vtf.id_trajet
    GROUP BY t.id, vtf.total_recette, vtf.total_depense, vtf.benefice
""")
@Synchronize({"Trajet", "Prevision_Finance", "VM_Trajet_Finance", "Type_Mouvement"})
public class VmPrevisionVsReel implements Serializable {
    @Id
    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "recette_prevue")
    private BigDecimal recettePrevue;

    @Column(name = "depense_prevue")
    private BigDecimal depensePrevue;

    @Column(name = "recette_reelle")
    private BigDecimal recetteReelle;

    @Column(name = "depense_reelle")
    private BigDecimal depenseReelle;

    @Column(name = "ecart_recette")
    private BigDecimal ecartRecette;

    @Column(name = "ecart_depense")
    private BigDecimal ecartDepense;

    @Column(name = "ecart_benefice")
    private BigDecimal ecartBenefice;
}