<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.Chauffeur" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.Period" %>
<%@ page import="java.time.LocalDate" %>
<%
    Chauffeur chauffeur = (Chauffeur) request.getAttribute("chauffeur");
    List<Chauffeur> chauffeurs = (List<Chauffeur>) request.getAttribute("chauffeurs");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>

<div class="container-fluid">
    <div class="d-sm-flex align-items-center justify-content-between mb-4">
        <h1 class="h3 mb-0 text-gray-800">Gestion des Chauffeurs</h1>
        <% if (chauffeur == null) { %>
        <a href="<%= request.getContextPath() %>/chauffeures?action=edit" class="btn btn-primary">
            <i class="fas fa-plus"></i> Nouveau Chauffeur
        </a>
        <% } %>
    </div>

    <div class="row">
        <% if (chauffeur != null || request.getParameter("action") != null && "edit".equals(request.getParameter("action"))) { %>
        <div class="col-lg-4">
            <jsp:include page="/WEB-INF/form/chauffeur-form.jsp" />
        </div>
        <div class="col-lg-8">
            <% } else { %>
            <div class="col-lg-12">
                <% } %>

                <div class="card shadow mb-4">
                    <div class="card-header py-3 d-flex justify-content-between align-items-center">
                        <h6 class="m-0 font-weight-bold text-primary">Liste des Chauffeurs</h6>
                        <span class="badge badge-info">
                            <%= chauffeurs != null ? chauffeurs.size() : 0 %> chauffeur(s)
                        </span>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover" id="dataTable" width="100%" cellspacing="0">
                                <thead class="thead-light">
                                <tr>
                                    <th>Nom Complet</th>
                                    <th>Date de Naissance</th>
                                    <th>Âge</th>
                                    <th>Numéro Permis</th>
                                    <th class="text-center">Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% if (chauffeurs != null && !chauffeurs.isEmpty()) {
                                    for (Chauffeur c : chauffeurs) {
                                        int age = Period.between(c.getDateNaissance(), LocalDate.now()).getYears();
                                %>
                                <tr>
                                    <td>
                                        <strong><%= c.getNom() %> <%= c.getPrenom() %></strong>
                                    </td>
                                    <td><%= c.getDateNaissance().format(dateFormatter) %></td>
                                    <td>
                                                    <span class="badge badge-secondary">
                                                        <%= age %> ans
                                                    </span>
                                    </td>
                                    <td><%= c.getNumeroPermis() %></td>
                                    <td class="text-center">
                                        <div class="btn-group" role="group">
                                            <a href="<%= request.getContextPath() %>/chauffeures/detail?id=<%= c.getId() %>"
                                               class="btn btn-sm btn-info"
                                               title="Voir détails">
                                                <i class="fas fa-eye"></i>
                                            </a>
                                            <a href="<%= request.getContextPath() %>/chauffeures?action=edit&id=<%= c.getId() %>"
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
                                    <td colspan="5" class="text-center text-muted">
                                        <i class="fas fa-info-circle"></i> Aucun chauffeur enregistré
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