<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.Vehicule" %>
<%@ page import="java.util.List" %>
<%
    Vehicule vehicule = (Vehicule) request.getAttribute("vehicule");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
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
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="<%= request.getContextPath() %>/vehicules?action=edit&id=<%= v.getId() %>"
                                               class="btn btn-sm btn-warning"
                                               title="Modifier">
                                                <i class="fas fa-edit"></i>
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