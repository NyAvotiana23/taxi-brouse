package com.mdgtaxi.service;

import com.mdgtaxi.entity.Client;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class ClientService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    public List<Client> getAllClients() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Client createClient(Client client) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(client);
            tx.commit();
            return client;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
