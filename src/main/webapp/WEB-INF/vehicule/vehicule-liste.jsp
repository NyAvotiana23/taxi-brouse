<%@ page import="com.mdgtaxi.service.VehiculeService" %>
<%@ page import="com.mdgtaxi.view.VmVehiculeDetail" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    VehiculeService service = new VehiculeService();
    // On pourrait ici récupérer des filtres depuis la requête pour appeler searchVehiculeDetailsWithFilters
    List<VmVehiculeDetail> vehicules = service.getAllVehiculeDetails();
%>
<div class="container">
    <h2>Liste des Véhicules</h2>
    <a href="vehicule-form.jsp" class="btn btn-primary">Ajouter un Véhicule</a>

    <table class="table">
        <thead>
        <tr>
            <th>Immatriculation</th>
            <th>Marque & Modèle</th>
            <th>Type</th>
            <th>Statut</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <% for(VmVehiculeDetail v : vehicules) { %>
        <tr>
            <td><%= v.getImmatriculation() %></td>
            <td><%= v.getMarque() %> <%= v.getModele() %></td>
            <td><%= v.getLibelleType() %></td>
            <td><span class="badge"><%= v.getLibelleStatut() %></span></td>
            <td>
                <a href="vehicule-detail.jsp?id=<%= v.getIdVehicule() %>">Détails</a> |
                <a href="vehicule-form.jsp?id=<%= v.getIdVehicule() %>">Modifier</a>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>