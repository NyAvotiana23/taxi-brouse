package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Immutable
@Table(name = "VM_Caisse_Detail")
public class VmCaisseDetail implements Serializable {
    @Id
    @Column(name = "id_caisse")
    private Long idCaisse;

    @Column(name = "libelle_type")
    private String libelleType;

    @Column(name = "nom")
    private String nom;

    @Column(name = "solde_initial")
    private BigDecimal soldeInitial;
}