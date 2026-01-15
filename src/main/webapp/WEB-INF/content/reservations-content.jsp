<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%
    TrajetReservation reservation = (TrajetReservation) request.getAttribute("reservation");
    List<TrajetReservationDetails> reservationDetails = (List<TrajetReservationDetails>) request.getAttribute("reservationDetails");
    List<TrajetReservation> reservations = (List<TrajetReservation>) request.getAttribute("reservations");
    List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<TypeObjectDTO> typePlaces = (List<TypeObjectDTO>) request.getAttribute("typePlaces");
    Map<String, Long> statsByStatus = (Map<String, Long>) request.getAttribute("statsByStatus");
    Double totalPlacesPrises = (Double) request.getAttribute("totalPlacesPrises");
    Double totalPlacesRestantes = (Double) request.getAttribute("totalPlacesRestantes");
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
                                                Places Prises
                                            </div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"><%= totalPlacesPrises != null ? String.format("%.1f", totalPlacesPrises) : "0" %></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card border-left-warning shadow h-100 py-2">
                                <div class="card-body">
                                    <div class="row no-gutters align-items-center">
                                        <div class="col mr-2">
                                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                                Places Restantes
                                            </div>
                                            <div class="h5 mb-0 font-weight-bold text-gray-800"><%= totalPlacesRestantes != null ? String.format("%.1f", totalPlacesRestantes) : "0" %></div>
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

    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <div class="row">
        <div class="col-md-5">
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
                    <form action="<%= request.getContextPath() %>/reservations" method="post" id="reservationForm">
                        <input type="hidden" name="id" value="<%= reservation != null ? reservation.getId() : "" %>">

                        <div class="mb-3">
                            <label for="idTrajet" class="form-label">Trajet *</label>
                            <select class="form-control" id="idTrajet" name="idTrajet" required>
                                <option value="">Choisir...</option>
                                <% if (trajets != null) {
                                    for (Trajet t : trajets) { %>
                                <option value="<%= t.getId() %>"
                                        <%= (preselectedTrajetId != null && preselectedTrajetId.equals(t.getId().toString())) || (reservation != null && reservation.getTrajet() != null && reservation.getTrajet().getId().equals(t.getId())) ? "selected" : "" %>>
                                    <%= t.getLigne().getVilleDepart().getNom() %> → <%= t.getLigne().getVilleArrivee().getNom() %> (<%= t.getDatetimeDepart() %>)
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
                                <!-- Will be loaded from database -->
                            </select>
                        </div>

                        <hr>
                        <h6 class="font-weight-bold text-primary mb-3">Types de Places</h6>
                        <div id="placesContainer">
                            <% if (reservationDetails != null && !reservationDetails.isEmpty()) {
                                for (TrajetReservationDetails detail : reservationDetails) { %>
                            <div class="row mb-2 place-row">
                                <div class="col-md-6">
                                    <select class="form-control" name="typePlaceId[]" required>
                                        <option value="">Type de place...</option>
                                        <% if (typePlaces != null) {
                                            for (TypeObjectDTO tp : typePlaces) { %>
                                        <option value="<%= tp.getId() %>" <%= detail.getTypePlace().getId().equals(tp.getId()) ? "selected" : "" %>>
                                            <%= tp.getLibelle() %>
                                        </option>
                                        <% }
                                        } %>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <input type="number" step="0.5" min="0" class="form-control"
                                           name="nombrePlaces[]" placeholder="Nombre"
                                           value="<%= detail.getNombrePlaces() %>" required>
                                </div>
                                <div class="col-md-2">
                                    <button type="button" class="btn btn-danger btn-sm remove-place">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </div>
                            </div>
                            <% }
                            } else { %>
                            <div class="row mb-2 place-row">
                                <div class="col-md-6">
                                    <select class="form-control" name="typePlaceId[]" required>
                                        <option value="">Type de place...</option>
                                        <% if (typePlaces != null) {
                                            for (TypeObjectDTO tp : typePlaces) { %>
                                        <option value="<%= tp.getId() %>"><%= tp.getLibelle() %></option>
                                        <% }
                                        } %>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <input type="number" step="0.5" min="0" class="form-control"
                                           name="nombrePlaces[]" placeholder="Nombre" required>
                                </div>
                                <div class="col-md-2">
                                    <button type="button" class="btn btn-danger btn-sm remove-place">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </div>
                            </div>
                            <% } %>
                        </div>

                        <button type="button" class="btn btn-secondary btn-sm mb-3" id="addPlaceBtn">
                            <i class="bi bi-plus-circle"></i> Ajouter un type de place
                        </button>

                        <div class="mt-3">
                            <button type="submit" class="btn btn-primary">Enregistrer</button>
                            <a href="<%= request.getContextPath() %>/reservations" class="btn btn-secondary">Annuler</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-7">
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
                                <th>Date</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (reservations != null) {
                                for (TrajetReservation r : reservations) { %>
                            <tr>
                                <td><%= r.getTrajet().getLigne().getVilleDepart().getNom() %> → <%= r.getTrajet().getLigne().getVilleArrivee().getNom() %></td>
                                <td><%= r.getClient().getNomClient() %></td>
                                <td><%= r.getNomPassager() %></td>
                                <td><%= r.getDateReservation() != null ? r.getDateReservation().toString().substring(0, 16) : "-" %></td>
                                <td><%= r.getReservationStatut().getLibelle() %></td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/reservations/detail?id=<%= r.getId() %>"
                                       class="btn btn-sm btn-primary" title="Détails">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                    <a href="<%= request.getContextPath() %>/reservations?action=edit&id=<%= r.getId() %>"
                                       class="btn btn-sm btn-info" title="Modifier">
                                        <i class="bi bi-pencil"></i>
                                    </a>
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

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Add new place row
        document.getElementById('addPlaceBtn').addEventListener('click', function() {
            const container = document.getElementById('placesContainer');
            const newRow = document.querySelector('.place-row').cloneNode(true);

            // Clear values
            newRow.querySelectorAll('select, input').forEach(el => {
                el.value = '';
            });

            container.appendChild(newRow);
            attachRemoveHandlers();
        });

        // Remove place row
        function attachRemoveHandlers() {
            document.querySelectorAll('.remove-place').forEach(btn => {
                btn.onclick = function() {
                    const rows = document.querySelectorAll('.place-row');
                    if (rows.length > 1) {
                        this.closest('.place-row').remove();
                    } else {
                        alert('Au moins un type de place est requis');
                    }
                };
            });
        }

        attachRemoveHandlers();
    });
</script>