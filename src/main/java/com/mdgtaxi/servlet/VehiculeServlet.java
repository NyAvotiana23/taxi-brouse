package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.MouvementStatusDto;
import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.CarburantType;
import com.mdgtaxi.entity.TypePlace;
import com.mdgtaxi.entity.Vehicule;
import com.mdgtaxi.entity.VehiculeEntretien;
import com.mdgtaxi.entity.VehiculeMouvementStatut;
import com.mdgtaxi.entity.VehiculeTarifTypePlace;
import com.mdgtaxi.entity.VehiculeType;
import com.mdgtaxi.service.MouvementStatusCreateService;
import com.mdgtaxi.service.MouvementStatusService;
import com.mdgtaxi.service.StatusService;
import com.mdgtaxi.service.TypeObjectService;
import com.mdgtaxi.service.VehiculeService;
import com.mdgtaxi.view.VmVehiculeCoutEntretien;
import com.mdgtaxi.view.VmVehiculeDetail;
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

@WebServlet("/vehicules")
public class VehiculeServlet extends HttpServlet {

    private final VehiculeService vehiculeService = new VehiculeService();
    private final TypeObjectService typeObjectService = new TypeObjectService();
    private final StatusService statusService = new StatusService();
    private final MouvementStatusService mouvementStatusService = new MouvementStatusService();
    private final MouvementStatusCreateService mouvementStatusCreateService = new MouvementStatusCreateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        // Load reference data
        loadReferenceData(req);

        if ("edit".equals(action)) {
            handleEdit(req);
            req.getRequestDispatcher("/vehicules-content.jsp").forward(req, resp);
            return;
        } else if ("detail".equals(action) && idParam != null) {
            handleDetail(req, Long.parseLong(idParam));
            req.getRequestDispatcher("/vehicule-detail-content.jsp").forward(req, resp);
            return;
        }

        // Handle filters
        Map<String, Object> filters = new HashMap<>();

        String marque = req.getParameter("filter_marque");
        if (marque != null && !marque.isEmpty()) {
            filters.put("marque", marque);
        }

        String modele = req.getParameter("filter_modele");
        if (modele != null && !modele.isEmpty()) {
            filters.put("modele", modele);
        }

        String immatriculation = req.getParameter("filter_immatriculation");
        if (immatriculation != null && !immatriculation.isEmpty()) {
            filters.put("immatriculation", immatriculation);
        }

        String maximumPassagerStr = req.getParameter("filter_maximumPassager");
        if (maximumPassagerStr != null && !maximumPassagerStr.isEmpty()) {
            try {
                Integer max = Integer.parseInt(maximumPassagerStr);
                filters.put("maximumPassager", max);
            } catch (NumberFormatException e) {
                // Ignore invalid number
            }
        }

        String capaciteCarburantStr = req.getParameter("filter_capaciteCarburant");
        if (capaciteCarburantStr != null && !capaciteCarburantStr.isEmpty()) {
            try {
                BigDecimal cap = new BigDecimal(capaciteCarburantStr);
                filters.put("capaciteCarburant", cap);
            } catch (NumberFormatException e) {
                // Ignore invalid number
            }
        }

        String depenseCarburant100kmStr = req.getParameter("filter_depenseCarburant100km");
        if (depenseCarburant100kmStr != null && !depenseCarburant100kmStr.isEmpty()) {
            try {
                BigDecimal dep = new BigDecimal(depenseCarburant100kmStr);
                filters.put("depenseCarburant100km", dep);
            } catch (NumberFormatException e) {
                // Ignore invalid number
            }
        }

        String vehiculeTypeLibelle = req.getParameter("filter_vehiculeType");
        if (vehiculeTypeLibelle != null && !vehiculeTypeLibelle.isEmpty()) {
            filters.put("vehiculeType.libelle", vehiculeTypeLibelle);
        }

