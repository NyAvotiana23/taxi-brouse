package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.entity.Chauffeur;
import com.mdgtaxi.service.ChauffeurService;
import com.mdgtaxi.service.StatusService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/chauffeures")
public class ChauffeurServlet extends HttpServlet {

    private final ChauffeurService chauffeurService = new ChauffeurService();
    private final StatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Load reference data
        loadReferenceData(req);

        if ("edit".equals(action)) {
            handleEdit(req);
        } else if ("view".equals(action)) {
            handleView(req, resp);
            return;
        }

        // Load all chauffeurs
        List<Chauffeur> chauffeurs = chauffeurService.getAllChauffeurs();
        req.setAttribute("chauffeurs", chauffeurs);

        req.getRequestDispatcher("/chauffeures.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Chauffeur chauffeur = buildChauffeurFromRequest(req);

            if (chauffeur.getId() == null) {
                chauffeurService.createChauffeur(chauffeur);
            } else {
                chauffeurService.updateChauffeur(chauffeur);
            }

            resp.sendRedirect(req.getContextPath() + "/chauffeures");
        } catch (Exception e) {
            req.setAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            loadReferenceData(req);
            req.getRequestDispatcher("/chauffeures.jsp").forward(req, resp);
        }
    }

    private void handleEdit(HttpServletRequest req) {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.valueOf(idParam);
            Chauffeur chauffeur = chauffeurService.getChauffeurById(id);
            req.setAttribute("chauffeur", chauffeur);
        }
    }

    private void handleView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            req.setAttribute("id", idParam);
            req.getRequestDispatcher("/chauffeur-detail.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/chauffeures");
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<StatusObjectDto> chauffeurStatuts = statusService.findAllStatuses("Chauffeur");
        req.setAttribute("chauffeurStatuts", chauffeurStatuts);
    }

    private Chauffeur buildChauffeurFromRequest(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        Chauffeur chauffeur;

        if (idStr != null && !idStr.isEmpty()) {
            chauffeur = chauffeurService.getChauffeurById(Long.valueOf(idStr));
        } else {
            chauffeur = new Chauffeur();
        }

        // Set fields
        chauffeur.setNom(req.getParameter("nom"));
        chauffeur.setPrenom(req.getParameter("prenom"));
        chauffeur.setDateNaissance(LocalDate.parse(req.getParameter("dateNaissance")));
        chauffeur.setNumeroPermis(req.getParameter("numeroPermis"));

        return chauffeur;
    }
}