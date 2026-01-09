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
@Table(name = "VM_Caisse_Mouvement_Historique")
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