package com.mdgtaxi.service;

import com.mdgtaxi.entity.Ligne;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class LigneService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public Ligne createLigne(Ligne ligne) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ligne);
            tx.commit();
            return ligne;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Ligne getLigneById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Ligne.class, id);
        } finally {
            em.close();
        }
    }

    public List<Ligne> getAllLignes() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ligne> query = em.createQuery("SELECT l FROM Ligne l", Ligne.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Ligne updateLigne(Ligne ligne) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ligne updated = em.merge(ligne);
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

    public void deleteLigne(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Ligne ligne = em.find(Ligne.class, id);
            if (ligne != null) {
                em.remove(ligne);
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
