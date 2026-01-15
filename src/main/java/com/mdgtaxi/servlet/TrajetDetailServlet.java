package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.Client;
import com.mdgtaxi.entity.LigneDetail;
import com.mdgtaxi.entity.Trajet;
import com.mdgtaxi.entity.TrajetReservation;
import com.mdgtaxi.service.*;
import com.mdgtaxi.util.ExceptionUtil;
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
    private final LigneService ligneService = new LigneService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Trajet trajet = trajetService.getTrajetById(id);

        ExceptionUtil.throwExceptionOnObjectNull(trajet);



        List<LigneDetail> ligneDetails = ligneService.getLigneDetailList(trajet.getLigne().getId());


        List<TrajetReservation> reservations = reservationService.getReservationsByTrajetId(id);
        int placesPrises = reservationService.getPlacesPrisesForTrajet(id);
        int placesRestantes = trajet.getVehicule().getMaximumPassager() - placesPrises;
        List<Client> clients = clientService.getAllClients();  // If needed for form, etc.
        List<TypeObjectDTO> reservationStatuts = typeObjectService.findAllTypeObject("Reservation_Statut");

        req.setAttribute("trajet", trajet);
        req.setAttribute("placesPrises", placesPrises);
        req.setAttribute("placesRestantes", placesRestantes);
        req.setAttribute("reservations", reservations);
        req.setAttribute("clients", clients);
        req.setAttribute("reservationStatuts", reservationStatuts);
        req.setAttribute("ligneDetails", ligneDetails);


        req.getRequestDispatcher("/trajet-detail.jsp").forward(req, resp);
    }
}