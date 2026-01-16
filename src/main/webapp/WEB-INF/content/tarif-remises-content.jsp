<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>

<%
    TarifTypePlaceCategorieRemise tarifRemise = (TarifTypePlaceCategorieRemise) request.getAttribute("tarifRemise");
    List<TarifTypePlaceCategorieRemise> tarifRemises = (List<TarifTypePlaceCategorieRemise>) request.getAttribute("tarifRemises");
    List<TypePlace> typePlaces = (List<TypePlace>) request.getAttribute("typePlaces");
    List<TypeObjectDTO> categories = (List<TypeObjectDTO>) request.getAttribute("categories");
    String error = (String) request.getAttribute("error");
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">
            <i class="bi bi-percent"></i> Gestion des Tarifs avec Remise
        </h1>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i> <%= error %>
        <button type="button" class="close" data-dismiss="alert">
            <span>&times;</span>
        </button>
    </div>
    <% } %>

    <div class="row">
        <!-- Form Column -->
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3 bg-primary text-white">
                    <h6 class="m-0 font-weight-bold">
                        <i class="bi bi-<%= tarifRemise != null ? "pencil" : "plus" %>-circle"></i>
                        <% if (tarifRemise != null) { %>
                        Modifier Tarif Remise
                        <% } else { %>
                        Nouveau Tarif Remise
                        <% } %>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/tarif-remises" method="post">
                        <input type="hidden" name="id" value="<%= tarifRemise != null ? tarifRemise.getId() : "" %>">

                        <div class="mb-3">
                            <label for="idTypePlace" class="form-label">Type de Place *</label>
                            <select class="form-control" id="idTypePlace" name="idTypePlace" required>
                                <option value="">Choisir...</option>
                                <% if (typePlaces != null) {
                                    for (TypePlace tp : typePlaces) { %>
                                <option value="<%= tp.getId() %>"
                                        <%= (tarifRemise != null && tarifRemise.getTypePlace() != null && tarifRemise.getTypePlace().getId().equals(tp.getId())) ? "selected" : "" %>>
                                    <%= tp.getNomTypePlace() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="idCategoriePersonne" class="form-label">Catégorie Personne *</label>
                            <select class="form-control" id="idCategoriePersonne" name="idCategoriePersonne" required>
                                <option value="">Choisir...</option>
                                <% if (categories != null) {
                                    for (TypeObjectDTO cat : categories) { %>
                                <option value="<%= cat.getId() %>"
                                        <%= (tarifRemise != null && tarifRemise.getCategoriePersonne() != null && tarifRemise.getCategoriePersonne().getId().equals(cat.getId())) ? "selected" : "" %>>
                                    <%= cat.getLibelle() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="tarifUnitaireAvecRemise" class="form-label">Tarif Unitaire avec Remise (Ar)
                                *</label>
                            <input type="number" step="0.01" min="0" class="form-control"
                                   id="tarifUnitaireAvecRemise" name="tarifUnitaireAvecRemise"
                                   value="<%= tarifRemise != null ? String.format("%.2f", tarifRemise.getTarifUnitaireAvecRemise()) : "" %>"
                                   required>
                            <small class="form-text text-muted">
                                <i class="bi bi-info-circle"></i> Ce tarif sera appliqué à la place du tarif normal pour
                                cette catégorie.
                            </small>
                        </div>

                        <div class="alert alert-info" role="alert">
                            <small>
                                <i class="bi bi-lightbulb"></i>
                                <strong>Info:</strong> Lors de la réservation, si une remise existe pour la combinaison
                                Type de Place / Catégorie, ce tarif sera automatiquement utilisé.
                            </small>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save"></i> Enregistrer
                            </button>
                            <% if (tarifRemise != null) { %>
                            <a href="<%= request.getContextPath() %>/tarif-remises" class="btn btn-secondary">
                                <i class="bi bi-x-circle"></i> Annuler
                            </a>
                            <% } %>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Filters Card -->
            <div class="card shadow">
                <div class="card-header py-3 bg-info text-white">
                    <h6 class="m-0 font-weight-bold">
                        <i class="bi bi-funnel"></i> Filtres de Recherche
                    </h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/tarif-remises" method="get">
                        <div class="mb-3">
                            <label for="filter_idTypePlace" class="form-label">Type de Place</label>
                            <select class="form-control" id="filter_idTypePlace" name="filter_idTypePlace">
                                <option value="">Tous</option>
                                <% if (typePlaces != null) {
                                    for (TypePlace tp : typePlaces) { %>
                                <option value="<%= tp.getId() %>"
                                        <%= request.getParameter("filter_idTypePlace") != null && request.getParameter("filter_idTypePlace").equals(String.valueOf(tp.getId())) ? "selected" : "" %>>
                                    <%= tp.getNomTypePlace() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="filter_idCategorie" class="form-label">Catégorie</label>
                            <select class="form-control" id="filter_idCategorie" name="filter_idCategorie">
                                <option value="">Toutes</option>
                                <% if (categories != null) {
                                    for (TypeObjectDTO cat : categories) { %>
                                <option value="<%= cat.getId() %>"
                                        <%= request.getParameter("filter_idCategorie") != null && request.getParameter("filter_idCategorie").equals(String.valueOf(cat.getId())) ? "selected" : "" %>>
                                    <%= cat.getLibelle() %>
                                </option>
                                <% }
                                } %>
                            </select>
                        </div>

                        <div class="row">
                            <div class="col-6">
                                <div class="mb-3">
                                    <label for="filter_minTarif" class="form-label">Tarif Min</label>
                                    <input type="number" step="0.01" class="form-control"
                                           id="filter_minTarif" name="filter_minTarif"
                                           value="<%= request.getParameter("filter_minTarif") != null ? request.getParameter("filter_minTarif") : "" %>">
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="mb-3">
                                    <label for="filter_maxTarif" class="form-label">Tarif Max</label>
                                    <input type="number" step="0.01" class="form-control"
                                           id="filter_maxTarif" name="filter_maxTarif"
                                           value="<%= request.getParameter("filter_maxTarif") != null ? request.getParameter("filter_maxTarif") : "" %>">
                                </div>
                            </div>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-info btn-sm">
                                <i class="bi bi-search"></i> Filtrer
                            </button>
                            <a href="<%= request.getContextPath() %>/tarif-remises" class="btn btn-secondary btn-sm">
                                <i class="bi bi-x"></i> Réinitialiser
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- List Column -->
        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3 bg-success text-white">
                    <h6 class="m-0 font-weight-bold">
                        <i class="bi bi-list-ul"></i> Liste des Tarifs avec Remise
                        <span class="badge badge-light ml-2">
                            <%= tarifRemises != null ? tarifRemises.size() : 0 %>
                            <%= (tarifRemises != null && tarifRemises.size() > 1) ? "tarifs" : "tarif" %>
                        </span>
                    </h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover" width="100%" cellspacing="0">
                            <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Type de Place</th>
                                <th>Catégorie Personne</th>
                                <th>Tarif avec Remise</th>
                                <th class="text-center">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (tarifRemises != null && !tarifRemises.isEmpty()) {
                                for (TarifTypePlaceCategorieRemise tr : tarifRemises) { %>
                            <tr>
                                <td><%= tr.getId() %>
                                </td>
                                <td>
                                    <%= tr.getTypePlace().getNomTypePlace() %>
                                </td>
                                <td>
                                    <%= tr.getCategoriePersonne().getLibelle() %>
                                </td>
                                <td class="text-right">
                                    <strong class="text-success">
                                        <%= String.format("%,.2f", tr.getTarifUnitaireAvecRemise()) %> Ar
                                    </strong>
                                </td>
                                <td class="text-center">
                                    <a href="<%= request.getContextPath() %>/tarif-remises?action=edit&id=<%= tr.getId() %>"
                                       class="btn btn-sm btn-warning" title="Modifier">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <form action="<%= request.getContextPath() %>/tarif-remises" method="post"
                                          style="display: inline;"
                                          onsubmit="return confirm('Êtes-vous sûr de vouloir supprimer ce tarif avec remise?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="<%= tr.getId() %>">
                                        <button type="submit" class="btn btn-sm btn-danger" title="Supprimer">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                            <% }
                            } else { %>
                            <tr>
                                <td colspan="5" class="text-center text-muted">
                                    <i class="bi bi-inbox"></i> Aucun tarif avec remise trouvé.
                                    <% if (request.getParameter("filter_idTypePlace") != null ||
                                            request.getParameter("filter_idCategorie") != null ||
                                            request.getParameter("filter_minTarif") != null ||
                                            request.getParameter("filter_maxTarif") != null) { %>
                                    <br>
                                    <a href="<%= request.getContextPath() %>/tarif-remises"
                                       class="btn btn-sm btn-secondary mt-2">
                                        Afficher tous les tarifs
                                    </a>
                                    <% } %>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>

                    <% if (tarifRemises != null && !tarifRemises.isEmpty()) { %>
                    <div class="mt-3">
                        <div class="alert alert-light border" role="alert">
                            <h6 class="alert-heading">
                                <i class="bi bi-info-circle"></i> Comment ça fonctionne?
                            </h6>
                            <hr>
                            <small>
                                <ul class="mb-0">
                                    <li>Les tarifs avec remise sont appliqués automatiquement lors de la création d'une
                                        réservation
                                    </li>
                                    <li>Si aucune remise n'existe pour une combinaison Type de Place / Catégorie, le
                                        tarif normal du véhicule est utilisé
                                    </li>
                                    <li>Les remises peuvent être définies pour encourager certaines catégories de
                                        passagers (enfants, étudiants, seniors, etc.)
                                    </li>
                                </ul>
                            </small>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .d-flex.gap-2 > * {
        margin-right: 0.5rem;
    }

    .d-flex.gap-2 > *:last-child {
        margin-right: 0;
    }
</style>