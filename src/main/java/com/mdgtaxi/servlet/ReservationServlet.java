package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.*;
import com.mdgtaxi.service.ClientService;
import com.mdgtaxi.service.ReservationService;
import com.mdgtaxi.service.TrajetService;
import com.mdgtaxi.service.TypeObjectService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        List<TypeObjectDTO> reservationStatuts = typeObjectService.findAllTypeObject("Reservation_Statut");
        Integer totalPlacesPrises = reservationService.getTotalPlacesPrises();
        Integer totalPlacesRestantes = reservationService.getTotalPlacesRestantes();

        req.setAttribute("reservations", reservations);
        req.setAttribute("trajets", trajets);
        req.setAttribute("clients", clients);
        req.setAttribute("reservationStatuts", reservationStatuts);
        req.setAttribute("totalPlacesPrises", totalPlacesPrises);
        req.setAttribute("totalPlacesRestantes", totalPlacesRestantes);

        req.getRequestDispatcher("/reservations.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Long idTrajet = Long.valueOf(req.getParameter("idTrajet"));
        Long idClient = Long.valueOf(req.getParameter("idClient"));
        String nomPassager = req.getParameter("nomPassager");
        String numeroSiege = req.getParameter("numeroSiege");
        Long idReservationStatut = Long.valueOf(req.getParameter("idReservationStatut"));
        Integer nombrePlaceReservation = Integer.valueOf(req.getParameter("nombrePlaceReservation"));

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

        reservation.setNomPassager(nomPassager);
        reservation.setNumeroSiege(numeroSiege);

        ReservationStatut statut = new ReservationStatut();
        statut.setId(idReservationStatut);
        reservation.setReservationStatut(statut);

        reservation.setNombrePlaceReservation(nombrePlaceReservation);

        try {
            if (reservation.getId() == null) {
                reservationService.createReservation(reservation);
            } else {
                reservationService.updateReservation(reservation);
            }
            resp.sendRedirect(req.getContextPath() + "/reservations");
        } catch (Exception e) {
            req.setAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            doGet(req, resp);  // Re-forward to show error
        }
    }
}