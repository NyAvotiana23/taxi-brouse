package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.Client;
import com.mdgtaxi.entity.ProduitCategorie;
import com.mdgtaxi.entity.ProduitExtra;
import com.mdgtaxi.entity.ProduitExtraVente;
import com.mdgtaxi.entity.ProduitExtraVentePayement;
import com.mdgtaxi.service.ProduitExtraVenteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/produit-extra-ventes")
public class ProduitExtraVenteServlet extends HttpServlet {

    private final ProduitExtraVenteService venteService = new ProduitExtraVenteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Charger les données de référence
        loadReferenceData(req);

        if ("detail".equals(action)) {
            handleDetail(req, resp);
        } else if ("add".equals(action)) {
            req.getRequestDispatcher("/produit-extra-vente-form.jsp").forward(req, resp);
        } else if ("edit".equals(action)) {
            handleEdit(req, resp);
        } else if ("delete".equals(action)) {
            handleDelete(req, resp);
        } else if ("addPayement".equals(action)) {
            handleAddPayementForm(req, resp);
        } else {
            handleList(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("save".equals(action)) {
            handleSave(req, resp);
        } else if ("savePayement".equals(action)) {
            handleSavePayement(req, resp);
        } else if ("deletePayement".equals(action)) {
            handleDeletePayement(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<ProduitCategorie> categories = venteService.getAllCategories();
        List<ProduitExtra> produits = venteService.getAllProduits();
        List<Client> clients = venteService.getAllClients();

        req.setAttribute("categories", categories);
        req.setAttribute("produits", produits);
        req.setAttribute("clients", clients);
    }

    private void handleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> filters = collectFilters(req);

        List<ProduitExtraVente> ventes;
        if (filters.isEmpty()) {
            ventes = venteService.getAllVentes();
        } else {
            ventes = venteService.searchVentes(filters);
        }

        // Calculer les montants pour chaque vente
        Map<Long, BigDecimal> montantsTotaux = new HashMap<>();
        Map<Long, BigDecimal> montantsPayes = new HashMap<>();
        Map<Long, BigDecimal> montantsRestes = new HashMap<>();

        for (ProduitExtraVente vente : ventes) {
            BigDecimal montantTotal = venteService.getMontantTotalVente(vente);
            BigDecimal montantPaye = venteService.getMontantPayeVente(vente.getId());
            BigDecimal montantReste = montantTotal.subtract(montantPaye);

            montantsTotaux.put(vente.getId(), montantTotal);
            montantsPayes.put(vente.getId(), montantPaye);
            montantsRestes.put(vente.getId(), montantReste);
        }

        req.setAttribute("ventes", ventes);
        req.setAttribute("montantsTotaux", montantsTotaux);
        req.setAttribute("montantsPayes", montantsPayes);
        req.setAttribute("montantsRestes", montantsRestes);

        req.getRequestDispatcher("/produit-extra-ventes.jsp").forward(req, resp);
    }

    private void handleDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
            return;
        }

        Long id = Long.valueOf(idStr);
        ProduitExtraVente vente = venteService.getVenteById(id);

        if (vente == null) {
            req.setAttribute("error", "Vente non trouvée");
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
            return;
        }

        List<ProduitExtraVentePayement> payements = venteService.getPayementsByVente(id);
        BigDecimal montantTotal = venteService.getMontantTotalVente(vente);
        BigDecimal montantPaye = venteService.getMontantPayeVente(id);
        BigDecimal montantReste = montantTotal.subtract(montantPaye);

        req.setAttribute("vente", vente);
        req.setAttribute("payements", payements);
        req.setAttribute("montantTotal", montantTotal);
        req.setAttribute("montantPaye", montantPaye);
        req.setAttribute("montantReste", montantReste);

        req.getRequestDispatcher("/produit-extra-vente-detail.jsp").forward(req, resp);
    }

