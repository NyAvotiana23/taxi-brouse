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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/paiements")
public class DiffusionPaiementServlet extends HttpServlet {

    private final DiffusionPaiementService paiementService = new DiffusionPaiementService();
    private final DiffusionService diffusionService = new DiffusionService();
    private final SocieteService societeService = new SocieteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        loadReferenceData(req);

        if ("edit".equals(action)) {
            if (id != null) {
                DiffusionPaiement paiement = paiementService.getPaiementById(id);
                req.setAttribute("paiement", paiement);
            }
        } else if ("detail".equals(action)) {
            if (id != null) {
                handleDetail(req, id);
                req.getRequestDispatcher("/paiement-detail.jsp").forward(req, resp);
                return;
            }
        } else if ("delete".equals(action)) {
            if (id != null) {
                paiementService.deletePaiement(id);
                resp.sendRedirect(req.getContextPath() + "/paiements");
                return;
            }
        }

        List<DiffusionPaiement> paiements = paiementService.getAllPaiements();
        req.setAttribute("paiements", paiements);
        req.getRequestDispatcher("/paiements.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        try {
            if ("delete".equals(action) && id != null) {
                paiementService.deletePaiement(id);
                resp.sendRedirect(req.getContextPath() + "/paiements");
            } else if ("update".equals(action) && id != null) {
                DiffusionPaiement paiement = buildPaiementFromRequest(req);
                paiement.setId(id);
                paiementService.updatePaiement(paiement);
                resp.sendRedirect(req.getContextPath() + "/paiements?action=detail&id=" + id);
            } else {
                // Create
                DiffusionPaiement paiement = buildPaiementFromRequest(req);
                paiement = paiementService.createPaiement(paiement);
                resp.sendRedirect(req.getContextPath() + "/paiements");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Erreur: " + e.getMessage());
            loadReferenceData(req);
            if (id != null) {
                handleDetail(req, id);
                req.getRequestDispatcher("/paiement-detail.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/paiements.jsp").forward(req, resp);
            }
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<Diffusion> diffusions = diffusionService.getAllDiffusions();
        List<Societe> societes = societeService.getAllSocietes();

        req.setAttribute("diffusions", diffusions);
        req.setAttribute("societes", societes);
    }

    private void handleDetail(HttpServletRequest req, Long id) {
        DiffusionPaiement paiement = paiementService.getPaiementById(id);
        req.setAttribute("paiement", paiement);
    }

    private DiffusionPaiement buildPaiementFromRequest(HttpServletRequest req) {
        DiffusionPaiement paiement = new DiffusionPaiement();

        Diffusion diffusion = new Diffusion();
        diffusion.setId(Long.valueOf(req.getParameter("idDiffusion")));
        paiement.setDiffusion(diffusion);

        Societe societe = new Societe();
        societe.setId(Long.valueOf(req.getParameter("idSociete")));
        paiement.setSociete(societe);

        String datePaiementStr = req.getParameter("datePaiement");
        if (datePaiementStr != null && !datePaiementStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            paiement.setDatePaiement(LocalDateTime.parse(datePaiementStr, formatter));
        } else {
            paiement.setDatePaiement(LocalDateTime.now());
        }

        paiement.setMontantPaye(new BigDecimal(req.getParameter("montant")));

        return paiement;
    }
}