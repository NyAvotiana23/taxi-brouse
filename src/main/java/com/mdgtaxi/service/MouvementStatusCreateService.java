package com.mdgtaxi.service;

import com.mdgtaxi.entity.*;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class MouvementStatusCreateService {
    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    /**
     * Creates a new mouvement statut for the given entity.
     * Updates the entity's current statut field if applicable.
     *
     * @param tableName      Name of the table (e.g., "Chauffeur", "Vehicule",
     *                       "Trajet", "Trajet_Reservation")
     * @param idEntity       ID of the entity
     * @param idNewStatut    ID of the new statut
     * @param dateChangement Date of the changement
     * @param observation    Optional observation
     * @return The created mouvement statut object
     */
    public Object createMouvementStatut(String tableName, Long idEntity, Long idNewStatut, LocalDateTime dateChangement,
            String observation) {
        Class<?> entityClass = null;
        Class<?> mouvementClass = null;
        Class<?> statutClass = null;
        String entityField = null;
        boolean hasStatutField = false;
        String statutFieldName = null;

        if ("Chauffeur".equals(tableName)) {
            entityClass = Chauffeur.class;
            mouvementClass = ChauffeurMouvementStatut.class;
            statutClass = ChauffeurStatut.class;
            entityField = "chauffeur";
            hasStatutField = false;
        } else if ("Vehicule".equals(tableName)) {
            entityClass = Vehicule.class;
            mouvementClass = VehiculeMouvementStatut.class;
            statutClass = VehiculeStatut.class;
            entityField = "vehicule";
            hasStatutField = false;
        } else if ("Trajet".equals(tableName)) {
            entityClass = Trajet.class;
            mouvementClass = TrajetMouvementStatut.class;
            statutClass = TrajetStatut.class;
            entityField = "trajet";
            hasStatutField = true;
            statutFieldName = "trajetStatut";
        } else if ("Trajet_Reservation".equals(tableName)) {
            entityClass = TrajetReservation.class;
            mouvementClass = TrajetReservationMouvementStatut.class;
            statutClass = ReservationStatut.class;
            entityField = "trajetReservation";
            hasStatutField = true;
            statutFieldName = "reservationStatut";
        } else {
            throw new IllegalArgumentException("Unsupported tableName: " + tableName);
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Find the entity
            Object entity = em.find(entityClass, idEntity);
            if (entity == null) {
                throw new IllegalArgumentException("Entity not found with id: " + idEntity);
            }

            // Find the new statut
            Object newStatut = em.find(statutClass, idNewStatut);
            if (newStatut == null) {
                throw new IllegalArgumentException("Statut not found with id: " + idNewStatut);
            }

            // Create new mouvement instance
            Object mouvement = mouvementClass.getConstructor().newInstance();

            // Set entity
            Method setEntity = mouvementClass.getMethod("set" + capitalize(entityField), entityClass);
            setEntity.invoke(mouvement, entity);

            // Set nouveauStatut
            Method setNouveauStatut = mouvementClass.getMethod("setNouveauStatut", statutClass);
            setNouveauStatut.invoke(mouvement, newStatut);

            // Set dateMouvement
            Method setDateMouvement = mouvementClass.getMethod("setDateMouvement", LocalDateTime.class);
            setDateMouvement.invoke(mouvement, dateChangement);

            // Set observation
            Method setObservation = mouvementClass.getMethod("setObservation", String.class);
            setObservation.invoke(mouvement, observation);

            // Persist the mouvement
            em.persist(mouvement);

            // If the entity has a direct statut field, update it
            if (hasStatutField) {
                Method setStatut = entityClass.getMethod("set" + capitalize(statutFieldName), statutClass);
                setStatut.invoke(entity, newStatut);
            }

            tx.commit();
            return mouvement;
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException
                | InstantiationException e) {
            if (tx.isActive())
                tx.rollback();
            throw new RuntimeException("Reflection error: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
