package com.mdgtaxi.service;

import com.mdgtaxi.entity.Trajet;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class TrajetService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public Trajet createTrajet(Trajet trajet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(trajet);
            tx.commit();
            return trajet;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Trajet getTrajetById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Trajet.class, id);
        } finally {
            em.close();
        }
    }

    public List<Trajet> getAllTrajets() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trajet> query = em.createQuery("SELECT t FROM Trajet t", Trajet.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Trajet updateTrajet(Trajet trajet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Trajet updated = em.merge(trajet);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteTrajet(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Trajet trajet = em.find(Trajet.class, id);
            if (trajet != null) {
                em.remove(trajet);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
