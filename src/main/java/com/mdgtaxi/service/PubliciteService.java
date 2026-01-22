package com.mdgtaxi.service;

import com.mdgtaxi.entity.Publicite;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class PubliciteService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // CREATE
    public Publicite createPublicite(Publicite publicite) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(publicite);
            tx.commit();
            return publicite;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // READ - by ID
    public Publicite getPubliciteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Publicite.class, id);
        } finally {
            em.close();
        }
    }

    // READ - all
    public List<Publicite> getAllPublicites() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Publicite> query = em.createQuery(
                    "SELECT p FROM Publicite p ORDER BY p.id DESC",
                    Publicite.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ - by Societe
    public List<Publicite> getPublicitesBySocieteId(Long societeId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Publicite> query = em.createQuery(
                    "SELECT p FROM Publicite p WHERE p.societe.id = :societeId ORDER BY p.id DESC",
                    Publicite.class
            );
            query.setParameter("societeId", societeId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public Publicite updatePublicite(Publicite publicite) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Publicite updated = em.merge(publicite);
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
    public void deletePublicite(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Publicite publicite = em.find(Publicite.class, id);
            if (publicite != null) {
                em.remove(publicite);
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