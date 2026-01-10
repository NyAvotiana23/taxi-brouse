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
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails du Trajet</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
</head>
<body>
    <jsp:include page="/WEB-INF/includes/navbar.jsp" />
    
    <div class="d-flex">
        <jsp:include page="/WEB-INF/includes/sidebar.jsp" />
        
        <div class="flex-grow-1 p-4">
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
                                        <p><strong>Véhicule:</strong> 
                                            <%= trajet.getVehicule().getMarque() %> <%= trajet.getVehicule().getModele() %> 
                                            (<%= trajet.getVehicule().getImmatriculation() %>)
                                            <strong>Passager supporter :  <%= trajet.getVehicule().getMaximumPassager() %> </strong>
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>Date de Départ:</strong> <%= trajet.getDatetimeDepart() %></p>
                                        <p><strong>Date d'Arrivée:</strong> 
                                            <%= trajet.getDatetimeArrivee() != null ? trajet.getDatetimeArrivee() : "Non définie" %>
                                        </p>
                                        <p><strong>Nombre de Passagers maximum : </strong> <%= trajet.getNombrePassager() %></p>
                                        <p><strong>Frais Unitaire:</strong> <%= trajet.getFraisUnitaire() %> Ar</p>
                                        <p><strong>Place restante  :</strong> <%=placesRestantes%> </p>
                                        <p><strong>Place prises  :</strong> <%=placesPrises%> </p>

                                        <p><strong>Chiffre d'affaire  :</strong> <%=chiffredaffaire%> </p>

                                        <p><strong>Statut:</strong> 
                                            <span class="badge bg-info"><%= trajet.getTrajetStatut().getLibelle() %></span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Reservation Form and List -->
                <div class="row">
                    <!-- Reservation Form -->
                    <div class="col-md-5">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3 bg-success text-white">
                                <h6 class="m-0 font-weight-bold">Nouvelle Réservation</h6>
                            </div>
                            <div class="card-body">
                                <form action="<%= request.getContextPath() %>/reservations" method="post">
                                    <input type="hidden" name="idTrajet" value="<%= trajet.getId() %>">
                                    <input type="hidden" name="returnToTrajet" value="<%= trajet.getId() %>">

                                    <div class="mb-3">
                                        <label for="idClient" class="form-label">Client</label>
                                        <select class="form-control" id="idClient" name="idClient" required>
                                            <option value="">Choisir un client...</option>
                                            <% if (clients != null) {
                                                for (Client c : clients) { %>
                                                    <option value="<%= c.getId() %>"><%= c.getNomClient() %></option>
                                                <% }
                                            } %>
                                        </select>
                                    </div>

                                    <div class="mb-3">
                                        <label for="nomPassager" class="form-label">Nom du Passager</label>
                                        <input type="text" class="form-control" id="nomPassager" name="nomPassager" required>
                                    </div>

                                    <div class="mb-3">
                                        <label for="numeroSiege" class="form-label">Numéro de Siège</label>
                                        <input type="text" class="form-control" id="numeroSiege" name="numeroSiege">
                                    </div>

                                    <div class="mb-3">
                                        <label for="nombrePlaceReservation" class="form-label">Nombre de Places</label>
                                        <input type="number" class="form-control" id="nombrePlaceReservation" 
                                            name="nombrePlaceReservation" value="1" min="1" required>
                                    </div>

<%--                                    <div class="mb-3">--%>
<%--                                        <label for="idReservationStatut" class="form-label">Statut</label>--%>
<%--                                        <select class="form-control" id="idReservationStatut" name="idReservationStatut" required>--%>
<%--                                            <option value="">Choisir un statut...</option>--%>
<%--                                            <% if (reservationStatuts != null) {--%>
<%--                                                for (TypeObjectDTO s : reservationStatuts) { %>--%>
<%--                                                    <option value="<%= s.getId() %>"><%= s.getLibelle() %></option>--%>
<%--                                                <% }--%>
<%--                                            } %>--%>
<%--                                        </select>--%>
<%--                                    </div>--%>

                                    <button type="submit" class="btn btn-success w-100">
                                        <i class="bi bi-plus-circle"></i> Créer la Réservation
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Reservations List -->
                    <div class="col-md-7">
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
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% for (TrajetReservation r : reservations) { %>
                                                    <tr>
                                                        <td><%= r.getClient().getNomClient() %></td>
                                                        <td><%= r.getNomPassager() %></td>
                                                        <td><%= r.getNumeroSiege() != null ? r.getNumeroSiege() : "-" %></td>
                                                        <td><%= r.getNombrePlaceReservation() %></td>
                                                        <td>
                                                            <span class="badge bg-info">
                                                                <%= r.getReservationStatut().getLibelle() %>
                                                            </span>
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
        </div>
    </div>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
