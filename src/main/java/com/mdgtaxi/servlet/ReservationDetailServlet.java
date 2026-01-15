package com.mdgtaxi.servlet;

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
    private final ClientService clientService = new ClientService();
    private final TrajetService trajetService = new TrajetService();
    private final StatusService statusService = new StatusService();
    private final TypeObjectService typeObjectService = new TypeObjectService();

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
            req.getRequestDispatcher("/reservation-detail.jsp").forward(req, resp);
        }
    }

    private void loadReservationDetails(HttpServletRequest req, Long id) {
        TrajetReservation reservation = reservationService.getReservationById(id);
        req.setAttribute("reservation", reservation);

        // Load reservation details (types de places)
        List<TrajetReservationDetails> reservationDetails = reservationService.getReservationDetailsByReservationId(id);
        req.setAttribute("reservationDetails", reservationDetails);

        // Load payments
        List<TrajetReservationPaiement> payments = reservationService.getPaymentsByReservation(id);
        req.setAttribute("payments", payments);

        // Load status history
        List<TrajetReservationMouvementStatut> statusHistory = reservationService.getStatusHistoryByReservation(id);
        req.setAttribute("statusHistory", statusHistory);

        // Calculate amounts
        BigDecimal montantTotal = reservationService.getMontantTotalReservation(id);
        BigDecimal montantPaye = reservationService.getTotalPaiementRecu(id);
        BigDecimal soldeRestant = reservationService.getSoldeRestant(id);

        req.setAttribute("montantTotal", montantTotal);
        req.setAttribute("montantPaye", montantPaye);
        req.setAttribute("soldeRestant", soldeRestant);

        // Load reference data
        List<Client> clients = clientService.getAllClients();
        req.setAttribute("clients", clients);

        List<Trajet> trajets = trajetService.getAllTrajets();
        req.setAttribute("trajets", trajets);

        List<StatusObjectDto> reservationStatuts = statusService.findAllStatuses("Trajet_Reservation");
        req.setAttribute("reservationStatuts", reservationStatuts);

        List<TypeObjectDTO> modePaiements = typeObjectService.findAllTypeObject("Mode_Paiement");
        req.setAttribute("modePaiements", modePaiements);

        List<TypeObjectDTO> typePlaces = typeObjectService.findAllTypeObject("Type_Place");
        req.setAttribute("typePlaces", typePlaces);
    }

    private void handleChangeStatut(HttpServletRequest req, Long reservationId) {
        Long idStatut = Long.valueOf(req.getParameter("idStatut"));
        String observation = req.getParameter("observation");
        LocalDateTime dateChangement = LocalDateTime.now();

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
        Long idClient = Long.valueOf(req.getParameter("idClient"));
        String idCaisseStr = req.getParameter("idCaisse");
        Long idCaisse = (idCaisseStr != null && !idCaisseStr.isEmpty()) ? Long.valueOf(idCaisseStr) : null;
        LocalDateTime datePaiement = LocalDateTime.now();

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