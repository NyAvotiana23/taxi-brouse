<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.view.VmTrajetPrevisionCaTotal" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%
    List<VmTrajetPrevisionCaTotal> previsions = (List<VmTrajetPrevisionCaTotal>) request.getAttribute("previsions");
    List<Ville> villes = (List<Ville>) request.getAttribute("villes");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
    String error = (String) request.getAttribute("error");

    // Calculate totals
    BigDecimal totalCaPrevisionnel = BigDecimal.ZERO;
    if (previsions != null) {
        for (VmTrajetPrevisionCaTotal p : previsions) {
            if (p.getCaPrevisionnel() != null) {
                totalCaPrevisionnel = totalCaPrevisionnel.add(p.getCaPrevisionnel());
            }
        }
    }
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">
            <i class="bi bi-graph-up-arrow"></i> Chiffre d'Affaires Prévisionnel
        </h1>
        <div>
            <a href="<%= request.getContextPath() %>/chiffre-affaire?type=reel" class="btn btn-info">
                <i class="bi bi-cash-stack"></i> CA Réel
            </a>
            <a href="<%= request.getContextPath() %>/chiffre-affaire?type=complet" class="btn btn-success">
                <i class="bi bi-clipboard-data"></i> CA Complet
            </a>
        </div>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i> <%= error %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <!-- Summary Cards -->
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card border-left-primary shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                Total Trajets
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= previsions != null ? previsions.size() : 0 %>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-bus-front fs-2 text-gray-300"></i>
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
                                CA Prévisionnel Total
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", totalCaPrevisionnel) %> Ar
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-currency-exchange fs-2 text-gray-300"></i>
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
                                CA Moyen par Trajet
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= previsions != null && !previsions.isEmpty()
                                        ? String.format("%,.2f", totalCaPrevisionnel.divide(BigDecimal.valueOf(previsions.size()), 2, BigDecimal.ROUND_HALF_UP))
                                        : "0.00" %> Ar
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-calculator fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Filters -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-funnel"></i> Filtres de Recherche
            </h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/chiffre-affaire" method="get">
                <input type="hidden" name="type" value="prevision">
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="idTrajet" class="form-label">ID Trajet</label>
                        <input type="number" class="form-control" id="idTrajet" name="idTrajet"
                               value="<%= request.getParameter("idTrajet") != null ? request.getParameter("idTrajet") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="villeDepart" class="form-label">Ville Départ</label>
                        <select class="form-control" id="villeDepart" name="villeDepart">
                            <option value="">Toutes</option>
                            <% if (villes != null) {
                                for (Ville v : villes) { %>
                            <option value="<%= v.getNom() %>"
                                    <%= request.getParameter("villeDepart") != null && request.getParameter("villeDepart").equals(v.getNom()) ? "selected" : "" %>>
                                <%= v.getNom() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="villeArrive" class="form-label">Ville Arrivée</label>
                        <select class="form-control" id="villeArrive" name="villeArrive">
                            <option value="">Toutes</option>
                            <% if (villes != null) {
                                for (Ville v : villes) { %>
                            <option value="<%= v.getNom() %>"
                                    <%= request.getParameter("villeArrive") != null && request.getParameter("villeArrive").equals(v.getNom()) ? "selected" : "" %>>
                                <%= v.getNom() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="vehicule" class="form-label">Véhicule</label>
                        <select class="form-control" id="vehicule" name="vehicule">
                            <option value="">Tous</option>
                            <% if (vehicules != null) {
                                for (Vehicule v : vehicules) { %>
                            <option value="<%= v.getImmatriculation() %>"
                                    <%= request.getParameter("vehicule") != null && request.getParameter("vehicule").equals(v.getImmatriculation()) ? "selected" : "" %>>
                                <%= v.getImmatriculation() %> - <%= v.getMarque() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="dateDebut" class="form-label">Date Début</label>
                        <input type="date" class="form-control" id="dateDebut" name="dateDebut"
                               value="<%= request.getParameter("dateDebut") != null ? request.getParameter("dateDebut") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="dateFin" class="form-label">Date Fin</label>
                        <input type="date" class="form-control" id="dateFin" name="dateFin"
                               value="<%= request.getParameter("dateFin") != null ? request.getParameter("dateFin") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="heureDebut" class="form-label">Heure Début</label>
                        <input type="time" class="form-control" id="heureDebut" name="heureDebut"
                               value="<%= request.getParameter("heureDebut") != null ? request.getParameter("heureDebut") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="heureFin" class="form-label">Heure Fin</label>
                        <input type="time" class="form-control" id="heureFin" name="heureFin"
                               value="<%= request.getParameter("heureFin") != null ? request.getParameter("heureFin") : "" %>">
                    </div>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-search"></i> Rechercher
                    </button>
                    <a href="<%= request.getContextPath() %>/chiffre-affaire?type=prevision" class="btn btn-secondary">
                        <i class="bi bi-arrow-counterclockwise"></i> Réinitialiser
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Results Table -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-table"></i> Liste des Trajets - CA Prévisionnel
            </h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover" width="100%">
                    <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Départ</th>
                        <th>Arrivée</th>
                        <th>Date</th>
                        <th>Heure</th>
                        <th>Véhicule</th>
                        <th>Chauffeur</th>
                        <th>Places Totales</th>
                        <th>Places Prises</th>
                        <th>Places Restantes</th>
                        <th>CA Prévisionnel</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (previsions != null && !previsions.isEmpty()) {
                        for (VmTrajetPrevisionCaTotal p : previsions) { %>
                    <tr>
                        <td><%= p.getIdTrajet() %></td>
                        <td><%= p.getNomVilleDepart() %></td>
                        <td><%= p.getNomVilleArrive() %></td>
                        <td><%= p.getDateDepart() %></td>
                        <td><%= p.getHeureDepart() %></td>
                        <td><%= p.getImmatriculationVehicule() %></td>
                        <td><%= p.getNomChauffeur() %> <%= p.getPrenomChauffeur() %></td>
                        <td class="text-center">
                            <span class="badge bg-secondary"><%= String.format("%.1f", p.getNombrePlacesTotales()) %></span>
                        </td>
                        <td class="text-center">
                            <span class="badge bg-primary"><%= String.format("%.1f", p.getNombrePlacesPrises()) %></span>
                        </td>
                        <td class="text-center">
                            <span class="badge bg-success"><%= String.format("%.1f", p.getNombrePlacesRestantes()) %></span>
                        </td>
                        <td class="text-end">
                            <strong class="text-success"><%= String.format("%,.2f", p.getCaPrevisionnel()) %> Ar</strong>
                        </td>
                        <td>
                            <a href="<%= request.getContextPath() %>/trajets/detail?id=<%= p.getIdTrajet() %>"
                               class="btn btn-sm btn-info" title="Voir détails">
                                <i class="bi bi-eye"></i>
                            </a>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="12" class="text-center text-muted py-4">
                            <i class="bi bi-inbox fs-3 d-block mb-2"></i>
                            Aucun trajet trouvé
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                    <% if (previsions != null && !previsions.isEmpty()) { %>
                    <tfoot class="table-light">
                    <tr>
                        <th colspan="10" class="text-end">Total:</th>
                        <th class="text-end">
                            <strong class="text-success"><%= String.format("%,.2f", totalCaPrevisionnel) %> Ar</strong>
                        </th>
                        <th></th>
                    </tr>
                    </tfoot>
                    <% } %>
                </table>
            </div>
        </div>
    </div>
</div>