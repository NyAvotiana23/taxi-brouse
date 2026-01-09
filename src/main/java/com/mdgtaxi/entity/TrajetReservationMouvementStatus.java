package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Trajet_Reservation_Mouvement_Status")
public class TrajetReservationMouvementStatus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_trajet_reservation", nullable = false)
    private TrajetReservation trajetReservation;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;

    @ManyToOne
    @JoinColumn(name = "id_ancien_statut")
    private ReservationStatus ancienStatut;

    @ManyToOne
    @JoinColumn(name = "id_nouveau_statut", nullable = false)
    private ReservationStatus nouveauStatut;

    @Column(name = "observation")
    private String observation;
}