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
import java.util.Map;
import java.util.HashMap;

@WebServlet("/reservations/detail")
public class ReservationDetailServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final ClientService clientService = new ClientService();
    private final TrajetService trajetService = new TrajetService();
    private final StatusService statusService = new StatusService();
    private final TypeObjectService typeObjectService = new TypeObjectService();
    private final VehiculeService vehiculeService = new VehiculeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/reservations");
            return;
        }

        Long id = Long.parseLong(idParam);
        String action = req.getParameter("action");

        // Handle edit detail
        if ("editDetail".equals(action)) {
            String detailIdStr = req.getParameter("detailId");
            if (detailIdStr != null && !detailIdStr.isEmpty()) {
                TrajetReservationDetails detail = reservationService.getReservationDetailById(Long.valueOf(detailIdStr));
                req.setAttribute("editingDetail", detail);
            }
        }

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
            switch (action != null ? action : "") {
                case "changeStatut":
                    handleChangeStatut(req, id);
                    break;
                case "addPayment":
                    handleAddPayment(req, id);
                    break;
                case "addDetail":
                    handleAddDetail(req, id);
                    break;
                case "updateDetail":
                    handleUpdateDetail(req, id);
                    break;
                case "deleteDetail":
                    handleDeleteDetail(req, id);
                    break;
                default:
                    break;
            }

            resp.sendRedirect(req.getContextPath() + "/reservations/detail?id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
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

        // Load available type places for this vehicle
        List<VehiculeTarifTypePlace> tarifPlaces = vehiculeService.getTarifTypePlacesByVehicule(
                reservation.getTrajet().getVehicule().getId()
        );
        req.setAttribute("tarifPlaces", tarifPlaces);

        // Calculate sold per type for availability
        Map<Long, Double> soldPerType = trajetService.getSoldPlacesPerType(reservation.getTrajet().getId());
        req.setAttribute("soldPerType", soldPerType);

        // Load categories for remise
        List<TypeObjectDTO> categories = typeObjectService.findAllTypeObject("Categorie_Personne");
        req.setAttribute("categories", categories);
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

    private void handleAddDetail(HttpServletRequest req, Long reservationId) throws Exception {
        Long idTypePlace = Long.valueOf(req.getParameter("idTypePlace"));
        Long idCategoriePersonne = Long.valueOf(req.getParameter("idCategoriePersonne"));
        double nombrePlaces = Double.parseDouble(req.getParameter("nombrePlaces"));

        TrajetReservation reservation = reservationService.getReservationById(reservationId);

        // Get tarif with remise
        double tarifUnitaire = reservationService.getTarifAvecRemise(
                reservation.getTrajet().getId(),
                idTypePlace,
                idCategoriePersonne
        );

        TrajetReservationDetails detail = new TrajetReservationDetails();
        detail.setTrajetReservation(reservation);

        TypePlace typePlace = new TypePlace();
        typePlace.setId(idTypePlace);
        detail.setTypePlace(typePlace);

        CategoriePersonne categorie = new CategoriePersonne();
        categorie.setId(idCategoriePersonne);
        detail.setCategoriePersonne(categorie);

        detail.setNombrePlaces(nombrePlaces);
        detail.setTarifUnitaire(tarifUnitaire);

        reservationService.createReservationDetail(detail);
    }

    private void handleUpdateDetail(HttpServletRequest req, Long reservationId) throws Exception {
        Long detailId = Long.valueOf(req.getParameter("detailId"));
        Long idTypePlace = Long.valueOf(req.getParameter("idTypePlace"));
        Long idCategoriePersonne = Long.valueOf(req.getParameter("idCategoriePersonne"));
        double nombrePlaces = Double.parseDouble(req.getParameter("nombrePlaces"));

        TrajetReservation reservation = reservationService.getReservationById(reservationId);
        TrajetReservationDetails detail = reservationService.getReservationDetailById(detailId);

        if (detail == null || !detail.getTrajetReservation().getId().equals(reservationId)) {
            throw new IllegalArgumentException("Invalid detail ID");
        }

        // Get tarif with remise
        double tarifUnitaire = reservationService.getTarifAvecRemise(
                reservation.getTrajet().getId(),
                idTypePlace,
                idCategoriePersonne
        );

        TypePlace typePlace = new TypePlace();
        typePlace.setId(idTypePlace);
        detail.setTypePlace(typePlace);

        CategoriePersonne categorie = new CategoriePersonne();
        categorie.setId(idCategoriePersonne);
        detail.setCategoriePersonne(categorie);

        detail.setNombrePlaces(nombrePlaces);
        detail.setTarifUnitaire(tarifUnitaire);

        reservationService.updateReservationDetail(detail);
    }

    private void handleDeleteDetail(HttpServletRequest req, Long reservationId) {
        Long detailId = Long.valueOf(req.getParameter("detailId"));
        TrajetReservationDetails detail = reservationService.getReservationDetailById(detailId);

        if (detail == null || !detail.getTrajetReservation().getId().equals(reservationId)) {
            throw new IllegalArgumentException("Invalid detail ID");
        }

        reservationService.deleteReservationDetail(detailId);
    }
}