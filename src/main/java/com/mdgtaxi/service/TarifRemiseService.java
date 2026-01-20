package com.mdgtaxi.service;

import com.mdgtaxi.entity.RemisePourcentage;
import com.mdgtaxi.entity.TarifTypePlaceCategorieRemise;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TarifRemiseService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    /**
     * Create a new tarif remise
     */
    public TarifTypePlaceCategorieRemise createTarifRemise(TarifTypePlaceCategorieRemise tarifRemise) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(tarifRemise);
            tx.commit();
            return tarifRemise;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Get tarif remise by ID
     */
    public TarifTypePlaceCategorieRemise getTarifRemiseById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TarifTypePlaceCategorieRemise.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Get all tarif remises
     */
    public List<TarifTypePlaceCategorieRemise> getAllTarifRemises() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TarifTypePlaceCategorieRemise t ORDER BY t.id DESC",
                    TarifTypePlaceCategorieRemise.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<TarifTypePlaceCategorieRemise> getAllTarifRemises(Long idCategorie) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TarifTypePlaceCategorieRemise t WHERE t.categoriePersonne.id = :idCategorie ORDER BY t.id DESC",
                    TarifTypePlaceCategorieRemise.class
            );
            query.setParameter("idCategorie", idCategorie);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public RemisePourcentage getRemisePourcentById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(RemisePourcentage.class, id);
        } finally {
            em.close();
        }
    }

    public boolean hastTarif (Long idCategorie, Long idTypePlace) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TarifTypePlaceCategorieRemise t WHERE t.categoriePersonne.id = :idCategorie ORDER BY t.id DESC",
                    TarifTypePlaceCategorieRemise.class
            );
            query.setParameter("idCategorie", idCategorie);
            if( query.getResultList().isEmpty()) {

            };
        } finally {
            em.close();
        }
        return false;
    }

    public void appliquerRemisePourcent(Long idRemise) throws Exception {
        RemisePourcentage val = getRemisePourcentById(idRemise);

        List<TarifTypePlaceCategorieRemise> tarifTypePlaceCategorieRemises = getAllTarifRemises(val.getCategorieParRapport().getId());

        for (TarifTypePlaceCategorieRemise t : tarifTypePlaceCategorieRemises) {
            t.setId(null);

            t.setCategoriePersonne(val.getCategorieApplication());
            double nouveautarif = t.getTarifUnitaireAvecRemise() * (1 + (val.getRemisePourcent()) / 100);
            t.setTarifUnitaireAvecRemise(nouveautarif);

            createTarifRemise(t);
        }
    }

    /**
     * Get all tarif pourcentages remises
     */
    public List<RemisePourcentage> getAllTarifPourcentagesRemises() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<RemisePourcentage> query = em.createQuery(
                    "SELECT t FROM RemisePourcentage t ORDER BY t.id DESC",
                    RemisePourcentage.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    /**
     * Update tarif remise
     */
    public TarifTypePlaceCategorieRemise updateTarifRemise(TarifTypePlaceCategorieRemise tarifRemise) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TarifTypePlaceCategorieRemise updated = em.merge(tarifRemise);
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

    /**
     * Delete tarif remise
     */
    public void deleteTarifRemise(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TarifTypePlaceCategorieRemise tarifRemise = em.find(TarifTypePlaceCategorieRemise.class, id);
            if (tarifRemise != null) {
                em.remove(tarifRemise);
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

    /**
     * Search tarif remises with filters
     */
    public List<TarifTypePlaceCategorieRemise> searchTarifRemisesWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TarifTypePlaceCategorieRemise> cq = cb.createQuery(TarifTypePlaceCategorieRemise.class);
            Root<TarifTypePlaceCategorieRemise> root = cq.from(TarifTypePlaceCategorieRemise.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "typePlace.id":
                        predicates.add(cb.equal(root.get("typePlace").get("id"), value));
                        break;
                    case "categoriePersonne.id":
                        predicates.add(cb.equal(root.get("categoriePersonne").get("id"), value));
                        break;
                    case "tarifUnitaireAvecRemise":
                        predicates.add(cb.equal(root.get("tarifUnitaireAvecRemise"), value));
                        break;
                    case "tarifUnitaireAvecRemise>=":
                        predicates.add(cb.greaterThanOrEqualTo(root.get("tarifUnitaireAvecRemise"), (Double) value));
                        break;
                    case "tarifUnitaireAvecRemise<=":
                        predicates.add(cb.lessThanOrEqualTo(root.get("tarifUnitaireAvecRemise"), (Double) value));
                        break;
                    default:
                        break;
                }
            }

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            cq.orderBy(cb.desc(root.get("id")));

            TypedQuery<TarifTypePlaceCategorieRemise> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get tarif remise by type place and categorie personne
     */
    public TarifTypePlaceCategorieRemise getTarifRemiseByTypePlaceAndCategorie(Long idTypePlace, Long idCategoriePersonne) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TarifTypePlaceCategorieRemise t WHERE t.typePlace.id = :idTypePlace AND t.categoriePersonne.id = :idCategoriePersonne",
                    TarifTypePlaceCategorieRemise.class
            );
            query.setParameter("idTypePlace", idTypePlace);
            query.setParameter("idCategoriePersonne", idCategoriePersonne);
            List<TarifTypePlaceCategorieRemise> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Get all tarif remises by type place
     */
    public List<TarifTypePlaceCategorieRemise> getTarifRemisesByTypePlace(Long idTypePlace) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TarifTypePlaceCategorieRemise t WHERE t.typePlace.id = :idTypePlace",
                    TarifTypePlaceCategorieRemise.class
            );
            query.setParameter("idTypePlace", idTypePlace);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get all tarif remises by categorie personne
     */
    public List<TarifTypePlaceCategorieRemise> getTarifRemisesByCategorie(Long idCategoriePersonne) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TarifTypePlaceCategorieRemise t WHERE t.categoriePersonne.id = :idCategoriePersonne",
                    TarifTypePlaceCategorieRemise.class
            );
            query.setParameter("idCategoriePersonne", idCategoriePersonne);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Check if tarif remise already exists for this combination
     */
    public boolean existsTarifRemise(Long idTypePlace, Long idCategoriePersonne, Long excludeId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COUNT(t) FROM TarifTypePlaceCategorieRemise t " +
                    "WHERE t.typePlace.id = :idTypePlace AND t.categoriePersonne.id = :idCategoriePersonne";

            if (excludeId != null) {
                jpql += " AND t.id != :excludeId";
            }

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("idTypePlace", idTypePlace);
            query.setParameter("idCategoriePersonne", idCategoriePersonne);

            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }

            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}