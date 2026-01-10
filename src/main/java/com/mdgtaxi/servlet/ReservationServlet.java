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
        List<TypeObjectDTO> reservationStatuts = typeObjectService.findAllTypeObject("Reservation_Status");

        // Calculer les statistiques
        Map<String, Long> statsByStatus = reservationService.getReservationStatsByStatus();
        int totalPlacesPrises = reservationService.getTotalPlacesPrises();
        int totalPlacesRestantes = reservationService.getTotalPlacesRestantes();

        req.setAttribute("reservations", reservations);
        req.setAttribute("trajets", trajets);
        req.setAttribute("clients", clients);
        req.setAttribute("reservationStatuts", reservationStatuts);
        req.setAttribute("statsByStatus", statsByStatus);
        req.setAttribute("totalPlacesPrises", totalPlacesPrises);
        req.setAttribute("totalPlacesRestantes", totalPlacesRestantes);

        req.getRequestDispatcher("/reservations.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Long idTrajet = Long.valueOf(req.getParameter("idTrajet"));
        Long idClient = Long.valueOf(req.getParameter("idClient"));

        // Récupérer dynamiquement le statut "En attente" au lieu d'utiliser un ID
        // hardcodé
        TypeObjectDTO statutEnAttente = typeObjectService.findTypeObjectByLibelle("Reservation_Status", "En attente");
        Long idReservationStatut = (statutEnAttente != null) ? statutEnAttente.getId() : 3L;

        String nomPassager = req.getParameter("nomPassager");
        String numeroSiege = req.getParameter("numeroSiege");
        Integer nombrePlaceReservation = Integer.valueOf(req.getParameter("nombrePlaceReservation"));
        String returnToTrajet = req.getParameter("returnToTrajet");

        // Validation : vérifier que les places demandées ne dépassent pas les places
        // disponibles
        int placesRestantes = reservationService.getPlacesRestantesForTrajet(idTrajet);

        // Si on modifie une réservation existante, il faut ajouter ses places actuelles
        // aux places restantes
        if (idStr != null && !idStr.isEmpty()) {
            TrajetReservation existingReservation = reservationService.getReservationById(Long.valueOf(idStr));
            if (existingReservation != null) {
                placesRestantes += existingReservation.getNombrePlaceReservation();
            }
        }

        if (nombrePlaceReservation > placesRestantes) {
            // Rediriger avec un message d'erreur
            req.setAttribute("error", "Nombre de places insuffisant. Places restantes : " + placesRestantes);

            // Recharger les données pour afficher le formulaire
            List<TrajetReservation> reservations = reservationService.getAllReservations();
            List<Trajet> trajets = trajetService.getAllTrajets();
            List<Client> clients = clientService.getAllClients();
            List<TypeObjectDTO> reservationStatuts = typeObjectService.findAllTypeObject("Reservation_Status");
            Map<String, Long> statsByStatus = reservationService.getReservationStatsByStatus();
            int totalPlacesPrises = reservationService.getTotalPlacesPrises();
            int totalPlacesRestantes = reservationService.getTotalPlacesRestantes();

            req.setAttribute("reservations", reservations);
            req.setAttribute("trajets", trajets);
            req.setAttribute("clients", clients);
            req.setAttribute("reservationStatuts", reservationStatuts);
            req.setAttribute("statsByStatus", statsByStatus);
            req.setAttribute("totalPlacesPrises", totalPlacesPrises);
            req.setAttribute("totalPlacesRestantes", totalPlacesRestantes);

            req.getRequestDispatcher("/reservations.jsp").forward(req, resp);
            return;
        }

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
        reservation.setReservationStatut(statut);

        reservation.setNomPassager(nomPassager);
        reservation.setNumeroSiege(numeroSiege);
        reservation.setNombrePlaceReservation(nombrePlaceReservation);
        reservation.setDateReservation(LocalDateTime.now()); // Or parse from input if needed

        if (reservation.getId() == null) {
            reservationService.createReservation(reservation);
        } else {
            reservationService.updateReservation(reservation);
        }

        // Redirect back to trajet detail if parameter is present
        if (returnToTrajet != null && !returnToTrajet.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/trajets/detail?id=" + returnToTrajet);
        } else {
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }
    }
}
