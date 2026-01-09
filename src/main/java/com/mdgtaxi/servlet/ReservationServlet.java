package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.*;
import com.mdgtaxi.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/reservations")
public class ReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final TrajetService trajetService = new TrajetService();
    private final ClientService clientService = new ClientService();
    private final TypeObjectService typeObjectService = new TypeObjectService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            Long id = Long.valueOf(req.getParameter("id"));
            TrajetReservation reservation = reservationService.getReservationById(id);
            req.setAttribute("reservation", reservation);
        }

        List<TrajetReservation> reservations = reservationService.getAllReservations();
        List<Trajet> trajets = trajetService.getAllTrajets();
        List<Client> clients = clientService.getAllClients();
        List<TypeObjectDTO> reservationStatuts = typeObjectService.findAllTypeObject("Reservation_Status");

        req.setAttribute("reservations", reservations);
        req.setAttribute("trajets", trajets);
        req.setAttribute("clients", clients);
        req.setAttribute("reservationStatuts", reservationStatuts);

        req.getRequestDispatcher("/reservations.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Long idTrajet = Long.valueOf(req.getParameter("idTrajet"));
        Long idClient = Long.valueOf(req.getParameter("idClient"));
        Long idReservationStatut = Long.valueOf(req.getParameter("idReservationStatut"));
        String nomPassager = req.getParameter("nomPassager");
        String numeroSiege = req.getParameter("numeroSiege");
        Integer nombrePlaceReservation = Integer.valueOf(req.getParameter("nombrePlaceReservation"));
        BigDecimal montant = new BigDecimal(req.getParameter("montant"));

        TrajetReservation reservation = new TrajetReservation();
        if (idStr != null && !idStr.isEmpty()) {
            reservation = reservationService.getReservationById(Long.valueOf(idStr));
        }

        Trajet trajet = new Trajet();
        trajet.setId(idTrajet);
        reservation.setTrajet(trajet);

        Client client = new Client();
        client.setId(idClient);
        reservation.setClient(client);

        ReservationStatus statut = new ReservationStatus();
        statut.setId(idReservationStatut);
        reservation.setReservationStatus(statut);

        reservation.setNomPassager(nomPassager);
        reservation.setNumeroSiege(numeroSiege);
        reservation.setNombrePlaceReservation(nombrePlaceReservation);
        reservation.setMontant(montant);
        reservation.setDateReservation(LocalDateTime.now()); // Or parse from input if needed

        if (reservation.getId() == null) {
            reservationService.createReservation(reservation);
        } else {
            reservationService.updateReservation(reservation);
        }

        resp.sendRedirect(req.getContextPath() + "/reservations");
    }
}
