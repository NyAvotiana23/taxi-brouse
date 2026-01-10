<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.Chauffeur" %>
<%@ page import="java.util.List" %>
<%
    Chauffeur chauffeur = (Chauffeur) request.getAttribute("chauffeur");
    List<Chauffeur> chauffeurs = (List<Chauffeur>) request.getAttribute("chauffeurs");
%>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Chauffeurs</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <% if (chauffeur != null) { %>
                            Modifier Chauffeur
                        <% } else { %>
                            Nouveau Chauffeur
                        <% } %>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/chauffeures" method="post">
                        <input type="hidden" name="id" value="<%= chauffeur != null ? chauffeur.getId() : "" %>">

                        <div class="mb-3">
                            <label for="nom" class="form-label">Nom</label>
                            <input type="text" class="form-control" id="nom" name="nom" 
                                value="<%= chauffeur != null ? chauffeur.getNom() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="prenom" class="form-label">Prénom</label>
                            <input type="text" class="form-control" id="prenom" name="prenom" 
                                value="<%= chauffeur != null ? chauffeur.getPrenom() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="dateNaissance" class="form-label">Date de Naissance</label>
                            <input type="date" class="form-control" id="dateNaissance" name="dateNaissance" 
                                value="<%= chauffeur != null ? chauffeur.getDateNaissance() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="numeroPermis" class="form-label">Numéro Permis</label>
                            <input type="text" class="form-control" id="numeroPermis" name="numeroPermis" 
                                value="<%= chauffeur != null ? chauffeur.getNumeroPermis() : "" %>" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="<%= request.getContextPath() %>/chauffeures" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Chauffeurs</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Nom</th>
                                    <th>Prénom</th>
                                    <th>Date Naissance</th>
                                    <th>Permis</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (chauffeurs != null) {
                                    for (Chauffeur c : chauffeurs) { %>
                                        <tr>
                                            <td><%= c.getNom() %></td>
                                            <td><%= c.getPrenom() %></td>
                                            <td><%= c.getDateNaissance() %></td>
                                            <td><%= c.getNumeroPermis() %></td>
                                            <td>
                                                <a href="<%= request.getContextPath() %>/chauffeures?action=edit&id=<%= c.getId() %>" class="btn btn-sm btn-info">Modifier</a>
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
