<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col">
            <h2><i class="bi bi-megaphone-fill"></i> Détails de la Diffusion #${diffusion.id}</h2>
        </div>
        <div class="col-auto">
            <a href="${pageContext.request.contextPath}/diffusions" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> Retour
            </a>
            <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#editDiffusionModal">
                <i class="bi bi-pencil"></i> Modifier
            </button>
            <button class="btn btn-danger" onclick="confirmDelete(${diffusion.id})">
                <i class="bi bi-trash"></i> Supprimer
            </button>
        </div>
    </div>

    <!-- Message d'erreur -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row">
        <!-- Informations de la Diffusion -->
        <div class="col-md-6">
            <div class="card mb-3">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0"><i class="bi bi-info-circle"></i> Informations Générales</h5>
                </div>
                <div class="card-body">
                    <table class="table table-borderless">
                        <tr>
                            <th style="width: 40%;">ID Diffusion:</th>
                            <td>${diffusion.id}</td>
                        </tr>
                        <tr>
                            <th>Montant Unité:</th>
                            <td><strong>${diffusion.code}</strong></td>
                        </tr>
                        <tr>
                            <th>Nombre de Diffusions:</th>
                            <td><strong>${diffusion.nombre}</strong></td>
                        </tr>
                        <tr>
                            <th>Montant Total:</th>
                            <td>
                                <span class="badge bg-success fs-6">
                                    <c:set var="total" value="${diffusion.code * diffusion.nombre}"/>
                                    <fmt:formatNumber value="${total}" type="currency" currencySymbol="Ar"/>
                                </span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>

        <!-- Informations Société et Publicité -->
        <div class="col-md-6">
            <div class="card mb-3">
                <div class="card-header bg-info text-white">
                    <h5 class="mb-0"><i class="bi bi-building"></i> Société & Publicité</h5>
                </div>
                <div class="card-body">
                    <table class="table table-borderless">
                        <tr>
                            <th style="width: 40%;">Société:</th>
                            <td><strong>${diffusion.publicite.societe.nom}</strong></td>
                        </tr>
                        <tr>
                            <th>Description Société:</th>
                            <td>${diffusion.publicite.societe.description}</td>
                        </tr>
                        <tr>
                            <th>Publicité:</th>
                            <td>${diffusion.publicite.description}</td>
                        </tr>
                        <tr>
                            <th>Durée Publicité:</th>
                            <td>${diffusion.publicite.duree}</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Informations du Trajet -->
    <div class="card mb-3">
        <div class="card-header bg-warning">
            <h5 class="mb-0"><i class="bi bi-geo-alt-fill"></i> Informations du Trajet</h5>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <table class="table table-borderless">
                        <tr>
                            <th style="width: 40%;">ID Trajet:</th>
                            <td>
                                <a href="${pageContext.request.contextPath}/trajets?action=detail&id=${diffusion.trajet.id}">
                                    #${diffusion.trajet.id}
                                </a>
                            </td>
                        </tr>
                        <tr>
                            <th>Ligne:</th>
                            <td>
                                <strong>${diffusion.trajet.ligne.depart}</strong> 
                                <i class="bi bi-arrow-right"></i> 
                                <strong>${diffusion.trajet.ligne.arrivee}</strong>
                            </td>
                        </tr>
                        <tr>
                            <th>Chauffeur:</th>
                            <td>${diffusion.trajet.chauffeur.nom} ${diffusion.trajet.chauffeur.prenom}</td>
                        </tr>
                        <tr>
                            <th>Véhicule:</th>
                            <td>${diffusion.trajet.vehicule.immatriculation}</td>
                        </tr>
                    </table>
                </div>
                <div class="col-md-6">
                    <table class="table table-borderless">
                        <tr>
                            <th style="width: 40%;">Date Départ:</th>
                            <td>
                                <fmt:formatDate value="${diffusion.trajet.datetimeDepart}" 
                                              pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                        </tr>
                        <tr>
                            <th>Date Arrivée:</th>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty diffusion.trajet.datetimeArrivee}">
                                        <fmt:formatDate value="${diffusion.trajet.datetimeArrivee}" 
                                                      pattern="dd/MM/yyyy HH:mm"/>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">Non définie</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <th>Nombre Passagers:</th>
                            <td>${diffusion.trajet.nombrePassager}</td>
                        </tr>
                        <tr>
                            <th>Statut:</th>
                            <td>
                                <span class="badge bg-secondary">
                                    ${diffusion.trajet.trajetStatut.libelle}
                                </span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal Modification Diffusion -->
<div class="modal fade" id="editDiffusionModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/diffusions">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${diffusion.id}">
                <div class="modal-header">
                    <h5 class="modal-title">Modifier la Diffusion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="idPublicite" class="form-label">Publicité *</label>
                        <select class="form-select" id="idPublicite" name="idPublicite" required>
                            <c:forEach var="publicite" items="${publicites}">
                                <option value="${publicite.id}" 
                                        ${publicite.id == diffusion.publicite.id ? 'selected' : ''}>
                                    ${publicite.societe.nom} - ${publicite.description}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="idTrajet" class="form-label">Trajet *</label>
                        <select class="form-select" id="idTrajet" name="idTrajet" required>
                            <c:forEach var="trajet" items="${trajets}">
                                <option value="${trajet.id}" 
                                        ${trajet.id == diffusion.trajet.id ? 'selected' : ''}>
                                    ${trajet.ligne.depart} → ${trajet.ligne.arrivee} - 
                                    <fmt:formatDate value="${trajet.datetimeDepart}" pattern="dd/MM/yyyy HH:mm"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="montantUnite" class="form-label">Montant Unité *</label>
                        <input type="text" class="form-control" id="montantUnite" 
                               name="montantUnite" value="${diffusion.code}" required>
                    </div>
                    <div class="mb-3">
                        <label for="nombre" class="form-label">Nombre *</label>
                        <input type="text" class="form-control" id="nombre" 
                               name="nombre" value="${diffusion.nombre}" required>
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