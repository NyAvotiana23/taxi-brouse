package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.*;
import com.mdgtaxi.service.*;
import com.mdgtaxi.util.ExceptionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/trajets/detail")
public class TrajetDetailServlet extends HttpServlet {

    private final TrajetService trajetService = new TrajetService();
    private final ReservationService reservationService = new ReservationService();
    private final ClientService clientService = new ClientService();
    private final TypeObjectService typeObjectService = new TypeObjectService();
    private final LigneService ligneService = new LigneService();


    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long idClient = Long.valueOf(req.getParameter("idClient"));
        Long trajetId = Long.valueOf(req.getParameter("trajetId"));
        String nomPassager = req.getParameter("nomPassager");
        String datestr = req.getParameter("datereservation");
        LocalDateTime date = LocalDateTime.parse(datestr);
        Long idReservationStatut = Long.valueOf(req.getParameter("idReservationStatut"));

        Trajet trajet = new Trajet();
        trajet.setId(trajetId);

        ReservationStatut reservationStatut = new ReservationStatut();
        reservationStatut.setId(idReservationStatut);

        Client c =  new Client();
        c.setId(idClient);

        TrajetReservation trajetReservation = new TrajetReservation();
        trajetReservation.setTrajet(trajet);
        trajetReservation.setDateReservation(date);
        trajetReservation.setReservationStatut(reservationStatut);
        trajetReservation.setNomPassager(nomPassager);
        trajetReservation.setClient(c);

        trajetReservation = reservationService.createReservation(trajetReservation);

        resp.sendRedirect(req.getContextPath() + "/reservations/detail?id=" + trajetReservation.getId());

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Trajet trajet = trajetService.getTrajetById(id);

        ExceptionUtil.throwExceptionOnObjectNull(trajet);


        List<LigneDetail> ligneDetails = ligneService.getLigneDetailList(trajet.getLigne().getId());


        List<TrajetReservation> reservations = reservationService.getReservationsByTrajetId(id);
        double placesPrises = reservationService.getPlacesPrisesForTrajet(id);
        double placesRestantes = trajet.getVehicule().getMaximumPassager() - placesPrises;

        double caPrevisionnel = reservationService.getCAprevisionnel(id);

        List<Client> clients = clientService.getAllClients();  // If needed for form, etc.
        List<TypeObjectDTO> reservationStatuts = typeObjectService.findAllTypeObject("Reservation_Statut");

        req.setAttribute("trajet", trajet);
        req.setAttribute("placesPrises", placesPrises);
        req.setAttribute("placesRestantes", placesRestantes);
        req.setAttribute("reservations", reservations);
        req.setAttribute("clients", clients);
        req.setAttribute("reservationStatuts", reservationStatuts);
        req.setAttribute("ligneDetails", ligneDetails);
        req.setAttribute("caPrevisionnel", caPrevisionnel);



        req.getRequestDispatcher("/trajet-detail.jsp").forward(req, resp);
    }
}