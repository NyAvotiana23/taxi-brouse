<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col">
            <h2><i class="bi bi-megaphone-fill"></i> Gestion des Diffusions par Société</h2>
        </div>
        <div class="col-auto">
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addDiffusionModal">
                <i class="bi bi-plus-circle"></i> Nouvelle Diffusion
            </button>
        </div>
    </div>

    <!-- Filtres -->
    <div class="card mb-3">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/diffusions" class="row g-3">
                <div class="col-md-3">
                    <label for="societeId" class="form-label">Société</label>
                    <select class="form-select" id="societeId" name="societeId">
                        <option value="">Toutes les sociétés</option>
                        <c:forEach var="societe" items="${societes}">
                            <option value="${societe.id}" ${param.societeId == societe.id ? 'selected' : ''}>
                                ${societe.nom}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3">
                    <label for="publiciteId" class="form-label">Publicité</label>
                    <select class="form-select" id="publiciteId" name="publiciteId">
                        <option value="">Toutes les publicités</option>
                        <c:forEach var="publicite" items="${publicites}">
                            <option value="${publicite.id}" ${param.publiciteId == publicite.id ? 'selected' : ''}>
                                ${publicite.description}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <label for="mois" class="form-label">Mois</label>
                    <select class="form-select" id="mois" name="mois">
                        <option value="">Tous</option>
                        <option value="1" ${param.mois == '1' ? 'selected' : ''}>Janvier</option>
                        <option value="2" ${param.mois == '2' ? 'selected' : ''}>Février</option>
                        <option value="3" ${param.mois == '3' ? 'selected' : ''}>Mars</option>
                        <option value="4" ${param.mois == '4' ? 'selected' : ''}>Avril</option>
                        <option value="5" ${param.mois == '5' ? 'selected' : ''}>Mai</option>
                        <option value="6" ${param.mois == '6' ? 'selected' : ''}>Juin</option>
                        <option value="7" ${param.mois == '7' ? 'selected' : ''}>Juillet</option>
                        <option value="8" ${param.mois == '8' ? 'selected' : ''}>Août</option>
                        <option value="9" ${param.mois == '9' ? 'selected' : ''}>Septembre</option>
                        <option value="10" ${param.mois == '10' ? 'selected' : ''}>Octobre</option>
                        <option value="11" ${param.mois == '11' ? 'selected' : ''}>Novembre</option>
                        <option value="12" ${param.mois == '12' ? 'selected' : ''}>Décembre</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <label for="annee" class="form-label">Année</label>
                    <input type="number" class="form-control" id="annee" name="annee" 
                           value="${param.annee}" placeholder="2026">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-secondary w-100">
                        <i class="bi bi-funnel"></i> Filtrer
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Message d'erreur -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Table des diffusions -->
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Société</th>
                            <th>Publicité</th>
                            <th>Trajet</th>
                            <th>Date du Trajet</th>
                            <th>Montant Unité</th>
                            <th>Nombre</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="diffusion" items="${diffusions}">
                            <tr>
                                <td>${diffusion.id}</td>
                                <td>${diffusion.publicite.societe.nom}</td>
                                <td>${diffusion.publicite.description}</td>
                                <td>
                                    ${diffusion.trajet.ligne.villeDepart.nom} → ${diffusion.trajet.ligne.villeArrivee.nom}
                                </td>
                                <td>
                                    <fmt:parseDate value="${diffusion.trajet.datetimeDepart}" 
                                                   pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td><fmt:formatNumber value="${diffusion.montantUnite}" type="number" groupingUsed="true"/> Ar</td>
                                <td>${diffusion.nombre}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/diffusions?action=detail&id=${diffusion.id}" 
                                       class="btn btn-sm btn-info">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/diffusions?action=edit&id=${diffusion.id}" 
                                       class="btn btn-sm btn-warning">
                                        <i class="bi bi-pencil"></i>
                                    </a>
                                    <button type="button" class="btn btn-sm btn-danger" 
                                            onclick="confirmDelete(${diffusion.id})">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Modal Ajout Diffusion -->
<div class="modal fade" id="addDiffusionModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/diffusions">
                <div class="modal-header">
                    <h5 class="modal-title">Nouvelle Diffusion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="idPublicite" class="form-label">Publicité *</label>
                        <select class="form-select" id="idPublicite" name="idPublicite" required>
                            <option value="">Sélectionner une publicité</option>
                            <c:forEach var="publicite" items="${publicites}">
                                <option value="${publicite.id}">
                                    ${publicite.societe.nom} - ${publicite.description}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="idTrajet" class="form-label">Trajet *</label>
                        <select class="form-select" id="idTrajet" name="idTrajet" required>
                            <option value="">Sélectionner un trajet</option>
                            <c:forEach var="trajet" items="${trajets}">
                                <option value="${trajet.id}">
                                    #${trajet.id} - ${trajet.ligne.villeDepart.nom} → ${trajet.ligne.villeArrivee.nom}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="montantUnite" class="form-label">Montant Unité *</label>
                        <div class="input-group">
                            <input type="text" class="form-control" id="montantUnite" 
                                   name="montantUnite" value="${montantUniteDefaut}" required>
                            <span class="input-group-text">Ar</span>
                        </div>
                        <small class="text-muted">Valeur par défaut depuis la configuration (COUT_DIFFUSION_TAXI)</small>
                    </div>
                    <div class="mb-3">
                        <label for="nombre" class="form-label">Nombre *</label>
                        <input type="text" class="form-control" id="nombre" 
                               name="nombre" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                    <button type="submit" class="btn btn-primary">Enregistrer</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function confirmDelete(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette diffusion ?')) {
        window.location.href = '${pageContext.request.contextPath}/diffusions?action=delete&id=' + id;
    }
}
</script>