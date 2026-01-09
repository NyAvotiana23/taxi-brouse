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
@Table(name = "VM_Prevision_Detail")
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