// Update to servlets.txt (ReservationDetailServlet class, using correct entity names)
package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.MouvementStatusDto;
import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.dto.TypeObjectDTO;
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

@WebServlet("/reservations/detail")
public class ReservationDetailServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final ClientService clientService = new ClientService(); // Assume exists
    private final TrajetService trajetService = new TrajetService(); // Assume exists
    private final StatusService statusService = new StatusService();
    private final TypeObjectService typeObjectService = new TypeObjectService(); // Assume exists for ModePaiement, but adjust if needed

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/reservations");
            return;
        }

        Long id = Long.parseLong(idParam);
        loadReservationDetails(req, id);

        req.getRequestDispatcher("/reservation-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/reservations");
            return;
        }

        Long id = Long.parseLong(idParam);

        try {
            if ("changeStatut".equals(action)) {
                handleChangeStatut(req, id);
            } else if ("addPayment".equals(action)) {
                handleAddPayment(req, id);
            }

            resp.sendRedirect(req.getContextPath() + "/reservations/detail?id=" + id);
        } catch (Exception e) {
            req.setAttribute("error", "Erreur: " + e.getMessage());
            loadReservationDetails(req, id);
            req.getRequestDispatcher("/reservation-detail-content.jsp").forward(req, resp);
        }
    }

    private void loadReservationDetails(HttpServletRequest req, Long id) {
        TrajetReservation reservation = reservationService.getReservationById(id);
        req.setAttribute("reservation", reservation);

        List<TrajetReservationPaiement> payments = reservationService.getPaymentsByReservation(id);
        req.setAttribute("payments", payments);

        List<TrajetReservationMouvementStatut> statusHistory = reservationService.getStatusHistoryByReservation(id);
        req.setAttribute("statusHistory", statusHistory);

        List<Client> clients = clientService.getAllClients(); // For form if needed
        req.setAttribute("clients", clients);

        List<Trajet> trajets = trajetService.getAllTrajets(); // For form if needed
        req.setAttribute("trajets", trajets);

        List<StatusObjectDto> reservationStatuts = statusService.findAllStatuses("Trajet_Reservation");
        req.setAttribute("reservationStatuts", reservationStatuts);

        // Assuming ModePaiement is a TypeObject, or adjust service accordingly
        List<TypeObjectDTO> modePaiements = typeObjectService.findAllTypeObject("Mode_Paiement");
        req.setAttribute("modePaiements", modePaiements);
    }

    private void handleChangeStatut(HttpServletRequest req, Long reservationId) {
        Long idStatut = Long.valueOf(req.getParameter("idStatut"));
        String observation = req.getParameter("observation");
        LocalDateTime dateChangement = LocalDateTime.now(); // Or parse from param

        TrajetReservationMouvementStatut mouvement = new TrajetReservationMouvementStatut();
        TrajetReservation reservation = new TrajetReservation();
        reservation.setId(reservationId);
        mouvement.setTrajetReservation(reservation);
        ReservationStatut statut = new ReservationStatut();
        statut.setId(idStatut);
        mouvement.setNouveauStatut(statut);
        mouvement.setDateMouvement(dateChangement);
        mouvement.setObservation(observation);

        reservationService.changeStatus(mouvement);
    }

    private void handleAddPayment(HttpServletRequest req, Long reservationId) {
        BigDecimal montant = new BigDecimal(req.getParameter("montant"));
        Long idModePaiement = Long.valueOf(req.getParameter("idModePaiement"));
        Long idClient = Long.valueOf(req.getParameter("idClient")); // Assuming from form, or use reservation's client
        Long idCaisse = req.getParameter("idCaisse") != null ? Long.valueOf(req.getParameter("idCaisse")) : null;
        LocalDateTime datePaiement = LocalDateTime.now(); // Or parse

        TrajetReservationPaiement paiement = new TrajetReservationPaiement();
        Client client = new Client();
        client.setId(idClient);
        paiement.setClient(client);
        TrajetReservation reservation = new TrajetReservation();
        reservation.setId(reservationId);
        paiement.setTrajetReservation(reservation);
        if (idCaisse != null) {
            Caisse caisse = new Caisse();
            caisse.setId(idCaisse);
            paiement.setCaisse(caisse);
        }
        paiement.setMontant(montant);
        ModePaiement mode = new ModePaiement();
        mode.setId(idModePaiement);
        paiement.setModePaiement(mode);
        paiement.setDatePaiement(datePaiement);

        reservationService.createPayment(paiement);
    }
}