package com.mdgtaxi.service;

import com.mdgtaxi.entity.Client;
import com.mdgtaxi.entity.ProduitCategorie;
import com.mdgtaxi.entity.ProduitExtra;
import com.mdgtaxi.entity.ProduitExtraVente;
import com.mdgtaxi.entity.ProduitExtraVentePayement;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProduitExtraVenteService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // ===== ProduitCategorie =====
    
    public List<ProduitCategorie> getAllCategories() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProduitCategorie> query = em.createQuery(
                "SELECT c FROM ProduitCategorie c ORDER BY c.libelle", ProduitCategorie.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public ProduitCategorie getCategorieById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ProduitCategorie.class, id);
        } finally {
            em.close();
        }
    }

    // ===== ProduitExtra =====
    
    public List<ProduitExtra> getAllProduits() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProduitExtra> query = em.createQuery(
                "SELECT p FROM ProduitExtra p ORDER BY p.nom", ProduitExtra.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ProduitExtra> getProduitsByCategorie(Long categorieId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProduitExtra> query = em.createQuery(
                "SELECT p FROM ProduitExtra p WHERE p.produitCategorie.id = :categorieId ORDER BY p.nom", 
                ProduitExtra.class);
            query.setParameter("categorieId", categorieId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public ProduitExtra getProduitById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ProduitExtra.class, id);
        } finally {
            em.close();
        }
    }

    // ===== ProduitExtraVente =====
    
    public List<ProduitExtraVente> getAllVentes() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProduitExtraVente> query = em.createQuery(
                "SELECT v FROM ProduitExtraVente v ORDER BY v.date DESC", ProduitExtraVente.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public ProduitExtraVente getVenteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(ProduitExtraVente.class, id);
        } finally {
            em.close();
        }
    }

    public List<ProduitExtraVente> searchVentes(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProduitExtraVente> cq = cb.createQuery(ProduitExtraVente.class);
            Root<ProduitExtraVente> root = cq.from(ProduitExtraVente.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "produitId":
                        predicates.add(cb.equal(root.get("produitExtra").get("id"), value));
                        break;
                    case "categorieId":
                        predicates.add(cb.equal(root.get("produitExtra").get("produitCategorie").get("id"), value));
                        break;
                    case "clientId":
                        predicates.add(cb.equal(root.get("client").get("id"), value));
                        break;
                    case "dateDebut":
                        predicates.add(cb.greaterThanOrEqualTo(root.get("date"), (LocalDateTime) value));
                        break;
                    case "dateFin":
                        predicates.add(cb.lessThanOrEqualTo(root.get("date"), (LocalDateTime) value));
                        break;
                    default:
                        break;
                }
            }

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            cq.orderBy(cb.desc(root.get("date")));

            TypedQuery<ProduitExtraVente> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public ProduitExtraVente saveVente(ProduitExtraVente vente) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (vente.getId() == null) {
                em.persist(vente);
            } else {
                vente = em.merge(vente);
            }
            em.getTransaction().commit();
            return vente;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteVente(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ProduitExtraVente vente = em.find(ProduitExtraVente.class, id);
            if (vente != null) {
                em.remove(vente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // ===== ProduitExtraVentePayement =====
    
    public List<ProduitExtraVentePayement> getPayementsByVente(Long venteId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ProduitExtraVentePayement> query = em.createQuery(
                "SELECT p FROM ProduitExtraVentePayement p WHERE p.produitExtraVente.id = :venteId ORDER BY p.datePayement DESC", 
                ProduitExtraVentePayement.class);
            query.setParameter("venteId", venteId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public ProduitExtraVentePayement savePayement(ProduitExtraVentePayement payement) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (payement.getId() == null) {
                em.persist(payement);
            } else {
                payement = em.merge(payement);
            }
            em.getTransaction().commit();
            return payement;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void deletePayement(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ProduitExtraVentePayement payement = em.find(ProduitExtraVentePayement.class, id);
            if (payement != null) {
                em.remove(payement);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // ===== Calculs =====
    
    public BigDecimal getMontantTotalVente(ProduitExtraVente vente) {
        BigDecimal prixUnitaire = vente.getPrixUnitaire() != null ? vente.getPrixUnitaire() : BigDecimal.ZERO;
        int quantite = vente.getQuantite() != null ? vente.getQuantite() : 0;
        double remise = vente.getRemise() != null ? vente.getRemise() : 0.0;
        
        BigDecimal montantBrut = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        BigDecimal montantRemise = montantBrut.multiply(BigDecimal.valueOf(remise / 100.0));
        return montantBrut.subtract(montantRemise);
    }

    public BigDecimal getMontantPayeVente(Long venteId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<BigDecimal> query = em.createQuery(
                "SELECT COALESCE(SUM(p.montant), 0) FROM ProduitExtraVentePayement p WHERE p.produitExtraVente.id = :venteId", 
                BigDecimal.class);
            query.setParameter("venteId", venteId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public BigDecimal getMontantResteVente(Long venteId) {
        ProduitExtraVente vente = getVenteById(venteId);
        if (vente == null) return BigDecimal.ZERO;
        
        BigDecimal montantTotal = getMontantTotalVente(vente);
        BigDecimal montantPaye = getMontantPayeVente(venteId);
        return montantTotal.subtract(montantPaye);
    }

    // ===== Clients =====
    
    public List<Client> getAllClients() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c ORDER BY c.nom", Client.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Client getClientById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }
}
