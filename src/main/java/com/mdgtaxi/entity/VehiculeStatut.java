package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Vehicule_Statut")
public class VehiculeStatut implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle", nullable = false, unique = true)
    private String libelle;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "span_html", nullable= false)
    private String spanHtml;


}