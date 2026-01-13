package com.mdgtaxi.listner; // Adjust package as needed

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mdgtaxi.util.HibernateUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class PersistenceInitializer implements ServletContextListener {
    private  EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        emf = HibernateUtil.getEntityManagerFactory();
        emf.createEntityManager().close();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}