<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.Vehicule" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%@ page import="java.util.List" %>
<%
    Vehicule vehicule = (Vehicule) request.getAttribute("vehicule");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
    List<TypeObjectDTO> vehiculeTypes = (List<TypeObjectDTO>) request.getAttribute("vehiculeTypes");
    List<TypeObjectDTO> carburantTypes = (List<TypeObjectDTO>) request.getAttribute("carburantTypes");
%>

<div class="container-fluid">
    <div class="d-sm-flex align-items-center justify-content-between mb-4">
        <h1 class="h3 mb-0 text-gray-800">Gestion des Véhicules</h1>
        <% if (vehicule == null) { %>
        <a href="<%= request.getContextPath() %>/vehicules?action=edit" class="btn btn-primary">
            <i class="fas fa-plus"></i> Nouveau Véhicule
        </a>
        <% } %>
    </div>

    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Filtres</h6>
        </div>
        <div class="card-body">
            <form method="get" action="<%= request.getContextPath() %>/vehicules">
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label>Marque</label>
                        <input type="text" name="filter_marque" class="form-control" value="<%= request.getParameter("filter_marque") != null ? request.getParameter("filter_marque") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label>Modèle</label>
                        <input type="text" name="filter_modele" class="form-control" value="<%= request.getParameter("filter_modele") != null ? request.getParameter("filter_modele") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label>Immatriculation</label>
                        <input type="text" name="filter_immatriculation" class="form-control" value="<%= request.getParameter("filter_immatriculation") != null ? request.getParameter("filter_immatriculation") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label>Capacité Passagers</label>
                        <input type="number" name="filter_maximumPassager" class="form-control" value="<%= request.getParameter("filter_maximumPassager") != null ? request.getParameter("filter_maximumPassager") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label>Capacité Carburant</label>
                        <input type="number" step="0.01" name="filter_capaciteCarburant" class="form-control" value="<%= request.getParameter("filter_capaciteCarburant") != null ? request.getParameter("filter_capaciteCarburant") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label>Dépense Carburant /100km</label>
                        <input type="number" step="0.01" name="filter_depenseCarburant100km" class="form-control" value="<%= request.getParameter("filter_depenseCarburant100km") != null ? request.getParameter("filter_depenseCarburant100km") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label>Type Véhicule</label>
                        <select name="filter_vehiculeType" class="form-control">
                            <option value="">Tous</option>
                            <% if (vehiculeTypes != null) {
                                for (TypeObjectDTO type : vehiculeTypes) { %>
                            <option value="<%= type.getLibelle() %>" <%= type.getLibelle().equals(request.getParameter("filter_vehiculeType")) ? "selected" : "" %>><%= type.getLibelle() %></option>
                            <% }} %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label>Type Carburant</label>
                        <select name="filter_carburantType" class="form-control">
                            <option value="">Tous</option>
                            <% if (carburantTypes != null) {
                                for (TypeObjectDTO type : carburantTypes) { %>
                            <option value="<%= type.getLibelle() %>" <%= type.getLibelle().equals(request.getParameter("filter_carburantType")) ? "selected" : "" %>><%= type.getLibelle() %></option>
                            <% }} %>
                        </select>
                    </div>
                </div>
                <div class="mt-3">
                    <button type="submit" class="btn btn-primary">Appliquer Filtres</button>
                    <a href="<%= request.getContextPath() %>/vehicules" class="btn btn-secondary">Réinitialiser</a>
                </div>
            </form>
        </div>
    </div>

    <div class="row">
        <% if (vehicule != null || request.getParameter("action") != null && "edit".equals(request.getParameter("action"))) { %>
        <div class="col-lg-5">
            <jsp:include page="/WEB-INF/form/vehicule-form.jsp" />
        </div>
        <div class="col-lg-7">
            <% } else { %>
            <div class="col-lg-12">
                <% } %>

                <div class="card shadow mb-4">
                    <div class="card-header py-3 d-flex justify-content-between align-items-center">
                        <h6 class="m-0 font-weight-bold text-primary">Liste des Véhicules</h6>
                        <span class="badge badge-info">
                            <%= vehicules != null ? vehicules.size() : 0 %> véhicule(s)
                        </span>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover" id="dataTable" width="100%" cellspacing="0">
                                <thead class="thead-light">
                                <tr>
                                    <th>Immatriculation</th>
                                    <th>Marque</th>
                                    <th>Modèle</th>
                                    <th>Type</th>
                                    <th>Carburant</th>
                                    <th>Capacité</th>
                                    <th class="text-center">Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% if (vehicules != null && !vehicules.isEmpty()) {
                                    for (Vehicule v : vehicules) { %>
                                <tr>
                                    <td>
                                        <strong><%= v.getImmatriculation() %></strong>
                                    </td>
                                    <td><%= v.getMarque() %></td>
                                    <td><%= v.getModele() %></td>
                                    <td>
                                                    <span class="badge badge-secondary">
                                                        <%= v.getVehiculeType().getLibelle() %>
                                                    </span>
                                    </td>
                                    <td>
                                                    <span class="badge badge-info">
                                                        <%= v.getCarburantType().getLibelle() %>
                                                    </span>
                                    </td>
                                    <td class="text-center">
                                        <i class="fas fa-users"></i> <%= v.getMaximumPassager() %>
                                    </td>
                                    <td class="text-center">
                                        <div class="btn-group" role="group">
                                            <a href="<%= request.getContextPath() %>/vehicules/detail?id=<%= v.getId() %>"
                                               class="btn btn-sm btn-info"
                                               title="Voir détails">
                                                Détails
                                            </a>
                                            <a href="<%= request.getContextPath() %>/vehicules?action=edit&id=<%= v.getId() %>"
                                               class="btn btn-sm btn-warning"
                                               title="Modifier">
                                                Modifier
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                                <% }
                                } else { %>
                                <tr>
                                    <td colspan="7" class="text-center text-muted">
                                        <i class="fas fa-info-circle"></i> Aucun véhicule enregistré
                                    </td>
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

    <style>
        .table thead th {
            border-bottom: 2px solid #dee2e6;
            font-weight: 600;
        }

        .btn-group .btn {
            margin: 0 2px;
        }

        .badge {
            padding: 0.35em 0.65em;
            font-size: 0.875rem;
        }
    </style>