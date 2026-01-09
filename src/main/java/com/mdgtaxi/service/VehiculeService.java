package com.mdgtaxi.service;

import com.mdgtaxi.entity.CarburantType;
import com.mdgtaxi.entity.Vehicule;
import com.mdgtaxi.entity.VehiculeEntretien;
import com.mdgtaxi.entity.VehiculeMouvementStatut;
import com.mdgtaxi.entity.VehiculeStatut;
import com.mdgtaxi.entity.VehiculeType;
import com.mdgtaxi.util.HibernateUtil;
import com.mdgtaxi.view.VmVehiculeCoutEntretien;
import com.mdgtaxi.view.VmVehiculeDetail;
import com.mdgtaxi.view.VmVehiculeHistoriqueStatut;
import com.mdgtaxi.view.VmVehiculeStatutActuel;

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

public class VehiculeService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // CRUD Operations for Vehicule

    public Vehicule createVehicule(Vehicule vehicule) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vehicule);
            tx.commit();
            return vehicule;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Vehicule getVehiculeById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Vehicule.class, id);
        } finally {
            em.close();
        }
    }

    public List<Vehicule> getAllVehicules() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Vehicule> query = em.createQuery("SELECT v FROM Vehicule v", Vehicule.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Vehicule updateVehicule(Vehicule vehicule) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Vehicule updated = em.merge(vehicule);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteVehicule(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Vehicule vehicule = em.find(Vehicule.class, id);
            if (vehicule != null) {
                em.remove(vehicule);
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

    public VmVehiculeDetail getVehiculeDetail(Long idVehicule) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmVehiculeDetail.class, idVehicule);
        } finally {
            em.close();
        }
    }

    public List<VmVehiculeDetail> getAllVehiculeDetails() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmVehiculeDetail> query = em.createQuery("SELECT vd FROM VmVehiculeDetail vd", VmVehiculeDetail.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public VmVehiculeStatutActuel getCurrentStatut(Long idVehicule) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmVehiculeStatutActuel.class, idVehicule);
        } finally {
            em.close();
        }
    }

    public List<VmVehiculeHistoriqueStatut> getHistoriqueStatut(Long idVehicule) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VmVehiculeHistoriqueStatut> query = em.createQuery(
                    "SELECT hs FROM VmVehiculeHistoriqueStatut hs WHERE hs.idVehicule = :idVehicule ORDER BY hs.dateMouvement DESC",
                    VmVehiculeHistoriqueStatut.class);
            query.setParameter("idVehicule", idVehicule);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public VmVehiculeCoutEntretien getCoutEntretien(Long idVehicule) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(VmVehiculeCoutEntretien.class, idVehicule);
        } finally {
            em.close();
        }
    }

    // Operations for Related Entities

    public VehiculeEntretien addEntretien(VehiculeEntretien entretien) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entretien);
            tx.commit();
            return entretien;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<VehiculeEntretien> getEntretiensByVehicule(Long idVehicule) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<VehiculeEntretien> query = em.createQuery(
                    "SELECT ve FROM VehiculeEntretien ve WHERE ve.vehicule.id = :idVehicule ORDER BY ve.dateDebutEntretien DESC",
                    VehiculeEntretien.class);
            query.setParameter("idVehicule", idVehicule);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public VehiculeMouvementStatut changeStatut(VehiculeMouvementStatut mouvement) {
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

    // Multi-filter criteria function for Vehicule (using Criteria API)
    // Filters are provided as a Map<String, Object> where keys are field names like "marque", "modele", "immatriculation", etc.
    // For relationships, use "vehiculeType.libelle", "carburantType.libelle"
    // Supports exact match for strings, equality for numbers, etc.

    public List<Vehicule> searchVehiculesWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Vehicule> cq = cb.createQuery(Vehicule.class);
            Root<Vehicule> root = cq.from(Vehicule.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "marque":
                        predicates.add(cb.equal(root.get("marque"), value));
                        break;
                    case "modele":
                        predicates.add(cb.equal(root.get("modele"), value));
                        break;
                    case "immatriculation":
                        predicates.add(cb.equal(root.get("immatriculation"), value));
                        break;
                    case "maximumPassager":
                        predicates.add(cb.equal(root.get("maximumPassager"), value));
                        break;
                    case "capaciteCarburant":
                        predicates.add(cb.equal(root.get("capaciteCarburant"), value));
                        break;
                    case "depenseCarburant100km":
                        predicates.add(cb.equal(root.get("depenseCarburant100km"), value));
                        break;
                    case "vehiculeType.libelle":
                        predicates.add(cb.equal(root.get("vehiculeType").get("libelle"), value));
                        break;
                    case "carburantType.libelle":
                        predicates.add(cb.equal(root.get("carburantType").get("libelle"), value));
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

            TypedQuery<Vehicule> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Similar multi-filter for VmVehiculeDetail (since it's a view, we can filter on its fields)
    public List<VmVehiculeDetail> searchVehiculeDetailsWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<VmVehiculeDetail> cq = cb.createQuery(VmVehiculeDetail.class);
            Root<VmVehiculeDetail> root = cq.from(VmVehiculeDetail.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "immatriculation":
                        predicates.add(cb.equal(root.get("immatriculation"), value));
                        break;
                    case "marque":
                        predicates.add(cb.equal(root.get("marque"), value));
                        break;
                    case "modele":
                        predicates.add(cb.equal(root.get("modele"), value));
                        break;
                    case "maximumPassager":
                        predicates.add(cb.equal(root.get("maximumPassager"), value));
                        break;
                    case "capaciteCarburant":
                        predicates.add(cb.equal(root.get("capaciteCarburant"), value));
                        break;
                    case "depenseCarburant100km":
                        predicates.add(cb.equal(root.get("depenseCarburant100km"), value));
                        break;
                    case "libelleType":
                        predicates.add(cb.equal(root.get("libelleType"), value));
                        break;
                    case "typeCarburant":
                        predicates.add(cb.equal(root.get("typeCarburant"), value));
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

            TypedQuery<VmVehiculeDetail> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}