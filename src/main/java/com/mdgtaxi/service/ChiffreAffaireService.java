package com.mdgtaxi.service;

import com.mdgtaxi.util.HibernateUtil;
import com.mdgtaxi.view.VmTrajetCaComplet;
import com.mdgtaxi.view.VmTrajetCaReel;
import com.mdgtaxi.view.VmTrajetPrevisionCaTotal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChiffreAffaireService {
    
    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // ===== VmTrajetPrevisionCaTotal =====
    
    public List<VmTrajetPrevisionCaTotal> getAllPrevisions() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmTrajetPrevisionCaTotal> query = em.createQuery(
                "SELECT v FROM VmTrajetPrevisionCaTotal v ORDER BY v.dateDepart DESC, v.heureDepart DESC",
                VmTrajetPrevisionCaTotal.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public VmTrajetPrevisionCaTotal getPrevisionByTrajetId(Long idTrajet) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmTrajetPrevisionCaTotal.class, idTrajet);
        } finally {
            em.close();
        }
    }

    public List<VmTrajetPrevisionCaTotal> searchPrevisions(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VmTrajetPrevisionCaTotal> cq = cb.createQuery(VmTrajetPrevisionCaTotal.class);
            Root<VmTrajetPrevisionCaTotal> root = cq.from(VmTrajetPrevisionCaTotal.class);

            List<Predicate> predicates = buildPredicates(cb, root, filters);

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            cq.orderBy(cb.desc(root.get("dateDepart")), cb.desc(root.get("heureDepart")));

            TypedQuery<VmTrajetPrevisionCaTotal> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ===== VmTrajetCaReel =====
    
    public List<VmTrajetCaReel> getAllCaReels() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmTrajetCaReel> query = em.createQuery(
                "SELECT v FROM VmTrajetCaReel v ORDER BY v.dateDepart DESC, v.heureDepart DESC",
                VmTrajetCaReel.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public VmTrajetCaReel getCaReelByTrajetId(Long idTrajet) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmTrajetCaReel.class, idTrajet);
        } finally {
            em.close();
        }
    }

    public List<VmTrajetCaReel> searchCaReels(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VmTrajetCaReel> cq = cb.createQuery(VmTrajetCaReel.class);
            Root<VmTrajetCaReel> root = cq.from(VmTrajetCaReel.class);

            List<Predicate> predicates = buildPredicates(cb, root, filters);

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            cq.orderBy(cb.desc(root.get("dateDepart")), cb.desc(root.get("heureDepart")));

            TypedQuery<VmTrajetCaReel> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ===== VmTrajetCaComplet =====
    
    public List<VmTrajetCaComplet> getAllCaComplets() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmTrajetCaComplet> query = em.createQuery(
                "SELECT v FROM VmTrajetCaComplet v ORDER BY v.dateDepart DESC, v.heureDepart DESC",
                VmTrajetCaComplet.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public VmTrajetCaComplet getCaCompletByTrajetId(Long idTrajet) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmTrajetCaComplet.class, idTrajet);
        } finally {
            em.close();
        }
    }

    public List<VmTrajetCaComplet> searchCaComplets(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VmTrajetCaComplet> cq = cb.createQuery(VmTrajetCaComplet.class);
            Root<VmTrajetCaComplet> root = cq.from(VmTrajetCaComplet.class);

            List<Predicate> predicates = buildPredicates(cb, root, filters);

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            cq.orderBy(cb.desc(root.get("dateDepart")), cb.desc(root.get("heureDepart")));

            TypedQuery<VmTrajetCaComplet> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // ===== Méthodes utilitaires =====
    
    private <T> List<Predicate> buildPredicates(CriteriaBuilder cb, Root<T> root, Map<String, Object> filters) {
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) continue;

            switch (key) {
                case "idTrajet":
                    predicates.add(cb.equal(root.get("idTrajet"), value));
                    break;
                case "nomVilleDepart":
                    predicates.add(cb.like(cb.lower(root.get("nomVilleDepart")), "%" + value.toString().toLowerCase() + "%"));
                    break;
                case "nomVilleArrive":
                    predicates.add(cb.like(cb.lower(root.get("nomVilleArrive")), "%" + value.toString().toLowerCase() + "%"));
                    break;
                case "immatriculationVehicule":
                    predicates.add(cb.like(cb.lower(root.get("immatriculationVehicule")), "%" + value.toString().toLowerCase() + "%"));
                    break;
                case "dateDepart":
                    predicates.add(cb.equal(root.get("dateDepart"), value));
                    break;
                case "dateDepart>=":
                    predicates.add(cb.greaterThanOrEqualTo(root.get("dateDepart"), (LocalDate) value));
                    break;
                case "dateDepart<=":
                    predicates.add(cb.lessThanOrEqualTo(root.get("dateDepart"), (LocalDate) value));
                    break;
                case "heureDepart":
                    predicates.add(cb.equal(root.get("heureDepart"), value));
                    break;
                case "heureDepart>=":
                    predicates.add(cb.greaterThanOrEqualTo(root.get("heureDepart"), (LocalTime) value));
                    break;
                case "heureDepart<=":
                    predicates.add(cb.lessThanOrEqualTo(root.get("heureDepart"), (LocalTime) value));
                    break;
                default:
                    // Ignorer les filtres inconnus
                    break;
            }
        }

        return predicates;
    }
}