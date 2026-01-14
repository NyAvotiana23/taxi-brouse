package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.MouvementStatusDto;
import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.entity.VehiculeEntretien;
import com.mdgtaxi.entity.VehiculeStatut;
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
            }

            resp.sendRedirect(req.getContextPath() + "/vehicules/detail?id=" + id);
        } catch (Exception e) {
            req.setAttribute("error", "Erreur: " + e.getMessage());
            loadVehiculeDetails(req, id);
            req.getRequestDispatcher("/vehicule-detail.jsp").forward(req, resp);
        }
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