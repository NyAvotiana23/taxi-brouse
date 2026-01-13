package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.Chauffeur;
import com.mdgtaxi.service.ChauffeurService;
import com.mdgtaxi.view.VmChauffeurActivite;
import com.mdgtaxi.view.VmChauffeurDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/chauffeures/detail")
public class ChauffeurDetailServlet extends HttpServlet {

    private final ChauffeurService chauffeurService = new ChauffeurService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        Chauffeur chauffeur = chauffeurService.getChauffeurById(id);

        req.setAttribute("chauffeur", chauffeur);

        req.getRequestDispatcher("/chauffeur-detail.jsp").forward(req, resp);
    }
}