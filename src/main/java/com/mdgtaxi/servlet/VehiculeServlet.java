package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.CarburantType;
import com.mdgtaxi.entity.Vehicule;
import com.mdgtaxi.entity.VehiculeType;
import com.mdgtaxi.service.TypeObjectService;
import com.mdgtaxi.service.VehiculeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/vehicules")
public class VehiculeServlet extends HttpServlet {

    private final VehiculeService vehiculeService = new VehiculeService();
    private final TypeObjectService typeObjectService = new TypeObjectService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            Long id = Long.valueOf(req.getParameter("id"));
            Vehicule vehicule = vehiculeService.getVehiculeById(id);
            req.setAttribute("vehicule", vehicule);
        }

        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        List<TypeObjectDTO> vehiculeTypes = typeObjectService.findAllTypeObject("Vehicule_Type");
        List<TypeObjectDTO> carburantTypes = typeObjectService.findAllTypeObject("Carburant_Type");

        req.setAttribute("vehicules", vehicules);
        req.setAttribute("vehiculeTypes", vehiculeTypes);
        req.setAttribute("carburantTypes", carburantTypes);

        req.getRequestDispatcher("/vehicules.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String marque = req.getParameter("marque");
        String modele = req.getParameter("modele");
        String immatriculation = req.getParameter("immatriculation");
        Integer maximumPassager = Integer.valueOf(req.getParameter("maximumPassager"));
        BigDecimal capaciteCarburant = new BigDecimal(req.getParameter("capaciteCarburant"));
        BigDecimal depenseCarburant100km = new BigDecimal(req.getParameter("depenseCarburant100km"));
        Long idType = Long.valueOf(req.getParameter("idType"));
        Long idTypeCarburant = Long.valueOf(req.getParameter("idTypeCarburant"));

        Vehicule vehicule = new Vehicule();
        if (idStr != null && !idStr.isEmpty()) {
            vehicule = vehiculeService.getVehiculeById(Long.valueOf(idStr));
        }

        vehicule.setMarque(marque);
        vehicule.setModele(modele);
        vehicule.setImmatriculation(immatriculation);
        vehicule.setMaximumPassager(maximumPassager);
        vehicule.setCapaciteCarburant(capaciteCarburant);
        vehicule.setDepenseCarburant100km(depenseCarburant100km);

        VehiculeType type = new VehiculeType();
        type.setId(idType);
        vehicule.setVehiculeType(type);

        CarburantType carburantType = new CarburantType();
        carburantType.setId(idTypeCarburant);
        vehicule.setCarburantType(carburantType);

        if (vehicule.getId() == null) {
            vehiculeService.createVehicule(vehicule);
        } else {
            vehiculeService.updateVehicule(vehicule);
        }

        resp.sendRedirect(req.getContextPath() + "/vehicules");
    }
}