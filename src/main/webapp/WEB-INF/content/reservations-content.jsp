<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.mdgtaxi.service.TypeObjectDTO" %>
<%
    TrajetReservation reservation = (TrajetReservation) request.getAttribute("reservation");
    List<TrajetReservation> reservations = (List<TrajetReservation>) request.getAttribute("reservations");
    List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<TypeObjectDTO> reservationStatuts = (List<TypeObjectDTO>) request.getAttribute("reservationStatuts");
    Map<String, Long> statsByStatus = (Map<String, Long>) request.getAttribute("statsByStatus");
    Integer totalPlacesPrises = (Integer) request.getAttribute("totalPlacesPrises");
    Integer totalPlacesRestantes = (Integer) request.getAttribute("totalPlacesRestantes");
    String error = (String) request.getAttribute("error");

    String preselectedTrajetId = request.getParameter("idTrajet");
%>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Réservations</h1>

    <!-- Statistiques -->
    <div class="row mb-4">
        <div class="col-md-12">
            <div class="card shadow">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Statistiques des Réservations</h6>
                </div>
                <div class="card-body">
                    <div class="row">
                        <% if (statsByStatus != null) {
                            for (Map.Entry<String, Long> entry : statsByStatus.entrySet()) { %>
                                <div class="col-md-3">
                                    <div class="card border-left-primary shadow h-100 py-2">
                                        <div class="card-body">
                                            <div class="row no-gutters align-items-center">
                                                <div class="col mr-2">
                                                    <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                                        <%= entry.getKey() %>
                                                    </div>
                                                    <div class="h5 mb-0 font-weight-bold text-gray-800"><%= entry.getValue() %></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            <% }
                        } %>
                        <div class="col-md-3">
                            <div class="card border-left-success shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                                Total Places Prises
                                            </div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"><%= totalPlacesPrises != null ? totalPlacesPrises : 0 %></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card border-left-info shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                                Places Restantes
                                            </div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"><%= totalPlacesRestantes != null ? totalPlacesRestantes : 0 %></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Message d'erreur -->
    <% if (error != null && !error.isEmpty()) { %>
        <div class="row mb-4">
            <div class="col-md-12">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Erreur !</strong> <%= error %>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </div>
        </div>
    <% } %>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <% if (reservation != null) { %>
                            Modifier Réservation
                        <% } else { %>
                            Nouvelle Réservation
                        <% } %>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/reservations" method="post">
                        <input type="hidden" name="id" value="<%= reservation != null ? reservation.getId() : "" %>">
                        <input type="hidden" name="returnToTrajet" value="<%= preselectedTrajetId != null ? preselectedTrajetId : "" %>">

                        <div class="mb-3">
                            <label for="idTrajet" class="form-label">Trajet</label>
                            <select class="form-control" id="idTrajet" name="idTrajet" required>
                                <option value="">Choisir...</option>
                                <% if (trajets != null) {
                                    for (Trajet t : trajets) { 
                                        boolean selected = false;
                                        if (reservation != null && reservation.getTrajet() != null && reservation.getTrajet().getId().equals(t.getId())) {
                                            selected = true;
                                        } else if (preselectedTrajetId != null && t.getId().toString().equals(preselectedTrajetId)) {
                                            selected = true;
                                        }
                                %>
                                        <option value="<%= t.getId() %>" <%= selected ? "selected" : "" %>>
                                            <%= t.getLigne().getVilleDepart().getNom() %> -> <%= t.getLigne().getVilleArrivee().getNom() %> (<%= t.getDatetimeDepart() %>)
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idClient" class="form-label">Client</label>
                            <select class="form-control" id="idClient" name="idClient" required>
                                <option value="">Choisir...</option>
                                <% if (clients != null) {
                                    for (Client c : clients) { %>
                                        <option value="<%= c.getId() %>" 
                                            <%= (reservation != null && reservation.getClient() != null && reservation.getClient().getId().equals(c.getId())) ? "selected" : "" %>>
                                            <%= c.getNomClient() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="nomPassager" class="form-label">Nom Passager</label>
                            <input type="text" class="form-control" id="nomPassager" name="nomPassager" 
                                value="<%= reservation != null ? reservation.getNomPassager() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="numeroSiege" class="form-label">Numéro Siège</label>
                            <input type="text" class="form-control" id="numeroSiege" name="numeroSiege" 
                                value="<%= reservation != null && reservation.getNumeroSiege() != null ? reservation.getNumeroSiege() : "" %>">
                        </div>
                        <div class="mb-3">
                            <label for="nombrePlaceReservation" class="form-label">Nombre Places</label>
                            <input type="number" class="form-control" id="nombrePlaceReservation" name="nombrePlaceReservation" 
                                value="<%= reservation != null ? reservation.getNombrePlaceReservation() : "" %>" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="<%= request.getContextPath() %>/reservations" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Réservations</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Trajet</th>
                                    <th>Client</th>
                                    <th>Passager</th>
                                    <th>Siège</th>
                                    <th>Statut</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (reservations != null) {
                                    for (TrajetReservation r : reservations) { %>
                                        <tr>
                                            <td><%= r.getTrajet().getLigne().getVilleDepart().getNom() %> -> <%= r.getTrajet().getLigne().getVilleArrivee().getNom() %></td>
                                            <td><%= r.getClient().getNomClient() %></td>
                                            <td><%= r.getNomPassager() %></td>
                                            <td><%= r.getNumeroSiege() != null ? r.getNumeroSiege() : "-" %></td>
                                            <td><%= r.getReservationStatut().getLibelle() %></td>
                                            <td>
                                                <a href="<%= request.getContextPath() %>/reservations?action=edit&id=<%= r.getId() %>" class="btn btn-sm btn-info">Modifier</a>
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
