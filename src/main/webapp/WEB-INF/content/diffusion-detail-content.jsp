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
            <button type="button"
                    class="btn btn-success"
                    onclick="showPaiementModal(${diffusion.id}, '${diffusion.societe.nom}', ${montantTotal}, ${montantReste})"
            ${montantReste <= 0 ? 'disabled' : ''}>
                <i class="bi bi-cash-coin"></i> Ajouter un Paiement
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

    <!-- Résumé financier -->
    <div class="row mb-4">
        <div class="col-md-3">
            <div class="card bg-info text-white">
                <div class="card-body">
                    <h6 class="card-title"><i class="bi bi-calculator"></i> Montant Total</h6>
                    <h3><fmt:formatNumber value="${montantTotal}" type="number" groupingUsed="true"/> Ar</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-success text-white">
                <div class="card-body">
                    <h6 class="card-title"><i class="bi bi-check-circle"></i> Montant Payé</h6>
                    <h3><fmt:formatNumber value="${montantPaye}" type="number" groupingUsed="true"/> Ar</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card ${montantReste > 0 ? 'bg-warning text-dark' : 'bg-success text-white'}">
                <div class="card-body">
                    <h6 class="card-title"><i class="bi bi-hourglass-split"></i> Reste à Payer</h6>
                    <h3><fmt:formatNumber value="${montantReste}" type="number" groupingUsed="true"/> Ar</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-primary text-white">
                <div class="card-body">
                    <h6 class="card-title"><i class="bi bi-percent"></i> Progression</h6>
                    <c:set var="pourcentage" value="${(montantPaye / montantTotal) * 100}"/>
                    <h3><fmt:formatNumber value="${pourcentage}" pattern="#0.0"/>%</h3>
                    <div class="progress mt-2" style="height: 10px;">
                        <div class="progress-bar bg-light"
                             role="progressbar"
                             style="width: ${pourcentage > 100 ? 100 : pourcentage}%"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

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
                            <td><strong>#${diffusion.id}</strong></td>
                        </tr>
                        <tr>
                            <th>Société:</th>
                            <td><strong>${diffusion.societe.nom}</strong></td>
                        </tr>
                        <tr>
                            <th>Description:</th>
                            <td>${diffusion.societe.description}</td>
                        </tr>
                        <tr>
                            <th>Date de Création:</th>
                            <td>
                                <c:if test="${not empty diffusion.dateCreation}">
                                    <fmt:parseDate value="${diffusion.dateCreation}"
                                                   pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>

        <!-- Détails de Diffusion -->
        <div class="col-md-6">
            <div class="card mb-3">
                <div class="card-header bg-info text-white">
                    <h5 class="mb-0"><i class="bi bi-list-ul"></i> Détails de Diffusion (${details.size()})</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive" style="max-height: 300px; overflow-y: auto;">
                        <table class="table table-sm">
                            <thead class="sticky-top bg-light">
                            <tr>
                                <th>Publicité</th>
                                <th>Trajet</th>
                                <th class="text-end">Quantité</th>
                                <th class="text-end">P.U.</th>
                                <th class="text-end">Total</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="detail" items="${details}">
                                <c:set var="totalDetail" value="${detail.montantUnitaire * detail.nombreRepetition}"/>
                                <tr>
                                    <td>
                                        <small>${detail.publicite.description}</small>
                                    </td>
                                    <td>
                                        <small>
                                                ${detail.trajet.ligne.villeDepart.nom} →
                                                ${detail.trajet.ligne.villeArrivee.nom}
                                        </small>
                                    </td>
                                    <td class="text-end">${detail.nombreRepetition}</td>
                                    <td class="text-end">
                                        <fmt:formatNumber value="${detail.montantUnitaire}" type="number" groupingUsed="true"/>
                                    </td>
                                    <td class="text-end">
                                        <strong>
                                            <fmt:formatNumber value="${totalDetail}" type="number" groupingUsed="true"/>
                                        </strong>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty details}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted">
                                        Aucun détail disponible
                                    </td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Historique des Paiements -->
    <div class="card mb-3">
        <div class="card-header bg-success text-white">
            <h5 class="mb-0">
                <i class="bi bi-cash-stack"></i> Historique des Paiements (${paiements.size()})
            </h5>
        </div>
        <div class="card-body">
            <c:if test="${empty paiements}">
                <div class="alert alert-warning">
                    <i class="bi bi-exclamation-triangle"></i> Aucun paiement enregistré pour cette diffusion
                </div>
            </c:if>

            <c:forEach var="paiement" items="${paiements}" varStatus="status">
                <div class="card mb-3 border-success">
                    <div class="card-header bg-light">
                        <div class="row align-items-center">
                            <div class="col">
                                <h6 class="mb-0">
                                    <i class="bi bi-receipt"></i> Paiement #${paiement.id}
                                </h6>
                            </div>
                            <div class="col-auto">
                                <span class="badge bg-success fs-6">
                                    <fmt:formatNumber value="${paiement.montantPaye}" type="number" groupingUsed="true"/> Ar
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row mb-2">
                            <div class="col-md-4">
                                <strong>Société Payeuse:</strong><br>
                                    ${paiement.societe.nom}
                            </div>
                            <div class="col-md-4">
                                <strong>Date de Paiement:</strong><br>
                                <fmt:parseDate value="${paiement.datePaiement}"
                                               pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </div>
                            <div class="col-md-4">
                                <strong>Montant:</strong><br>
                                <span class="text-success fs-5">
                                    <fmt:formatNumber value="${paiement.montantPaye}" type="number" groupingUsed="true"/> Ar
                                </span>
                            </div>
                        </div>

                        <!-- Répartition du paiement -->
                        <div class="mt-3">
                            <h6 class="text-muted mb-2">
                                <i class="bi bi-pie-chart"></i> Répartition du Paiement
                            </h6>
                            <div class="table-responsive">
                                <table class="table table-sm table-bordered">
                                    <thead class="table-light">
                                    <tr>
                                        <th>Publicité</th>
                                        <th>Trajet</th>
                                        <th class="text-end">Montant Détail</th>
                                        <th class="text-end">% Payé</th>
                                        <th class="text-end">Montant Réparti</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:set var="repartitions" value="${null}"/>
                                    <jsp:useBean id="paiementService" class="com.mdgtaxi.service.DiffusionPaiementService"/>
                                    <c:set var="repartitions" value="${paiementService.getRepartitionsByPaiementId(paiement.id)}"/>

                                    <c:forEach var="repartition" items="${repartitions}">
                                        <c:set var="montantDetail" value="${repartition.diffusiondDetail.montantUnitaire * repartition.diffusiondDetail.nombreRepetition}"/>
                                        <tr>
                                            <td>
                                                <small>${repartition.diffusiondDetail.publicite.description}</small>
                                            </td>
                                            <td>
                                                <small>
                                                        ${repartition.diffusiondDetail.trajet.ligne.villeDepart.nom} →
                                                        ${repartition.diffusiondDetail.trajet.ligne.villeArrivee.nom}
                                                </small>
                                            </td>
                                            <td class="text-end">
                                                <fmt:formatNumber value="${montantDetail}" type="number" groupingUsed="true"/> Ar
                                            </td>
                                            <td class="text-end">
                                                    <span class="badge bg-info">
                                                        <fmt:formatNumber value="${repartition.pourcentage}" pattern="#0.00"/>%
                                                    </span>
                                            </td>
                                            <td class="text-end">
                                                <strong class="text-success">
                                                    <fmt:formatNumber value="${repartition.montantPaye}" type="number" groupingUsed="true"/> Ar
                                                </strong>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty repartitions}">
                                        <tr>
                                            <td colspan="5" class="text-center text-muted">
                                                Aucune répartition disponible
                                            </td>
                                        </tr>
                                    </c:if>
                                    </tbody>
                                    <c:if test="${not empty repartitions}">
                                        <tfoot class="table-light">
                                        <tr>
                                            <th colspan="4" class="text-end">Total Réparti:</th>
                                            <th class="text-end">
                                                <fmt:formatNumber value="${paiement.montantPaye}" type="number" groupingUsed="true"/> Ar
                                            </th>
                                        </tr>
                                        </tfoot>
                                    </c:if>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<!-- Modal Ajout Paiement -->