        String carburantTypeLibelle = req.getParameter("filter_carburantType");
        if (carburantTypeLibelle != null && !carburantTypeLibelle.isEmpty()) {
            filters.put("carburantType.libelle", carburantTypeLibelle);
        }

        // Load vehicles with filters
        List<Vehicule> vehicules = filters.isEmpty() ? vehiculeService.getAllVehicules() : vehiculeService.searchVehiculesWithFilters(filters);
        req.setAttribute("vehicules", vehicules);

        req.getRequestDispatcher("/vehicules-content.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        if ("changeStatut".equals(action) && idParam != null) {
            handleChangeStatut(req, Long.parseLong(idParam));
            resp.sendRedirect(req.getContextPath() + "/vehicules/detail?id=" + idParam);
            return;
        } else if ("addEntretien".equals(action) && idParam != null) {
            handleAddEntretien(req, Long.parseLong(idParam));
            resp.sendRedirect(req.getContextPath() + "/vehicules/detail?id=" + idParam);
            return;
        }

        try {
            Vehicule vehicule = buildVehiculeFromRequest(req);

            if (vehicule.getId() == null) {
                vehicule = vehiculeService.createVehicule(vehicule);
            } else {
                vehicule = vehiculeService.updateVehicule(vehicule);
            }

            // Handle tarif type places
            handleTarifTypePlaces(req, vehicule);

            resp.sendRedirect(req.getContextPath() + "/vehicules");
        } catch (Exception e) {
            req.setAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            loadReferenceData(req);
            req.getRequestDispatcher("/vehicules-content.jsp").forward(req, resp);
        }
    }

    private void handleEdit(HttpServletRequest req) {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            Long id = Long.valueOf(idParam);
            Vehicule vehicule = vehiculeService.getVehiculeById(id);
            req.setAttribute("vehicule", vehicule);

            // Load tarif type places
            List<VehiculeTarifTypePlace> tarifPlaces = vehiculeService.getTarifTypePlacesByVehicule(id);
            for (VehiculeTarifTypePlace vttp : tarifPlaces) {
                if (vttp.getTypePlace().getId() == 1L) { // Premium
                    req.setAttribute("premiumPlaces", vttp.getNombrePlace());
                    req.setAttribute("premiumTarif", vttp.getTarifUnitaire());
                } else if (vttp.getTypePlace().getId() == 2L) { // Standard
                    req.setAttribute("standardPlaces", vttp.getNombrePlace());
                    req.setAttribute("standardTarif", vttp.getTarifUnitaire());
                }
            }
        }
    }

    private void handleDetail(HttpServletRequest req, Long id) {
        // Load vehicle detail
        VmVehiculeDetail detail = vehiculeService.getVehiculeDetail(id);
        req.setAttribute("detail", detail);

        // Load cost
        VmVehiculeCoutEntretien cout = vehiculeService.getCoutEntretien(id);
        req.setAttribute("cout", cout);

        // Load maintenances
        List<VehiculeEntretien> entretiens = vehiculeService.getEntretiensByVehicule(id);
        req.setAttribute("entretiens", entretiens);

        // Load current status
        MouvementStatusDto currentStatus = mouvementStatusService.getCurrentStatus("Vehicule", id);
        req.setAttribute("currentStatus", currentStatus);

        // Load status history
        List<MouvementStatusDto> statusHistory = mouvementStatusService.getStatusHistory("Vehicule", id);
        req.setAttribute("statusHistory", statusHistory);

        // Load available statuses
        List<StatusObjectDto> availableStatuts = statusService.findAllStatuses("Vehicule");
        req.setAttribute("availableStatuts", availableStatuts);

        // Load tarif type places
        List<VehiculeTarifTypePlace> tarifPlaces = vehiculeService.getTarifTypePlacesByVehicule(id);
        req.setAttribute("tarifPlaces", tarifPlaces);

        // Load max CA
        double maxCA = vehiculeService.getTotalMaxChiffreAffairePossible(id);
        req.setAttribute("maxCA", maxCA);
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<TypeObjectDTO> vehiculeTypes = typeObjectService.findAllTypeObject("Vehicule_Type");
        List<TypeObjectDTO> carburantTypes = typeObjectService.findAllTypeObject("Carburant_Type");
        List<StatusObjectDto> vehiculeStatuts = statusService.findAllStatuses("Vehicule");
        List<TypeObjectDTO> typePlaces = typeObjectService.findAllTypeObject("Type_Place"); // Assuming this exists

        req.setAttribute("vehiculeTypes", vehiculeTypes);
        req.setAttribute("carburantTypes", carburantTypes);
        req.setAttribute("vehiculeStatuts", vehiculeStatuts);
        req.setAttribute("typePlaces", typePlaces);
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

    private void handleTarifTypePlaces(HttpServletRequest req, Vehicule vehicule) {
        // Assuming fixed types: 1=Premium, 2=Standard
        handleTarifTypePlace(req, vehicule, 1L, "premiumPlaces", "premiumTarif", 140000.0);
        handleTarifTypePlace(req, vehicule, 2L, "standardPlaces", "standardTarif", 80000.0);
    }

    private void handleTarifTypePlace(HttpServletRequest req, Vehicule vehicule, Long typeId, String placesParam, String tarifParam, double defaultTarif) {
        String placesStr = req.getParameter(placesParam);
        String tarifStr = req.getParameter(tarifParam);

        if (placesStr != null && !placesStr.isEmpty()) {
            double nombrePlaces = Double.parseDouble(placesStr);
            double tarif = tarifStr != null && !tarifStr.isEmpty() ? Double.parseDouble(tarifStr) : defaultTarif;

            VehiculeTarifTypePlace vttp = vehiculeService.getTarifTypePlaceByVehiculeAndType(vehicule.getId(), typeId);
            if (vttp == null) {
                vttp = new VehiculeTarifTypePlace();
                vttp.setVehicule(vehicule);
                TypePlace typePlace = new TypePlace();
                typePlace.setId(typeId);
                vttp.setTypePlace(typePlace);
            }
            vttp.setNombrePlace(nombrePlaces);
            vttp.setTarifUnitaire(tarif);

            vehiculeService.createOrUpdateTarifTypePlace(vttp);
        }
    }

    private void handleChangeStatut(HttpServletRequest req, Long vehiculeId) {
        Long idStatut = Long.valueOf(req.getParameter("idStatut"));
        String observation = req.getParameter("observation");
        String dateChangementStr = req.getParameter("dateChangement");

        LocalDateTime dateChangement;
        if (dateChangementStr != null && !dateChangementStr.isEmpty()) {
            dateChangement = LocalDateTime.parse(dateChangementStr);
        } else {
            dateChangement = LocalDateTime.now();
        }

        mouvementStatusCreateService.createMouvementStatut(
                "Vehicule",
                vehiculeId,
                idStatut,
                dateChangement,
                observation
        );
    }

    private void handleAddEntretien(HttpServletRequest req, Long vehiculeId) {
        String motif = req.getParameter("motif");
        String montantStr = req.getParameter("montantDepense");
        String dateDebutStr = req.getParameter("dateDebutEntretien");
        String dateFinStr = req.getParameter("dateFinEntretien");

        VehiculeEntretien entretien = new VehiculeEntretien();
        Vehicule vehicule = new Vehicule();
        vehicule.setId(vehiculeId);
        entretien.setVehicule(vehicule);
        entretien.setMotif(motif);
        entretien.setMontantDepense(new BigDecimal(montantStr));

        if (dateDebutStr != null && !dateDebutStr.isEmpty()) {
            entretien.setDateDebutEntretien(LocalDateTime.parse(dateDebutStr));
        } else {
            entretien.setDateDebutEntretien(LocalDateTime.now());
        }

        if (dateFinStr != null && !dateFinStr.isEmpty()) {
            entretien.setDateFinEntretien(LocalDateTime.parse(dateFinStr));
        }

        vehiculeService.addEntretien(entretien);
    }
}