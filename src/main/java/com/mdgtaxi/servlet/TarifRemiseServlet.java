package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.CategoriePersonne;
import com.mdgtaxi.entity.TarifTypePlaceCategorieRemise;
import com.mdgtaxi.entity.TypePlace;
import com.mdgtaxi.service.TarifRemiseService;
import com.mdgtaxi.service.TypeObjectService;
import com.mdgtaxi.service.VehiculeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/tarif-remises")
public class TarifRemiseServlet extends HttpServlet {

    private final TarifRemiseService tarifRemiseService = new TarifRemiseService();
    private final VehiculeService vehiculeService = new VehiculeService();
    private final TypeObjectService typeObjectService = new TypeObjectService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        // Load reference data
        loadReferenceData(req);

        if ("edit".equals(action)) {
            handleEdit(req);
        }

        // Collect filters
        Map<String, Object> filters = collectFilters(req);

        // Load tarif remises with filters
        List<TarifTypePlaceCategorieRemise> tarifRemises = filters.isEmpty()
                ? tarifRemiseService.getAllTarifRemises()
                : tarifRemiseService.searchTarifRemisesWithFilters(filters);

        req.setAttribute("tarifRemises", tarifRemises);

        req.getRequestDispatcher("/tarif-remises.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("delete".equals(action)) {
                handleDelete(req);
            } else {
                handleSave(req);
            }

            resp.sendRedirect(req.getContextPath() + "/tarif-remises");
        } catch (Exception e) {
            req.setAttribute("error", "Erreur: " + e.getMessage());
            loadReferenceData(req);

            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                TarifTypePlaceCategorieRemise tarifRemise = buildTarifRemiseFromRequest(req);
                req.setAttribute("tarifRemise", tarifRemise);
            }

            doGet(req, resp);
        }
    }

    private void loadReferenceData(HttpServletRequest req) {
        List<TypePlace> typePlaces = vehiculeService.getAllTypePlaces();
        List<TypeObjectDTO> categories = typeObjectService.findAllTypeObject("Categorie_Personne");

        req.setAttribute("typePlaces", typePlaces);
        req.setAttribute("categories", categories);
    }

    private Map<String, Object> collectFilters(HttpServletRequest req) {
        Map<String, Object> filters = new HashMap<>();

        String idTypePlace = req.getParameter("filter_idTypePlace");
        if (idTypePlace != null && !idTypePlace.isEmpty()) {
            filters.put("typePlace.id", Long.valueOf(idTypePlace));
        }

        String idCategorie = req.getParameter("filter_idCategorie");
        if (idCategorie != null && !idCategorie.isEmpty()) {
            filters.put("categoriePersonne.id", Long.valueOf(idCategorie));
        }

        String minTarif = req.getParameter("filter_minTarif");
        if (minTarif != null && !minTarif.isEmpty()) {
            filters.put("tarifUnitaireAvecRemise>=", Double.valueOf(minTarif));
        }

        String maxTarif = req.getParameter("filter_maxTarif");
        if (maxTarif != null && !maxTarif.isEmpty()) {
            filters.put("tarifUnitaireAvecRemise<=", Double.valueOf(maxTarif));
        }

        return filters;
    }

    private void handleEdit(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            Long id = Long.valueOf(idStr);
            TarifTypePlaceCategorieRemise tarifRemise = tarifRemiseService.getTarifRemiseById(id);
            req.setAttribute("tarifRemise", tarifRemise);
        }
    }

    private void handleSave(HttpServletRequest req) throws Exception {
        TarifTypePlaceCategorieRemise tarifRemise = buildTarifRemiseFromRequest(req);

        // Check if combination already exists
        Long excludeId = tarifRemise.getId();
        boolean exists = tarifRemiseService.existsTarifRemise(
                tarifRemise.getTypePlace().getId(),
                tarifRemise.getCategoriePersonne().getId(),
                excludeId
        );

        if (exists) {
            throw new Exception("Un tarif avec remise existe déjà pour cette combinaison Type de Place / Catégorie Personne");
        }

        if (tarifRemise.getId() == null) {
            tarifRemiseService.createTarifRemise(tarifRemise);
        } else {
            tarifRemiseService.updateTarifRemise(tarifRemise);
        }
    }

    private void handleDelete(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            tarifRemiseService.deleteTarifRemise(Long.valueOf(idStr));
        }
    }

    private TarifTypePlaceCategorieRemise buildTarifRemiseFromRequest(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        TarifTypePlaceCategorieRemise tarifRemise;

        if (idStr != null && !idStr.isEmpty()) {
            tarifRemise = tarifRemiseService.getTarifRemiseById(Long.valueOf(idStr));
        } else {
            tarifRemise = new TarifTypePlaceCategorieRemise();
        }

        // Set Type Place
        TypePlace typePlace = new TypePlace();
        typePlace.setId(Long.valueOf(req.getParameter("idTypePlace")));
        tarifRemise.setTypePlace(typePlace);

        // Set Categorie Personne
        CategoriePersonne categoriePersonne = new CategoriePersonne();
        categoriePersonne.setId(Long.valueOf(req.getParameter("idCategoriePersonne")));
        tarifRemise.setCategoriePersonne(categoriePersonne);

        // Set Tarif with discount
        double tarifAvecRemise = Double.parseDouble(req.getParameter("tarifUnitaireAvecRemise"));
        tarifRemise.setTarifUnitaireAvecRemise(tarifAvecRemise);

        return tarifRemise;
    }
}