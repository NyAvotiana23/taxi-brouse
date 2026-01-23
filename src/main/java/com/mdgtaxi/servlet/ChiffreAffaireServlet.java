package com.mdgtaxi.servlet;

import com.mdgtaxi.service.ChiffreAffaireService;
import com.mdgtaxi.service.VilleService;
import com.mdgtaxi.service.VehiculeService;
import com.mdgtaxi.entity.Ville;
import com.mdgtaxi.entity.Vehicule;
import com.mdgtaxi.view.VmTrajetCaComplet;
import com.mdgtaxi.view.VmTrajetCaReel;
import com.mdgtaxi.view.VmTrajetPrevisionCaTotal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/chiffre-affaire")
public class ChiffreAffaireServlet extends HttpServlet {

    private final ChiffreAffaireService caService = new ChiffreAffaireService();
    private final VilleService villeService = new VilleService();
    private final VehiculeService vehiculeService = new VehiculeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type"); // prevision, reel, complet
        String action = req.getParameter("action");
        String idTrajetStr = req.getParameter("idTrajet");

        // Charger les données de référence pour les filtres
        loadReferenceData(req);

        // Si on demande le détail d'un trajet spécifique
        if ("detail".equals(action) && idTrajetStr != null && !idTrajetStr.isEmpty()) {
            Long idTrajet = Long.valueOf(idTrajetStr);
            handleDetail(req, type, idTrajet);
            req.getRequestDispatcher("/chiffre-affaire-detail.jsp").forward(req, resp);
            return;
        }

        // Collecter les filtres
        Map<String, Object> filters = collectFilters(req);

        // Sélectionner la vue à afficher selon le type
        if ("reel".equals(type)) {
            List<VmTrajetCaReel> caReels = filters.isEmpty() 
                ? caService.getAllCaReels() 
                : caService.searchCaReels(filters);
            req.setAttribute("caReels", caReels);
            req.setAttribute("type", "reel");
            req.getRequestDispatcher("/chiffre-affaire-reel.jsp").forward(req, resp);
        } else if ("complet".equals(type)) {
            List<VmTrajetCaComplet> caComplets = filters.isEmpty() 
                ? caService.getAllCaComplets() 
                : caService.searchCaComplets(filters);
            req.setAttribute("caComplets", caComplets);
            req.setAttribute("type", "complet");
            req.getRequestDispatcher("/chiffre-affaire-complet.jsp").forward(req, resp);
        } else {
            // Par défaut : prévision
            List<VmTrajetPrevisionCaTotal> previsions = filters.isEmpty() 
                ? caService.getAllPrevisions() 
                : caService.searchPrevisions(filters);
            req.setAttribute("previsions", previsions);
            req.setAttribute("type", "prevision");
            req.getRequestDispatcher("/chiffre-affaire-prevision.jsp").forward(req, resp);
        }
    }

    private void handleDetail(HttpServletRequest req, String type, Long idTrajet) {
        if ("reel".equals(type)) {
            VmTrajetCaReel caReel = caService.getCaReelByTrajetId(idTrajet);
            req.setAttribute("caReel", caReel);
            req.setAttribute("type", "reel");
        } else if ("complet".equals(type)) {
            VmTrajetCaComplet caComplet = caService.getCaCompletByTrajetId(idTrajet);
            req.setAttribute("caComplet", caComplet);
            req.setAttribute("type", "complet");
        } else {
            VmTrajetPrevisionCaTotal prevision = caService.getPrevisionByTrajetId(idTrajet);
            req.setAttribute("prevision", prevision);
            req.setAttribute("type", "prevision");
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<Ville> villes = villeService.getAllVilles();
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();

        req.setAttribute("villes", villes);
        req.setAttribute("vehicules", vehicules);
    }

    private Map<String, Object> collectFilters(HttpServletRequest req) {
        Map<String, Object> filters = new HashMap<>();

        // Filtre par ID trajet
        String idTrajetStr = req.getParameter("idTrajet");
        if (idTrajetStr != null && !idTrajetStr.isEmpty()) {
            filters.put("idTrajet", Long.valueOf(idTrajetStr));
        }

        // Filtre par ville de départ
        String villeDepart = req.getParameter("villeDepart");
        if (villeDepart != null && !villeDepart.isEmpty()) {
            filters.put("nomVilleDepart", villeDepart);
        }

        // Filtre par ville d'arrivée
        String villeArrive = req.getParameter("villeArrive");
        if (villeArrive != null && !villeArrive.isEmpty()) {
            filters.put("nomVilleArrive", villeArrive);
        }

        // Filtre par immatriculation véhicule
        String vehicule = req.getParameter("vehicule");
        if (vehicule != null && !vehicule.isEmpty()) {
            filters.put("immatriculationVehicule", vehicule);
        }

        // Filtre par date de départ exacte
        String dateDepartStr = req.getParameter("dateDepart");
        if (dateDepartStr != null && !dateDepartStr.isEmpty()) {
            filters.put("dateDepart", LocalDate.parse(dateDepartStr));
        }

        // Filtre par plage de dates
        String dateDebut = req.getParameter("dateDebut");
        if (dateDebut != null && !dateDebut.isEmpty()) {
            filters.put("dateDepart>=", LocalDate.parse(dateDebut));
        }

        String dateFin = req.getParameter("dateFin");
        if (dateFin != null && !dateFin.isEmpty()) {
            filters.put("dateDepart<=", LocalDate.parse(dateFin));
        }

        // Filtre par heure de départ exacte
        String heureDepartStr = req.getParameter("heureDepart");
        if (heureDepartStr != null && !heureDepartStr.isEmpty()) {
            filters.put("heureDepart", LocalTime.parse(heureDepartStr));
        }

        // Filtre par plage d'heures
        String heureDebut = req.getParameter("heureDebut");
        if (heureDebut != null && !heureDebut.isEmpty()) {
            filters.put("heureDepart>=", LocalTime.parse(heureDebut));
        }

        String heureFin = req.getParameter("heureFin");
        if (heureFin != null && !heureFin.isEmpty()) {
            filters.put("heureDepart<=", LocalTime.parse(heureFin));
        }

        return filters;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Rediriger vers GET avec les paramètres de filtre
        String type = req.getParameter("type");
        if (type == null) type = "prevision";
        
        resp.sendRedirect(req.getContextPath() + "/chiffre-affaire?type=" + type + 
            "&" + req.getQueryString());
    }
}