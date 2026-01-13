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
        c.id AS id_caisse,
        c.solde_initial + COALESCE(SUM(
                                           CASE
                                               WHEN tm.libelle = 'Recette' THEN cm.montant
                                               WHEN tm.libelle = 'Dépense' THEN -cm.montant
                                               ELSE 0
                                               END
                                   ), 0) AS solde_actuel,
        COUNT(cm.id) AS nombre_mouvements,
        MAX(cm.date_mouvement) AS dernier_mouvement
    FROM Caisse c
             LEFT JOIN Caisse_Mouvement cm ON c.id = cm.id_caisse
             LEFT JOIN Type_Mouvement tm ON cm.id_type_mouvement = tm.id
    GROUP BY c.id, c.solde_initial
""")
@Synchronize({"Caisse", "Caisse_Mouvement", "Type_Mouvement"})
public class VmCaisseSoldeActuel implements Serializable {
    @Id
    @Column(name = "id_caisse")
    private Long idCaisse;

    @Column(name = "solde_actuel")
    private BigDecimal soldeActuel;

    @Column(name = "nombre_mouvements")
    private Long nombreMouvements;

    @Column(name = "dernier_mouvement")
    private LocalDateTime dernierMouvement;
}