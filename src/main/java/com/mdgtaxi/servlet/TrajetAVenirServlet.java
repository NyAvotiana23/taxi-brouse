package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.Ligne;
import com.mdgtaxi.entity.Trajet;
import com.mdgtaxi.service.LigneService;
import com.mdgtaxi.service.TrajetService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/trajets-a-venir")
public class TrajetAVenirServlet extends HttpServlet {
    private final TrajetService trajetService = new TrajetService();
    private final LigneService ligneService = new LigneService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDateTime now = LocalDateTime.now();
        List<Ligne> lignes = ligneService.getAllLignes();
        req.setAttribute("lignes", lignes);

        String ligneIdStr = req.getParameter("idLigne");
        String dateStr = req.getParameter("date");

        Long selectedLigneId = null;
        LocalDate selectedDate = null;
        List<Trajet> trajets;
        List<LocalDate> dates = null;

        if (ligneIdStr != null && !ligneIdStr.isEmpty()) {
            try {
                selectedLigneId = Long.parseLong(ligneIdStr);
                req.setAttribute("selectedLigneId", selectedLigneId);
                dates = trajetService.getDistinctUpcomingDatesByLigne(now, selectedLigneId);
                req.setAttribute("dates", dates);

                if (dateStr != null && !dateStr.isEmpty()) {
                    try {
                        selectedDate = LocalDate.parse(dateStr);
                        req.setAttribute("selectedDate", selectedDate);
                        trajets = trajetService.getUpcomingTrajetsByLigneAndDate(now, selectedLigneId, selectedDate);
                    } catch (DateTimeParseException e) {
                        trajets = trajetService.getUpcomingTrajetsByLigne(now, selectedLigneId);
                    }
                } else {
                    trajets = trajetService.getUpcomingTrajetsByLigne(now, selectedLigneId);
                }
            } catch (NumberFormatException e) {
                trajets = trajetService.getAllUpcomingTrajets(now);
            }
        } else {
            trajets = trajetService.getAllUpcomingTrajets(now);
        }

        req.setAttribute("trajets", trajets);
        req.getRequestDispatcher("/trajets-a-venir.jsp").forward(req, resp);
    }
}