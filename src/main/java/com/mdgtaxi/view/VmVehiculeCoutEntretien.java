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
        v.id AS id_vehicule,
        COALESCE(SUM(ve.montant_depense), 0) AS total_depense_entretien,
        COUNT(ve.id) AS nombre_entretiens,
        MAX(ve.date_debut_entretien) AS dernier_entretien
    FROM Vehicule v
             LEFT JOIN Vehicule_Entretien ve ON v.id = ve.id_vehicule
    GROUP BY v.id
""")
@Synchronize({"Vehicule", "Vehicule_Entretien"})
public class VmVehiculeCoutEntretien implements Serializable {
    @Id
    @Column(name = "id_vehicule")
    private Long idVehicule;

    @Column(name = "total_depense_entretien")
    private BigDecimal totalDepenseEntretien;

    @Column(name = "nombre_entretiens")
    private Long nombreEntretiens;

    @Column(name = "dernier_entretien")
    private LocalDateTime dernierEntretien;
}