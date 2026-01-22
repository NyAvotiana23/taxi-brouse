package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Configuration")
public class Configuration implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "valeur")
    private String valeur;

    @ManyToOne
    @JoinColumn(name = "id_unite", nullable = false)
    private Unite unite;

    @Column(name = "code")
    private String code;
}
