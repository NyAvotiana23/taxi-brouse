package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Subselect("""
    SELECT
        pf.id,
        pf.id_trajet,
        pf.table_origine,
        pf.id_entite_origine,
        tm.libelle AS type_mouvement,
        pf.montant,
        pf.date,
        pf.description
    FROM Prevision_Finance pf
             INNER JOIN Type_Mouvement tm ON pf.id_type_mouvement = tm.id
    ORDER BY pf.date DESC
""")
@Synchronize({"Prevision_Finance", "Type_Mouvement"})
public class VmPrevisionDetail implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "table_origine")
    private String tableOrigine;

    @Column(name = "id_entite_origine")
    private Long idEntiteOrigine;

    @Column(name = "type_mouvement")
    private String typeMouvement;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "description")
    private String description;
}