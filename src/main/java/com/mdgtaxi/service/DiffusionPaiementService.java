package com.mdgtaxi.service;

import com.mdgtaxi.entity.Diffusion;
import com.mdgtaxi.entity.DiffusionPaiement;
import com.mdgtaxi.entity.Societe;
import com.mdgtaxi.util.HibernateUtil;
import com.mdgtaxi.view.VmDiffusionPaiement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiffusionPaiementService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // CREATE
    public DiffusionPaiement createPaiement(DiffusionPaiement paiement) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(paiement);
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

    // New method for proportional payment based on filters
    public void makeProportionalPayment(Long idPublicite, List<Long> idSocietes, Long idTrajet,
                                        Integer mois, Integer annee,
                                        Integer nombreMin, Integer nombreMax,
                                        BigDecimal amount) {
        List<VmDiffusionPaiement> vms = getVmPaiements(idPublicite, idSocietes, idTrajet, mois, annee, nombreMin, nombreMax);

        BigDecimal totalRemaining = BigDecimal.ZERO;
        for (VmDiffusionPaiement vm : vms) {
            BigDecimal remaining = vm.getMontantTotal().subtract(vm.getMontantPaye());
            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                totalRemaining = totalRemaining.add(remaining);
            }
        }

        if (totalRemaining.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException("Aucun montant dû pour les critères sélectionnés.");
        }

        if (amount.compareTo(totalRemaining) > 0) {
            throw new IllegalArgumentException("Le montant de paiement excède le montant dû.");
        }

        BigDecimal ratio = amount.divide(totalRemaining, 10, RoundingMode.HALF_UP);

        LocalDateTime date = LocalDateTime.now();

        for (VmDiffusionPaiement vm : vms) {
            BigDecimal remaining = vm.getMontantTotal().subtract(vm.getMontantPaye());
            if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal payAmount = remaining.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
                if (payAmount.compareTo(BigDecimal.ZERO) > 0) {
                    DiffusionPaiement p = new DiffusionPaiement();
                    Diffusion d = new Diffusion();
                    d.setId(vm.getIdDiffusion());
                    p.setDiffusion(d);
                    Societe s = new Societe();
                    s.setId(vm.getIdSociete());
                    p.setSociete(s);
                    p.setDatePaiement(date);
                    p.setMontantPaye(payAmount);
                    createPaiement(p);
                }
            }
        }
    }
}