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

        List<Trajet> trajets = trajetService.getAllTrajets();
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
