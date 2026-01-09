package com.mdgtaxi.service;

import com.mdgtaxi.entity.Chauffeur;
import com.mdgtaxi.entity.ChauffeurMouvementStatut;
import com.mdgtaxi.entity.ChauffeurStatut;
import com.mdgtaxi.util.HibernateUtil;
import com.mdgtaxi.view.VmChauffeurActivite;
import com.mdgtaxi.view.VmChauffeurDetail;
import com.mdgtaxi.view.VmChauffeurHistoriqueStatut;
import com.mdgtaxi.view.VmChauffeurStatutActuel;

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

public class ChauffeurService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // CRUD Operations for Chauffeur

    public Chauffeur createChauffeur(Chauffeur chauffeur) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(chauffeur);
            tx.commit();
            return chauffeur;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Chauffeur getChauffeurById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Chauffeur.class, id);
        } finally {
            em.close();
        }
    }

    public List<Chauffeur> getAllChauffeurs() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Chauffeur> query = em.createQuery("SELECT c FROM Chauffeur c", Chauffeur.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Chauffeur updateChauffeur(Chauffeur chauffeur) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Chauffeur updated = em.merge(chauffeur);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteChauffeur(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Chauffeur chauffeur = em.find(Chauffeur.class, id);
            if (chauffeur != null) {
                em.remove(chauffeur);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Operations using Views

    public VmChauffeurDetail getChauffeurDetail(Long idChauffeur) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmChauffeurDetail.class, idChauffeur);
        } finally {
            em.close();
        }
    }

    public List<VmChauffeurDetail> getAllChauffeurDetails() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmChauffeurDetail> query = em.createQuery("SELECT cd FROM VmChauffeurDetail cd", VmChauffeurDetail.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public VmChauffeurStatutActuel getCurrentStatut(Long idChauffeur) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmChauffeurStatutActuel.class, idChauffeur);
        } finally {
            em.close();
        }
    }

    public List<VmChauffeurHistoriqueStatut> getHistoriqueStatut(Long idChauffeur) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmChauffeurHistoriqueStatut> query = em.createQuery(
                    "SELECT hs FROM VmChauffeurHistoriqueStatut hs WHERE hs.idChauffeur = :idChauffeur ORDER BY hs.dateMouvement DESC",
                    VmChauffeurHistoriqueStatut.class);
            query.setParameter("idChauffeur", idChauffeur);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public VmChauffeurActivite getActivite(Long idChauffeur) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmChauffeurActivite.class, idChauffeur);
        } finally {
            em.close();
        }
    }

    // Operations for Related Entities

    public ChauffeurMouvementStatut changeStatut(ChauffeurMouvementStatut mouvement) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(mouvement);
            tx.commit();
            return mouvement;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<ChauffeurMouvementStatut> getMouvementsByChauffeur(Long idChauffeur) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ChauffeurMouvementStatut> query = em.createQuery(
                    "SELECT cms FROM ChauffeurMouvementStatut cms WHERE cms.chauffeur.id = :idChauffeur ORDER BY cms.dateMouvement DESC",
                    ChauffeurMouvementStatut.class);
            query.setParameter("idChauffeur", idChauffeur);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Multi-filter criteria function for Chauffeur (using Criteria API)
    // Filters are provided as a Map<String, Object> where keys are field names like "nom", "prenom", "numeroPermis", etc.

    public List<Chauffeur> searchChauffeursWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Chauffeur> cq = cb.createQuery(Chauffeur.class);
            Root<Chauffeur> root = cq.from(Chauffeur.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "nom":
                        predicates.add(cb.equal(root.get("nom"), value));
                        break;
                    case "prenom":
                        predicates.add(cb.equal(root.get("prenom"), value));
                        break;
                    case "dateNaissance":
                        predicates.add(cb.equal(root.get("dateNaissance"), value));
                        break;
                    case "numeroPermis":
                        predicates.add(cb.equal(root.get("numeroPermis"), value));
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

            TypedQuery<Chauffeur> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Similar multi-filter for VmChauffeurDetail (since it's a view, we can filter on its fields)

    public List<VmChauffeurDetail> searchChauffeurDetailsWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VmChauffeurDetail> cq = cb.createQuery(VmChauffeurDetail.class);
            Root<VmChauffeurDetail> root = cq.from(VmChauffeurDetail.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "nom":
                        predicates.add(cb.equal(root.get("nom"), value));
                        break;
                    case "prenom":
                        predicates.add(cb.equal(root.get("prenom"), value));
                        break;
                    case "nomComplet":
                        predicates.add(cb.equal(root.get("nomComplet"), value));
                        break;
                    case "dateNaissance":
                        predicates.add(cb.equal(root.get("dateNaissance"), value));
                        break;
                    case "age":
                        predicates.add(cb.equal(root.get("age"), value));
                        break;
                    case "numeroPermis":
                        predicates.add(cb.equal(root.get("numeroPermis"), value));
                        break;
                    case "libelleStatut":
                        predicates.add(cb.equal(root.get("libelleStatut"), value));
                        break;
                    // Add more as needed
                    default:
                        break;
                }
            }

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            TypedQuery<VmChauffeurDetail> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}