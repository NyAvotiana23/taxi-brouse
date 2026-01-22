package com.mdgtaxi.service;

import com.mdgtaxi.entity.Unite;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class UniteService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // CREATE
    public Unite createUnite(Unite unite) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(unite);
            tx.commit();
            return unite;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // READ - by ID
    public Unite getUniteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Unite.class, id);
        } finally {
            em.close();
        }
    }

    // READ - all
    public List<Unite> getAllUnites() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Unite> query = em.createQuery(
                    "SELECT u FROM Unite u ORDER BY u.libelle ASC",
                    Unite.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public Unite updateUnite(Unite unite) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Unite updated = em.merge(unite);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // DELETE
    public void deleteUnite(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Unite unite = em.find(Unite.class, id);
            if (unite != null) {
                em.remove(unite);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Optionnel : recherche par libellé
    public List<Unite> findByLibelleContaining(String libelle) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Unite> query = em.createQuery(
                    "SELECT u FROM Unite u WHERE LOWER(u.libelle) LIKE LOWER(:libelle) ORDER BY u.libelle ASC",
                    Unite.class
            );
            query.setParameter("libelle", "%" + libelle + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}