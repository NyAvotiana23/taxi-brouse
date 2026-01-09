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
@Table(name = "VM_Caisse_Solde_Actuel")
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