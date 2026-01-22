package com.mdgtaxi.servlet;

import com.mdgtaxi.service.*;
import com.mdgtaxi.view.VmDiffusionPaiement;
import com.mdgtaxi.entity.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/paiements-dashboard")
public class DiffusionPaiementDashboardServlet extends HttpServlet {

    private final DiffusionPaiementService paiementService = new DiffusionPaiementService();
    private final SocieteService societeService = new SocieteService();
    private final PubliciteService publiciteService = new PubliciteService();
    private final TrajetService trajetService = new TrajetService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Load reference data for filters
        loadReferenceData(req);

        // Collect filters
        Map<String, Object> filters = collectFilters(req);

        List<VmDiffusionPaiement> vmPaiements;

        if (!filters.isEmpty()) {
            vmPaiements = paiementService.getVmPaiements(
                    (Long) filters.get("idPublicite"),
                    (List<Long>) filters.get("idSocietes"),
                    (Long) filters.get("idTrajet"),
                    (Integer) filters.get("mois"),
                    (Integer) filters.get("annee"),
                    (Integer) filters.get("nombreMin"),
                    (Integer) filters.get("nombreMax")
            );
        } else {
            vmPaiements = paiementService.getAllVmPaiements();
        }

        req.setAttribute("vmPaiements", vmPaiements);
        req.getRequestDispatcher("/paiements-dashboard.jsp").forward(req, resp);
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<Societe> societes = societeService.getAllSocietes();
        List<Publicite> publicites = publiciteService.getAllPublicites();
        List<Trajet> trajets = trajetService.getAllTrajets();

        req.setAttribute("societes", societes);
        req.setAttribute("publicites", publicites);
        req.setAttribute("trajets", trajets);
    }

    private Map<String, Object> collectFilters(HttpServletRequest req) {
        Map<String, Object> filters = new HashMap<>();

        String publiciteId = req.getParameter("publiciteId");
        if (publiciteId != null && !publiciteId.isEmpty()) {
            filters.put("idPublicite", Long.valueOf(publiciteId));
        }

        String[] societeIds = req.getParameterValues("societeIds");
        if (societeIds != null && societeIds.length > 0) {
            List<Long> idSocietes = new ArrayList<>();
            for (String id : societeIds) {
                if (!id.isEmpty()) {
                    idSocietes.add(Long.valueOf(id));
                }
            }
            if (!idSocietes.isEmpty()) {
                filters.put("idSocietes", idSocietes);
            }
        }

        String trajetId = req.getParameter("trajetId");
        if (trajetId != null && !trajetId.isEmpty()) {
            filters.put("idTrajet", Long.valueOf(trajetId));
        }

        String mois = req.getParameter("mois");
        if (mois != null && !mois.isEmpty()) {
            filters.put("mois", Integer.valueOf(mois));
        }

        String annee = req.getParameter("annee");
        if (annee != null && !annee.isEmpty()) {
            filters.put("annee", Integer.valueOf(annee));
        }

        String nombreMin = req.getParameter("nombreMin");
        if (nombreMin != null && !nombreMin.isEmpty()) {
            filters.put("nombreMin", Integer.valueOf(nombreMin));
        }

        String nombreMax = req.getParameter("nombreMax");
        if (nombreMax != null && !nombreMax.isEmpty()) {
            filters.put("nombreMax", Integer.valueOf(nombreMax));
        }

        return filters;
    }
}