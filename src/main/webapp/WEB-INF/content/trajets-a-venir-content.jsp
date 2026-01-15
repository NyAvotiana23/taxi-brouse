<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.Trajet" %>
<%@ page import="com.mdgtaxi.entity.Ligne" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%
    List<Ligne> lignes = (List<Ligne>) request.getAttribute("lignes");
    Long selectedLigneId = (Long) request.getAttribute("selectedLigneId");
    List<LocalDate> dates = (List<LocalDate>) request.getAttribute("dates");
    LocalDate selectedDate = (LocalDate) request.getAttribute("selectedDate");
    List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
%>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Trajets à Venir</h1>

    <!-- Ligne Filter -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Filtrer par Ligne</h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/trajets-a-venir" method="get">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <select class="form-control" name="idLigne" id="idLigne">
                            <option value="">Toutes les Lignes</option>
                            <% if (lignes != null) {
                                for (Ligne l : lignes) { %>
                            <option value="<%= l.getId() %>" <%= (selectedLigneId != null && selectedLigneId.equals(l.getId())) ? "selected" : "" %>>
                                <%= l.getVilleDepart().getNom() %> → <%= l.getVilleArrivee().getNom() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-primary">Filtrer</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Date Filter (appears only if Ligne is selected) -->
    <% if (selectedLigneId != null) { %>
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Filtrer par Date</h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/trajets-a-venir" method="get">
                <input type="hidden" name="idLigne" value="<%= selectedLigneId %>">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <select class="form-control" name="date" id="date">
                            <option value="">Toutes les Dates</option>
                            <% if (dates != null) {
                                for (LocalDate d : dates) { %>
                            <option value="<%= d.toString() %>" <%= (selectedDate != null && selectedDate.equals(d)) ? "selected" : "" %>>
                                <%= d %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <button type="submit" class="btn btn-primary">Filtrer</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <% } %>

    <!-- Filtered Trajets List -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Liste des Trajets à Venir</h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Ligne</th>
                        <th>Date Début</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (trajets != null && !trajets.isEmpty()) {
                        for (Trajet t : trajets) { %>
                    <tr>
                        <td><%= t.getId() %></td>
                        <td><%= t.getLigne().getVilleDepart().getNom() %> → <%= t.getLigne().getVilleArrivee().getNom() %></td>
                        <td><%= t.getDatetimeDepart() %></td>
                        <td><%= t.getTrajetStatut() != null ? t.getTrajetStatut().getLibelle() : "N/A" %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/trajets/detail?id=<%= t.getId() %>" class="btn btn-sm btn-primary">Détails</a>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="5" class="text-center">Aucun trajet à venir trouvé.</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>