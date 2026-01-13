<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.Ligne" %>
<%@ page import="com.mdgtaxi.entity.Ville" %>
<%@ page import="java.util.List" %>
<%
    Ligne ligne = (Ligne) request.getAttribute("ligne");
    List<Ligne> lignes = (List<Ligne>) request.getAttribute("lignes");
    List<Ville> villes = (List<Ville>) request.getAttribute("villes");
%>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Lignes</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <% if (ligne != null) { %>
                        Modifier Ligne
                        <% } else { %>
                        Nouvelle Ligne
                        <% } %>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/lignes" method="post">
                        <input type="hidden" name="id" value="<%= ligne != null ? ligne.getId() : "" %>">

                        <div class="mb-3">
                            <label for="idVilleDepart" class="form-label">Ville Départ</label>
                            <select class="form-control" id="idVilleDepart" name="idVilleDepart" required>
                                <option value="">Choisir...</option>
                                <% if (villes != null) {
                                    for (Ville v : villes) { %>
                                <option value="<%= v.getId() %>"
                                        <%= (ligne != null && ligne.getVilleDepart() != null && ligne.getVilleDepart().getId().equals(v.getId())) ? "selected" : "" %>>
                                    <%= v.getNom() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idVilleArrivee" class="form-label">Ville Arrivée</label>
                            <select class="form-control" id="idVilleArrivee" name="idVilleArrivee" required>
                                <option value="">Choisir...</option>
                                <% if (villes != null) {
                                    for (Ville v : villes) { %>
                                <option value="<%= v.getId() %>"
                                        <%= (ligne != null && ligne.getVilleArrivee() != null && ligne.getVilleArrivee().getId().equals(v.getId())) ? "selected" : "" %>>
                                    <%= v.getNom() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="distanceKm" class="form-label">Distance (km)</label>
                            <input type="number" step="0.01" class="form-control" id="distanceKm" name="distanceKm"
                                   value="<%= ligne != null ? ligne.getDistanceKm() : "" %>" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="<%= request.getContextPath() %>/lignes" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Lignes</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                            <tr>
                                <th>Départ</th>
                                <th>Arrivée</th>
                                <th>Distance</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (lignes != null) {
                                for (Ligne l : lignes) { %>
                            <tr>
                                <td><%= l.getVilleDepart().getNom() %></td>
                                <td><%= l.getVilleArrivee().getNom() %></td>
                                <td><%= l.getDistanceKm() %> km</td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/lignes/detail?id=<%= l.getId() %>" class="btn btn-sm btn-success">Détails</a>
                                    <a href="<%= request.getContextPath() %>/lignes?action=edit&id=<%= l.getId() %>" class="btn btn-sm btn-info">Modifier</a>
                                </td>
                            </tr>
                            <% }
                            } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>