package com.mdgtaxi.service;

import com.mdgtaxi.entity.Trajet;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class TrajetService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public Trajet createTrajet(Trajet trajet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(trajet);
            tx.commit();
            return trajet;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Trajet getTrajetById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Trajet.class, id);
        } finally {
            em.close();
        }
    }

    public List<Trajet> getAllTrajets() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trajet> query = em.createQuery("SELECT t FROM Trajet t", Trajet.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Trajet> getTrajetsByLigneId(Long ligneId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trajet> query = em.createQuery(
                    "SELECT t FROM Trajet t WHERE t.ligne.id = :ligneId ORDER BY t.datetimeDepart DESC",
                    Trajet.class);
            query.setParameter("ligneId", ligneId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Trajet> getFilteredTrajets(Long idLigne, Long idChauffeur, Long idVehicule,
            Long idTrajetStatut, String dateDebut, String dateFin, Integer minScore) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT t FROM Trajet t WHERE 1=1");

            if (idLigne != null) {
                jpql.append(" AND t.ligne.id = :idLigne");
            }
            if (idChauffeur != null) {
                jpql.append(" AND t.chauffeur.id = :idChauffeur");
            }
            if (idVehicule != null) {
                jpql.append(" AND t.vehicule.id = :idVehicule");
            }
            if (idTrajetStatut != null) {
                jpql.append(" AND t.trajetStatut.id = :idTrajetStatut");
            }
            if (dateDebut != null && !dateDebut.isEmpty()) {
                jpql.append(" AND t.datetimeDepart >= :dateDebut");
            }
            if (dateFin != null && !dateFin.isEmpty()) {
                jpql.append(" AND t.datetimeDepart <= :dateFin");
            }
            if (minScore != null) {
                jpql.append(" AND t.trajetStatut.score >= :minScore");
            }

            jpql.append(" ORDER BY t.datetimeDepart DESC");

            TypedQuery<Trajet> query = em.createQuery(jpql.toString(), Trajet.class);

            if (idLigne != null) {
                query.setParameter("idLigne", idLigne);
            }
            if (idChauffeur != null) {
                query.setParameter("idChauffeur", idChauffeur);
            }
            if (idVehicule != null) {
                query.setParameter("idVehicule", idVehicule);
            }
            if (idTrajetStatut != null) {
                query.setParameter("idTrajetStatut", idTrajetStatut);
            }
            if (dateDebut != null && !dateDebut.isEmpty()) {
                query.setParameter("dateDebut", java.time.LocalDateTime.parse(dateDebut));
            }
            if (dateFin != null && !dateFin.isEmpty()) {
                query.setParameter("dateFin", java.time.LocalDateTime.parse(dateFin));
            }
            if (minScore != null) {
                query.setParameter("minScore", minScore);
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Trajet updateTrajet(Trajet trajet) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Trajet updated = em.merge(trajet);
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

    public void deleteTrajet(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Trajet trajet = em.find(Trajet.class, id);
            if (trajet != null) {
                em.remove(trajet);
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
}
