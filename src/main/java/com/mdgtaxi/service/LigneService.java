package com.mdgtaxi.service;

import com.mdgtaxi.entity.Ligne;
import com.mdgtaxi.entity.LigneDetail;
import com.mdgtaxi.entity.Trajet;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class LigneService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public List<LigneDetail> getLigneDetailList (Long idLigne) {

        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<LigneDetail> query = em.createQuery(
                    "SELECT l FROM LigneDetail l WHERE l.ligne.id = :ligneId",
                    LigneDetail.class
            );
            query.setParameter("ligneId", idLigne);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Ligne> searchLignesWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Ligne> cq = cb.createQuery(Ligne.class);
            Root<Ligne> root = cq.from(Ligne.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "distanceKm":
                        predicates.add(cb.equal(root.get("distanceKm"), value));
                        break;
                    case "distanceKm>=":
                        predicates.add(cb.greaterThanOrEqualTo(root.get("distanceKm"), (BigDecimal) value));
                        break;
                    case "distanceKm<=":
                        predicates.add(cb.lessThanOrEqualTo(root.get("distanceKm"), (BigDecimal) value));
                        break;
                    case "villeDepart.id":
                        predicates.add(cb.equal(root.get("villeDepart").get("id"), value));
                        break;
                    case "villeArrivee.id":
                        predicates.add(cb.equal(root.get("villeArrivee").get("id"), value));
                        break;
                    default:
                        // Ignore unknown filters
                        break;
                }
            }

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            TypedQuery<Ligne> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

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