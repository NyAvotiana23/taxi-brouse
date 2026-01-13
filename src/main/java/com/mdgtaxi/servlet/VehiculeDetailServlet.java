package com.mdgtaxi.servlet;

import com.mdgtaxi.entity.VehiculeEntretien;
import com.mdgtaxi.service.VehiculeService;
import com.mdgtaxi.view.VmVehiculeCoutEntretien;
import com.mdgtaxi.view.VmVehiculeDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/vehicules/detail")
public class VehiculeDetailServlet extends HttpServlet {

    private final VehiculeService vehiculeService = new VehiculeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        VmVehiculeDetail detail = vehiculeService.getVehiculeDetail(id);
        VmVehiculeCoutEntretien cout = vehiculeService.getCoutEntretien(id);
        List<VehiculeEntretien> entretiens = vehiculeService.getEntretiensByVehicule(id);

        req.setAttribute("detail", detail);
        req.setAttribute("cout", cout);
        req.setAttribute("entretiens", entretiens);

        req.getRequestDispatcher("/vehicule-detail.jsp").forward(req, resp);
    }
}