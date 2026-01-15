package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.MouvementStatusDto;
import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.CarburantType;
import com.mdgtaxi.entity.TypePlace;
import com.mdgtaxi.entity.Vehicule;
import com.mdgtaxi.entity.VehiculeEntretien;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/vehicules")
public class VehiculeServlet extends HttpServlet {

    private final VehiculeService vehiculeService = new VehiculeService();
    private final TypeObjectService typeObjectService = new TypeObjectService();
    private final MouvementStatusService mouvementStatusService = new MouvementStatusService();
    private final MouvementStatusCreateService mouvementStatusCreateService = new MouvementStatusCreateService();
    private final StatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Load reference data
        loadReferenceData(req);

        if ("edit".equals(action)) {
            handleEdit(req);
            // Load tarif map for pre-filling
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                Long id = Long.valueOf(idStr);
                List<VehiculeTarifTypePlace> tarifPlaces = vehiculeService.getTarifTypePlacesByVehicule(id);
                Map<Long, VehiculeTarifTypePlace> tarifMap = new HashMap<>();
                for (VehiculeTarifTypePlace vttp : tarifPlaces) {
                    tarifMap.put(vttp.getTypePlace().getId(), vttp);
                }
                req.setAttribute("tarifMap", tarifMap);
            }
        } else if ("detail".equals(action)) {
            handleDetail(req, req.getParameter("id"));
            req.getRequestDispatcher("/vehicule-detail.jsp").forward(req, resp);
            return;
        }

        // Collect filters
        Map<String, Object> filters = collectFilters(req);

        // Load vehicules with filters
        List<Vehicule> vehicules = filters.isEmpty() ? vehiculeService.getAllVehicules() : vehiculeService.searchVehiculesWithFilters(filters);
        req.setAttribute("vehicules", vehicules);

        req.getRequestDispatcher("/vehicules.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        Long id = idStr != null && !idStr.isEmpty() ? Long.valueOf(idStr) : null;

        try {
            if ("save".equals(action)) {
                Vehicule vehicule = buildVehiculeFromRequest(req);
                vehicule = vehiculeService.createVehicule(vehicule);

                // Handle dynamic tarif type places
                List<TypeObjectDTO> typePlaces = typeObjectService.findAllTypeObject("Type_Place");
                for (TypeObjectDTO type : typePlaces) {
                    Long typeId = type.getId();
                    String placesStr = req.getParameter("places_" + typeId);
                    String tarifStr = req.getParameter("tarif_" + typeId);

                    if (placesStr != null && !placesStr.trim().isEmpty()) {
                        try {
                            double nombrePlaces = Double.parseDouble(placesStr);
                            if (nombrePlaces > 0) {
                                double tarif = (tarifStr != null && !tarifStr.trim().isEmpty())
                                        ? Double.parseDouble(tarifStr)
                                        : 0.0;  // Default to 0 if not provided

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
                        } catch (NumberFormatException e) {
                            // Optional: log or handle invalid input
                        }
                    }
                }

                resp.sendRedirect(req.getContextPath() + "/vehicules");
            } else if ("changeStatut".equals(action)) {
                handleChangeStatut(req, id);
                resp.sendRedirect(req.getContextPath() + "/vehicules/detail?id=" + id);
            } else if ("addEntretien".equals(action)) {
                handleAddEntretien(req, id);
                resp.sendRedirect(req.getContextPath() + "/vehicules/detail?id=" + id);
            } else {
                resp.sendRedirect(req.getContextPath() + "/vehicules");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Erreur: " + e.getMessage());
            if (id != null) {
                handleDetail(req, idStr);
                req.getRequestDispatcher("/vehicule-detail.jsp").forward(req, resp);
            } else {
                loadReferenceData(req);
                req.setAttribute("vehicule", buildVehiculeFromRequest(req)); // To repopulate form
                req.getRequestDispatcher("/vehicules.jsp").forward(req, resp);
            }
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<TypeObjectDTO> vehiculeTypes = typeObjectService.findAllTypeObject("Vehicule_Type");
        List<TypeObjectDTO> carburantTypes = typeObjectService.findAllTypeObject("Carburant_Type");
        List<StatusObjectDto> vehiculeStatuts = statusService.findAllStatuses("Vehicule");


        List<TypePlace> typePlaces = vehiculeService.getAllTypePlaces();

        req.setAttribute("vehiculeTypes", vehiculeTypes);
        req.setAttribute("carburantTypes", carburantTypes);
        req.setAttribute("vehiculeStatuts", vehiculeStatuts);
        req.setAttribute("typePlaces", typePlaces);
    }

    private Map<String, Object> collectFilters(HttpServletRequest req) {
        Map<String, Object> filters = new HashMap<>();

        String marque = req.getParameter("filter_marque");
        if (marque != null && !marque.isEmpty()) filters.put("marque", marque);

        String modele = req.getParameter("filter_modele");
        if (modele != null && !modele.isEmpty()) filters.put("modele", modele);

        String immatriculation = req.getParameter("filter_immatriculation");
        if (immatriculation != null && !immatriculation.isEmpty()) filters.put("immatriculation", immatriculation);

        String maxPassagerStr = req.getParameter("filter_maximumPassager");
        if (maxPassagerStr != null && !maxPassagerStr.isEmpty()) {
            try {
                filters.put("maximumPassager", Integer.parseInt(maxPassagerStr));
            } catch (NumberFormatException ignored) {}
        }

        String capaciteStr = req.getParameter("filter_capaciteCarburant");
        if (capaciteStr != null && !capaciteStr.isEmpty()) {
            try {
                filters.put("capaciteCarburant", new BigDecimal(capaciteStr));
            } catch (NumberFormatException ignored) {}
        }

        String depenseStr = req.getParameter("filter_depenseCarburant100km");
        if (depenseStr != null && !depenseStr.isEmpty()) {
            try {
                filters.put("depenseCarburant100km", new BigDecimal(depenseStr));
            } catch (NumberFormatException ignored) {}
        }

        String vehType = req.getParameter("filter_vehiculeType");
        if (vehType != null && !vehType.isEmpty()) filters.put("vehiculeType.libelle", vehType);

        String carbType = req.getParameter("filter_carburantType");
        if (carbType != null && !carbType.isEmpty()) filters.put("carburantType.libelle", carbType);

        return filters;
    }

    private void handleEdit(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            Long id = Long.valueOf(idStr);
            Vehicule vehicule = vehiculeService.getVehiculeById(id);
            req.setAttribute("vehicule", vehicule);
        }
    }

    private void handleDetail(HttpServletRequest req, String idStr) {
        if (idStr == null || idStr.isEmpty()) return;
        Long id = Long.valueOf(idStr);

        VmVehiculeDetail detail = vehiculeService.getVehiculeDetail(id);
        req.setAttribute("detail", detail);

        VmVehiculeCoutEntretien cout = vehiculeService.getCoutEntretien(id);
        req.setAttribute("cout", cout);

        List<VehiculeEntretien> entretiens = vehiculeService.getEntretiensByVehicule(id);
        req.setAttribute("entretiens", entretiens);

        MouvementStatusDto currentStatus = mouvementStatusService.getCurrentStatus("Vehicule", id);
        req.setAttribute("currentStatus", currentStatus);

        List<MouvementStatusDto> statusHistory = mouvementStatusService.getStatusHistory("Vehicule", id);
        req.setAttribute("statusHistory", statusHistory);

        List<StatusObjectDto> availableStatuts = statusService.findAllStatuses("Vehicule");
        req.setAttribute("availableStatuts", availableStatuts);

        List<VehiculeTarifTypePlace> tarifPlaces = vehiculeService.getTarifTypePlacesByVehicule(id);
        req.setAttribute("tarifPlaces", tarifPlaces);

        double maxCA = vehiculeService.getTotalMaxChiffreAffairePossible(id);
        req.setAttribute("maxCA", maxCA);
    }

    private Vehicule buildVehiculeFromRequest(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        Vehicule vehicule = (idStr != null && !idStr.isEmpty()) ? vehiculeService.getVehiculeById(Long.valueOf(idStr)) : new Vehicule();

        vehicule.setMarque(req.getParameter("marque"));
        vehicule.setModele(req.getParameter("modele"));
        vehicule.setImmatriculation(req.getParameter("immatriculation"));
        vehicule.setMaximumPassager(Integer.valueOf(req.getParameter("maximumPassager")));

        String capaciteCarburant = req.getParameter("capaciteCarburant");
        if (capaciteCarburant != null && !capaciteCarburant.isEmpty()) {
            vehicule.setCapaciteCarburant(new BigDecimal(capaciteCarburant));
        }

        String depenseCarburant100km = req.getParameter("depenseCarburant100km");
        if (depenseCarburant100km != null && !depenseCarburant100km.isEmpty()) {
            vehicule.setDepenseCarburant100km(new BigDecimal(depenseCarburant100km));
        }

        VehiculeType vehiculeType = new VehiculeType();
        vehiculeType.setId(Long.valueOf(req.getParameter("idType")));
        vehicule.setVehiculeType(vehiculeType);

        CarburantType carburantType = new CarburantType();
        carburantType.setId(Long.valueOf(req.getParameter("idTypeCarburant")));
        vehicule.setCarburantType(carburantType);

        return vehicule;
    }

    private void handleChangeStatut(HttpServletRequest req, Long vehiculeId) {
        Long idStatut = Long.valueOf(req.getParameter("idStatut"));
        String observation = req.getParameter("observation");
        String dateChangementStr = req.getParameter("dateChangement");

        LocalDateTime dateChangement = (dateChangementStr != null && !dateChangementStr.isEmpty())
                ? LocalDateTime.parse(dateChangementStr)
                : LocalDateTime.now();

        mouvementStatusCreateService.createMouvementStatut("Vehicule", vehiculeId, idStatut, dateChangement, observation);
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

        entretien.setDateDebutEntretien((dateDebutStr != null && !dateDebutStr.isEmpty())
                ? LocalDateTime.parse(dateDebutStr)
                : LocalDateTime.now());

        if (dateFinStr != null && !dateFinStr.isEmpty()) {
            entretien.setDateFinEntretien(LocalDateTime.parse(dateFinStr));
        }

        vehiculeService.addEntretien(entretien);
    }
}