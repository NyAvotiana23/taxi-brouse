package com.mdgtaxi.service;

import com.mdgtaxi.dto.MouvementStatusCriteria;
import com.mdgtaxi.dto.MouvementStatusDto;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour gérer les mouvements de statut individuels
 * Supporte: Chauffeur, Vehicule, Trajet, Trajet_Reservation
 */
public class MouvementStatusService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    /**
     * Récupère le statut actuel d'une entité
     * 
     * @param tableName Nom de la table (Chauffeur, Vehicule, Trajet,
     *                  Trajet_Reservation)
     * @param idEntite  ID de l'entité
     * @return Le statut actuel ou null
     */
    public MouvementStatusDto getCurrentStatus(String tableName, Long idEntite) {
        return getStatusAtDate(tableName, idEntite, LocalDateTime.now());
    }

    /**
     * Récupère le statut d'une entité à une date donnée
     * 
     * @param tableName Nom de la table
     * @param idEntite  ID de l'entité
     * @param date      Date à laquelle on veut connaître le statut
     * @return Le statut à cette date ou null
     */
    public MouvementStatusDto getStatusAtDate(String tableName, Long idEntite, LocalDateTime date) {
        String mouvementEntity = getMouvementEntityName(tableName);
        String statusEntity = getStatusEntityName(tableName);
        String idField = getIdFieldName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.MouvementStatusDto(" +
                    "m.id, " +
                    "m." + idField + ", " +
                    "'" + tableName + "', " +
                    "s.id, " +
                    "s.libelle, " +
                    "s.score, " +
                    "s.spanHtml, " +
                    "m.dateMouvement, " +
                    "m.observation) " +
                    "FROM " + mouvementEntity + " m " +
                    "INNER JOIN " + statusEntity + " s ON m.nouveauStatut.id = s.id " +
                    "WHERE m." + idField + " = :idEntite " +
                    "AND m.dateMouvement <= :date " +
                    "ORDER BY m.dateMouvement DESC";

            TypedQuery<MouvementStatusDto> query = em.createQuery(jpql, MouvementStatusDto.class);
            query.setParameter("idEntite", idEntite);
            query.setParameter("date", date);
            query.setMaxResults(1);

            List<MouvementStatusDto> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Récupère l'historique complet des mouvements de statut
     * 
     * @param tableName Nom de la table
     * @param idEntite  ID de l'entité
     * @return Liste des mouvements ordonnés par date décroissante
     */
    public List<MouvementStatusDto> getStatusHistory(String tableName, Long idEntite) {
        String mouvementEntity = getMouvementEntityName(tableName);
        String statusEntity = getStatusEntityName(tableName);
        String idField = getIdFieldName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.MouvementStatusDto(" +
                    "m.id, " +
                    "m." + idField + ", " +
                    "'" + tableName + "', " +
                    "s.id, " +
                    "s.libelle, " +
                    "s.score, " +
                    "s.spanHtml, " +
                    "m.dateMouvement, " +
                    "m.observation) " +
                    "FROM " + mouvementEntity + " m " +
                    "INNER JOIN " + statusEntity + " s ON m.nouveauStatut.id = s.id " +
                    "WHERE m." + idField + " = :idEntite " +
                    "ORDER BY m.dateMouvement DESC";

            TypedQuery<MouvementStatusDto> query = em.createQuery(jpql, MouvementStatusDto.class);
            query.setParameter("idEntite", idEntite);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Récupère les mouvements de statut selon des critères multiples
     * 
     * @param tableName Nom de la table
     * @param criteria  Critères de recherche
     * @return Liste des mouvements filtrés ordonnés par date décroissante
     */
    public List<MouvementStatusDto> getMouvementsByCriteria(String tableName, MouvementStatusCriteria criteria) {
        String mouvementEntity = getMouvementEntityName(tableName);
        String statusEntity = getStatusEntityName(tableName);
        String idField = getIdFieldName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT new com.mdgtaxi.dto.MouvementStatusDto(")
                    .append("m.id, ")
                    .append("m.").append(idField).append(", ")
                    .append("'").append(tableName).append("', ")
                    .append("s.id, ")
                    .append("s.libelle, ")
                    .append("s.score, ")
                    .append("s.spanHtml, ")
                    .append("m.dateMouvement, ")
                    .append("m.observation) ")
                    .append("FROM ").append(mouvementEntity).append(" m ")
                    .append("INNER JOIN ").append(statusEntity).append(" s ON m.nouveauStatut.id = s.id ")
                    .append("WHERE 1=1 ");

            // Filtre par ID d'entité
            if (criteria.getIdEntite() != null) {
                jpql.append("AND m.").append(idField).append(" = :idEntite ");
            }

            // Filtre par libellé nouveau statut
            if (criteria.getLibelleNouveauStatut() != null && !criteria.getLibelleNouveauStatut().isEmpty()) {
                jpql.append("AND s.libelle = :libelleNouveau ");
            }

            // Filtres par score nouveau statut
            if (criteria.getExactScoreNouveau() != null) {
                jpql.append("AND s.score = :exactScoreNouveau ");
            } else {
                if (criteria.getMinScoreNouveau() != null) {
                    jpql.append("AND s.score >= :minScoreNouveau ");
                }
                if (criteria.getMaxScoreNouveau() != null) {
                    jpql.append("AND s.score <= :maxScoreNouveau ");
                }
            }

            // Filtre par plage de dates
            if (criteria.getDateDebut() != null) {
                jpql.append("AND m.dateMouvement >= :dateDebut ");
            }
            if (criteria.getDateFin() != null) {
                jpql.append("AND m.dateMouvement <= :dateFin ");
            }

            // Filtre par observation
            if (criteria.getObservationContains() != null && !criteria.getObservationContains().isEmpty()) {
                jpql.append("AND LOWER(m.observation) LIKE LOWER(:observation) ");
            }

            jpql.append("ORDER BY m.dateMouvement DESC");

            TypedQuery<MouvementStatusDto> query = em.createQuery(jpql.toString(), MouvementStatusDto.class);

            // Paramétrage de la requête
            if (criteria.getIdEntite() != null) {
                query.setParameter("idEntite", criteria.getIdEntite());
            }
            if (criteria.getLibelleNouveauStatut() != null && !criteria.getLibelleNouveauStatut().isEmpty()) {
                query.setParameter("libelleNouveau", criteria.getLibelleNouveauStatut());
            }
            if (criteria.getExactScoreNouveau() != null) {
                query.setParameter("exactScoreNouveau", criteria.getExactScoreNouveau());
            } else {
                if (criteria.getMinScoreNouveau() != null) {
                    query.setParameter("minScoreNouveau", criteria.getMinScoreNouveau());
                }
                if (criteria.getMaxScoreNouveau() != null) {
                    query.setParameter("maxScoreNouveau", criteria.getMaxScoreNouveau());
                }
            }
            if (criteria.getDateDebut() != null) {
                query.setParameter("dateDebut", criteria.getDateDebut());
            }
            if (criteria.getDateFin() != null) {
                query.setParameter("dateFin", criteria.getDateFin());
            }
            if (criteria.getObservationContains() != null && !criteria.getObservationContains().isEmpty()) {
                query.setParameter("observation", "%" + criteria.getObservationContains() + "%");
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Récupère tous les mouvements pour une table (sans filtre d'entité spécifique)
     * 
     * @param tableName Nom de la table
     * @return Liste de tous les mouvements
     */
    public List<MouvementStatusDto> getAllMouvements(String tableName) {
        MouvementStatusCriteria criteria = MouvementStatusCriteria.builder().build();
        return getMouvementsByCriteria(tableName, criteria);
    }

    /**
     * Récupère les mouvements dans une période donnée
     * 
     * @param tableName Nom de la table
     * @param dateDebut Date de début
     * @param dateFin   Date de fin
     * @return Liste des mouvements dans la période
     */
    public List<MouvementStatusDto> getMouvementsByPeriod(String tableName, LocalDateTime dateDebut,
            LocalDateTime dateFin) {
        MouvementStatusCriteria criteria = MouvementStatusCriteria.builder()
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .build();
        return getMouvementsByCriteria(tableName, criteria);
    }

    /**
     * Récupère les mouvements vers un statut spécifique
     * 
     * @param tableName            Nom de la table
     * @param libelleNouveauStatut Libellé du nouveau statut
     * @return Liste des mouvements vers ce statut
     */
    public List<MouvementStatusDto> getMouvementsToStatus(String tableName, String libelleNouveauStatut) {
        MouvementStatusCriteria criteria = MouvementStatusCriteria.builder()
                .libelleNouveauStatut(libelleNouveauStatut)
                .build();
        return getMouvementsByCriteria(tableName, criteria);
    }

    // Méthodes de mapping

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