<div class="modal fade" id="paiementModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/diffusions">
                <input type="hidden" name="action" value="createPaiement">
                <input type="hidden" name="id" id="paiementDiffusionId" value="${diffusion.id}">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title">
                        <i class="bi bi-cash-coin"></i> Nouveau Paiement
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="alert alert-info">
                        <strong>Diffusion:</strong> <span id="paiementInfoDiffusion">#${diffusion.id}</span><br>
                        <strong>Société:</strong> <span id="paiementInfoSociete">${diffusion.societe.nom}</span><br>
                        <strong>Montant total:</strong>
                        <span id="paiementInfoTotal">
                            <fmt:formatNumber value="${montantTotal}" type="number" groupingUsed="true"/>
                        </span> Ar<br>
                        <strong>Reste à payer:</strong>
                        <span id="paiementInfoReste">
                            <fmt:formatNumber value="${montantReste}" type="number" groupingUsed="true"/>
                        </span> Ar
                    </div>

                    <div class="mb-3">
                        <label for="idSociete" class="form-label">Société Payeuse *</label>
                        <select class="form-select" id="idSociete" name="idSociete" required>
                            <option value="">Sélectionner une société</option>
                            <c:forEach var="societe" items="${societes}">
                                <option value="${societe.id}" ${societe.id == diffusion.societe.id ? 'selected' : ''}>
                                        ${societe.nom}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="datePaiement" class="form-label">Date de Paiement *</label>
                        <input type="datetime-local"
                               class="form-control"
                               id="datePaiement"
                               name="datePaiement"
                               required>
                    </div>

                    <div class="mb-3">
                        <label for="montantPaye" class="form-label">Montant Payé *</label>
                        <div class="input-group">
                            <input type="number"
                                   step="0.01"
                                   class="form-control"
                                   id="montantPaye"
                                   name="montantPaye"
                                   value="${montantReste}"
                                   required
                                   min="0">
                            <span class="input-group-text">Ar</span>
                        </div>
                        <small class="text-muted">
                            <i class="bi bi-info-circle"></i>
                            Le montant sera automatiquement réparti proportionnellement sur tous les détails de la diffusion
                        </small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                    <button type="submit" class="btn btn-success">
                        <i class="bi bi-check-circle"></i> Enregistrer le Paiement
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // Initialiser la date actuelle
    document.addEventListener('DOMContentLoaded', function() {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');

        const datetimeLocal = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
        if (document.getElementById('datePaiement')) {
            document.getElementById('datePaiement').value = datetimeLocal;
        }
    });

    function showPaiementModal(diffusionId, societe, montantTotal, montantReste) {
        const modal = new bootstrap.Modal(document.getElementById('paiementModal'));
        modal.show();
    }
</script>