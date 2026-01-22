package com.mdgtaxi.servlet;

import com.mdgtaxi.service.DiffusionService;
import com.mdgtaxi.view.VmDiffusionMensuelGlobal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet("/diffusions-global")
public class DiffusionGlobalServlet extends HttpServlet {

    private final DiffusionService diffusionService = new DiffusionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Collecte des filtres
        Map<String, Object> filters = collectFilters(req);
        
        List<VmDiffusionMensuelGlobal> diffusionsGlobal;
        
        if (!filters.isEmpty()) {
            diffusionsGlobal = diffusionService.getDiffusionMensuel(
                (Integer) filters.get("mois"),
                (Integer) filters.get("annee"),
                (Double) filters.get("montantMin"),
                (Double) filters.get("montantMax")
            );
        } else {
            diffusionsGlobal = diffusionService.getAllDiffusionMensuel();
        }
        
        req.setAttribute("diffusionsGlobal", diffusionsGlobal);
        req.getRequestDispatcher("/diffusions-global.jsp").forward(req, resp);
    }

    private Map<String, Object> collectFilters(HttpServletRequest req) {
        Map<String, Object> filters = new HashMap<>();

        String mois = req.getParameter("mois");
        if (mois != null && !mois.isEmpty()) {
            filters.put("mois", Integer.valueOf(mois));
        }

        String annee = req.getParameter("annee");
        if (annee != null && !annee.isEmpty()) {
            filters.put("annee", Integer.valueOf(annee));
        }

        String montantMin = req.getParameter("montantMin");
        if (montantMin != null && !montantMin.isEmpty()) {
            filters.put("montantMin", Double.valueOf(montantMin));
        }

        String montantMax = req.getParameter("montantMax");
        if (montantMax != null && !montantMax.isEmpty()) {
            filters.put("montantMax", Double.valueOf(montantMax));
        }

        return filters;
    }
}