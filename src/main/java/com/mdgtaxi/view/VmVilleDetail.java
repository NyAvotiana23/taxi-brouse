package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Immutable
@Table(name = "VM_Ville_Detail")
public class VmVilleDetail implements Serializable {
    @Id
    @Column(name = "id_ville")
    private Long idVille;

    @Column(name = "nom_ville")
    private String nomVille;

    @Column(name = "nom_region")
    private String nomRegion;

    @Column(name = "nom_province")
    private String nomProvince;

    @Column(name = "id_region")
    private Long idRegion;

    @Column(name = "id_province")
    private Long idProvince;
}