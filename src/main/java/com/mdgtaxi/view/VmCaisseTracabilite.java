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
        cm.id AS id_mouvement,
        cm.id_caisse,
        c.nom AS caisse,
        cm.table_origine,
        cm.id_entite_origine,
        tm.libelle AS type_mouvement,
        cm.montant,
        cm.date_mouvement,
        CASE cm.table_origine
            WHEN 'Trajet_Reservation_Paiement' THEN
                (SELECT CONCAT('Paiement réservation #', trp.id, ' - ', tr.nom_passager)
                 FROM Trajet_Reservation_Paiement trp
                          INNER JOIN Trajet_Reservation tr ON trp.id_trajet_reservation = tr.id
                 WHERE trp.id = cm.id_entite_origine)
            WHEN 'Trajet_Arret_Detail' THEN
                (SELECT CONCAT('Incident trajet #', tad.id_trajet, ' - ', tma.libelle)
                 FROM Trajet_Arret_Detail tad
                          INNER JOIN Trajet_Motif_Arret tma ON tad.id_trajet_motif_arret = tma.id
                 WHERE tad.id = cm.id_entite_origine)
            WHEN 'Vehicule_Entretien' THEN
                (SELECT CONCAT('Entretien véhicule #', ve.id_vehicule, ' - ', ve.motif)
                 FROM Vehicule_Entretien ve
                 WHERE ve.id = cm.id_entite_origine)
            WHEN 'Trajet_Carburant_Detail' THEN
                (SELECT CONCAT('Carburant trajet #', tcd.id_trajet, ' - ', tcd.quantite_carburant_ajoute, 'L')
                 FROM Trajet_Carburant_Detail tcd
                 WHERE tcd.id = cm.id_entite_origine)
            ELSE 'Autre opération'
            END AS description_origine
    FROM Caisse_Mouvement cm
             INNER JOIN Caisse c ON cm.id_caisse = c.id
             INNER JOIN Type_Mouvement tm ON cm.id_type_mouvement = tm.id
    ORDER BY cm.date_mouvement DESC
""")
@Synchronize({"Caisse_Mouvement", "Caisse", "Type_Mouvement", "Trajet_Reservation_Paiement", "Trajet_Reservation", "Trajet_Arret_Detail", "Trajet_Motif_Arret", "Vehicule_Entretien", "Trajet_Carburant_Detail"})
public class VmCaisseTracabilite implements Serializable {
    @Id
    @Column(name = "id_mouvement")
    private Long idMouvement;

    @Column(name = "id_caisse")
    private Long idCaisse;

    @Column(name = "caisse")
    private String caisse;

    @Column(name = "table_origine")
    private String tableOrigine;

    @Column(name = "id_entite_origine")
    private Long idEntiteOrigine;

    @Column(name = "type_mouvement")
    private String typeMouvement;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "description_origine")
    private String descriptionOrigine;
}