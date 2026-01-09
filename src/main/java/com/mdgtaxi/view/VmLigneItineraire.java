package com.mdgtaxi.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Immutable
@Table(name = "VM_Ligne_Itineraire")
public class VmLigneItineraire implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Since no unique ID, use generated
    private Long id; // Artificial ID if needed

    @Column(name = "id_ligne")
    private Long idLigne;

    @Column(name = "ordre")
    private Integer ordre;

    @Column(name = "nom_arret")
    private String nomArret;

    @Column(name = "nom_ville")
    private String nomVille;

    @Column(name = "nom_region")
    private String nomRegion;
}