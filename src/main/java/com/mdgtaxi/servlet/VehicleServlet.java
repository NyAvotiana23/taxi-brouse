package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.Vehicle;
import com.mdgtaxi.util.HibernateUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import java.io.IOException;
import java.util.List;


@WebServlet("/vehicle")
public class VehicleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v", Vehicle.class);
            List<Vehicle> vehicles = query.getResultList();
            req.setAttribute("vehicles", vehicles);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/vehicle-list.jsp");
            dispatcher.forward(req, resp);
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String model = req.getParameter("model");
        String plateNumber = req.getParameter("plateNumber");

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(model);
        vehicle.setPlateNumber(plateNumber);

        EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vehicle);
            tx.commit();
            resp.sendRedirect("vehicle");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new ServletException(e);
        } finally {
            em.close();
        }
    }
}
