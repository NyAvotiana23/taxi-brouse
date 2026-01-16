package com.mdgtaxi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "Trajet_Reservation")
public class TrajetReservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "id_reservation_statut", nullable = false)
    private ReservationStatut reservationStatut;


    @Column(name = "nom_passager", nullable = false)
    private String nomPassager;


    @Column(name = "date_reservation", nullable = false)
        private LocalDateTime dateReservation;

    @OneToMany(mappedBy = "trajetReservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrajetReservationDetails> trajetReservationDetails;

}