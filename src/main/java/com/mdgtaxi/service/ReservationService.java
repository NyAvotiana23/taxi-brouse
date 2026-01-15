package com.mdgtaxi.service;

import com.mdgtaxi.entity.Trajet;
import com.mdgtaxi.entity.TrajetReservation;
import com.mdgtaxi.entity.TrajetReservationMouvementStatut;
import com.mdgtaxi.entity.TrajetReservationPaiement;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public List<TrajetReservation> searchReservationsWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<TrajetReservation> cq = cb.createQuery(TrajetReservation.class);
            Root<TrajetReservation> root = cq.from(TrajetReservation.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "numeroSiege":
                        predicates.add(cb.equal(root.get("numeroSiege"), value));
                        break;
                    case "nomPassager":
                        predicates.add(cb.equal(root.get("nomPassager"), value));
                        break;
                    case "dateReservation":
                        predicates.add(cb.equal(root.get("dateReservation"), value));
                        break;
                    case "nombrePlaceReservation":
                        predicates.add(cb.equal(root.get("nombrePlaceReservation"), value));
                        break;
                    case "client.id":
                        predicates.add(cb.equal(root.get("client").get("id"), value));
                        break;
                    case "trajet.id":
                        predicates.add(cb.equal(root.get("trajet").get("id"), value));
                        break;
                    case "reservationStatut.id":
                        predicates.add(cb.equal(root.get("reservationStatut").get("id"), value));
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

            TypedQuery<TrajetReservation> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public TrajetReservation createReservation(TrajetReservation reservation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(reservation);
            tx.commit();
            return reservation;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public TrajetReservation getReservationById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TrajetReservation.class, id);
        } finally {
            em.close();
        }
    }

    public List<TrajetReservation> getAllReservations() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetReservation> query = em.createQuery("SELECT r FROM TrajetReservation r",
                    TrajetReservation.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<TrajetReservation> getReservationsByTrajetId(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetReservation> query = em.createQuery(
                    "SELECT r FROM TrajetReservation r WHERE r.trajet.id = :trajetId",
                    TrajetReservation.class);
            query.setParameter("trajetId", trajetId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public TrajetReservation updateReservation(TrajetReservation reservation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TrajetReservation updated = em.merge(reservation);
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

    public void deleteReservation(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TrajetReservation reservation = em.find(TrajetReservation.class, id);
            if (reservation != null) {
                em.remove(reservation);
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
     * Obtient les statistiques de réservations par statut
     *
     * @return Map avec le libellé du statut comme clé et le nombre de réservations
     * comme valeur
     */
    public Map<String, Long> getReservationStatsByStatus() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT r.reservationStatut.libelle, COUNT(r) FROM TrajetReservation r GROUP BY r.reservationStatut.libelle";
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            List<Object[]> results = query.getResultList();

            Map<String, Long> stats = new HashMap<>();
            for (Object[] result : results) {
                stats.put((String) result[0], (Long) result[1]);
            }
            return stats;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le nombre total de places réservées pour un trajet
     *
     * @param trajetId ID du trajet
     * @return Nombre total de places réservées
     */
    public int getPlacesPrisesForTrajet(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(r.nombrePlaceReservation), 0) FROM TrajetReservation r " +
                    "WHERE r.trajet.id = :trajetId AND r.reservationStatut.libelle != 'Annulée'";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("trajetId", trajetId);
            Long result = query.getSingleResult();
            return result != null ? result.intValue() : 0;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le nombre de places restantes pour un trajet
     *
     * @param trajetId ID du trajet
     * @return Nombre de places restantes
     */
    public int getPlacesRestantesForTrajet(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            Trajet trajet = em.find(Trajet.class, trajetId);
            if (trajet == null) {
                return 0;
            }

            int capaciteMax = trajet.getVehicule().getMaximumPassager();
            int placesPrises = getPlacesPrisesForTrajet(trajetId);

            return capaciteMax - placesPrises;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le total de places prises pour tous les trajets
     *
     * @return Nombre total de places réservées
     */
    public int getTotalPlacesPrises() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(r.nombrePlaceReservation), 0) FROM TrajetReservation r " +
                    "WHERE r.reservationStatut.libelle != 'Annulée'";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            Long result = query.getSingleResult();
            return result != null ? result.intValue() : 0;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le total de places restantes pour tous les trajets
     *
     * @return Nombre total de places restantes
     */
    public int getTotalPlacesRestantes() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT t FROM Trajet t";
            TypedQuery<Trajet> query = em.createQuery(jpql, Trajet.class);
            List<Trajet> trajets = query.getResultList();

            int totalCapacite = 0;
            for (Trajet trajet : trajets) {
                totalCapacite += trajet.getVehicule().getMaximumPassager();
            }

            int totalPlacesPrises = getTotalPlacesPrises();
            return totalCapacite - totalPlacesPrises;
        } finally {
            em.close();
        }
    }
    public List<TrajetReservationPaiement> getPaymentsByReservation(Long reservationId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetReservationPaiement> query = em.createQuery(
                    "SELECT p FROM TrajetReservationPaiement p WHERE p.trajetReservation.id = :id ORDER BY p.datePaiement DESC",
                    TrajetReservationPaiement.class);
            query.setParameter("id", reservationId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public double getTotalPaiementRecu (Long ) {

    }

    public TrajetReservationPaiement createPayment(TrajetReservationPaiement paiement) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(paiement);
            tx.commit();
            return paiement;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<TrajetReservationMouvementStatut> getStatusHistoryByReservation(Long reservationId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetReservationMouvementStatut> query = em.createQuery(
                    "SELECT rms FROM TrajetReservationMouvementStatut rms WHERE rms.trajetReservation.id = :id ORDER BY rms.dateMouvement DESC",
                    TrajetReservationMouvementStatut.class);
            query.setParameter("id", reservationId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public TrajetReservationMouvementStatut changeStatus(TrajetReservationMouvementStatut mouvement) {
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
}
