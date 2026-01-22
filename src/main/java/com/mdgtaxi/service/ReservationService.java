package com.mdgtaxi.service;

import com.mdgtaxi.entity.*;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationService {

    private final VehiculeService vehiculeService = new VehiculeService();
    private final TrajetService trajetService = new TrajetService();


    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();


    public double getCAprevisionnel(Long idTrajet) {
        double val = 0;

        List<TrajetReservation> reservations = getReservationsByTrajetId(idTrajet);

        for (TrajetReservation t : reservations) {
            List<TrajetReservationDetails> trajetReservationDetails = getReservationDetailsByReservationId(t.getId());
            for (TrajetReservationDetails d : trajetReservationDetails) {
                val += d.getTarifUnitaire() * d.getNombrePlaces();
            }

        }
        return val;
    }

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
                    case "nomPassager":
                        predicates.add(cb.equal(root.get("nomPassager"), value));
                        break;
                    case "dateReservation":
                        predicates.add(cb.equal(root.get("dateReservation"), value));
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
                    default:
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

    public TrajetReservationDetails createReservationDetail(TrajetReservationDetails detail) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(detail);
            tx.commit();
            return detail;
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
            TypedQuery<TrajetReservation> query = em.createQuery("SELECT r FROM TrajetReservation r ORDER BY r.dateReservation DESC",
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
                    "SELECT r FROM TrajetReservation r WHERE r.trajet.id = :trajetId ORDER BY r.dateReservation DESC",
                    TrajetReservation.class);
            query.setParameter("trajetId", trajetId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<TrajetReservationDetails> getReservationDetailsByReservationId(Long reservationId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetReservationDetails> query = em.createQuery(
                    "SELECT rd FROM TrajetReservationDetails rd WHERE rd.trajetReservation.id = :reservationId",
                    TrajetReservationDetails.class);
            query.setParameter("reservationId", reservationId);
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

    public TrajetTarifTypePlaceCategorieRemise getTarifRemise(Long idTrajet, Long idTypePlace, Long idCategoriePersonne) {

        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT p FROM TrajetTarifTypePlaceCategorieRemise p WHERE p.typePlace.id = :idTypePlace " +
                            "AND  p.categoriePersonne.id = :idCategoriePersonne AND p.trajet.id =: idTrajet",
                    TrajetTarifTypePlaceCategorieRemise.class);

            query.setParameter("idTypePlace", idTypePlace);
            query.setParameter("idCategoriePersonne", idCategoriePersonne);
            query.setParameter("idTrajet", idTrajet);


            List<TrajetTarifTypePlaceCategorieRemise> remise = query.getResultList();
            if (!remise.isEmpty()) {
                return remise.get(0);
            }
            return null;
        } finally {
            em.close();
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

    public TrajetRemisePourcentage getRemisePourcentageByCategorieApplication(Long idCategorieApplication) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetRemisePourcentage> query = em.createQuery(
                    "SELECT t FROM TrajetRemisePourcentage t WHERE t.categorieApplication = :idCategorieApplication ORDER BY t.id DESC",
                    TrajetRemisePourcentage.class
            );

            List<TrajetRemisePourcentage> trajetRemisePourcentages = query.getResultList();

            if (!trajetRemisePourcentages.isEmpty()) {
                return trajetRemisePourcentages.get(0);
            }

            return null;
        } finally {
            em.close();
        }
    }

    public double getTarifAvecRemise(Long idTrajet, Long idTypePlace, Long idCategoriePersonne) throws Exception {
        TrajetTarifTypePlaceCategorieRemise remise = getTarifRemise(idTrajet, idTypePlace, idCategoriePersonne);

        if (remise != null) {
            return remise.getTarifUnitaireAvecRemise();
        }

        Trajet t = trajetService.getTrajetById(idTrajet);

        VehiculeTarifTypePlace vehiculeTarifTypePlace = vehiculeService.getTarifTypePlaceByVehiculeAndType(t.getVehicule().getId(), idTypePlace);

        TrajetRemisePourcentage trajetRemisePourcentageApplication = getRemisePourcentageByCategorieApplication(idCategoriePersonne);


        if (vehiculeTarifTypePlace == null) {
            throw new Exception("La voiture avec l'id : " + t.getVehicule().getId() + " Ne contient pas la place : " + idTypePlace);
        }

        double val = vehiculeTarifTypePlace.getTarifUnitaire();

        if (trajetRemisePourcentageApplication != null) {
            TrajetTarifTypePlaceCategorieRemise remiseFrom = getTarifRemise(idTrajet, idTypePlace, trajetRemisePourcentageApplication.getCategorieParRapport().getId());
            if (remiseFrom != null) {
                val = remiseFrom.getTarifUnitaireAvecRemise() * (1 + trajetRemisePourcentageApplication.getRemisePourcent() / 100);
            }
        }

        return val;

    }

    /**
     * Calcule le nombre total de places réservées pour un trajet (toutes types confondus)
     */
    public double getPlacesPrisesForTrajet(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(rd.nombrePlaces), 0) FROM TrajetReservation r " +
                    "JOIN r.trajetReservationDetails rd " +
                    "WHERE r.trajet.id = :trajetId AND r.reservationStatut.libelle != 'Annulée'";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("trajetId", trajetId);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le nombre de places réservées pour un trajet par type de place
     */
    public double getPlacesPrisesForTrajet(Long trajetId, Long idTypePlace) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(rd.nombrePlaces), 0) FROM TrajetReservation r " +
                    "JOIN r.trajetReservationDetails rd " +
                    "WHERE r.trajet.id = :trajetId AND rd.typePlace.id = :idTypePlace " +
                    "AND r.reservationStatut.libelle != 'Annulée'";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("trajetId", trajetId);
            query.setParameter("idTypePlace", idTypePlace);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le nombre de places restantes pour un trajet
     */
    public double getPlacesRestantesForTrajet(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            Trajet trajet = em.find(Trajet.class, trajetId);
            if (trajet == null) {
                return 0.0;
            }

            // Total des places disponibles sur le véhicule
            String jpql = "SELECT COALESCE(SUM(vttp.nombrePlace), 0) FROM VehiculeTarifTypePlace vttp " +
                    "WHERE vttp.vehicule.id = :idVehicule";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("idVehicule", trajet.getVehicule().getId());
            Double capaciteMax = query.getSingleResult();

            double placesPrises = getPlacesPrisesForTrajet(trajetId);
            return (capaciteMax != null ? capaciteMax : 0.0) - placesPrises;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le nombre de places restantes pour un type de place spécifique
     */
    public double getPlacesRestantesForTrajet(Long trajetId, Long idTypePlace) {
        EntityManager em = emf.createEntityManager();
        try {
            Trajet trajet = em.find(Trajet.class, trajetId);
            if (trajet == null) {
                return 0.0;
            }

            // Places disponibles pour ce type
            String jpql = "SELECT vttp.nombrePlace FROM VehiculeTarifTypePlace vttp " +
                    "WHERE vttp.vehicule.id = :idVehicule AND vttp.typePlace.id = :idTypePlace";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("idVehicule", trajet.getVehicule().getId());
            query.setParameter("idTypePlace", idTypePlace);
            Double capacite = query.getSingleResult();

            double placesPrises = getPlacesPrisesForTrajet(trajetId, idTypePlace);
            return (capacite != null ? capacite : 0.0) - placesPrises;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le montant total réel de la réservation basé sur les détails avec leurs tarifs
     */
    public BigDecimal getMontantTotalReservation(Long reservationId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(rd.nombrePlaces * rd.tarifUnitaire), 0) " +
                    "FROM TrajetReservationDetails rd " +
                    "WHERE rd.trajetReservation.id = :reservationId";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("reservationId", reservationId);
            Double result = query.getSingleResult();
            return BigDecimal.valueOf(result != null ? result : 0.0);
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le total des paiements reçus pour une réservation
     */
    public BigDecimal getTotalPaiementRecu(Long idReservation) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(p.montant), 0) FROM TrajetReservationPaiement p " +
                    "WHERE p.trajetReservation.id = :idReservation";
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("idReservation", idReservation);
            BigDecimal result = query.getSingleResult();
            return result != null ? result : BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le solde restant à payer
     */
    public BigDecimal getSoldeRestant(Long reservationId) {
        BigDecimal total = getMontantTotalReservation(reservationId);
        BigDecimal paye = getTotalPaiementRecu(reservationId);
        return total.subtract(paye);
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

    /**
     * Calcule le total de toutes les places prises (tous trajets, toutes réservations)
     */
    public double getTotalPlacesPrises() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(rd.nombrePlaces), 0) FROM TrajetReservation r " +
                    "JOIN r.trajetReservationDetails rd " +
                    "WHERE r.reservationStatut.libelle != 'Annulée'";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    /**
     * Calcule le total de toutes les places restantes (estimation globale)
     */
    public double getTotalPlacesRestantes() {
        EntityManager em = emf.createEntityManager();
        try {
            // Total capacité de tous les trajets
            String jpqlCapacite = "SELECT t FROM Trajet t";
            TypedQuery<Trajet> queryTrajets = em.createQuery(jpqlCapacite, Trajet.class);
            List<Trajet> trajets = queryTrajets.getResultList();

            double totalCapacite = 0.0;
            for (Trajet trajet : trajets) {
                String jpql = "SELECT COALESCE(SUM(vttp.nombrePlace), 0) FROM VehiculeTarifTypePlace vttp " +
                        "WHERE vttp.vehicule.id = :idVehicule";
                TypedQuery<Double> query = em.createQuery(jpql, Double.class);
                query.setParameter("idVehicule", trajet.getVehicule().getId());
                Double cap = query.getSingleResult();
                totalCapacite += (cap != null ? cap : 0.0);
            }

            double totalPlacesPrises = getTotalPlacesPrises();
            return totalCapacite - totalPlacesPrises;
        } finally {
            em.close();
        }
    }
    // Add these methods to your existing ReservationService class

    public TrajetReservationDetails getReservationDetailById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(TrajetReservationDetails.class, id);
        } finally {
            em.close();
        }
    }

    public TrajetReservationDetails updateReservationDetail(TrajetReservationDetails detail) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TrajetReservationDetails updated = em.merge(detail);
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

    public void deleteReservationDetail(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TrajetReservationDetails detail = em.find(TrajetReservationDetails.class, id);
            if (detail != null) {
                em.remove(detail);
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