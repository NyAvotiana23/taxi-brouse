package com.mdgtaxi.service;

import com.mdgtaxi.entity.Societe;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class SocieteService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // CREATE
    public Societe createSociete(Societe societe) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(societe);
            tx.commit();
            return societe;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // READ - by ID
    public Societe getSocieteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Societe.class, id);
        } finally {
            em.close();
        }
    }

    // READ - all
    public List<Societe> getAllSocietes() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Societe> query = em.createQuery(
                    "SELECT s FROM Societe s ORDER BY s.nom ASC",
                    Societe.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public Societe updateSociete(Societe societe) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Societe updated = em.merge(societe);
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
    public void deleteSociete(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Societe societe = em.find(Societe.class, id);
            if (societe != null) {
                em.remove(societe);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Optionnel : recherche par nom (partiel)
    public List<Societe> findByNomContaining(String nom) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Societe> query = em.createQuery(
                    "SELECT s FROM Societe s WHERE LOWER(s.nom) LIKE LOWER(:nom) ORDER BY s.nom ASC",
                    Societe.class
            );
            query.setParameter("nom", "%" + nom + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}