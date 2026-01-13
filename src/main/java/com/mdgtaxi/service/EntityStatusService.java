package com.mdgtaxi.service;

import com.mdgtaxi.dto.EntityDetailWithStatusDto;
import com.mdgtaxi.dto.EntityListWithStatusDto;
import com.mdgtaxi.dto.EntityStatusCriteria;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour récupérer des entités avec leurs statuts selon différents critères
 * Supporte: Chauffeur, Vehicule, Trajet, Trajet_Reservation
 */
public class EntityStatusService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    /**
     * Récupère toutes les entités avec leur statut à une date donnée
     * @param tableName Nom de la table (Chauffeur, Vehicule, Trajet, Trajet_Reservation)
     * @param date Date (null = maintenant)
     * @return EntityListWithStatusDto contenant toutes les entités avec leurs statuts
     */
    public EntityListWithStatusDto getAllEntitiesWithStatusAtDate(String tableName, LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }

        String entityName = getEntityName(tableName);
        String mouvementEntity = getMouvementEntityName(tableName);
        String statusEntity = getStatusEntityName(tableName);
        String idField = getIdFieldName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            // Requête pour récupérer les entités avec leur statut actuel
            String jpql = "SELECT e, s.id, s.libelle, s.score, s.spanHtml " +
                    "FROM " + entityName + " e " +
                    "LEFT JOIN " + mouvementEntity + " m ON e.id = m." + idField + " " +
                    "LEFT JOIN " + statusEntity + " s ON m.nouveauStatut.id = s.id " +
                    "WHERE m.dateMouvement = (" +
                    "  SELECT MAX(m2.dateMouvement) " +
                    "  FROM " + mouvementEntity + " m2 " +
                    "  WHERE m2." + idField + " = e.id " +
                    "  AND m2.dateMouvement <= :date" +
                    ") " +
                    "OR NOT EXISTS (" +
                    "  SELECT 1 FROM " + mouvementEntity + " m3 " +
                    "  WHERE m3." + idField + " = e.id" +
                    ")";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("date", date);
            List<Object[]> results = query.getResultList();

            List<EntityDetailWithStatusDto> entities = new ArrayList<>();
            for (Object[] row : results) {
                entities.add(new EntityDetailWithStatusDto(
                        row[0],  // entity
                        (Long) row[1],  // idStatut
                        (String) row[2],  // libelleStatut
                        (Integer) row[3],  // scoreStatut
                        (String) row[4]  // spanHtmlStatut
                ));
            }

            return new EntityListWithStatusDto(entities, date, (long) entities.size());
        } finally {
            em.close();
        }
    }

    /**
     * Récupère les entités selon des critères de statut multiples
     * @param tableName Nom de la table
     * @param criteria Critères de recherche (libellé, score min/max, date)
     * @return EntityListWithStatusDto contenant les entités filtrées
     */
    public EntityListWithStatusDto getEntitiesByCriteria(String tableName, EntityStatusCriteria criteria) {
        LocalDateTime effectiveDate = criteria.getEffectiveDate();

        String entityName = getEntityName(tableName);
        String mouvementEntity = getMouvementEntityName(tableName);
        String statusEntity = getStatusEntityName(tableName);
        String idField = getIdFieldName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            // Construction dynamique de la requête avec les critères
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT e, s.id, s.libelle, s.score, s.spanHtml ")
                    .append("FROM ").append(entityName).append(" e ")
                    .append("INNER JOIN ").append(mouvementEntity).append(" m ON e.id = m.").append(idField).append(" ")
                    .append("INNER JOIN ").append(statusEntity).append(" s ON m.nouveauStatut.id = s.id ")
                    .append("WHERE m.dateMouvement <= :date ")
                    .append("AND m.dateMouvement = (")
                    .append("  SELECT MAX(m2.dateMouvement) ")
                    .append("  FROM ").append(mouvementEntity).append(" m2 ")
                    .append("  WHERE m2.").append(idField).append(" = e.id ")
                    .append("  AND m2.dateMouvement <= :date")
                    .append(") ");

            // Ajout des critères de filtre
            boolean hasStatusCriteria = false;

            if (criteria.getLibelleStatut() != null && !criteria.getLibelleStatut().isEmpty()) {
                jpql.append("AND s.libelle = :libelle ");
                hasStatusCriteria = true;
            }

            if (criteria.getExactScore() != null) {
                jpql.append("AND s.score = :exactScore ");
                hasStatusCriteria = true;
            } else {
                if (criteria.getMinScore() != null) {
                    jpql.append("AND s.score >= :minScore ");
                    hasStatusCriteria = true;
                }
                if (criteria.getMaxScore() != null) {
                    jpql.append("AND s.score <= :maxScore ");
                    hasStatusCriteria = true;
                }
            }

            TypedQuery<Object[]> query = em.createQuery(jpql.toString(), Object[].class);
            query.setParameter("date", effectiveDate);

            if (criteria.getLibelleStatut() != null && !criteria.getLibelleStatut().isEmpty()) {
                query.setParameter("libelle", criteria.getLibelleStatut());
            }
            if (criteria.getExactScore() != null) {
                query.setParameter("exactScore", criteria.getExactScore());
            } else {
                if (criteria.getMinScore() != null) {
                    query.setParameter("minScore", criteria.getMinScore());
                }
                if (criteria.getMaxScore() != null) {
                    query.setParameter("maxScore", criteria.getMaxScore());
                }
            }

            List<Object[]> results = query.getResultList();
            List<EntityDetailWithStatusDto> entities = new ArrayList<>();

            for (Object[] row : results) {
                entities.add(new EntityDetailWithStatusDto(
                        row[0],  // entity
                        (Long) row[1],  // idStatut
                        (String) row[2],  // libelleStatut
                        (Integer) row[3],  // scoreStatut
                        (String) row[4]  // spanHtmlStatut
                ));
            }

            return new EntityListWithStatusDto(entities, effectiveDate, (long) entities.size());
        } finally {
            em.close();
        }
    }

    /**
     * Récupère les entités par libellé de statut à une date donnée
     * @param tableName Nom de la table
     * @param libelleStatut Libellé du statut
     * @param date Date (null = maintenant)
     * @return EntityListWithStatusDto
     */
    public EntityListWithStatusDto getEntitiesByLibelleStatut(String tableName, String libelleStatut, LocalDateTime date) {
        EntityStatusCriteria criteria = EntityStatusCriteria.builder()
                .libelleStatut(libelleStatut)
                .atDate(date)
                .build();
        return getEntitiesByCriteria(tableName, criteria);
    }

    /**
     * Récupère les entités par score exact à une date donnée
     * @param tableName Nom de la table
     * @param score Score exact
     * @param date Date (null = maintenant)
     * @return EntityListWithStatusDto
     */
    public EntityListWithStatusDto getEntitiesByExactScore(String tableName, int score, LocalDateTime date) {
        EntityStatusCriteria criteria = EntityStatusCriteria.builder()
                .exactScore(score)
                .atDate(date)
                .build();
        return getEntitiesByCriteria(tableName, criteria);
    }

    /**
     * Récupère les entités par plage de scores à une date donnée
     * @param tableName Nom de la table
     * @param minScore Score minimum (null = pas de minimum)
     * @param maxScore Score maximum (null = pas de maximum)
     * @param date Date (null = maintenant)
     * @return EntityListWithStatusDto
     */
    public EntityListWithStatusDto getEntitiesByScoreRange(String tableName, Integer minScore, Integer maxScore, LocalDateTime date) {
        EntityStatusCriteria criteria = EntityStatusCriteria.builder()
                .minScore(minScore)
                .maxScore(maxScore)
                .atDate(date)
                .build();
        return getEntitiesByCriteria(tableName, criteria);
    }

    /**
     * Compte le nombre d'entités par statut à une date donnée
     * @param tableName Nom de la table
     * @param date Date (null = maintenant)
     * @return Liste de tableaux [libelle_statut, count]
     */
    public List<Object[]> countEntitiesByStatusAtDate(String tableName, LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }

        String mouvementEntity = getMouvementEntityName(tableName);
        String statusEntity = getStatusEntityName(tableName);
        String idField = getIdFieldName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT s.libelle, COUNT(DISTINCT m." + idField + ") " +
                    "FROM " + mouvementEntity + " m " +
                    "INNER JOIN " + statusEntity + " s ON m.nouveauStatut.id = s.id " +
                    "WHERE m.dateMouvement <= :date " +
                    "AND m.dateMouvement = (" +
                    "  SELECT MAX(m2.dateMouvement) " +
                    "  FROM " + mouvementEntity + " m2 " +
                    "  WHERE m2." + idField + " = m." + idField + " " +
                    "  AND m2.dateMouvement <= :date" +
                    ") " +
                    "GROUP BY s.libelle, s.score " +
                    "ORDER BY s.score";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("date", date);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Méthodes de mapping

    private String getEntityName(String tableName) {
        return switch (tableName) {
            case "Chauffeur" -> "Chauffeur";
            case "Vehicule" -> "Vehicule";
            case "Trajet" -> "Trajet";
            case "Trajet_Reservation" -> "TrajetReservation";
            default -> throw new IllegalArgumentException("Table non supportée: " + tableName);
        };
    }

    private String getMouvementEntityName(String tableName) {
        return switch (tableName) {
            case "Chauffeur" -> "ChauffeurMouvementStatut";
            case "Vehicule" -> "VehiculeMouvementStatut";
            case "Trajet" -> "TrajetMouvementStatut";
            case "Trajet_Reservation" -> "TrajetReservationMouvementStatut";
            default -> throw new IllegalArgumentException("Table non supportée: " + tableName);
        };
    }

    private String getStatusEntityName(String tableName) {
        return switch (tableName) {
            case "Chauffeur" -> "ChauffeurStatut";
            case "Vehicule" -> "VehiculeStatut";
            case "Trajet" -> "TrajetStatut";
            case "Trajet_Reservation" -> "ReservationStatut";
            default -> throw new IllegalArgumentException("Table non supportée: " + tableName);
        };
    }

    private String getIdFieldName(String tableName) {
        return switch (tableName) {
            case "Chauffeur" -> "chauffeur.id";
            case "Vehicule" -> "vehicule.id";
            case "Trajet" -> "trajet.id";
            case "Trajet_Reservation" -> "trajetReservation.id";
            default -> throw new IllegalArgumentException("Table non supportée: " + tableName);
        };
    }
}