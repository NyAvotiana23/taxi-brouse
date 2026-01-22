package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.*;
import com.mdgtaxi.service.TarifRemiseService;
import com.mdgtaxi.service.TypeObjectService;
import com.mdgtaxi.service.VehiculeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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

        String editRemisePourcentId = (String) req.getSession().getAttribute("editRemisePourcentId");
        if (editRemisePourcentId != null) {
            TrajetRemisePourcentage editTrajetRemisePourcentage = tarifRemiseService.getRemisePourcentById(Long.valueOf(editRemisePourcentId));
            req.setAttribute("remisePourcentage", editTrajetRemisePourcentage);
            req.getSession().removeAttribute("editRemisePourcentId");
        }

// Check for success/error messages from session
        String success = (String) req.getSession().getAttribute("success");
        if (success != null) {
            req.setAttribute("success", success);
            req.getSession().removeAttribute("success");
        }


        // Load reference data
        loadReferenceData(req);

        if ("edit".equals(action)) {
            handleEdit(req);
        }

        // Collect filters
        Map<String, Object> filters = collectFilters(req);

        // Load tarif remises with filters
        List<TrajetTarifTypePlaceCategorieRemise> tarifRemises = filters.isEmpty()
                ? tarifRemiseService.getAllTarifRemises()
                : tarifRemiseService.searchTarifRemisesWithFilters(filters);

        List<TrajetRemisePourcentage> trajetRemisePourcentages = tarifRemiseService.getAllTarifPourcentagesRemises();
        req.setAttribute("remisePourcentages", trajetRemisePourcentages);
        req.setAttribute("tarifRemises", tarifRemises);


        req.getRequestDispatcher("/tarif-remises.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String trajetIdStr = req.getParameter("trajetId");
        Long trajetId = trajetIdStr != null && !trajetIdStr.isEmpty() ? Long.valueOf(trajetIdStr) : null;

        try {
            if ("delete".equals(action)) {
                handleDelete(req);
            } else if ("deletePourcent".equals(action)) {
                handleDeletePourcent(req);
            } else if ("appliquerPourcent".equals(action)) {
                handleAppliquerPourcent(req, trajetId);
            } else if ("savePourcent".equals(action)) {
                handleSavePourcent(req, trajetId);
            } else {
                handleSave(req, trajetId);
            }

            // Redirect based on context
            if (trajetId != null) {
                resp.sendRedirect(req.getContextPath() + "/trajets/detail?id=" + trajetId);
            } else {
                resp.sendRedirect(req.getContextPath() + "/tarif-remises");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Erreur: " + e.getMessage());
            if (trajetId != null) {
                resp.sendRedirect(req.getContextPath() + "/trajets/detail?id=" + trajetId);
            } else {
                resp.sendRedirect(req.getContextPath() + "/tarif-remises");
            }
        }
    }

    private void handleSave(HttpServletRequest req, Long trajetId) throws Exception {
        TrajetTarifTypePlaceCategorieRemise tarifRemise = buildTarifRemiseFromRequest(req, trajetId);

        boolean exists = tarifRemiseService.existsTarifRemise(
                tarifRemise.getTrajet().getId(),
                tarifRemise.getTypePlace().getId(),
                tarifRemise.getCategoriePersonne().getId(),
                tarifRemise.getId()
        );

        if (exists) {
            throw new Exception("Un tarif avec remise existe déjà pour cette combinaison");
        }

        if (tarifRemise.getId() == null) {
            tarifRemiseService.createTarifRemise(tarifRemise);
        } else {
            tarifRemiseService.updateTarifRemise(tarifRemise);
        }
    }

    private void handleSavePourcent(HttpServletRequest req, Long trajetId) throws Exception {
        TrajetRemisePourcentage remisePourcent = buildRemisePourcentFromRequest(req, trajetId);

        if (remisePourcent.getCategorieApplication().getId()
                .equals(remisePourcent.getCategorieParRapport().getId())) {
            throw new Exception("Les catégories doivent être différentes");
        }

        if (remisePourcent.getId() == null) {
            createRemisePourcentage(remisePourcent);
        } else {
            updateRemisePourcentage(remisePourcent);
        }
    }

    private void handleAppliquerPourcent(HttpServletRequest req, Long trajetId) throws Exception {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            Long id = Long.valueOf(idStr);
            if (trajetId != null) {
                tarifRemiseService.appliquerRemisePourcentTrajet(id, trajetId);
            } else {
                tarifRemiseService.appliquerRemisePourcent(id);
            }
        }
    }

    private void createRemisePourcentage(TrajetRemisePourcentage trajetRemisePourcentage) {
        EntityManagerFactory emf = com.mdgtaxi.util.HibernateUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(trajetRemisePourcentage);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private void updateRemisePourcentage(TrajetRemisePourcentage trajetRemisePourcentage) {
        EntityManagerFactory emf = com.mdgtaxi.util.HibernateUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(trajetRemisePourcentage);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private void deleteRemisePourcentage(Long id) {
        EntityManagerFactory emf = com.mdgtaxi.util.HibernateUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TrajetRemisePourcentage trajetRemisePourcentage = em.find(TrajetRemisePourcentage.class, id);
            if (trajetRemisePourcentage != null) {
                em.remove(trajetRemisePourcentage);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private void handleDeletePourcent(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            deleteRemisePourcentage(Long.valueOf(idStr));
        }
    }

    private TrajetTarifTypePlaceCategorieRemise buildTarifRemiseFromRequest(HttpServletRequest req, Long trajetId) {
        String idStr = req.getParameter("id");
        TrajetTarifTypePlaceCategorieRemise tarifRemise;

        if (idStr != null && !idStr.isEmpty()) {
            tarifRemise = tarifRemiseService.getTarifRemiseById(Long.valueOf(idStr));
        } else {
            tarifRemise = new TrajetTarifTypePlaceCategorieRemise();
        }

        // Set Trajet
        Trajet trajet = new Trajet();
        trajet.setId(trajetId != null ? trajetId : Long.valueOf(req.getParameter("trajetId")));
        tarifRemise.setTrajet(trajet);

        // Set Type Place
        TypePlace typePlace = new TypePlace();
        typePlace.setId(Long.valueOf(req.getParameter("idTypePlace")));
        tarifRemise.setTypePlace(typePlace);

        // Set Categorie Personne
        CategoriePersonne categoriePersonne = new CategoriePersonne();
        categoriePersonne.setId(Long.valueOf(req.getParameter("idCategoriePersonne")));
        tarifRemise.setCategoriePersonne(categoriePersonne);

        // Set Tarif
        double tarifAvecRemise = Double.parseDouble(req.getParameter("tarifUnitaireAvecRemise"));
        tarifRemise.setTarifUnitaireAvecRemise(tarifAvecRemise);

        return tarifRemise;
    }

    private TrajetRemisePourcentage buildRemisePourcentFromRequest(HttpServletRequest req, Long trajetId) {
        String idStr = req.getParameter("id");
        TrajetRemisePourcentage remisePourcent;

        if (idStr != null && !idStr.isEmpty()) {
            remisePourcent = tarifRemiseService.getRemisePourcentById(Long.valueOf(idStr));
        } else {
            remisePourcent = new TrajetRemisePourcentage();
        }

        // Set Trajet
        Trajet trajet = new Trajet();
        trajet.setId(trajetId != null ? trajetId : Long.valueOf(req.getParameter("trajetId")));
        remisePourcent.setTrajet(trajet);

        // Set Categorie Application
        CategoriePersonne categorieApplication = new CategoriePersonne();
        categorieApplication.setId(Long.valueOf(req.getParameter("idCategorieApplication")));
        remisePourcent.setCategorieApplication(categorieApplication);

        // Set Categorie Par Rapport
        CategoriePersonne categorieParRapport = new CategoriePersonne();
        categorieParRapport.setId(Long.valueOf(req.getParameter("idCategorieParRapport")));
        remisePourcent.setCategorieParRapport(categorieParRapport);

        // Set Remise Pourcent
        double remise = Double.parseDouble(req.getParameter("remisePourcent"));
        remisePourcent.setRemisePourcent(remise);

        return remisePourcent;
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
            TrajetTarifTypePlaceCategorieRemise tarifRemise = tarifRemiseService.getTarifRemiseById(id);
            req.setAttribute("tarifRemise", tarifRemise);
        }
    }

    private void handleSave(HttpServletRequest req) throws Exception {
        TrajetTarifTypePlaceCategorieRemise tarifRemise = buildTarifRemiseFromRequest(req);

        // Check if combination already exists
        Long excludeId = tarifRemise.getId();
        boolean exists = tarifRemiseService.existsTarifRemise(
                tarifRemise.getTrajet().getId(),
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

    private TrajetTarifTypePlaceCategorieRemise buildTarifRemiseFromRequest(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        TrajetTarifTypePlaceCategorieRemise tarifRemise;

        if (idStr != null && !idStr.isEmpty()) {
            tarifRemise = tarifRemiseService.getTarifRemiseById(Long.valueOf(idStr));
        } else {
            tarifRemise = new TrajetTarifTypePlaceCategorieRemise();
        }

        Trajet trajet = new Trajet();
        trajet.setId(Long.valueOf(req.getParameter("idTrajet")));

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