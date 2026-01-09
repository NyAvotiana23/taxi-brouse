package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "VM_Vehicule_Cout_Entretien")
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