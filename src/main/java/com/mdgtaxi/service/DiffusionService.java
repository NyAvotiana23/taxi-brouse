package com.mdgtaxi.service;

import com.mdgtaxi.entity.Diffusion;
import com.mdgtaxi.util.HibernateUtil;
import com.mdgtaxi.view.VmDiffusionMensuelGlobal;
import com.mdgtaxi.view.VmDiffusionPaiement;

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

public class DiffusionService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // CREATE
    public Diffusion createDiffusion(Diffusion diffusion) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(diffusion);
            tx.commit();
            return diffusion;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // READ - Get by ID
    public Diffusion getDiffusionById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Diffusion.class, id);
        } finally {
            em.close();
        }
    }

    // READ - Get all
    public List<Diffusion> getAllDiffusions() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Diffusion> query = em.createQuery(
                    "SELECT d FROM Diffusion d ORDER BY d.id DESC",
                    Diffusion.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ - Get by Publicite ID
    public List<Diffusion> getDiffusionsByPubliciteId(Long publiciteId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Diffusion> query = em.createQuery(
                    "SELECT d FROM Diffusion d WHERE d.publicite.id = :publiciteId ORDER BY d.id DESC",
                    Diffusion.class
            );
            query.setParameter("publiciteId", publiciteId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ - Get by Trajet ID
    public List<Diffusion> getDiffusionsByTrajetId(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Diffusion> query = em.createQuery(
                    "SELECT d FROM Diffusion d WHERE d.trajet.id = :trajetId ORDER BY d.id DESC",
                    Diffusion.class
            );
            query.setParameter("trajetId", trajetId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public Diffusion updateDiffusion(Diffusion diffusion) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Diffusion updated = em.merge(diffusion);
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

    // DELETE
    public void deleteDiffusion(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Diffusion diffusion = em.find(Diffusion.class, id);
            if (diffusion != null) {
                em.remove(diffusion);
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

    // Fonction spécifique : getDiffusionMensuel avec filtrage
    public List<VmDiffusionMensuelGlobal> getDiffusionMensuel(Integer mois, Integer annee, 
                                                               Double montantMin, Double montantMax) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VmDiffusionMensuelGlobal> cq = cb.createQuery(VmDiffusionMensuelGlobal.class);
            Root<VmDiffusionMensuelGlobal> root = cq.from(VmDiffusionMensuelGlobal.class);

            List<Predicate> predicates = new ArrayList<>();

            // Filtre sur le mois
            if (mois != null) {
                predicates.add(cb.equal(root.get("mois"), mois));
            }

            // Filtre sur l'année
            if (annee != null) {
                predicates.add(cb.equal(root.get("annee"), annee));
            }

            // Filtre sur le montant minimum
            if (montantMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("montantTotal"), montantMin));
            }

            // Filtre sur le montant maximum
            if (montantMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("montantTotal"), montantMax));
            }

            // Appliquer les prédicats
            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            // Tri par année et mois décroissants
            cq.orderBy(cb.desc(root.get("annee")), cb.desc(root.get("mois")));

            TypedQuery<VmDiffusionMensuelGlobal> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Fonction supplémentaire : obtenir toutes les statistiques mensuelles
    public List<VmDiffusionMensuelGlobal> getAllDiffusionMensuel() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmDiffusionMensuelGlobal> query = em.createQuery(
                    "SELECT v FROM VmDiffusionMensuelGlobal v ORDER BY v.annee DESC, v.mois DESC",
                    VmDiffusionMensuelGlobal.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Fonction de filtrage avancé des diffusions
    public List<Diffusion> getDiffusion(Long idPublicite, Long idSociete, Long idTrajet,
                                        Integer mois, Integer annee,
                                        Integer nombreRepetitionMin, Integer nombreRepetitionMax) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Diffusion> cq = cb.createQuery(Diffusion.class);
            Root<Diffusion> root = cq.from(Diffusion.class);

            List<Predicate> predicates = new ArrayList<>();

            // Filtre sur la publicité
            if (idPublicite != null) {
                predicates.add(cb.equal(root.get("publicite").get("id"), idPublicite));
            }

            // Filtre sur la société (via publicité)
            if (idSociete != null) {
                predicates.add(cb.equal(root.get("publicite").get("societe").get("id"), idSociete));
            }

            // Filtre sur le trajet
            if (idTrajet != null) {
                predicates.add(cb.equal(root.get("trajet").get("id"), idTrajet));
            }

            // Filtre sur le mois (via trajet.datetimeDepart)
            if (mois != null) {
                predicates.add(cb.equal(
                    cb.function("MONTH", Integer.class, root.get("trajet").get("datetimeDepart")),
                    mois
                ));
            }

            // Filtre sur l'année (via trajet.datetimeDepart)
            if (annee != null) {
                predicates.add(cb.equal(
                    cb.function("YEAR", Integer.class, root.get("trajet").get("datetimeDepart")),
                    annee
                ));
            }

            // Filtre sur le nombre minimum
            if (nombreRepetitionMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                    cb.function("CAST", Integer.class, root.get("nombre")),
                    nombreRepetitionMin
                ));
            }

            // Filtre sur le nombre maximum
            if (nombreRepetitionMax != null) {
                predicates.add(cb.lessThanOrEqualTo(
                    cb.function("CAST", Integer.class, root.get("nombre")),
                    nombreRepetitionMax
                ));
            }

            // Appliquer les prédicats
            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            // Tri par ID décroissant
            cq.orderBy(cb.desc(root.get("id")));

            TypedQuery<Diffusion> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<VmDiffusionPaiement> getDiffusionPaiements(Long idPublicite, List<Long> idSocietes, Long idTrajet,
                                                           Integer mois, Integer annee,
                                                           Integer nombreRepetitionMin, Integer nombreRepetitionMax) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VmDiffusionPaiement> cq = cb.createQuery(VmDiffusionPaiement.class);
            Root<VmDiffusionPaiement> root = cq.from(VmDiffusionPaiement.class);

            List<Predicate> predicates = new ArrayList<>();

            // Filtre sur la publicité
            if (idPublicite != null) {
                predicates.add(cb.equal(root.get("idPublicite"), idPublicite));
            }

            // Filtre sur les sociétés (multiple)
            if (idSocietes != null && !idSocietes.isEmpty()) {
                predicates.add(root.get("idSociete").in(idSocietes));
            }

            // Filtre sur le trajet
            if (idTrajet != null) {
                predicates.add(cb.equal(root.get("idTrajet"), idTrajet));
            }

            // Filtre sur le mois
            if (mois != null) {
                predicates.add(cb.equal(root.get("mois"), mois));
            }

            // Filtre sur l'année
            if (annee != null) {
                predicates.add(cb.equal(root.get("annee"), annee));
            }

            // Filtre sur le nombre minimum
            if (nombreRepetitionMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("nombre"), nombreRepetitionMin));
            }

            // Filtre sur le nombre maximum
            if (nombreRepetitionMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("nombre"), nombreRepetitionMax));
            }

            // Appliquer les prédicats
            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            // Tri par ID diffusion décroissant
            cq.orderBy(cb.desc(root.get("idDiffusion")));

            TypedQuery<VmDiffusionPaiement> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}