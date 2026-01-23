package com.mdgtaxi.service;

import com.mdgtaxi.entity.*;
import com.mdgtaxi.util.HibernateUtil;
import com.mdgtaxi.view.VmDiffusionPaiement;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiffusionPaiementService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    /**
     * Crée un paiement et génère automatiquement la répartition proportionnelle
     * sur tous les détails de diffusion associés
     */
    public DiffusionPaiement createPaiementAvecRepartition(DiffusionPaiement paiement) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Persister le paiement
            em.persist(paiement);
            em.flush(); // Pour obtenir l'ID généré

            // Récupérer tous les détails de diffusion pour cette diffusion
            TypedQuery<DiffusionDetail> query = em.createQuery(
                    "SELECT dd FROM DiffusionDetail dd WHERE dd.diffusion.id = :diffusionId",
                    DiffusionDetail.class
            );
            query.setParameter("diffusionId", paiement.getDiffusion().getId());
            List<DiffusionDetail> details = query.getResultList();

            if (details.isEmpty()) {
                throw new IllegalStateException("Aucun détail de diffusion trouvé pour la diffusion #"
                        + paiement.getDiffusion().getId());
            }

            // Calculer le montant total à payer de la diffusion
            BigDecimal montantTotalDiffusion = BigDecimal.ZERO;
            for (DiffusionDetail detail : details) {
                BigDecimal montantDetail = detail.getMontantUnitaire()
                        .multiply(new BigDecimal(detail.getNombreRepetition()));
                montantTotalDiffusion = montantTotalDiffusion.add(montantDetail);
            }

            // Calculer le pourcentage payé
            BigDecimal pourcentagePaye = paiement.getMontantPaye()
                    .divide(montantTotalDiffusion, 6, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));

            // Créer les répartitions pour chaque détail
            BigDecimal totalReparti = BigDecimal.ZERO;
            int lastIndex = details.size() - 1;

            for (int i = 0; i < details.size(); i++) {
                DiffusionDetail detail = details.get(i);
                DiffusionPaiementRepartition repartition = new DiffusionPaiementRepartition();

                repartition.setDiffusiondDetail(detail);
                repartition.setDiffusionPaiement(paiement);
                repartition.setPourcentage(pourcentagePaye.doubleValue());

                // Calculer le montant pour ce détail
                BigDecimal montantDetail = detail.getMontantUnitaire()
                        .multiply(new BigDecimal(detail.getNombreRepetition()));
                BigDecimal montantRepartition = montantDetail
                        .multiply(pourcentagePaye)
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

                // Ajustement pour le dernier élément pour éviter les erreurs d'arrondi
                if (i == lastIndex) {
                    montantRepartition = paiement.getMontantPaye().subtract(totalReparti);
                } else {
                    totalReparti = totalReparti.add(montantRepartition);
                }

                repartition.setMontantPaye(montantRepartition);
                em.persist(repartition);
            }

            tx.commit();
            return paiement;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // CREATE (ancienne méthode conservée pour compatibilité)
    public DiffusionPaiement createPaiement(DiffusionPaiement paiement) {
        return createPaiementAvecRepartition(paiement);
    }

    // READ - by ID
    public DiffusionPaiement getPaiementById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(DiffusionPaiement.class, id);
        } finally {
            em.close();
        }
    }

    // READ - all
    public List<DiffusionPaiement> getAllPaiements() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<DiffusionPaiement> query = em.createQuery(
                    "SELECT p FROM DiffusionPaiement p ORDER BY p.datePaiement DESC",
                    DiffusionPaiement.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ - by Diffusion ID
    public List<DiffusionPaiement> getPaiementsByDiffusionId(Long diffusionId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<DiffusionPaiement> query = em.createQuery(
                    "SELECT p FROM DiffusionPaiement p WHERE p.diffusion.id = :diffusionId ORDER BY p.datePaiement DESC",
                    DiffusionPaiement.class
            );
            query.setParameter("diffusionId", diffusionId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ - Répartitions by Paiement ID
    public List<DiffusionPaiementRepartition> getRepartitionsByPaiementId(Long paiementId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<DiffusionPaiementRepartition> query = em.createQuery(
                    "SELECT r FROM DiffusionPaiementRepartition r " +
                            "WHERE r.diffusionPaiement.id = :paiementId",
                    DiffusionPaiementRepartition.class
            );
            query.setParameter("paiementId", paiementId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ - by Societe ID
    public List<DiffusionPaiement> getPaiementsBySocieteId(Long societeId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<DiffusionPaiement> query = em.createQuery(
                    "SELECT p FROM DiffusionPaiement p WHERE p.societe.id = :societeId ORDER BY p.datePaiement DESC",
                    DiffusionPaiement.class
            );
            query.setParameter("societeId", societeId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public DiffusionPaiement updatePaiement(DiffusionPaiement paiement) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            DiffusionPaiement updated = em.merge(paiement);
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
    public void deletePaiement(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            DiffusionPaiement paiement = em.find(DiffusionPaiement.class, id);
            if (paiement != null) {
                // Supprimer d'abord les répartitions
                Query deleteRepartitions = em.createQuery(
                        "DELETE FROM DiffusionPaiementRepartition r WHERE r.diffusionPaiement.id = :paiementId"
                );
                deleteRepartitions.setParameter("paiementId", id);
                deleteRepartitions.executeUpdate();

                // Puis supprimer le paiement
                em.remove(paiement);
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

    // Get VmDiffusionPaiement with filters
    public List<VmDiffusionPaiement> getVmPaiements(Long idPublicite, List<Long> idSocietes, Long idTrajet,
                                                    Integer mois, Integer annee,
                                                    Integer nombreRepetitionMin, Integer nombreRepetitionMax) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VmDiffusionPaiement> cq = cb.createQuery(VmDiffusionPaiement.class);
            Root<VmDiffusionPaiement> root = cq.from(VmDiffusionPaiement.class);

            List<Predicate> predicates = new ArrayList<>();

            if (idPublicite != null) {
                predicates.add(cb.equal(root.get("idPublicite"), idPublicite));
            }

            if (idSocietes != null && !idSocietes.isEmpty()) {
                predicates.add(root.get("idSociete").in(idSocietes));
            }

            if (idTrajet != null) {
                predicates.add(cb.equal(root.get("idTrajet"), idTrajet));
            }

            if (mois != null) {
                predicates.add(cb.equal(root.get("mois"), mois));
            }

            if (annee != null) {
                predicates.add(cb.equal(root.get("annee"), annee));
            }

            if (nombreRepetitionMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("nombre"), nombreRepetitionMin));
            }

            if (nombreRepetitionMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("nombre"), nombreRepetitionMax));
            }

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            cq.orderBy(cb.desc(root.get("idDiffusion")));

            TypedQuery<VmDiffusionPaiement> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Get all VmDiffusionPaiement
    public List<VmDiffusionPaiement> getAllVmPaiements() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmDiffusionPaiement> query = em.createQuery(
                    "SELECT v FROM VmDiffusionPaiement v ORDER BY v.idDiffusion DESC",
                    VmDiffusionPaiement.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}