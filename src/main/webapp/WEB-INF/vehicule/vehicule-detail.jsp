<%@ page import="com.mdgtaxi.service.VehiculeService" %>
<%@ page import="com.mdgtaxi.view.VmVehiculeDetail" %>
<%@ page import="com.mdgtaxi.view.VmVehiculeCoutEntretien" %>
<%@ page import="com.mdgtaxi.entity.VehiculeEntretien" %>
<%@ page import="java.util.List" %>
<%
    Long id = Long.parseLong(request.getParameter("id"));
    VehiculeService service = new VehiculeService();
    VmVehiculeDetail detail = service.getVehiculeDetail(id);
    VmVehiculeCoutEntretien cout = service.getCoutEntretien(id);
    List<VehiculeEntretien> entretiens = service.getEntretiensByVehicule(id);
%>
<div class="container">
    <h3>Détails du Véhicule : <%= detail.getImmatriculation() %></h3>

    <div class="card">
        <div class="card-body">
            <p><strong>Marque :</strong> <%= detail.getMarque() %></p>
            <p><strong>Capacité :</strong> <%= detail.getMaximumPassager() %> places</p>
            <p><strong>Consommation :</strong> <%= detail.getDepenseCarburant100km() %> L/100km</p>
            <p><strong>Coût Total Entretien :</strong> <%= (cout != null) ? cout.getTotalDepenseEntretien() : 0 %> Ar</p>
        </div>
    </div>

    <h4>Changer le Statut</h4>
    <form action="../../vehicule-action" method="POST">
        <input type="hidden" name="idVehicule" value="<%= id %>">
        <select name="idStatut">
            <option value="1">En Service</option>
            <option value="2">En Entretien</option>
            <option value="3">Hors Service</option>
        </select>
        <button type="submit" name="action" value="changeStatut">Mettre à jour</button>
    </form>

    <h4>Historique des Entretiens</h4>
    <ul>
        <% for(VehiculeEntretien e : entretiens) { %>
        <li><%= e.getDateDebutEntretien() %> : <%= e.getMotif() %> (<%= e.getMontantDepense() %> Ar)</li>
        <% } %>
    </ul>
</div>