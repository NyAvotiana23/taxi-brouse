package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.Chauffeur;
import com.mdgtaxi.service.ChauffeurService;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            Long id = Long.valueOf(req.getParameter("id"));
            Chauffeur chauffeur = chauffeurService.getChauffeurById(id);
            req.setAttribute("chauffeur", chauffeur);
        }

        List<Chauffeur> chauffeurs = chauffeurService.getAllChauffeurs();
        req.setAttribute("chauffeurs", chauffeurs);

        req.getRequestDispatcher("/chauffeures.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String nom = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        LocalDate dateNaissance = LocalDate.parse(req.getParameter("dateNaissance"));
        String numeroPermis = req.getParameter("numeroPermis");

        Chauffeur chauffeur = new Chauffeur();
        if (idStr != null && !idStr.isEmpty()) {
            chauffeur = chauffeurService.getChauffeurById(Long.valueOf(idStr));
        }

        chauffeur.setNom(nom);
        chauffeur.setPrenom(prenom);
        chauffeur.setDateNaissance(dateNaissance);
        chauffeur.setNumeroPermis(numeroPermis);

        if (chauffeur.getId() == null) {
            chauffeurService.createChauffeur(chauffeur);
        } else {
            chauffeurService.updateChauffeur(chauffeur);
        }

        resp.sendRedirect(req.getContextPath() + "/chauffeures");
    }
}