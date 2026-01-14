package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.CarburantType;
import com.mdgtaxi.entity.Vehicule;
import com.mdgtaxi.entity.VehiculeType;
import com.mdgtaxi.service.StatusService;
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
    private final StatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Load reference data for dropdowns
        loadReferenceData(req);

        if ("edit".equals(action)) {
            handleEdit(req);
        } else if ("view".equals(action)) {
            handleView(req, resp);
            return;
        }

        // Load all vehicles for the list
        List<Vehicule> vehicules = vehiculeService.getAllVehicules();
        req.setAttribute("vehicules", vehicules);

        req.getRequestDispatcher("/vehicules.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Vehicule vehicule = buildVehiculeFromRequest(req);

            if (vehicule.getId() == null) {
                vehiculeService.createVehicule(vehicule);
            } else {
                vehiculeService.updateVehicule(vehicule);
            }

            resp.sendRedirect(req.getContextPath() + "/vehicules");
        } catch (Exception e) {
            req.setAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            loadReferenceData(req);
            req.getRequestDispatcher("/vehicules.jsp").forward(req, resp);
        }
    }

    private void handleEdit(HttpServletRequest req) {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.valueOf(idParam);
            Vehicule vehicule = vehiculeService.getVehiculeById(id);
            req.setAttribute("vehicule", vehicule);
        }
    }

    private void handleView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            req.setAttribute("id", idParam);
            req.getRequestDispatcher("/vehicule-detail.jsp").forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/vehicules");
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<TypeObjectDTO> vehiculeTypes = typeObjectService.findAllTypeObject("Vehicule_Type");
        List<TypeObjectDTO> carburantTypes = typeObjectService.findAllTypeObject("Carburant_Type");
        List<StatusObjectDto> vehiculeStatuts = statusService.findAllStatuses("Vehicule");

        req.setAttribute("vehiculeTypes", vehiculeTypes);
        req.setAttribute("carburantTypes", carburantTypes);
        req.setAttribute("vehiculeStatuts", vehiculeStatuts);
    }

    private Vehicule buildVehiculeFromRequest(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        Vehicule vehicule;

        if (idStr != null && !idStr.isEmpty()) {
            vehicule = vehiculeService.getVehiculeById(Long.valueOf(idStr));
        } else {
            vehicule = new Vehicule();
        }

        // Set basic fields
        vehicule.setMarque(req.getParameter("marque"));
        vehicule.setModele(req.getParameter("modele"));
        vehicule.setImmatriculation(req.getParameter("immatriculation"));
        vehicule.setMaximumPassager(Integer.valueOf(req.getParameter("maximumPassager")));

        // Set optional numeric fields
        String capaciteCarburant = req.getParameter("capaciteCarburant");
        if (capaciteCarburant != null && !capaciteCarburant.isEmpty()) {
            vehicule.setCapaciteCarburant(new BigDecimal(capaciteCarburant));
        }

        String depenseCarburant100km = req.getParameter("depenseCarburant100km");
        if (depenseCarburant100km != null && !depenseCarburant100km.isEmpty()) {
            vehicule.setDepenseCarburant100km(new BigDecimal(depenseCarburant100km));
        }

        // Set relationships
        VehiculeType vehiculeType = new VehiculeType();
        vehiculeType.setId(Long.valueOf(req.getParameter("idType")));
        vehicule.setVehiculeType(vehiculeType);

        CarburantType carburantType = new CarburantType();
        carburantType.setId(Long.valueOf(req.getParameter("idTypeCarburant")));
        vehicule.setCarburantType(carburantType);

        return vehicule;
    }
}