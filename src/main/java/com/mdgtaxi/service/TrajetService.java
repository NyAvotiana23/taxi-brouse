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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            TypedQuery<Trajet> query = em.createQuery("SELECT t FROM Trajet t ORDER BY t.datetimeDepart DESC", Trajet.class);
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

    public double getPlaceRestante(Long idTrajet) {
        EntityManager em = emf.createEntityManager();
        try {
            Trajet trajet = em.find(Trajet.class, idTrajet);
            if (trajet == null) {
                return 0.0;
            }
            double totalPlaces = trajet.getVehicule().getMaximumPassager();
            String jpql = "SELECT SUM(trd.nombrePlaces) FROM TrajetReservation tr JOIN tr.trajetReservationDetails trd WHERE tr.trajet.id = :idTrajet";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("idTrajet", idTrajet);
            Double taken = query.getSingleResult();
            double takenPlaces = taken != null ? taken : 0.0;
            return totalPlaces - takenPlaces;
        } finally {
            em.close();
        }
    }

    public double getPlaceRestante(Long idTrajet, Long idTypePlace) {
        EntityManager em = emf.createEntityManager();
        try {
            Trajet trajet = em.find(Trajet.class, idTrajet);
            if (trajet == null) {
                return 0.0;
            }
            String totalJpql = "SELECT vttp.nombrePlace FROM VehiculeTarifTypePlace vttp WHERE vttp.vehicule.id = :idVehicule AND vttp.typePlace.id = :idTypePlace";
            TypedQuery<Double> totalQuery = em.createQuery(totalJpql, Double.class);
            totalQuery.setParameter("idVehicule", trajet.getVehicule().getId());
            totalQuery.setParameter("idTypePlace", idTypePlace);
            Double total = totalQuery.getSingleResult();
            double totalForType = total != null ? total : 0.0;

            String takenJpql = "SELECT SUM(trd.nombrePlaces) FROM TrajetReservation tr JOIN tr.trajetReservationDetails trd WHERE tr.trajet.id = :idTrajet AND trd.typePlace.id = :idTypePlace";
            TypedQuery<Double> takenQuery = em.createQuery(takenJpql, Double.class);
            takenQuery.setParameter("idTrajet", idTrajet);
            takenQuery.setParameter("idTypePlace", idTypePlace);
            Double taken = takenQuery.getSingleResult();
            double takenForType = taken != null ? taken : 0.0;

            return totalForType - takenForType;
        } finally {
            em.close();
        }
    }

    public double getPlacePrise(Long idTrajet, Long idTypePlace) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT SUM(trd.nombrePlaces) FROM TrajetReservation tr JOIN tr.trajetReservationDetails trd WHERE tr.trajet.id = :idTrajet AND trd.typePlace.id = :idTypePlace";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("idTrajet", idTrajet);
            query.setParameter("idTypePlace", idTypePlace);
            Double taken = query.getSingleResult();
            return taken != null ? taken : 0.0;
        } finally {
            em.close();
        }
    }

    public double getPrevisionChiffreAffaire(Long idTrajet) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT SUM(trd.nombrePlaces * vttp.tarifUnitaire) " +
                    "FROM TrajetReservation tr " +
                    "JOIN tr.trajetReservationDetails trd " +
                    "JOIN VehiculeTarifTypePlace vttp ON vttp.vehicule.id = tr.trajet.vehicule.id AND vttp.typePlace.id = trd.typePlace.id " +
                    "WHERE tr.trajet.id = :idTrajet";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("idTrajet", idTrajet);
            Double prevision = query.getSingleResult();
            return prevision != null ? prevision : 0.0;
        } finally {
            em.close();
        }
    }

    public double getTotalPaiementRecu(Long idTrajet) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT SUM(p.montant) FROM TrajetReservationPaiement p WHERE p.trajetReservation.trajet.id = :idTrajet";
            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("idTrajet", idTrajet);
            Double total = query.getSingleResult();
            return total != null ? total : 0.0;
        } finally {
            em.close();
        }
    }

    public List<Trajet> searchTrajetsWithFilters(Map<String, Object> filters) {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Trajet> cq = cb.createQuery(Trajet.class);
            Root<Trajet> root = cq.from(Trajet.class);

            List<Predicate> predicates = new ArrayList<>();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value == null) continue;

                switch (key) {
                    case "nombrePassager":
                        predicates.add(cb.equal(root.get("nombrePassager"), value));
                        break;
                    case "datetimeDepart":
                        predicates.add(cb.equal(root.get("datetimeDepart"), value));
                        break;
                    case "datetimeDepart>=":
                        predicates.add(cb.greaterThanOrEqualTo(root.get("datetimeDepart"), (LocalDateTime) value));
                        break;
                    case "datetimeDepart<=":
                        predicates.add(cb.lessThanOrEqualTo(root.get("datetimeDepart"), (LocalDateTime) value));
                        break;
                    case "datetimeArrivee":
                        predicates.add(cb.equal(root.get("datetimeArrivee"), value));
                        break;
                    case "datetimeArrivee>=":
                        predicates.add(cb.greaterThanOrEqualTo(root.get("datetimeArrivee"), (LocalDateTime) value));
                        break;
                    case "datetimeArrivee<=":
                        predicates.add(cb.lessThanOrEqualTo(root.get("datetimeArrivee"), (LocalDateTime) value));
                        break;
                    case "fraisUnitaire":
                        predicates.add(cb.equal(root.get("fraisUnitaire"), value));
                        break;
                    case "ligne.id":
                        predicates.add(cb.equal(root.get("ligne").get("id"), value));
                        break;
                    case "chauffeur.id":
                        predicates.add(cb.equal(root.get("chauffeur").get("id"), value));
                        break;
                    case "vehicule.id":
                        predicates.add(cb.equal(root.get("vehicule").get("id"), value));
                        break;
                    case "trajetStatut.id":
                        predicates.add(cb.equal(root.get("trajetStatut").get("id"), value));
                        break;
                    default:
                        // Ignore unknown filters
                        break;
                }
            }

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            cq.orderBy(cb.desc(root.get("datetimeDepart")));

            TypedQuery<Trajet> query = em.createQuery(cq);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<TrajetTarifTypePlaceCategorieRemise> getTarifTypePlaceCategorieRemisesByTrajetId (Long idTrajet) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetTarifTypePlaceCategorieRemise> query = em.createQuery(
                    "SELECT p FROM TrajetTarifTypePlaceCategorieRemise p WHERE  p.trajet.id =: idTrajet",
                    TrajetTarifTypePlaceCategorieRemise.class);

            query.setParameter("idTrajet", idTrajet);

            List<TrajetTarifTypePlaceCategorieRemise> remise = query.getResultList();
            return remise;
        } finally {
            em.close();
        }
    }


    public Map<Long, Double> getSoldPlacesPerType(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Object[]> query = em.createQuery(
                    "SELECT tp.id, SUM(trd.nombrePlaces) " +
                            "FROM TrajetReservationDetails trd " +
                            "JOIN trd.typePlace tp " +
                            "JOIN trd.trajetReservation tr " +
                            "WHERE tr.trajet.id = :trajetId " +
                            "GROUP BY tp.id", Object[].class);
            query.setParameter("trajetId", trajetId);
            List<Object[]> results = query.getResultList();
            Map<Long, Double> map = new HashMap<>();
            for (Object[] row : results) {
                map.put((Long) row[0], ((Number) row[1]).doubleValue());
            }
            return map;
        } finally {
            em.close();
        }
    }

    public List<TrajetReservation> getReservationsByTrajet(Long trajetId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<TrajetReservation> query = em.createQuery(
                    "SELECT r FROM TrajetReservation r WHERE r.trajet.id = :id", TrajetReservation.class);
            query.setParameter("id", trajetId);
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

    public List<Trajet> getAllUpcomingTrajets(LocalDateTime now) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trajet> query = em.createQuery(
                    "SELECT t FROM Trajet t WHERE t.datetimeDepart >= :now ORDER BY t.datetimeDepart ASC",
                    Trajet.class
            );
            query.setParameter("now", now);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Trajet> getUpcomingTrajetsByLigne(LocalDateTime now, Long ligneId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trajet> query = em.createQuery(
                    "SELECT t FROM Trajet t WHERE t.datetimeDepart >= :now AND t.ligne.id = :ligneId ORDER BY t.datetimeDepart ASC",
                    Trajet.class
            );
            query.setParameter("now", now);
            query.setParameter("ligneId", ligneId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Trajet> getUpcomingTrajetsByLigneAndDate(LocalDateTime now, Long ligneId, LocalDate date) {
        LocalDateTime start = now.isAfter(date.atStartOfDay()) ? now : date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trajet> query = em.createQuery(
                    "SELECT t FROM Trajet t WHERE t.datetimeDepart >= :start AND t.datetimeDepart < :end AND t.ligne.id = :ligneId ORDER BY t.datetimeDepart ASC",
                    Trajet.class
            );
            query.setParameter("start", start);
            query.setParameter("end", end);
            query.setParameter("ligneId", ligneId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<LocalDate> getDistinctUpcomingDatesByLigne(LocalDateTime now, Long ligneId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<LocalDateTime> query = em.createQuery(
                    "SELECT t.datetimeDepart FROM Trajet t WHERE t.datetimeDepart >= :now AND t.ligne.id = :ligneId ORDER BY t.datetimeDepart ASC",
                    LocalDateTime.class
            );
            query.setParameter("now", now);
            query.setParameter("ligneId", ligneId);
            List<LocalDateTime> dateTimes = query.getResultList();
            return dateTimes.stream()
                    .map(LocalDateTime::toLocalDate)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }
}