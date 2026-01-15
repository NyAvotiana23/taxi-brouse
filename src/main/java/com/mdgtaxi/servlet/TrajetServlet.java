package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/trajets")
public class TrajetServlet extends HttpServlet {

    private final TrajetService trajetService = new TrajetService();
    private final LigneService ligneService = new LigneService();
    private final ChauffeurService chauffeurService = new ChauffeurService();
    private final VehiculeService vehiculeService = new VehiculeService();
    private final TypeObjectService typeObjectService = new TypeObjectService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            Long id = Long.valueOf(req.getParameter("id"));
            Trajet trajet = trajetService.getTrajetById(id);
            req.setAttribute("trajet", trajet);
        }

        // Build filters map from request parameters
        Map<String, Object> filters = new HashMap<>();

        String idLigne = req.getParameter("idLigne");
        if (idLigne != null && !idLigne.isEmpty()) {
            filters.put("ligne.id", Long.valueOf(idLigne));
        }

        String idChauffeur = req.getParameter("idChauffeur");
        if (idChauffeur != null && !idChauffeur.isEmpty()) {
            filters.put("chauffeur.id", Long.valueOf(idChauffeur));
        }

        String idVehicule = req.getParameter("idVehicule");
        if (idVehicule != null && !idVehicule.isEmpty()) {
            filters.put("vehicule.id", Long.valueOf(idVehicule));
        }

        String idTrajetStatut = req.getParameter("idTrajetStatut");
        if (idTrajetStatut != null && !idTrajetStatut.isEmpty()) {
            filters.put("trajetStatut.id", Long.valueOf(idTrajetStatut));
        }

        String startDate = req.getParameter("startDate");
        if (startDate != null && !startDate.isEmpty()) {
            filters.put("datetimeDepart>=", LocalDateTime.parse(startDate + "T00:00:00"));
        }

        String endDate = req.getParameter("endDate");
        if (endDate != null && !endDate.isEmpty()) {
            filters.put("datetimeDepart<=", LocalDateTime.parse(endDate + "T23:59:59"));
        }

        // Get filtered or all trajets
        List<Trajet> trajets;
        if (!filters.isEmpty()) {
            trajets = trajetService.searchTrajetsWithFilters(filters);
        } else {
            trajets = trajetService.getAllTrajets();
        }

        List<Ligne> lignes = ligneService.getAllLignes();
        List<Chauffeur> chauffeurs = chauffeurService.getAllChauffeurs();
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        List<TypeObjectDTO> trajetStatuts = typeObjectService.findAllTypeObject("Trajet_Statut");

        req.setAttribute("trajets", trajets);
        req.setAttribute("lignes", lignes);
        req.setAttribute("chauffeurs", chauffeurs);
        req.setAttribute("vehicules", vehicules);
        req.setAttribute("trajetStatuts", trajetStatuts);

        req.getRequestDispatcher("/trajets.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Long idLigne = Long.valueOf(req.getParameter("idLigne"));
        Long idChauffeur = Long.valueOf(req.getParameter("idChauffeur"));
        Long idVehicule = Long.valueOf(req.getParameter("idVehicule"));
        LocalDateTime datetimeDepart = LocalDateTime.parse(req.getParameter("datetimeDepart"));
        LocalDateTime datetimeArrivee = req.getParameter("datetimeArrivee") != null && !req.getParameter("datetimeArrivee").isEmpty()
                ? LocalDateTime.parse(req.getParameter("datetimeArrivee")) : null;
        Long idTrajetStatut = Long.valueOf(req.getParameter("idTrajetStatut"));
        BigDecimal fraisUnitaire = new BigDecimal(req.getParameter("fraisUnitaire"));
        Integer nombrePassager = req.getParameter("nombrePassager") != null && !req.getParameter("nombrePassager").isEmpty()
                ? Integer.valueOf(req.getParameter("nombrePassager")) : 0;

        Trajet trajet = new Trajet();
        if (idStr != null && !idStr.isEmpty()) {
            trajet = trajetService.getTrajetById(Long.valueOf(idStr));
        }

        Ligne ligne = new Ligne();
        ligne.setId(idLigne);
        trajet.setLigne(ligne);

        Chauffeur chauffeur = new Chauffeur();
        chauffeur.setId(idChauffeur);
        trajet.setChauffeur(chauffeur);

        Vehicule vehicule = new Vehicule();
        vehicule.setId(idVehicule);
        trajet.setVehicule(vehicule);

        trajet.setDatetimeDepart(datetimeDepart);
        trajet.setDatetimeArrivee(datetimeArrivee);

        TrajetStatut statut = new TrajetStatut();
        statut.setId(idTrajetStatut);
        trajet.setTrajetStatut(statut);

        trajet.setNombrePassager(nombrePassager);

        if (trajet.getId() == null) {
            trajetService.createTrajet(trajet);
        } else {
            trajetService.updateTrajet(trajet);
        }

        resp.sendRedirect(req.getContextPath() + "/trajets");
    }
}