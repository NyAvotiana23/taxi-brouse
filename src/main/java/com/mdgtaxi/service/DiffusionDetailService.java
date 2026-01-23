package com.mdgtaxi.service;

import com.mdgtaxi.entity.DiffusionDetail;
import com.mdgtaxi.util.HibernateUtil;

import javax.persistence.*;
import java.util.List;

public class DiffusionDetailService {

    private final EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();

    // READ - by Diffusion ID
    public List<DiffusionDetail> getDetailsByDiffusionId(Long diffusionId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<DiffusionDetail> query = em.createQuery(
                    "SELECT dd FROM DiffusionDetail dd WHERE dd.diffusion.id = :diffusionId",
                    DiffusionDetail.class
            );
            query.setParameter("diffusionId", diffusionId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // READ - by ID
    public DiffusionDetail getDetailById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(DiffusionDetail.class, id);
        } finally {
            em.close();
        }
    }
}