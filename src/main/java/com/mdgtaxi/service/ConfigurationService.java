package com.mdgtaxi.service;

import com.mdgtaxi.entity.Configuration;
import com.mdgtaxi.entity.Unite;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class ConfigurationService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // CREATE
    public Configuration createConfiguration(Configuration configuration) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(configuration);
            tx.commit();
            return configuration;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // READ - by ID
    public Configuration getConfigurationById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Configuration.class, id);
        } finally {
            em.close();
        }
    }

    // READ - by Code (la méthode demandée)
    public Configuration getByCode(String code) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Configuration> query = em.createQuery(
                "SELECT c FROM Configuration c WHERE c.code = :code",
                Configuration.class
            );
            query.setParameter("code", code);
            // On attend normalement 0 ou 1 résultat
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;  // ou throw new EntityNotFoundException() selon votre convention
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    // READ - all
    public List<Configuration> getAllConfigurations() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Configuration> query = em.createQuery(
                "SELECT c FROM Configuration c ORDER BY c.libelle ASC",
                Configuration.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ - by Unite (optionnel mais souvent utile)
    public List<Configuration> getConfigurationsByUnite(Long uniteId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Configuration> query = em.createQuery(
                "SELECT c FROM Configuration c WHERE c.unite.id = :uniteId ORDER BY c.libelle ASC",
                Configuration.class
            );
            query.setParameter("uniteId", uniteId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public Configuration updateConfiguration(Configuration configuration) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Configuration updated = em.merge(configuration);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // DELETE
    public void deleteConfiguration(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Configuration configuration = em.find(Configuration.class, id);
            if (configuration != null) {
                em.remove(configuration);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Méthode bonus : getValeurByCode (très pratique pour les paramètres applicatifs)
    public String getValeurByCode(String code) {
        Configuration config = getByCode(code);
        return (config != null) ? config.getValeur() : null;
    }

    // Variante avec valeur par défaut
    public String getValeurByCode(String code, String defaultValue) {
        Configuration config = getByCode(code);
        return (config != null && config.getValeur() != null) 
            ? config.getValeur() 
            : defaultValue;
    }
}