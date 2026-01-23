package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.*;
import com.mdgtaxi.service.*;
import com.mdgtaxi.view.VmDiffusionPaiement;

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

@WebServlet("/diffusions")
public class DiffusionServlet extends HttpServlet {

    private final DiffusionService diffusionService = new DiffusionService();
    private final DiffusionDetailService diffusionDetailService = new DiffusionDetailService();
    private final DiffusionPaiementService paiementService = new DiffusionPaiementService();
    private final SocieteService societeService = new SocieteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        loadReferenceData(req);

        if ("detail".equals(action)) {
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

        // Liste des diffusions avec leurs montants
        List<VmDiffusionPaiement> vmDiffusions = paiementService.getAllVmPaiements();
        req.setAttribute("vmDiffusions", vmDiffusions);
        req.getRequestDispatcher("/diffusions.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        try {
            if ("createPaiement".equals(action) && id != null) {
                // Créer un paiement pour une diffusion
                DiffusionPaiement paiement = buildPaiementFromRequest(req, id);
                paiementService.createPaiementAvecRepartition(paiement);
                resp.sendRedirect(req.getContextPath() + "/diffusions?action=detail&id=" + id);
            } else if ("delete".equals(action) && id != null) {
                diffusionService.deleteDiffusion(id);
                resp.sendRedirect(req.getContextPath() + "/diffusions");
            } else {
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
        List<Societe> societes = societeService.getAllSocietes();
        req.setAttribute("societes", societes);
    }

    private void handleDetail(HttpServletRequest req, Long id) {
        Diffusion diffusion = diffusionService.getDiffusionById(id);
        List<DiffusionDetail> details = diffusionDetailService.getDetailsByDiffusionId(id);
        List<DiffusionPaiement> paiements = paiementService.getPaiementsByDiffusionId(id);

        // Calculer le total à payer
        BigDecimal montantTotal = BigDecimal.ZERO;
        for (DiffusionDetail detail : details) {
            BigDecimal montantDetail = detail.getMontantUnitaire()
                    .multiply(new BigDecimal(detail.getNombreRepetition()));
            montantTotal = montantTotal.add(montantDetail);
        }

        // Calculer le total payé
        BigDecimal montantPaye = BigDecimal.ZERO;
        for (DiffusionPaiement paiement : paiements) {
            montantPaye = montantPaye.add(paiement.getMontantPaye());
        }

        // Calculer le reste
        BigDecimal montantReste = montantTotal.subtract(montantPaye);

        req.setAttribute("diffusion", diffusion);
        req.setAttribute("details", details);
        req.setAttribute("paiements", paiements);
        req.setAttribute("montantTotal", montantTotal);
        req.setAttribute("montantPaye", montantPaye);
        req.setAttribute("montantReste", montantReste);
    }

    private DiffusionPaiement buildPaiementFromRequest(HttpServletRequest req, Long diffusionId) {
        DiffusionPaiement paiement = new DiffusionPaiement();

        Diffusion diffusion = new Diffusion();
        diffusion.setId(diffusionId);
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

        paiement.setMontantPaye(new BigDecimal(req.getParameter("montantPaye")));

        return paiement;
    }
}