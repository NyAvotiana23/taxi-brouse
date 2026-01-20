package com.mdgtaxi.servlet;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.entity.CategoriePersonne;
import com.mdgtaxi.entity.RemisePourcentage;
import com.mdgtaxi.service.TarifRemiseService;
import com.mdgtaxi.service.TypeObjectService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.util.List;

@WebServlet("/tarif-remises-pourcent")
public class TarifRemisePourcentServlet extends HttpServlet {

    private final TarifRemiseService tarifRemiseService = new TarifRemiseService();
    private final TypeObjectService typeObjectService = new TypeObjectService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            // Store the edit action in session so the main servlet can handle it
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                req.getSession().setAttribute("editRemisePourcentId", idStr);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/tarif-remises");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("appliquer".equals(action)) {
                handleAppliquer(req);
                req.getSession().setAttribute("success", "Remise appliquée avec succès!");
            } else if ("delete".equals(action)) {
                handleDelete(req);
                req.getSession().setAttribute("success", "Remise en pourcentage supprimée avec succès!");
            } else {
                handleSave(req);
                req.getSession().setAttribute("success", "Remise en pourcentage enregistrée avec succès!");
            }

            resp.sendRedirect(req.getContextPath() + "/tarif-remises");
        } catch (Exception e) {
            req.getSession().setAttribute("error", "Erreur: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/tarif-remises");
        }
    }

    private void handleSave(HttpServletRequest req) throws Exception {
        RemisePourcentage remisePourcentage = buildRemisePourcentageFromRequest(req);

        // Validate that application and reference categories are different
        if (remisePourcentage.getCategorieApplication().getId()
                .equals(remisePourcentage.getCategorieParRapport().getId())) {
            throw new Exception("La catégorie d'application ne peut pas être la même que la catégorie de référence");
        }

        if (remisePourcentage.getId() == null) {
            createRemisePourcentage(remisePourcentage);
        } else {
            updateRemisePourcentage(remisePourcentage);
        }
    }

    private void handleDelete(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            deleteRemisePourcentage(Long.valueOf(idStr));
        }
    }

    private void handleAppliquer(HttpServletRequest req) throws Exception{
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            Long id = Long.valueOf(idStr);
            tarifRemiseService.appliquerRemisePourcent(id);
        }
    }

    private RemisePourcentage buildRemisePourcentageFromRequest(HttpServletRequest req) {
        String idStr = req.getParameter("id");
        RemisePourcentage remisePourcentage;

        if (idStr != null && !idStr.isEmpty()) {
            remisePourcentage = tarifRemiseService.getRemisePourcentById(Long.valueOf(idStr));
        } else {
            remisePourcentage = new RemisePourcentage();
        }

        // Set Categorie Application
        CategoriePersonne categorieApplication = new CategoriePersonne();
        categorieApplication.setId(Long.valueOf(req.getParameter("idCategorieApplication")));
        remisePourcentage.setCategorieApplication(categorieApplication);

        // Set Categorie Par Rapport
        CategoriePersonne categorieParRapport = new CategoriePersonne();
        categorieParRapport.setId(Long.valueOf(req.getParameter("idCategorieParRapport")));
        remisePourcentage.setCategorieParRapport(categorieParRapport);

        // Set Remise Pourcent
        double remisePourcent = Double.parseDouble(req.getParameter("remisePourcent"));
        remisePourcentage.setRemisePourcent(remisePourcent);

        return remisePourcentage;
    }

    // CRUD Methods using HibernateUtil pattern
    private void createRemisePourcentage(RemisePourcentage remisePourcentage) {
        EntityManagerFactory emf = com.mdgtaxi.util.HibernateUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(remisePourcentage);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private void updateRemisePourcentage(RemisePourcentage remisePourcentage) {
        EntityManagerFactory emf = com.mdgtaxi.util.HibernateUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(remisePourcentage);
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
            RemisePourcentage remisePourcentage = em.find(RemisePourcentage.class, id);
            if (remisePourcentage != null) {
                em.remove(remisePourcentage);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}