package com.mdgtaxi.service;

import com.mdgtaxi.util.HibernateUtil;
import com.mdgtaxi.view.VmChauffeurDetail;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

public class TypeObjectService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();


    public List<TypeObjectDTO> findAllTypeObject(String tableName) {
        // Liste des tables autorisées qui ont des colonnes id et libelle
        List<String> authorizedTables = Arrays.asList(
                "Carburant_Type", "Devise", "Vehicule_Type", "Vehicule_Statut",
                "Chauffeur_Statut", "Province", "Trajet_Statut", "Type_Client",
                "Type_Mouvement", "Mode_Paiement", "Reservation_Status", "Caisse_Type"
        );

        if (!authorizedTables.contains(tableName)) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }

        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT new com.mdgtaxi.service.TypeObjectDTO(e.id, e.libelle) FROM " + tableName + " e";
            TypedQuery<TypeObjectDTO> query = em.createQuery(jpql, TypeObjectDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
