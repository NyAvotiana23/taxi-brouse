package com.mdgtaxi.service;

import com.mdgtaxi.entity.TrajetRemisePourcentage;
import com.mdgtaxi.entity.TrajetTarifTypePlaceCategorieRemise;
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
    public TrajetTarifTypePlaceCategorieRemise createTarifRemise(TrajetTarifTypePlaceCategorieRemise tarifRemise) {
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
    public TrajetTarifTypePlaceCategorieRemise getTarifRemiseById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TrajetTarifTypePlaceCategorieRemise.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Get all tarif remises
     */
    public List<TrajetTarifTypePlaceCategorieRemise> getAllTarifRemises() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TrajetTarifTypePlaceCategorieRemise t ORDER BY t.id DESC",
                    TrajetTarifTypePlaceCategorieRemise.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<TrajetTarifTypePlaceCategorieRemise> getTarifRemisesByTrajet(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TrajetTarifTypePlaceCategorieRemise t WHERE t.trajet.id = :trajetId ORDER BY t.id DESC",
                    TrajetTarifTypePlaceCategorieRemise.class
            );
            query.setParameter("trajetId", trajetId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get all remise pourcentages for a specific trajet
     */
    public List<TrajetRemisePourcentage> getRemisePourcentagesByTrajet(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetRemisePourcentage> query = em.createQuery(
                    "SELECT t FROM TrajetRemisePourcentage t WHERE t.trajet.id = :trajetId ORDER BY t.id DESC",
                    TrajetRemisePourcentage.class
            );
            query.setParameter("trajetId", trajetId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Apply remise pourcentage for a specific trajet
     */
    public void appliquerRemisePourcentTrajet(Long idRemise, Long trajetId) throws Exception {
        TrajetRemisePourcentage val = getRemisePourcentById(idRemise);

        if (!val.getTrajet().getId().equals(trajetId)) {
            throw new Exception("Cette remise n'appartient pas à ce trajet");
        }

        List<TrajetTarifTypePlaceCategorieRemise> tarifRemises = getTarifRemisesByTrajetAndCategorie(
                trajetId,
                val.getCategorieParRapport().getId()
        );

        for (TrajetTarifTypePlaceCategorieRemise t : tarifRemises) {
            TrajetTarifTypePlaceCategorieRemise newRemise = new TrajetTarifTypePlaceCategorieRemise();

            newRemise.setTrajet(t.getTrajet());
            newRemise.setTypePlace(t.getTypePlace());
            newRemise.setCategoriePersonne(val.getCategorieApplication());

            double nouveauTarif = t.getTarifUnitaireAvecRemise() * (1 + (val.getRemisePourcent()) / 100);
            newRemise.setTarifUnitaireAvecRemise(nouveauTarif);

            createTarifRemise(newRemise);
        }
    }

    /**
     * Get tarif remises by trajet and categorie
     */
    private List<TrajetTarifTypePlaceCategorieRemise> getTarifRemisesByTrajetAndCategorie(Long trajetId, Long categorieId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TrajetTarifTypePlaceCategorieRemise t " +
                            "WHERE t.trajet.id = :trajetId AND t.categoriePersonne.id = :categorieId",
                    TrajetTarifTypePlaceCategorieRemise.class
            );
            query.setParameter("trajetId", trajetId);
            query.setParameter("categorieId", categorieId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }



    public List<TrajetTarifTypePlaceCategorieRemise> getAllTarifRemises(Long idCategorie) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TrajetTarifTypePlaceCategorieRemise t WHERE t.categoriePersonne.id = :idCategorie ORDER BY t.id DESC",
                    TrajetTarifTypePlaceCategorieRemise.class
            );
            query.setParameter("idCategorie", idCategorie);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public TrajetRemisePourcentage getRemisePourcentById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TrajetRemisePourcentage.class, id);
        } finally {
            em.close();
        }
    }

    public boolean hastTarif (Long idCategorie, Long idTypePlace) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TrajetTarifTypePlaceCategorieRemise t WHERE t.categoriePersonne.id = :idCategorie ORDER BY t.id DESC",
                    TrajetTarifTypePlaceCategorieRemise.class
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
        TrajetRemisePourcentage val = getRemisePourcentById(idRemise);

        List<TrajetTarifTypePlaceCategorieRemise> trajetTarifTypePlaceCategorieRemises = getAllTarifRemises(val.getCategorieParRapport().getId());

        for (TrajetTarifTypePlaceCategorieRemise t : trajetTarifTypePlaceCategorieRemises) {
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
    public List<TrajetRemisePourcentage> getAllTarifPourcentagesRemises() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetRemisePourcentage> query = em.createQuery(
                    "SELECT t FROM TrajetRemisePourcentage t ORDER BY t.id DESC",
                    TrajetRemisePourcentage.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    /**
     * Update tarif remise
     */
    public TrajetTarifTypePlaceCategorieRemise updateTarifRemise(TrajetTarifTypePlaceCategorieRemise tarifRemise) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TrajetTarifTypePlaceCategorieRemise updated = em.merge(tarifRemise);
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
            TrajetTarifTypePlaceCategorieRemise tarifRemise = em.find(TrajetTarifTypePlaceCategorieRemise.class, id);
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
    public List<TrajetTarifTypePlaceCategorieRemise> searchTarifRemisesWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TrajetTarifTypePlaceCategorieRemise> cq = cb.createQuery(TrajetTarifTypePlaceCategorieRemise.class);
            Root<TrajetTarifTypePlaceCategorieRemise> root = cq.from(TrajetTarifTypePlaceCategorieRemise.class);

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

            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get tarif remise by type place and categorie personne
     */
    public TrajetTarifTypePlaceCategorieRemise getTarifRemiseByTypePlaceAndCategorie(Long idTypePlace, Long idCategoriePersonne) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TrajetTarifTypePlaceCategorieRemise t WHERE t.typePlace.id = :idTypePlace AND t.categoriePersonne.id = :idCategoriePersonne",
                    TrajetTarifTypePlaceCategorieRemise.class
            );
            query.setParameter("idTypePlace", idTypePlace);
            query.setParameter("idCategoriePersonne", idCategoriePersonne);
            List<TrajetTarifTypePlaceCategorieRemise> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Get all tarif remises by type place
     */
    public List<TrajetTarifTypePlaceCategorieRemise> getTarifRemisesByTypePlace(Long idTypePlace) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TrajetTarifTypePlaceCategorieRemise t WHERE t.typePlace.id = :idTypePlace",
                    TrajetTarifTypePlaceCategorieRemise.class
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
    public List<TrajetTarifTypePlaceCategorieRemise> getTarifRemisesByCategorie(Long idCategoriePersonne) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT t FROM TrajetTarifTypePlaceCategorieRemise t WHERE t.categoriePersonne.id = :idCategoriePersonne",
                    TrajetTarifTypePlaceCategorieRemise.class
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
    public boolean existsTarifRemise(Long idTrajet, Long idTypePlace, Long idCategoriePersonne, Long excludeId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COUNT(t) FROM TrajetTarifTypePlaceCategorieRemise t " +
                    "WHERE t.typePlace.id = :idTypePlace AND t.categoriePersonne.id = :idCategoriePersonne AND t.trajet.id = :idTrajet";

            if (excludeId != null) {
                jpql += " AND t.id != :excludeId";
            }

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("idTypePlace", idTypePlace);
            query.setParameter("idCategoriePersonne", idCategoriePersonne);
            query.setParameter("idTrajet", idTrajet);


            if (excludeId != null) {
                query.setParameter("excludeId", excludeId);
            }

            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}