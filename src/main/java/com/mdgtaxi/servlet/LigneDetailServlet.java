package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.Ligne;
import com.mdgtaxi.entity.Trajet;
import com.mdgtaxi.service.LigneService;
import com.mdgtaxi.service.TrajetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/lignes/detail")
public class LigneDetailServlet extends HttpServlet {

    private final LigneService ligneService = new LigneService();
    private final TrajetService trajetService = new TrajetService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Ligne ligne = ligneService.getLigneById(id);
        List<Trajet> trajets = trajetService.getTrajetsByLigneId(id);

        req.setAttribute("ligne", ligne);
        req.setAttribute("trajets", trajets);

        req.getRequestDispatcher("/ligne-detail.jsp").forward(req, resp);
    }
}