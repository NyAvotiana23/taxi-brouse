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
import java.util.List;

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

        // Récupérer les paramètres de filtre
        String idLigneStr = req.getParameter("filterLigne");
        String idChauffeurStr = req.getParameter("filterChauffeur");
        String idVehiculeStr = req.getParameter("filterVehicule");
        String idTrajetStatutStr = req.getParameter("filterStatut");
        String dateDebut = req.getParameter("filterDateDebut");
        String dateFin = req.getParameter("filterDateFin");
        String minScoreStr = req.getParameter("filterScore");

        // Convertir les paramètres en Long si présents
        Long idLigne = (idLigneStr != null && !idLigneStr.isEmpty()) ? Long.valueOf(idLigneStr) : null;
        Long idChauffeur = (idChauffeurStr != null && !idChauffeurStr.isEmpty()) ? Long.valueOf(idChauffeurStr) : null;
        Long idVehicule = (idVehiculeStr != null && !idVehiculeStr.isEmpty()) ? Long.valueOf(idVehiculeStr) : null;
        Long idTrajetStatut = (idTrajetStatutStr != null && !idTrajetStatutStr.isEmpty())
                ? Long.valueOf(idTrajetStatutStr)
                : null;
        Integer minScore = (minScoreStr != null && !minScoreStr.isEmpty()) ? Integer.valueOf(minScoreStr) : null;

        // Utiliser le filtre si au moins un critère est présent
        List<Trajet> trajets;
        boolean hasFilters = idLigne != null || idChauffeur != null || idVehicule != null ||
                idTrajetStatut != null || (dateDebut != null && !dateDebut.isEmpty()) ||
                (dateFin != null && !dateFin.isEmpty()) || minScore != null;

        if (hasFilters) {
            trajets = trajetService.getFilteredTrajets(idLigne, idChauffeur, idVehicule, idTrajetStatut, dateDebut,
                    dateFin, minScore);
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

        // Passer les paramètres de filtre à la JSP pour maintenir l'état
        req.setAttribute("filterLigne", idLigneStr);
        req.setAttribute("filterChauffeur", idChauffeurStr);
        req.setAttribute("filterVehicule", idVehiculeStr);
        req.setAttribute("filterStatut", idTrajetStatutStr);
        req.setAttribute("filterDateDebut", dateDebut);
        req.setAttribute("filterDateFin", dateFin);
        req.setAttribute("filterScore", minScoreStr);

        req.getRequestDispatcher("/trajets.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Long idLigne = Long.valueOf(req.getParameter("idLigne"));
        Long idChauffeur = Long.valueOf(req.getParameter("idChauffeur"));
        Long idVehicule = Long.valueOf(req.getParameter("idVehicule"));
        Long idTrajetStatut = Long.valueOf(req.getParameter("idTrajetStatut"));
        LocalDateTime datetimeDepart = LocalDateTime.parse(req.getParameter("datetimeDepart"));
        String datetimeArriveeStr = req.getParameter("datetimeArrivee");
        LocalDateTime datetimeArrivee = (datetimeArriveeStr != null && !datetimeArriveeStr.isEmpty())
                ? LocalDateTime.parse(datetimeArriveeStr)
                : null;
        Integer nombrePassager = Integer.valueOf(req.getParameter("nombrePassager"));
        BigDecimal fraisUnitaire = new BigDecimal(req.getParameter("fraisUnitaire"));

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

        TrajetStatut statut = new TrajetStatut();
        statut.setId(idTrajetStatut);
        trajet.setTrajetStatut(statut);

        trajet.setDatetimeDepart(datetimeDepart);
        trajet.setDatetimeArrivee(datetimeArrivee);
        trajet.setNombrePassager(nombrePassager);
        trajet.setFraisUnitaire(fraisUnitaire);

        if (trajet.getId() == null) {
            trajetService.createTrajet(trajet);
        } else {
            trajetService.updateTrajet(trajet);
        }

        resp.sendRedirect(req.getContextPath() + "/trajets");
    }
}
