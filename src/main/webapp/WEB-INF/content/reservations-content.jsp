<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.dto.StatusObjectDto" %>
<%@ page import="java.util.Map" %>

<%
    TrajetReservation reservation = (TrajetReservation) request.getAttribute("reservation");
    List<TrajetReservation> reservations = (List<TrajetReservation>) request.getAttribute("reservations");
    List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<StatusObjectDto> reservationStatuts = (List<StatusObjectDto>) request.getAttribute("reservationStatuts");
    Map<String, Long> statsByStatus = (Map<String, Long>) request.getAttribute("statsByStatus");
    Double totalPlacesPrises = (Double) request.getAttribute("totalPlacesPrises");
    Double totalPlacesRestantes = (Double) request.getAttribute("totalPlacesRestantes");
%>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Réservations</h1>

    <!-- Statistics Cards -->
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card border-left-primary shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                Total Réservations
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= reservations != null ? reservations.size() : 0 %>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-ticket-detailed fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card border-left-success shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                Places Prises
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%.0f", totalPlacesPrises != null ? totalPlacesPrises : 0) %>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-people-fill fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card border-left-info shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                Places Disponibles
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%.0f", totalPlacesRestantes != null ? totalPlacesRestantes : 0) %>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-chair fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <!-- Form Column -->
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

                        <div class="mb-3">
                            <label for="idTrajet" class="form-label">Trajet *</label>
                            <select class="form-control" id="idTrajet" name="idTrajet" required>
                                <option value="">Choisir...</option>
                                <% if (trajets != null) {
                                    for (Trajet t : trajets) { %>
                                <option value="<%= t.getId() %>"
                                        <%= (reservation != null && reservation.getTrajet() != null && reservation.getTrajet().getId().equals(t.getId())) ? "selected" : "" %>>
                                    <%= t.getLigne().getVilleDepart().getNom() %> → <%= t.getLigne().getVilleArrivee().getNom() %>
                                    (<%= t.getDatetimeDepart() %>)
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="idClient" class="form-label">Client *</label>
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
                            <label for="nomPassager" class="form-label">Nom Passager *</label>
                            <input type="text" class="form-control" id="nomPassager" name="nomPassager"
                                   value="<%= reservation != null ? reservation.getNomPassager() : "" %>" required>
                        </div>

                        <div class="mb-3">
                            <label for="idReservationStatut" class="form-label">Statut *</label>
                            <select class="form-control" id="idReservationStatut" name="idReservationStatut" required>
                                <option value="">Choisir...</option>
                                <% if (reservationStatuts != null) {
                                    for (StatusObjectDto s : reservationStatuts) { %>
                                <option value="<%= s.getId() %>"
                                        <%= (reservation != null && reservation.getReservationStatut() != null && reservation.getReservationStatut().getId().equals(s.getId())) ? "selected" : "" %>>
                                    <%= s.getLibelle() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>

                        <div class="alert alert-info" role="alert">
                            <small><i class="bi bi-info-circle"></i> Les détails des places et paiements seront ajoutés après la création.</small>
                        </div>

                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-save"></i> Enregistrer
                        </button>
                        <% if (reservation != null) { %>
                        <a href="<%= request.getContextPath() %>/reservations" class="btn btn-secondary">Annuler</a>
                        <% } %>
                    </form>
                </div>
            </div>

            <!-- Stats by Status -->
            <% if (statsByStatus != null && !statsByStatus.isEmpty()) { %>
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Réservations par Statut</h6>
                </div>
                <div class="card-body">
                    <% for (Map.Entry<String, Long> entry : statsByStatus.entrySet()) { %>
                    <div class="mb-2">
                        <div class="d-flex justify-content-between">
                            <span><%= entry.getKey() %></span>
                            <span class="badge badge-primary"><%= entry.getValue() %></span>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>

        <!-- List Column -->
        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Réservations</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover" width="100%" cellspacing="0">
                            <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Trajet</th>
                                <th>Client</th>
                                <th>Passager</th>
                                <th>Date</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (reservations != null && !reservations.isEmpty()) {
                                for (TrajetReservation r : reservations) { %>
                            <tr>
                                <td><%= r.getId() %></td>
                                <td>
                                    <%= r.getTrajet().getLigne().getVilleDepart().getNom() %> →
                                    <%= r.getTrajet().getLigne().getVilleArrivee().getNom() %>
                                    <br>
                                    <small class="text-muted"><%= r.getTrajet().getDatetimeDepart() %></small>
                                </td>
                                <td><%= r.getClient().getNomClient() %></td>
                                <td><%= r.getNomPassager() %></td>
                                <td><%= r.getDateReservation() %></td>
                                <td>
                                    <span class="badge badge-info">
                                        <%= r.getReservationStatut().getLibelle() %>
                                    </span>
                                </td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/reservations?action=edit&id=<%= r.getId() %>"
                                       class="btn btn-sm btn-warning" title="Modifier">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <a href="<%= request.getContextPath() %>/reservations/detail?id=<%= r.getId() %>"
                                       class="btn btn-sm btn-primary" title="Détails">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                </td>
                            </tr>
                            <% }
                            } else { %>
                            <tr>
                                <td colspan="7" class="text-center">Aucune réservation trouvée.</td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>