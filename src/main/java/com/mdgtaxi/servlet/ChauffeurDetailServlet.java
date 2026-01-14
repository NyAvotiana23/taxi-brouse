package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.MouvementStatusDto;
import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.entity.ChauffeurMouvementStatut;
import com.mdgtaxi.service.ChauffeurService;
import com.mdgtaxi.service.MouvementStatusCreateService;
import com.mdgtaxi.service.MouvementStatusService;
import com.mdgtaxi.service.StatusService;
import com.mdgtaxi.view.VmChauffeurActivite;
import com.mdgtaxi.view.VmChauffeurDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/chauffeures/detail")
public class ChauffeurDetailServlet extends HttpServlet {

    private final ChauffeurService chauffeurService = new ChauffeurService();
    private final MouvementStatusService mouvementStatusService = new MouvementStatusService();
    private final MouvementStatusCreateService mouvementStatusCreateService = new MouvementStatusCreateService();
    private final StatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/chauffeures");
            return;
        }

        Long id = Long.parseLong(idParam);
        loadChauffeurDetails(req, id);

        req.getRequestDispatcher("/chauffeur-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/chauffeures");
            return;
        }

        Long id = Long.parseLong(idParam);

        try {
            if ("changeStatut".equals(action)) {
                handleChangeStatut(req, id);
            }

            resp.sendRedirect(req.getContextPath() + "/chauffeures/detail?id=" + id);
        } catch (Exception e) {
            req.setAttribute("error", "Erreur: " + e.getMessage());
            loadChauffeurDetails(req, id);
            req.getRequestDispatcher("/chauffeur-detail.jsp").forward(req, resp);
        }
    }

    private void loadChauffeurDetails(HttpServletRequest req, Long id) {
        // Load chauffeur detail view
        VmChauffeurDetail detail = chauffeurService.getChauffeurDetailById(id);
        req.setAttribute("detail", detail);

        // Load chauffeur activity
        VmChauffeurActivite activite = chauffeurService.getChauffeurActivite(id);
        req.setAttribute("activite", activite);

        // Load current status
        MouvementStatusDto currentStatus = mouvementStatusService.getCurrentStatus("Chauffeur", id);
        req.setAttribute("currentStatus", currentStatus);

        // Load status history
        List<MouvementStatusDto> statusHistory = mouvementStatusService.getStatusHistory("Chauffeur", id);
        req.setAttribute("statusHistory", statusHistory);

        // Load available statuses
        List<StatusObjectDto> availableStatuts = statusService.findAllStatuses("Chauffeur");
        req.setAttribute("availableStatuts", availableStatuts);
    }

    private void handleChangeStatut(HttpServletRequest req, Long chauffeurId) {
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
                "Chauffeur",
                chauffeurId,
                idStatut,
                dateChangement,
                observation
        );
    }
}