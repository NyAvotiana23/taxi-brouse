package com.mdgtaxi.service;

import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Service générique pour gérer les statuts (Chauffeur, Vehicule, Trajet, Reservation)
 */
public class StatusService {
    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    /**
     * Récupère tous les statuts pour une table donnée
     * @param tableName Nom de la table (Chauffeur_Statut, Vehicule_Statut, Trajet_Statut, Reservation_Statut)
     * @return Liste des statuts
     */
    public List<StatusObjectDto> findAllStatuses(String tableName) {
        String entityName = getStatusEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(s.id, s.libelle, s.score, s.spanHtml) " +
                    "FROM " + entityName + " s ORDER BY s.score";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Trouve un statut par son libellé
     * @param tableName Nom de la table
     * @param libelle Libellé du statut
     * @return Le statut ou null
     */
    public StatusObjectDto findStatusByLibelle(String tableName, String libelle) {
        String entityName = getStatusEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(s.id, s.libelle, s.score, s.spanHtml) " +
                    "FROM " + entityName + " s WHERE s.libelle = :libelle";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            query.setParameter("libelle", libelle);
            List<StatusObjectDto> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Trouve un statut par son score
     * @param tableName Nom de la table
     * @param score Score du statut
     * @return Le statut ou null
     */
    public StatusObjectDto findStatusByScore(String tableName, int score) {
        String entityName = getStatusEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(s.id, s.libelle, s.score, s.spanHtml) " +
                    "FROM " + entityName + " s WHERE s.score = :score";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            query.setParameter("score", score);
            List<StatusObjectDto> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Trouve un statut par son ID
     * @param tableName Nom de la table
     * @param id ID du statut
     * @return Le statut ou null
     */
    public StatusObjectDto findStatusById(String tableName, Long id) {
        String entityName = getStatusEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(s.id, s.libelle, s.score, s.spanHtml) " +
                    "FROM " + entityName + " s WHERE s.id = :id";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            query.setParameter("id", id);
            List<StatusObjectDto> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Récupère les statuts avec un score minimum
     * @param tableName Nom de la table
     * @param minScore Score minimum
     * @return Liste des statuts
     */
    public List<StatusObjectDto> findStatusesByMinScore(String tableName, int minScore) {
        String entityName = getStatusEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(s.id, s.libelle, s.score, s.spanHtml) " +
                    "FROM " + entityName + " s WHERE s.score >= :minScore ORDER BY s.score";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            query.setParameter("minScore", minScore);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Récupère les statuts avec un score maximum
     * @param tableName Nom de la table
     * @param maxScore Score maximum
     * @return Liste des statuts
     */
    public List<StatusObjectDto> findStatusesByMaxScore(String tableName, int maxScore) {
        String entityName = getStatusEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(s.id, s.libelle, s.score, s.spanHtml) " +
                    "FROM " + entityName + " s WHERE s.score <= :maxScore ORDER BY s.score";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            query.setParameter("maxScore", maxScore);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Récupère les statuts dans une plage de scores
     * @param tableName Nom de la table
     * @param minScore Score minimum
     * @param maxScore Score maximum
     * @return Liste des statuts
     */
    public List<StatusObjectDto> findStatusesByScoreRange(String tableName, int minScore, int maxScore) {
        String entityName = getStatusEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(s.id, s.libelle, s.score, s.spanHtml) " +
                    "FROM " + entityName + " s WHERE s.score BETWEEN :minScore AND :maxScore ORDER BY s.score";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            query.setParameter("minScore", minScore);
            query.setParameter("maxScore", maxScore);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Mapping des noms de tables vers les noms d'entités de statut
     */
    private String getStatusEntityName(String tableName) {
        return switch (tableName) {
            case "Chauffeur" -> "ChauffeurStatut";
            case "Vehicule" -> "VehiculeStatut";
            case "Trajet" -> "TrajetStatut";
            case "Trajet_Reservation" -> "ReservationStatut";
            default -> throw new IllegalArgumentException("Table non supportée: " + tableName);
        };
    }
}