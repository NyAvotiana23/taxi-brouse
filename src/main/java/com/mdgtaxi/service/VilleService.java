package com.mdgtaxi.service;

import com.mdgtaxi.entity.Ville;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<Ville> searchVillesWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Ville> cq = cb.createQuery(Ville.class);
            Root<Ville> root = cq.from(Ville.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "nom":
                        predicates.add(cb.equal(root.get("nom"), value));
                        break;
                    case "region.id":
                        predicates.add(cb.equal(root.get("region").get("id"), value));
                        break;
                    // Add more fields as needed
                    default:
                        // Ignore unknown filters
                        break;
                }
            }

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            TypedQuery<Ville> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
