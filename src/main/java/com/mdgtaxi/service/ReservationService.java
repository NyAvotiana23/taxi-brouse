package com.mdgtaxi.service;

import com.mdgtaxi.entity.TrajetReservation;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class ReservationService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public TrajetReservation createReservation(TrajetReservation reservation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(reservation);
            tx.commit();
            return reservation;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public TrajetReservation getReservationById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TrajetReservation.class, id);
        } finally {
            em.close();
        }
    }

    public List<TrajetReservation> getAllReservations() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetReservation> query = em.createQuery("SELECT r FROM TrajetReservation r",
                    TrajetReservation.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public TrajetReservation updateReservation(TrajetReservation reservation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TrajetReservation updated = em.merge(reservation);
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

    public void deleteReservation(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TrajetReservation reservation = em.find(TrajetReservation.class, id);
            if (reservation != null) {
                em.remove(reservation);
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
