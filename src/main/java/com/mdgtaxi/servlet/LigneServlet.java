package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.Ligne;
import com.mdgtaxi.entity.Ville;
import com.mdgtaxi.service.LigneService;
import com.mdgtaxi.service.VilleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/lignes")
public class LigneServlet extends HttpServlet {

    private final LigneService ligneService = new LigneService();
    private final VilleService villeService = new VilleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            Long id = Long.valueOf(req.getParameter("id"));
            Ligne ligne = ligneService.getLigneById(id);
            req.setAttribute("ligne", ligne);
        }

        List<Ligne> lignes = ligneService.getAllLignes();
        List<Ville> villes = villeService.getAllVilles();

        req.setAttribute("lignes", lignes);
        req.setAttribute("villes", villes);

        req.getRequestDispatcher("/lignes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Long idVilleDepart = Long.valueOf(req.getParameter("idVilleDepart"));
        Long idVilleArrivee = Long.valueOf(req.getParameter("idVilleArrivee"));
        BigDecimal distanceKm = new BigDecimal(req.getParameter("distanceKm"));

        Ligne ligne = new Ligne();
        if (idStr != null && !idStr.isEmpty()) {
            ligne = ligneService.getLigneById(Long.valueOf(idStr));
        }

        Ville villeDepart = new Ville();
        villeDepart.setId(idVilleDepart);
        ligne.setVilleDepart(villeDepart);

        Ville villeArrivee = new Ville();
        villeArrivee.setId(idVilleArrivee);
        ligne.setVilleArrivee(villeArrivee);

        ligne.setDistanceKm(distanceKm);

        if (ligne.getId() == null) {
            ligneService.createLigne(ligne);
        } else {
            ligneService.updateLigne(ligne);
        }

        resp.sendRedirect(req.getContextPath() + "/lignes");
    }
}