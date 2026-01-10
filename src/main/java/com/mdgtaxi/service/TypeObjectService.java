package com.mdgtaxi.service;

import com.mdgtaxi.dto.TypeObjectDTO;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class TypeObjectService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public List<TypeObjectDTO> findAllTypeObject(String tableName) {
        // Mapping des noms de tables vers les noms d'entités JPA
        String entityName = getEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.TypeObjectDTO(e.id, e.libelle) FROM " + entityName + " e";
            TypedQuery<TypeObjectDTO> query = em.createQuery(jpql, TypeObjectDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public TypeObjectDTO findTypeObjectByLibelle(String tableName, String libelle) {
        String entityName = getEntityName(tableName);

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.dto.TypeObjectDTO(e.id, e.libelle) FROM " + entityName
                    + " e WHERE e.libelle = :libelle";
            TypedQuery<TypeObjectDTO> query = em.createQuery(jpql, TypeObjectDTO.class);
            query.setParameter("libelle", libelle);
            List<TypeObjectDTO> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    private String getEntityName(String tableName) {
        return tableName.replace("_", "");
    }
}
