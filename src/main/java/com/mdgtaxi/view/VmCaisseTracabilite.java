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
@Table(name = "VM_Caisse_Tracabilite")
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