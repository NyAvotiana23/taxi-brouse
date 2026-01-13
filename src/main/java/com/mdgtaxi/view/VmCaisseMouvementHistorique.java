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
        cm.id,
        cm.id_caisse,
        c.nom AS nom_caisse,
        cm.date_mouvement,
        tm.libelle AS libelle_type_mouvement,
        cm.montant,
        cm.motif,
        cm.table_origine,
        cm.id_entite_origine
    FROM Caisse_Mouvement cm
             INNER JOIN Caisse c ON cm.id_caisse = c.id
             INNER JOIN Type_Mouvement tm ON cm.id_type_mouvement = tm.id
    ORDER BY cm.date_mouvement DESC
""")
@Synchronize({"Caisse_Mouvement", "Caisse", "Type_Mouvement"})
public class VmCaisseMouvementHistorique implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "id_caisse")
    private Long idCaisse;

    @Column(name = "nom_caisse")
    private String nomCaisse;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "libelle_type_mouvement")
    private String libelleTypeMouvement;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "motif")
    private String motif;

    @Column(name = "table_origine")
    private String tableOrigine;

    @Column(name = "id_entite_origine")
    private Long idEntiteOrigine;
}