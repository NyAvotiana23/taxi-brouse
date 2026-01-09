package com.mdgtaxi.service;

import com.mdgtaxi.entity.Ville;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class VilleService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public List<Ville> getAllVilles() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v", Ville.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
