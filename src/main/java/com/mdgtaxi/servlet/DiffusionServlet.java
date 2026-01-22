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
import java.util.*;

@WebServlet("/diffusions")
public class DiffusionServlet extends HttpServlet {

    private final DiffusionService diffusionService = new DiffusionService();
    private final PubliciteService publiciteService = new PubliciteService();
    private final SocieteService societeService = new SocieteService();
    private final TrajetService trajetService = new TrajetService();
    private final ConfigurationService configurationService = new ConfigurationService();

    // Code de configuration pour le coût unitaire de diffusion
    private static final String CODE_COUT_DIFFUSION = "COUT_DIFFUSION_TAXI";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        loadReferenceData(req);

        if ("edit".equals(action)) {
            if (id != null) {
                Diffusion diffusion = diffusionService.getDiffusionById(id);
                req.setAttribute("diffusion", diffusion);
            }
        } else if ("detail".equals(action)) {
            if (id != null) {
                handleDetail(req, id);
                req.getRequestDispatcher("/diffusion-detail.jsp").forward(req, resp);
                return;
            }
        } else if ("delete".equals(action)) {
            if (id != null) {
                diffusionService.deleteDiffusion(id);
                resp.sendRedirect(req.getContextPath() + "/diffusions");
                return;
            }
        }

        // Collecte des filtres
        Map<String, Object> filters = collectFilters(req);

        List<Diffusion> diffusions;
        if (!filters.isEmpty()) {
            diffusions = diffusionService.getDiffusion(
                    (Long) filters.get("idPublicite"),
                    (Long) filters.get("idSociete"),
                    (Long) filters.get("idTrajet"),
                    (Integer) filters.get("mois"),
                    (Integer) filters.get("annee"),
                    (Integer) filters.get("nombreMin"),
                    (Integer) filters.get("nombreMax")
            );
        } else {
            diffusions = diffusionService.getAllDiffusions();
        }

        req.setAttribute("diffusions", diffusions);
        req.getRequestDispatcher("/diffusions.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        try {
            if ("delete".equals(action) && id != null) {
                diffusionService.deleteDiffusion(id);
                resp.sendRedirect(req.getContextPath() + "/diffusions");
            } else if ("update".equals(action) && id != null) {
                Diffusion diffusion = buildDiffusionFromRequest(req);
                diffusion.setId(id);
                diffusionService.updateDiffusion(diffusion);
                resp.sendRedirect(req.getContextPath() + "/diffusions?action=detail&id=" + id);
            } else {
                // Create
                Diffusion diffusion = buildDiffusionFromRequest(req);
                diffusion = diffusionService.createDiffusion(diffusion);
                resp.sendRedirect(req.getContextPath() + "/diffusions");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Erreur: " + e.getMessage());
            loadReferenceData(req);
            if (id != null) {
                handleDetail(req, id);
                req.getRequestDispatcher("/diffusion-detail.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/diffusions.jsp").forward(req, resp);
            }
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<Publicite> publicites = publiciteService.getAllPublicites();
        List<Societe> societes = societeService.getAllSocietes();
        List<Trajet> trajets = trajetService.getAllTrajets();

        req.setAttribute("publicites", publicites);
        req.setAttribute("societes", societes);
        req.setAttribute("trajets", trajets);

        // Charger toutes les configurations
        List<Configuration> configurations = configurationService.getAllConfigurations();
        req.setAttribute("configurations", configurations);

        // Charger le montant unitaire par défaut depuis la configuration
        String montantDefaut = configurationService.getValeurByCode(CODE_COUT_DIFFUSION, "100000");
        req.setAttribute("montantUniteDefaut", montantDefaut);
    }

    private Map<String, Object> collectFilters(HttpServletRequest req) {
        Map<String, Object> filters = new HashMap<>();

        String publiciteId = req.getParameter("publiciteId");
        if (publiciteId != null && !publiciteId.isEmpty()) {
            filters.put("idPublicite", Long.valueOf(publiciteId));
        }

        String societeId = req.getParameter("societeId");
        if (societeId != null && !societeId.isEmpty()) {
            filters.put("idSociete", Long.valueOf(societeId));
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

    private void handleDetail(HttpServletRequest req, Long id) {
        Diffusion diffusion = diffusionService.getDiffusionById(id);
        req.setAttribute("diffusion", diffusion);
    }

    private Diffusion buildDiffusionFromRequest(HttpServletRequest req) {
        Diffusion diffusion = new Diffusion();

        Publicite publicite = new Publicite();
        publicite.setId(Long.valueOf(req.getParameter("idPublicite")));
        diffusion.setPublicite(publicite);

        Trajet trajet = new Trajet();
        trajet.setId(Long.valueOf(req.getParameter("idTrajet")));
        diffusion.setTrajet(trajet);

        diffusion.setMontantUnite(BigDecimal.valueOf(Double.parseDouble(req.getParameter("montantUnite"))));
        diffusion.setNombre(Integer.valueOf(req.getParameter("nombre")));

        return diffusion;
    }
}