    private void handleEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
            return;
        }

        Long id = Long.valueOf(idStr);
        ProduitExtraVente vente = venteService.getVenteById(id);

        if (vente == null) {
            req.setAttribute("error", "Vente non trouvée");
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
            return;
        }

        req.setAttribute("vente", vente);
        req.getRequestDispatcher("/produit-extra-vente-form.jsp").forward(req, resp);
    }

    private void handleSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ProduitExtraVente vente;
            String idStr = req.getParameter("id");

            if (idStr != null && !idStr.isEmpty()) {
                vente = venteService.getVenteById(Long.valueOf(idStr));
                if (vente == null) {
                    req.setAttribute("error", "Vente non trouvée");
                    resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
                    return;
                }
            } else {
                vente = new ProduitExtraVente();
            }

            // Récupérer le produit
            String produitIdStr = req.getParameter("produitId");
            if (produitIdStr != null && !produitIdStr.isEmpty()) {
                ProduitExtra produit = venteService.getProduitById(Long.valueOf(produitIdStr));
                vente.setProduitExtra(produit);
                // Définir le prix unitaire depuis le produit si non spécifié
                String prixStr = req.getParameter("prixUnitaire");
                if (prixStr != null && !prixStr.isEmpty()) {
                    vente.setPrixUnitaire(new BigDecimal(prixStr));
                } else if (produit != null) {
                    vente.setPrixUnitaire(produit.getPrixUnitaire());
                }
            }

            // Récupérer le client
            String clientIdStr = req.getParameter("clientId");
            if (clientIdStr != null && !clientIdStr.isEmpty()) {
                Client client = venteService.getClientById(Long.valueOf(clientIdStr));
                vente.setClient(client);
            }

            // Quantité
            String quantiteStr = req.getParameter("quantite");
            if (quantiteStr != null && !quantiteStr.isEmpty()) {
                vente.setQuantite(Integer.valueOf(quantiteStr));
            }

            // Remise
            String remiseStr = req.getParameter("remise");
            if (remiseStr != null && !remiseStr.isEmpty()) {
                vente.setRemise(Double.valueOf(remiseStr));
            } else {
                vente.setRemise(0.0);
            }

            // Date
            String dateStr = req.getParameter("date");
            if (dateStr != null && !dateStr.isEmpty()) {
                vente.setDate(LocalDate.parse(dateStr).atStartOfDay());
            } else {
                vente.setDate(LocalDateTime.now());
            }

            venteService.saveVente(vente);
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?success=Vente enregistrée avec succès");

        } catch (Exception e) {
            req.setAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            loadReferenceData(req);
            req.getRequestDispatcher("/produit-extra-vente-form.jsp").forward(req, resp);
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                venteService.deleteVente(Long.valueOf(idStr));
                resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?success=Vente supprimée avec succès");
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?error=Erreur lors de la suppression");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
        }
    }

    private void handleAddPayementForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String venteIdStr = req.getParameter("venteId");
        if (venteIdStr == null || venteIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
            return;
        }

        Long venteId = Long.valueOf(venteIdStr);
        ProduitExtraVente vente = venteService.getVenteById(venteId);

        if (vente == null) {
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
            return;
        }

        BigDecimal montantTotal = venteService.getMontantTotalVente(vente);
        BigDecimal montantPaye = venteService.getMontantPayeVente(venteId);
        BigDecimal montantReste = montantTotal.subtract(montantPaye);

        req.setAttribute("vente", vente);
        req.setAttribute("montantTotal", montantTotal);
        req.setAttribute("montantPaye", montantPaye);
        req.setAttribute("montantReste", montantReste);

        req.getRequestDispatcher("/produit-extra-vente-payement-form.jsp").forward(req, resp);
    }

    private void handleSavePayement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String venteIdStr = req.getParameter("venteId");
            String montantStr = req.getParameter("montant");
            String dateStr = req.getParameter("datePayement");

            if (venteIdStr == null || venteIdStr.isEmpty() || montantStr == null || montantStr.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?error=Données manquantes");
                return;
            }

            ProduitExtraVente vente = venteService.getVenteById(Long.valueOf(venteIdStr));
            if (vente == null) {
                resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?error=Vente non trouvée");
                return;
            }

            ProduitExtraVentePayement payement = new ProduitExtraVentePayement();
            payement.setProduitExtraVente(vente);
            payement.setMontant(new BigDecimal(montantStr));

            if (dateStr != null && !dateStr.isEmpty()) {
                payement.setDatePayement(LocalDate.parse(dateStr).atStartOfDay());
            } else {
                payement.setDatePayement(LocalDateTime.now());
            }

            venteService.savePayement(payement);
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?action=detail&id=" + venteIdStr + "&success=Paiement enregistré");

        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?error=Erreur lors de l'enregistrement du paiement");
        }
    }

    private void handleDeletePayement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String payementIdStr = req.getParameter("payementId");
        String venteIdStr = req.getParameter("venteId");

        if (payementIdStr != null && !payementIdStr.isEmpty()) {
            try {
                venteService.deletePayement(Long.valueOf(payementIdStr));
                resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?action=detail&id=" + venteIdStr + "&success=Paiement supprimé");
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes?action=detail&id=" + venteIdStr + "&error=Erreur lors de la suppression");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/produit-extra-ventes");
        }
    }

    private Map<String, Object> collectFilters(HttpServletRequest req) {
        Map<String, Object> filters = new HashMap<>();

        String produitIdStr = req.getParameter("produitId");
        if (produitIdStr != null && !produitIdStr.isEmpty()) {
            filters.put("produitId", Long.valueOf(produitIdStr));
        }

        String categorieIdStr = req.getParameter("categorieId");
        if (categorieIdStr != null && !categorieIdStr.isEmpty()) {
            filters.put("categorieId", Long.valueOf(categorieIdStr));
        }

        String clientIdStr = req.getParameter("clientId");
        if (clientIdStr != null && !clientIdStr.isEmpty()) {
            filters.put("clientId", Long.valueOf(clientIdStr));
        }

        String dateDebut = req.getParameter("dateDebut");
        if (dateDebut != null && !dateDebut.isEmpty()) {
            filters.put("dateDebut", LocalDate.parse(dateDebut).atStartOfDay());
        }

        String dateFin = req.getParameter("dateFin");
        if (dateFin != null && !dateFin.isEmpty()) {
            filters.put("dateFin", LocalDate.parse(dateFin).atTime(23, 59, 59));
        }

        return filters;
    }
}
