package com.mdgtaxi.servlet;

import com.mdgtaxi.service.ChiffreAffaireService;
import com.mdgtaxi.view.VmCAComplet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ca-complet")
public class CACompletServlet extends HttpServlet {

    private final ChiffreAffaireService caService = new ChiffreAffaireService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Collecte des filtres
        Integer mois = null;
        Integer annee = null;

        String moisStr = req.getParameter("mois");
        if (moisStr != null && !moisStr.isEmpty()) {
            try {
                mois = Integer.valueOf(moisStr);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Mois invalide");
            }
        }

        String anneeStr = req.getParameter("annee");
        if (anneeStr != null && !anneeStr.isEmpty()) {
            try {
                annee = Integer.valueOf(anneeStr);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Année invalide");
            }
        }

        // Récupérer les données avec ou sans filtres
        List<VmCAComplet> caComplets = caService.getCAPrevisionTotal(mois, annee);

        req.setAttribute("caComplets", caComplets);
        req.getRequestDispatcher("/ca-complet.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Rediriger vers GET avec les paramètres de filtre
        StringBuilder redirectUrl = new StringBuilder(req.getContextPath() + "/ca-complet?");
        
        String mois = req.getParameter("mois");
        if (mois != null && !mois.isEmpty()) {
            redirectUrl.append("mois=").append(mois).append("&");
        }
        
        String annee = req.getParameter("annee");
        if (annee != null && !annee.isEmpty()) {
            redirectUrl.append("annee=").append(annee);
        }
        
        resp.sendRedirect(redirectUrl.toString());
    }
}
