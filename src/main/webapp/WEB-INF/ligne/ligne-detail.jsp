<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%
    Ligne ligne = (Ligne) request.getAttribute("ligne");
    List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails de la Ligne</title>
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
                    <h1 class="h3 text-gray-800">Détails de la Ligne</h1>
                    <a href="<%= request.getContextPath() %>/lignes" class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Retour
                    </a>
                </div>

                <!-- Ligne Information Card -->
                <div class="row mb-4">
                    <div class="col-12">
                        <div class="card shadow">
                            <div class="card-header py-3 bg-primary text-white">
                                <h6 class="m-0 font-weight-bold">
                                    <i class="bi bi-signpost-2-fill"></i> Informations de la Ligne
                                </h6>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <h5 class="text-primary">
                                            <i class="bi bi-geo-fill text-success"></i> 
                                            <%= ligne.getVilleDepart().getNom() %>
                                        </h5>
                                        <p class="text-muted">Ville de Départ</p>
                                    </div>
                                    <div class="col-md-6">
                                        <h5 class="text-primary">
                                            <i class="bi bi-geo-alt-fill text-danger"></i> 
                                            <%= ligne.getVilleArrivee().getNom() %>
                                        </h5>
                                        <p class="text-muted">Ville d'Arrivée</p>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-12">
                                        <p class="mb-2">
                                            <strong><i class="bi bi-rulers"></i> Distance:</strong> 
                                            <span class="badge bg-info fs-6"><%= ligne.getDistanceKm() %> km</span>
                                        </p>
                                        <p class="mb-0">
                                            <strong><i class="bi bi-arrow-left-right"></i> Itinéraire:</strong> 
                                            <%= ligne.getVilleDepart().getNom() %> → <%= ligne.getVilleArrivee().getNom() %>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Trajets List -->
                <div class="row">
                    <div class="col-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">
                                    <i class="bi bi-list-ul"></i> Trajets sur cette Ligne 
                                    <span class="badge bg-primary"><%= trajets != null ? trajets.size() : 0 %></span>
                                </h6>
                            </div>
                            <div class="card-body">
                                <% if (trajets != null && !trajets.isEmpty()) { %>
                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover">
                                            <thead class="table-light">
                                                <tr>
                                                    <th><i class="bi bi-person"></i> Chauffeur</th>
                                                    <th><i class="bi bi-car-front"></i> Véhicule</th>
                                                    <th><i class="bi bi-calendar-event"></i> Date Départ</th>
                                                    <th><i class="bi bi-calendar-check"></i> Date Arrivée</th>
                                                    <th><i class="bi bi-people"></i> Passagers</th>
                                                    <th><i class="bi bi-cash"></i> Frais</th>
                                                    <th><i class="bi bi-info-circle"></i> Statut</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% for (Trajet t : trajets) { %>
                                                    <tr>
                                                        <td><%= t.getChauffeur().getNom() %> <%= t.getChauffeur().getPrenom() %></td>
                                                        <td><%= t.getVehicule().getImmatriculation() %></td>
                                                        <td><%= t.getDatetimeDepart() %></td>
                                                        <td><%= t.getDatetimeArrivee() != null ? t.getDatetimeArrivee() : "-" %></td>
                                                        <td><%= t.getNombrePassager() %></td>
                                                        <td><%= t.getFraisUnitaire() %> Ar</td>
                                                        <td>
                                                            <span class="badge bg-info">
                                                                <%= t.getTrajetStatut().getLibelle() %>
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <a href="<%= request.getContextPath() %>/trajets/detail?id=<%= t.getId() %>" 
                                                               class="btn btn-sm btn-success">
                                                                <i class="bi bi-eye"></i> Voir
                                                            </a>
                                                        </td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    </div>
                                <% } else { %>
                                    <div class="alert alert-info" role="alert">
                                        <i class="bi bi-info-circle"></i> Aucun trajet programmé pour cette ligne.
                                    </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Statistics Card -->
                <% if (trajets != null && !trajets.isEmpty()) { %>
                <div class="row">
                    <div class="col-md-4">
                        <div class="card border-left-primary shadow h-100 py-2">
                            <div class="card-body">
                                <div class="row no-gutters align-items-center">
                                    <div class="col mr-2">
                                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                            Total Trajets
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <%= trajets.size() %>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="bi bi-list-check fs-2 text-gray-300"></i>
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
                                            Distance Totale
                                        </div>
                                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                                            <%= ligne.getDistanceKm() %> km
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="bi bi-rulers fs-2 text-gray-300"></i>
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
                                            Itinéraire
                                        </div>
                                        <div class="h6 mb-0 font-weight-bold text-gray-800">
                                            <%= ligne.getVilleDepart().getNom() %> → <%= ligne.getVilleArrivee().getNom() %>
                                        </div>
                                    </div>
                                    <div class="col-auto">
                                        <i class="bi bi-signpost-2 fs-2 text-gray-300"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
        </div>
    </div>

    <jsp:include page="/WEB-INF/includes/footer.jsp" />
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
