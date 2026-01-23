package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Trajet_Reservation_Details")
public class TrajetReservationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_trajet_reservation", nullable = false)
    private TrajetReservation trajetReservation;

    @ManyToOne
    @JoinColumn(name = "id_categorie_personne", nullable = false)
    private CategoriePersonne categoriePersonne;


    @ManyToOne
    @JoinColumn(name = "id_type_place", nullable = false)
    private TypePlace typePlace;


    @Column(name = "nombre_places", nullable = false)
    private double nombrePlaces;

    @Column(name = "tarif_unitaire")
    private double tarifUnitaire = 0;


}
