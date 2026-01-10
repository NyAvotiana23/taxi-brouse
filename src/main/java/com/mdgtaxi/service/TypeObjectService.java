package com.mdgtaxi.service;

import com.mdgtaxi.util.HibernateUtil;
import com.mdgtaxi.view.VmChauffeurDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
            String jpql = "SELECT new com.mdgtaxi.service.TypeObjectDTO(e.id, e.libelle) FROM " + entityName + " e";
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
            String jpql = "SELECT new com.mdgtaxi.service.TypeObjectDTO(e.id, e.libelle) FROM " + entityName
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
        switch (tableName) {
            case "Carburant_Type":
                return "CarburantType";
            case "Devise":
                return "Devise";
            case "Vehicule_Type":
                return "VehiculeType";
            case "Vehicule_Statut":
                return "VehiculeStatut";
            case "Chauffeur_Statut":
                return "ChauffeurStatut";
            case "Province":
                return "Province";
            case "Trajet_Statut":
                return "TrajetStatut";
            case "Type_Client":
                return "TypeClient";
            case "Type_Mouvement":
                return "TypeMouvement";
            case "Mode_Paiement":
                return "ModePaiement";
            case "Reservation_Status":
                return "ReservationStatus";
            case "Caisse_Type":
                return "CaisseType";
            default:
                throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
    }
}
