// Add this TrajetServlet to servlets.txt

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/trajets")
public class TrajetServlet extends HttpServlet {

    private final TrajetService trajetService = new TrajetService();
    private final LigneService ligneService = new LigneService(); // Assume exists
    private final ChauffeurService chauffeurService = new ChauffeurService();
    private final VehiculeService vehiculeService = new VehiculeService();
    private final TypeObjectService typeObjectService = new TypeObjectService();
    private final ClientService clientService = new ClientService(); // Assume exists

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        loadReferenceData(req);

        if ("edit".equals(action)) {
            if (id != null) {
                Trajet trajet = trajetService.getTrajetById(id);
                req.setAttribute("trajet", trajet);
            }
        } else if ("detail".equals(action)) {
            if (id != null) {
                handleDetail(req, id);
                req.getRequestDispatcher("/trajet-detail.jsp").forward(req, resp);
                return;
            }
        }

        // Filters
        Map<String, Object> filters = collectFilters(req);

        List<Trajet> trajets = filters.isEmpty() ? trajetService.getAllTrajets() : trajetService.searchTrajetsWithFilters(filters); // Assume method
        req.setAttribute("trajets", trajets);

        req.getRequestDispatcher("/trajets.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        try {
            if (action == null || "save".equals(action)) {
                Trajet trajet = buildTrajetFromRequest(req);
                trajet = trajetService.createTrajet(trajet); // Assume method
                resp.sendRedirect(req.getContextPath() + "/trajets");
            } else {
                resp.sendRedirect(req.getContextPath() + "/trajets");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Erreur: " + e.getMessage());
            if (id != null) {
                handleDetail(req, id);
                req.getRequestDispatcher("/trajet-detail.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/trajets.jsp").forward(req, resp);
            }
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<Ligne> lignes = ligneService.getAllLignes(); // Assume method
        List<Chauffeur> chauffeurs = chauffeurService.getAllChauffeurs();
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        List<TypeObjectDTO> trajetStatuts = typeObjectService.findAllTypeObject("Trajet_Statut");
        List<Client> clients = clientService.getAllClients(); // For new reservation
        List<TypeObjectDTO> reservationStatuts = typeObjectService.findAllTypeObject("Reservation_Statut");

        req.setAttribute("lignes", lignes);
        req.setAttribute("chauffeurs", chauffeurs);
        req.setAttribute("vehicules", vehicules);
        req.setAttribute("trajetStatuts", trajetStatuts);
        req.setAttribute("clients", clients);
        req.setAttribute("reservationStatuts", reservationStatuts);
    }

    private Map<String, Object> collectFilters(HttpServletRequest req) {
        Map<String, Object> filters = new HashMap<>();

        String ligneId = req.getParameter("ligneId");
        if (ligneId != null && !ligneId.isEmpty()) filters.put("ligne.id", Long.valueOf(ligneId));

        String chauffeurId = req.getParameter("chauffeurId");
        if (chauffeurId != null && !chauffeurId.isEmpty()) filters.put("chauffeur.id", Long.valueOf(chauffeurId));

        String vehiculeId = req.getParameter("vehiculeId");
        if (vehiculeId != null && !vehiculeId.isEmpty()) filters.put("vehicule.id", Long.valueOf(vehiculeId));

        String statutId = req.getParameter("statutId");
        if (statutId != null && !statutId.isEmpty()) filters.put("trajetStatut.id", Long.valueOf(statutId));

        String startDateStr = req.getParameter("startDate");
        if (startDateStr != null && !startDateStr.isEmpty()) filters.put("datetimeDepart >= ", LocalDate.parse(startDateStr).atStartOfDay());

        String endDateStr = req.getParameter("endDate");
        if (endDateStr != null && !endDateStr.isEmpty()) filters.put("datetimeDepart <= ", LocalDate.parse(endDateStr).atTime(23, 59, 59));

        return filters;
    }

    private void handleDetail(HttpServletRequest req, Long id) {
        Trajet trajet = trajetService.getTrajetById(id);
        req.setAttribute("trajet", trajet);

        Vehicule vehicule = trajet.getVehicule();
        List<VehiculeTarifTypePlace> tarifPlaces = vehiculeService.getTarifTypePlacesByVehicule(vehicule.getId());
        req.setAttribute("tarifPlaces", tarifPlaces);


        List<TrajetTarifTypePlaceCategorieRemise> trajetTarifTypePlaceCategorieRemises = trajetService.getTarifTypePlaceCategorieRemisesByTrajetId(id);
        req.setAttribute("tarifTypePlaceCategorieRemises", trajetTarifTypePlaceCategorieRemises);


        Map<Long, Double> soldPerType = trajetService.getSoldPlacesPerType(id);
        req.setAttribute("soldPerType", soldPerType);

        List<TrajetReservation> reservations = trajetService.getReservationsByTrajet(id);
        req.setAttribute("reservations", reservations);

        // Other data like clients, reservationStatuts already loaded in loadReferenceData
    }

    private Trajet buildTrajetFromRequest(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        Trajet trajet = (idStr != null && !idStr.isEmpty()) ? trajetService.getTrajetById(Long.valueOf(idStr)) : new Trajet();

        Ligne ligne = new Ligne();
        ligne.setId(Long.valueOf(req.getParameter("idLigne")));
        trajet.setLigne(ligne);

        Chauffeur chauffeur = new Chauffeur();
        chauffeur.setId(Long.valueOf(req.getParameter("idChauffeur")));
        trajet.setChauffeur(chauffeur);

        Vehicule vehicule = new Vehicule();
        vehicule.setId(Long.valueOf(req.getParameter("idVehicule")));
        trajet.setVehicule(vehicule);

        trajet.setDatetimeDepart(LocalDateTime.parse(req.getParameter("datetimeDepart")));

        String datetimeArriveeStr = req.getParameter("datetimeArrivee");
        if (datetimeArriveeStr != null && !datetimeArriveeStr.isEmpty()) {
            trajet.setDatetimeArrivee(LocalDateTime.parse(datetimeArriveeStr));
        }

        TrajetStatut statut = new TrajetStatut();
        statut.setId(Long.valueOf(req.getParameter("idTrajetStatut")));
        trajet.setTrajetStatut(statut);

        return trajet;
    }
}