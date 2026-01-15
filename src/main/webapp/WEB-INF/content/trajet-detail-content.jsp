<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%@ page import="java.math.BigDecimal" %>
<%
    Trajet trajet = (Trajet) request.getAttribute("trajet");
    int placesRestantes = (Integer) request.getAttribute("placesRestantes");
    int placesPrises = (Integer) request.getAttribute("placesPrises");
    BigDecimal chiffredaffaire =
            BigDecimal.valueOf(placesPrises)
                    .multiply(trajet.getFraisUnitaire());

    List<TrajetReservation> reservations = (List<TrajetReservation>) request.getAttribute("reservations");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    List<TypeObjectDTO> reservationStatuts = (List<TypeObjectDTO>) request.getAttribute("reservationStatuts");
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">Détails du Trajet</h1>
        <a href="<%= request.getContextPath() %>/trajets" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Retour
        </a>
    </div>

    <!-- Trajet Information Card -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="card shadow">
                <div class="card-header py-3 bg-primary text-white">
                    <h6 class="m-0 font-weight-bold">Informations du Trajet</h6>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Ligne:</strong>
                                <%= trajet.getLigne().getVilleDepart().getNom() %> →
                                <%= trajet.getLigne().getVilleArrivee().getNom() %>
                            </p>
                            <p><strong>Distance:</strong> <%= trajet.getLigne().getDistanceKm() %> km</p>
                            <p><strong>Chauffeur:</strong>
                                <%= trajet.getChauffeur().getNom() %> <%= trajet.getChauffeur().getPrenom() %>
                            </p>
                            <p><strong>Véhicule:</strong> <%= trajet.getVehicule().getImmatriculation() %>
                            </p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Date Départ:</strong> <%= trajet.getDatetimeDepart() %>
                            </p>
                            <p><strong>Date Arrivée:</strong> <%= trajet.getDatetimeArrivee() %>
                            </p>
                            <p><strong>Statut:</strong> <%= trajet.getTrajetStatut().getLibelle() %>
                            </p>
                            <p><strong>Frais Unitaire:</strong> <%= trajet.getFraisUnitaire() %> Ar</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Statistiques Cards -->
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card border-left-primary shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                Places Prises
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800"><%= placesPrises %>
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
            <div class="card border-left-success shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                Places Restantes
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800"><%= placesRestantes %>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-seat-fill fs-2 text-gray-300"></i>
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
                                Chiffre d'Affaires
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800"><%= chiffredaffaire %> Ar</div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-currency-exchange fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row mt-4">
        <div class="col-12">
            <div class="card shadow">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Ajouter une Réservation</h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/reservations" method="post">
                        <input type="hidden" name="idTrajet" value="<%= trajet.getId() %>">

                        <div class="mb-3">
                            <label for="idClient" class="form-label">Client</label>
                            <select class="form-control" id="idClient" name="idClient" required>
                                <option value="">Choisir...</option>
                                <% if (clients != null) {
                                    for (Client c : clients) { %>
                                <option value="<%= c.getId() %>"><%= c.getNomClient() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="nomPassager" class="form-label">Nom Passager</label>
                            <input type="text" class="form-control" id="nomPassager" name="nomPassager" required>
                        </div>

                        <div class="mb-3">
                            <label for="dateReservation" class="form-label">Date de reservation</label>
                            <input type="datetime-local" class="form-control" id="dateReservation" name="dateReservation" required>
                        </div>


                        <div class="mb-3">
                            <label for="numeroSiege" class="form-label">Numéro Siège</label>
                            <input type="text" class="form-control" id="numeroSiege" name="numeroSiege">
                        </div>
                        <div class="mb-3">
                            <label for="idReservationStatut" class="form-label">Statut</label>
                            <select class="form-control" id="idReservationStatut" name="idReservationStatut" required>
                                <option value="">Choisir...</option>
                                <% if (reservationStatuts != null) {
                                    for (TypeObjectDTO s : reservationStatuts) { %>
                                <option value="<%= s.getId() %>"><%= s.getLibelle() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="nombrePlaceReservation" class="form-label">Nombre Places</label>
                            <input type="number" class="form-control" id="nombrePlaceReservation"
                                   name="nombrePlaceReservation" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Liste des Reservations -->
    <div class="row">
        <div class="col-12">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        Réservations pour ce Trajet
                        <span class="badge bg-primary"><%= reservations != null ? reservations.size() : 0 %></span>
                    </h6>
                </div>
                <div class="card-body">
                    <% if (reservations != null && !reservations.isEmpty()) { %>
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover">
                            <thead class="table-light">
                            <tr>
                                <th>Client</th>
                                <th>Passager</th>
                                <th>Siège</th>
                                <th>Places</th>
                                <th>Statut</th>
                                <th>Detail</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (TrajetReservation r : reservations) { %>
                            <tr>
                                <td><%= r.getClient().getNomClient() %>
                                </td>
                                <td><%= r.getNomPassager() %>
                                </td>
                                <td><%= r.getNumeroSiege() != null ? r.getNumeroSiege() : "-" %>
                                </td>
                                <td><%= r.getNombrePlaceReservation() %>
                                </td>
                                <td>
                                                <span class="badge bg-info">
                                                    <%= r.getReservationStatut().getLibelle() %>
                                                </span>
                                </td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/reservations/detail?id=<%= r.getId() %>"
                                       class="btn btn-sm btn-primary">Détail</a>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                    <% } else { %>
                    <div class="alert alert-info" role="alert">
                        <i class="bi bi-info-circle"></i> Aucune réservation pour ce trajet.
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>