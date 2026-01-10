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
import java.util.List;

@WebServlet("/trajets/detail")
public class TrajetDetailServlet extends HttpServlet {

    private final TrajetService trajetService = new TrajetService();
    private final ReservationService reservationService = new ReservationService();
    private final ClientService clientService = new ClientService();
    private final TypeObjectService typeObjectService = new TypeObjectService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/trajets");
            return;
        }

        Long trajetId = Long.valueOf(idStr);
        Trajet trajet = trajetService.getTrajetById(trajetId);

        if (trajet == null) {
            resp.sendRedirect(req.getContextPath() + "/trajets");
            return;
        }

        int placesRestantes = reservationService.getPlacesRestantesForTrajet(trajetId);
        int placesPrises = reservationService.getPlacesPrisesForTrajet(trajetId);

        List<TrajetReservation> reservations = reservationService.getReservationsByTrajetId(trajetId);
        List<Client> clients = clientService.getAllClients();
        List<TypeObjectDTO> reservationStatuts = typeObjectService.findAllTypeObject("Reservation_Status");

        req.setAttribute("trajet", trajet);
        req.setAttribute("reservations", reservations);
        req.setAttribute("clients", clients);
        req.setAttribute("reservationStatuts", reservationStatuts);
        req.setAttribute("placesRestantes", placesRestantes);
        req.setAttribute("placesPrises", placesPrises);

        req.getRequestDispatcher("/WEB-INF/trajet/trajet-detail.jsp").forward(req, resp);
    }
}
