package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.MouvementStatusDto;
import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.entity.*;
import com.mdgtaxi.service.MouvementStatusCreateService;
import com.mdgtaxi.service.MouvementStatusService;
import com.mdgtaxi.service.StatusService;
import com.mdgtaxi.service.VehiculeService;
import com.mdgtaxi.view.VmVehiculeCoutEntretien;
import com.mdgtaxi.view.VmVehiculeDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/vehicules/detail")
public class VehiculeDetailServlet extends HttpServlet {

    private final VehiculeService vehiculeService = new VehiculeService();
    private final MouvementStatusService mouvementStatusService = new MouvementStatusService();
    private final MouvementStatusCreateService mouvementStatusCreateService = new MouvementStatusCreateService();
    private final StatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/vehicules");
            return;
        }

        Long id = Long.parseLong(idParam);
        loadVehiculeDetails(req, id);

        req.getRequestDispatcher("/vehicule-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/vehicules");
            return;
        }

        Long id = Long.parseLong(idParam);

        try {
            if ("changeStatut".equals(action)) {
                handleChangeStatut(req, id);
            } else if ("addEntretien".equals(action)) {
                handleAddEntretien(req, id);
            } else if ("addTarifPlace".equals(action)) {  // NEW: Handle add
                handleAddTarifPlace(req, id);
            } else if ("updateTarifPlace".equals(action)) {  // NEW: Handle update
                handleUpdateTarifPlace(req, id);
            } else if ("deleteTarifPlace".equals(action)) {  // NEW: Handle delete
                handleDeleteTarifPlace(req, id);
            }

            resp.sendRedirect(req.getContextPath() + "/vehicules/detail?id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Erreur: " + e.getMessage());
            loadVehiculeDetails(req, id);
            req.getRequestDispatcher("/vehicule-detail.jsp").forward(req, resp);
        }
    }


    private void handleAddTarifPlace(HttpServletRequest req, Long vehiculeId) throws Exception {
        VehiculeTarifTypePlace vttp = new VehiculeTarifTypePlace();
        Vehicule vehicule = new Vehicule();
        vehicule.setId(vehiculeId);
        vttp.setVehicule(vehicule);

        Long typePlaceId = Long.parseLong(req.getParameter("typePlaceId"));
        TypePlace typePlace = new TypePlace();
        typePlace.setId(typePlaceId);
        vttp.setTypePlace(typePlace);

        vttp.setNombrePlace(Double.parseDouble(req.getParameter("nombrePlace")));
        vttp.setTarifUnitaire(Double.parseDouble(req.getParameter("tarifUnitaire")));

        vehiculeService.createOrUpdateTarifTypePlace(vttp);
    }

    private void handleUpdateTarifPlace(HttpServletRequest req, Long vehiculeId) throws Exception  {
        Long vttpId = Long.parseLong(req.getParameter("vttpId"));
        VehiculeTarifTypePlace vttp = vehiculeService.getTarifTypePlaceById(vttpId);  // Assume you add this method to VehiculeService
        if (vttp == null || !vttp.getVehicule().getId().equals(vehiculeId)) {
            throw new IllegalArgumentException("Invalid TarifTypePlace ID");
        }

        Long typePlaceId = Long.parseLong(req.getParameter("typePlaceId"));
        TypePlace typePlace = new TypePlace();
        typePlace.setId(typePlaceId);
        vttp.setTypePlace(typePlace);

        vttp.setNombrePlace(Double.parseDouble(req.getParameter("nombrePlace")));
        vttp.setTarifUnitaire(Double.parseDouble(req.getParameter("tarifUnitaire")));

        vehiculeService.createOrUpdateTarifTypePlace(vttp);
    }

    private void handleDeleteTarifPlace(HttpServletRequest req, Long vehiculeId) {
        Long vttpId = Long.parseLong(req.getParameter("vttpId"));
        VehiculeTarifTypePlace vttp = vehiculeService.getTarifTypePlaceById(vttpId);
        if (vttp == null || !vttp.getVehicule().getId().equals(vehiculeId)) {
            throw new IllegalArgumentException("Invalid TarifTypePlace ID");
        }
        vehiculeService.deleteTarifTypePlace(vttpId);  // Assume you add this method to VehiculeService
    }

    private void loadVehiculeDetails(HttpServletRequest req, Long id) {
        // Load vehicle detail
        VmVehiculeDetail detail = vehiculeService.getVehiculeDetail(id);
        req.setAttribute("detail", detail);

        // Load maintenance cost
        VmVehiculeCoutEntretien cout = vehiculeService.getCoutEntretien(id);
        req.setAttribute("cout", cout);

        // Load maintenance history
        List<VehiculeEntretien> entretiens = vehiculeService.getEntretiensByVehicule(id);
        req.setAttribute("entretiens", entretiens);

        // Load current status
        MouvementStatusDto currentStatus = mouvementStatusService.getCurrentStatus("Vehicule", id);
        req.setAttribute("currentStatus", currentStatus);

        // Load status history
        List<MouvementStatusDto> statusHistory = mouvementStatusService.getStatusHistory("Vehicule", id);
        req.setAttribute("statusHistory", statusHistory);

        // Load available statuses
        List<StatusObjectDto> availableStatuts = statusService.findAllStatuses("Vehicule");
        req.setAttribute("availableStatuts", availableStatuts);

        List<VehiculeTarifTypePlace> tarifPlaces = vehiculeService.getTarifTypePlacesByVehicule(id);
        req.setAttribute("tarifPlaces", tarifPlaces);

        double maxCA = vehiculeService.getTotalMaxChiffreAffairePossible(id);
        req.setAttribute("maxCA", maxCA);

        List<TypePlace> typePlaces = vehiculeService.getAllTypePlaces();
        req.setAttribute("typePlaces", typePlaces);
    }

    private void handleChangeStatut(HttpServletRequest req, Long vehiculeId) {
        Long idStatut = Long.valueOf(req.getParameter("idStatut"));
        String observation = req.getParameter("observation");
        String dateChangementStr = req.getParameter("dateChangement");

        LocalDateTime dateChangement;
        if (dateChangementStr != null && !dateChangementStr.isEmpty()) {
            dateChangement = LocalDateTime.parse(dateChangementStr);
        } else {
            dateChangement = LocalDateTime.now();
        }

        mouvementStatusCreateService.createMouvementStatut(
                "Vehicule",
                vehiculeId,
                idStatut,
                dateChangement,
                observation
        );
    }

    private void handleAddEntretien(HttpServletRequest req, Long vehiculeId) {
        VehiculeEntretien entretien = new VehiculeEntretien();

        com.mdgtaxi.entity.Vehicule vehicule = new com.mdgtaxi.entity.Vehicule();
        vehicule.setId(vehiculeId);
        entretien.setVehicule(vehicule);

        entretien.setMotif(req.getParameter("motif"));
        entretien.setMontantDepense(new BigDecimal(req.getParameter("montantDepense")));

        String dateDebutStr = req.getParameter("dateDebutEntretien");
        if (dateDebutStr != null && !dateDebutStr.isEmpty()) {
            entretien.setDateDebutEntretien(LocalDateTime.parse(dateDebutStr));
        } else {
            entretien.setDateDebutEntretien(LocalDateTime.now());
        }

        String dateFinStr = req.getParameter("dateFinEntretien");
        if (dateFinStr != null && !dateFinStr.isEmpty()) {
            entretien.setDateFinEntretien(LocalDateTime.parse(dateFinStr));
        }

        vehiculeService.addEntretien(entretien);
    }
}