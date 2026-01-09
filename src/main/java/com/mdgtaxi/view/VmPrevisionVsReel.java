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
@Table(name = "VM_Prevision_vs_Reel")
public class VmPrevisionVsReel implements Serializable {
    @Id
    @Column(name = "id_trajet")
    private Long idTrajet;

    @Column(name = "recette_prevue")
    private BigDecimal recettePrevue;

    @Column(name = "depense_prevue")
    private BigDecimal depensePrevue;

    @Column(name = "recette_reelle")
    private BigDecimal recetteReelle;

    @Column(name = "depense_reelle")
    private BigDecimal depenseReelle;

    @Column(name = "ecart_recette")
    private BigDecimal ecartRecette;

    @Column(name = "ecart_depense")
    private BigDecimal ecartDepense;

    @Column(name = "ecart_benefice")
    private BigDecimal ecartBenefice;
}