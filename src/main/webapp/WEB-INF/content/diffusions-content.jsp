<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col">
            <h2><i class="bi bi-megaphone-fill"></i> Gestion des Diffusions</h2>
        </div>
    </div>

    <!-- Message d'erreur -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Statistiques globales -->
    <div class="row mb-3">
        <c:set var="totalAPayer" value="0"/>
        <c:set var="totalPaye" value="0"/>
        <c:set var="totalReste" value="0"/>
        <c:forEach var="vm" items="${vmDiffusions}">
            <c:set var="totalAPayer" value="${totalAPayer + vm.montantAPayer}"/>
            <c:set var="totalPaye" value="${totalPaye + vm.montantPaye}"/>
            <c:set var="totalReste" value="${totalReste + vm.montantReste}"/>
        </c:forEach>

        <div class="col-md-3">
            <div class="card bg-info text-white">
                <div class="card-body">
                    <h6 class="card-title">Total à Payer</h6>
                    <h3><fmt:formatNumber value="${totalAPayer}" type="currency" currencySymbol="Ar"/></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-success text-white">
                <div class="card-body">
                    <h6 class="card-title">Total Payé</h6>
                    <h3><fmt:formatNumber value="${totalPaye}" type="currency" currencySymbol="Ar"/></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-warning text-dark">
                <div class="card-body">
                    <h6 class="card-title">Reste à Payer</h6>
                    <h3><fmt:formatNumber value="${totalReste}" type="currency" currencySymbol="Ar"/></h3>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card bg-primary text-white">
                <div class="card-body">
                    <h6 class="card-title">Nombre de Diffusions</h6>
                    <h3>${vmDiffusions.size()}</h3>
                </div>
            </div>
        </div>
    </div>

    <!-- Table des diffusions -->
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Société</th>
                        <th>Date Création</th>
                        <th class="text-end">Montant à Payer</th>
                        <th class="text-end">Montant Payé</th>
                        <th class="text-end">Reste</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="vm" items="${vmDiffusions}">
                        <c:set var="pourcentagePaye" value="${(vm.montantPaye / vm.montantAPayer) * 100}"/>
                        <tr>
                            <td><strong>#${vm.idDiffusion}</strong></td>
                            <td>${vm.nomSociete}</td>
                            <td>
                                <fmt:parseDate value="${vm.dateCreation}"
                                               pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </td>
                            <td class="text-end">
                                <strong><fmt:formatNumber value="${vm.montantAPayer}" type="number" groupingUsed="true"/> Ar</strong>
                            </td>
                            <td class="text-end">
                                    <span class="badge bg-success">
                                        <fmt:formatNumber value="${vm.montantPaye}" type="number" groupingUsed="true"/> Ar
                                    </span>
                            </td>
                            <td class="text-end">
                                <c:choose>
                                    <c:when test="${vm.montantReste > 0}">
                                            <span class="badge bg-warning text-dark">
                                                <fmt:formatNumber value="${vm.montantReste}" type="number" groupingUsed="true"/> Ar
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-success">Payé</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="progress" style="height: 25px; min-width: 100px;">
                                    <div class="progress-bar ${pourcentagePaye >= 100 ? 'bg-success' : pourcentagePaye >= 50 ? 'bg-info' : 'bg-warning'}"
                                         role="progressbar"
                                         style="width: ${pourcentagePaye > 100 ? 100 : pourcentagePaye}%"
                                         aria-valuenow="${pourcentagePaye}"
                                         aria-valuemin="0"
                                         aria-valuemax="100">
                                        <fmt:formatNumber value="${pourcentagePaye}" pattern="#0.0"/>%
                                    </div>
                                </div>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/diffusions?action=detail&id=${vm.idDiffusion}"
                                   class="btn btn-sm btn-info" title="Voir détails">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <button type="button"
                                        class="btn btn-sm btn-success"
                                        onclick="showPaiementModal(${vm.idDiffusion}, '${vm.nomSociete}', ${vm.montantAPayer}, ${vm.montantReste})"
                                        title="Ajouter un paiement"
                                    ${vm.montantReste <= 0 ? 'disabled' : ''}>
                                    <i class="bi bi-cash-coin"></i>
                                </button>
                                <button type="button"
                                        class="btn btn-sm btn-danger"
                                        onclick="confirmDelete(${vm.idDiffusion})"
                                        title="Supprimer">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty vmDiffusions}">
                        <tr>
                            <td colspan="8" class="text-center text-muted">
                                Aucune diffusion disponible
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Modal Ajout Paiement -->
<div class="modal fade" id="paiementModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/diffusions">
                <input type="hidden" name="action" value="createPaiement">
                <input type="hidden" name="id" id="paiementDiffusionId">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title">
                        <i class="bi bi-cash-coin"></i> Nouveau Paiement
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <div class="alert alert-info">
                        <strong>Diffusion:</strong> <span id="paiementInfoDiffusion"></span><br>
                        <strong>Société:</strong> <span id="paiementInfoSociete"></span><br>
                        <strong>Montant total:</strong> <span id="paiementInfoTotal"></span> Ar<br>
                        <strong>Reste à payer:</strong> <span id="paiementInfoReste"></span> Ar
                    </div>

                    <div class="mb-3">
                        <label for="idSociete" class="form-label">Société Payeuse *</label>
                        <select class="form-select" id="idSociete" name="idSociete" required>
                            <option value="">Sélectionner une société</option>
                            <c:forEach var="societe" items="${societes}">
                                <option value="${societe.id}">${societe.nom}</option>
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
                                   required
                                   min="0">
                            <span class="input-group-text">Ar</span>
                        </div>
                        <small class="text-muted">
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
        document.getElementById('datePaiement').value = datetimeLocal;
    });

    function showPaiementModal(diffusionId, societe, montantTotal, montantReste) {
        document.getElementById('paiementDiffusionId').value = diffusionId;
        document.getElementById('paiementInfoDiffusion').textContent = '#' + diffusionId;
        document.getElementById('paiementInfoSociete').textContent = societe;
        document.getElementById('paiementInfoTotal').textContent = montantTotal.toLocaleString('fr-FR');
        document.getElementById('paiementInfoReste').textContent = montantReste.toLocaleString('fr-FR');
        document.getElementById('montantPaye').value = montantReste.toFixed(2);

        const modal = new bootstrap.Modal(document.getElementById('paiementModal'));
        modal.show();
    }

    function confirmDelete(id) {
        if (confirm('Êtes-vous sûr de vouloir supprimer cette diffusion et tous ses paiements associés ?')) {
            window.location.href = '${pageContext.request.contextPath}/diffusions?action=delete&id=' + id;
        }
    }
</script>