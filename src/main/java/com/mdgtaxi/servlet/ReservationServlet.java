package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.*;
import com.mdgtaxi.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@WebServlet("/reservations")
public class ReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final TrajetService trajetService = new TrajetService();
    private final ClientService clientService = new ClientService();
    private final TypeObjectService typeObjectService = new TypeObjectService();
    private final StatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            Long id = Long.valueOf(req.getParameter("id"));
            TrajetReservation reservation = reservationService.getReservationById(id);
            req.setAttribute("reservation", reservation);

            // Load reservation details
            List<TrajetReservationDetails> details = reservationService.getReservationDetailsByReservationId(id);
            req.setAttribute("reservationDetails", details);
        }

        List<TrajetReservation> reservations = reservationService.getAllReservations();
        List<Trajet> trajets = trajetService.getAllTrajets();
        List<Client> clients = clientService.getAllClients();
        List<TypeObjectDTO> typePlaces = typeObjectService.findAllTypeObject("Type_Place");

        Map<String, Long> statsByStatus = reservationService.getReservationStatsByStatus();
        Double totalPlacesPrises = reservationService.getTotalPlacesPrises();
        Double totalPlacesRestantes = reservationService.getTotalPlacesRestantes();

        req.setAttribute("reservations", reservations);
        req.setAttribute("trajets", trajets);
        req.setAttribute("clients", clients);
        req.setAttribute("typePlaces", typePlaces);
        req.setAttribute("statsByStatus", statsByStatus);
        req.setAttribute("totalPlacesPrises", totalPlacesPrises);
        req.setAttribute("totalPlacesRestantes", totalPlacesRestantes);

        req.getRequestDispatcher("/reservations.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idStr = req.getParameter("id");
            Long idTrajet = Long.valueOf(req.getParameter("idTrajet"));
            Long idClient = Long.valueOf(req.getParameter("idClient"));
            String nomPassager = req.getParameter("nomPassager");
            Long idReservationStatut = Long.valueOf(req.getParameter("idReservationStatut"));

            // Create or update reservation
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

            reservation.setDateReservation(LocalDateTime.now());
            reservation.setNomPassager(nomPassager);

            ReservationStatut statut = new ReservationStatut();
            statut.setId(idReservationStatut);
            reservation.setReservationStatut(statut);

            // Save reservation
            if (reservation.getId() == null) {
                reservation = reservationService.createReservation(reservation);
            } else {
                reservation = reservationService.updateReservation(reservation);
            }

            // Process reservation details (types de places)
            String[] typePlaceIds = req.getParameterValues("typePlaceId[]");
            String[] nombrePlaces = req.getParameterValues("nombrePlaces[]");

            if (typePlaceIds != null && nombrePlaces != null) {
                for (int i = 0; i < typePlaceIds.length; i++) {
                    if (typePlaceIds[i] != null && !typePlaceIds[i].isEmpty()
                            && nombrePlaces[i] != null && !nombrePlaces[i].isEmpty()) {

                        double nbPlaces = Double.parseDouble(nombrePlaces[i]);
                        if (nbPlaces > 0) {
                            TrajetReservationDetails detail = new TrajetReservationDetails();
                            detail.setTrajetReservation(reservation);

                            TypePlace typePlace = new TypePlace();
                            typePlace.setId(Long.valueOf(typePlaceIds[i]));
                            detail.setTypePlace(typePlace);

                            detail.setNombrePlaces(nbPlaces);

                            reservationService.createReservationDetail(detail);
                        }
                    }
                }
            }

            resp.sendRedirect(req.getContextPath() + "/reservations");
        } catch (Exception e) {
            req.setAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            doGet(req, resp);
        }
    }
}