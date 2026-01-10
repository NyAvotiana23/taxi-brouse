package com.mdgtaxi.service;

import com.mdgtaxi.dto.StatusObjectDto;
import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class StatusObjectService {
    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public List<StatusObjectDto> findAllStatusObject(String tableName) {
        // Mapping des noms de tables vers les noms d'entités JPA
        String entityName = getEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(e.id, e.libelle, e.score) FROM " + entityName + " e";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public StatusObjectDto findStatusObjectByLibelle(String tableName, String libelle) {
        String entityName = getEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(e.id, e.libelle, e.score) FROM " + entityName
                    + " e WHERE e.libelle = :libelle";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            query.setParameter("libelle", libelle);
            List<StatusObjectDto> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    public StatusObjectDto findStatusObjectByScore(String tableName, int score) {
        String entityName = getEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.StatusObjectDto(e.id, e.libelle,e.score) FROM " + entityName
                    + " e WHERE e.score = :score";
            TypedQuery<StatusObjectDto> query = em.createQuery(jpql, StatusObjectDto.class);
            query.setParameter("score", score);
            List<StatusObjectDto> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    private String getEntityName(String tableName) {
        return tableName.replace("_", "");
    }
}
