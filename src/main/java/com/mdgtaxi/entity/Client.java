package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Client")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_type_client", nullable = false)
    private TypeClient typeClient;

    @Column(name = "nom_client", nullable = false)
    private String nomClient;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;
}