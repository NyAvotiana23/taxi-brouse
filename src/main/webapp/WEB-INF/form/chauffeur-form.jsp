<%@ page import="com.mdgtaxi.entity.Chauffeur" %>
<%
    Chauffeur chauffeur = (Chauffeur) request.getAttribute("chauffeur");
    String error = (String) request.getAttribute("error");
%>

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
        <% if (error != null) { %>
        <div class="alert alert-danger" role="alert">
            <%= error %>
        </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/chauffeures" method="post">
            <input type="hidden" name="id" value="<%= chauffeur != null ? chauffeur.getId() : "" %>">

            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="nom" class="form-label">Nom <span class="text-danger">*</span></label>
                        <input type="text"
                               class="form-control"
                               id="nom"
                               name="nom"
                               value="<%= chauffeur != null ? chauffeur.getNom() : "" %>"
                               required>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="prenom" class="form-label">Prénom <span class="text-danger">*</span></label>
                        <input type="text"
                               class="form-control"
                               id="prenom"
                               name="prenom"
                               value="<%= chauffeur != null ? chauffeur.getPrenom() : "" %>"
                               required>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="dateNaissance" class="form-label">Date de Naissance <span class="text-danger">*</span></label>
                        <input type="date"
                               class="form-control"
                               id="dateNaissance"
                               name="dateNaissance"
                               value="<%= chauffeur != null ? chauffeur.getDateNaissance() : "" %>"
                               required>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="numeroPermis" class="form-label">Numéro de Permis <span class="text-danger">*</span></label>
                        <input type="text"
                               class="form-control"
                               id="numeroPermis"
                               name="numeroPermis"
                               value="<%= chauffeur != null ? chauffeur.getNumeroPermis() : "" %>"
                               required>
                    </div>
                </div>
            </div>

            <div class="mt-4">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Enregistrer
                </button>
                <a href="<%= request.getContextPath() %>/chauffeures" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Annuler
                </a>
            </div>
        </form>
    </div>
</div>