<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col">
            <h2><i class="bi bi-cash-coin"></i> Dashboard Paiements Diffusions</h2>
            <p class="text-muted">Suivi du chiffre d'affaires et des paiements par société</p>
        </div>
    </div>

    <!-- Filtres -->
    <div class="card mb-3">
        <div class="card-header bg-primary text-white">
            <i class="bi bi-funnel"></i> Filtres
        </div>
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/paiements-dashboard" class="row g-3">
                <div class="col-md-3">
                    <label for="societeIds" class="form-label">Société</label>
                    <select class="form-select" id="societeIds" name="societeIds" multiple>
                        <c:forEach var="societe" items="${societes}">
                            <option value="${societe.id}" ${paramValues.societeIds != null && fn:contains(paramValues.societeIds, societe.id) ? 'selected' : ''}>
                                ${societe.nom}
                            </option>
                        </c:forEach>
                    </select>
                    <small class="text-muted">Ctrl+clic pour multi-sélection</small>
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
                <div class="col-md-3">
                    <label for="trajetId" class="form-label">Trajet</label>
                    <select class="form-select" id="trajetId" name="trajetId">
                        <option value="">Tous les trajets</option>
                        <c:forEach var="trajet" items="${trajets}">
                            <option value="${trajet.id}" ${param.trajetId == trajet.id ? 'selected' : ''}>
                                #${trajet.id} - ${trajet.ligne.villeDepart.nom} → ${trajet.ligne.villeArrivee.nom}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="bi bi-search"></i> Rechercher
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Statistiques globales -->
    <c:set var="totalAPayer" value="0"/>
    <c:set var="totalPaye" value="0"/>
    <c:set var="totalReste" value="0"/>
    <c:forEach var="vm" items="${vmPaiements}">
        <c:set var="totalAPayer" value="${totalAPayer + vm.montantAPayer}"/>
        <c:set var="totalPaye" value="${totalPaye + vm.montantPaye}"/>
        <c:set var="totalReste" value="${totalReste + vm.montantReste}"/>
    </c:forEach>
    
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card bg-primary text-white">
                <div class="card-body text-center">
                    <h5 class="card-title"><i class="bi bi-graph-up-arrow"></i> Chiffre d'Affaires Total</h5>
                    <h2><fmt:formatNumber value="${totalAPayer}" type="number" groupingUsed="true"/> Ar</h2>
                    <small>Montant total des diffusions</small>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-success text-white">
                <div class="card-body text-center">
                    <h5 class="card-title"><i class="bi bi-check-circle"></i> Montant Payé</h5>
                    <h2><fmt:formatNumber value="${totalPaye}" type="number" groupingUsed="true"/> Ar</h2>
                    <small>Paiements reçus</small>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-warning text-dark">
                <div class="card-body text-center">
                    <h5 class="card-title"><i class="bi bi-exclamation-triangle"></i> Reste à Payer</h5>
                    <h2><fmt:formatNumber value="${totalReste}" type="number" groupingUsed="true"/> Ar</h2>
                    <small>Créances en cours</small>
                </div>
            </div>
        </div>
    </div>

    <!-- Résumé par Société -->
    <div class="card mb-4">
        <div class="card-header bg-info text-white">
            <i class="bi bi-building"></i> Récapitulatif par Société
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>Société</th>
                            <th class="text-end">Chiffre d'Affaires</th>
                            <th class="text-end">Montant Payé</th>
                            <th class="text-end">Reste à Payer</th>
                            <th class="text-center">% Payé</th>
                            <th class="text-center">Statut</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Groupement par société via scriptlet --%>
                        <%@ page import="java.util.*, com.mdgtaxi.view.VmDiffusionPaiement, java.math.BigDecimal" %>
                        <%
                            List<VmDiffusionPaiement> vmPaiements = (List<VmDiffusionPaiement>) request.getAttribute("vmPaiements");
                            Map<Long, Map<String, Object>> societeStats = new LinkedHashMap<>();
                            
                            if (vmPaiements != null) {
                                for (VmDiffusionPaiement vm : vmPaiements) {
                                    Long societeId = vm.getIdSociete();
                                    if (!societeStats.containsKey(societeId)) {
                                        Map<String, Object> stats = new HashMap<>();
                                        stats.put("nom", vm.getNomSociete());
                                        stats.put("totalAPayer", BigDecimal.ZERO);
                                        stats.put("totalPaye", BigDecimal.ZERO);
                                        stats.put("totalReste", BigDecimal.ZERO);
                                        societeStats.put(societeId, stats);
                                    }
                                    Map<String, Object> stats = societeStats.get(societeId);
                                    BigDecimal currentAPayer = (BigDecimal) stats.get("totalAPayer");
                                    BigDecimal currentPaye = (BigDecimal) stats.get("totalPaye");
                                    BigDecimal currentReste = (BigDecimal) stats.get("totalReste");
                                    
                                    stats.put("totalAPayer", currentAPayer.add(vm.getMontantAPayer() != null ? vm.getMontantAPayer() : BigDecimal.ZERO));
                                    stats.put("totalPaye", currentPaye.add(vm.getMontantPaye() != null ? vm.getMontantPaye() : BigDecimal.ZERO));
                                    stats.put("totalReste", currentReste.add(vm.getMontantReste() != null ? vm.getMontantReste() : BigDecimal.ZERO));
                                }
                            }
                            request.setAttribute("societeStats", societeStats);
                        %>
                        
                        <c:forEach var="entry" items="${societeStats}">
                            <c:set var="stats" value="${entry.value}"/>
                            <c:set var="pctPaye" value="${stats.totalAPayer > 0 ? (stats.totalPaye / stats.totalAPayer * 100) : 0}"/>
                            <tr>
                                <td><strong>${stats.nom}</strong></td>
                                <td class="text-end">
                                    <fmt:formatNumber value="${stats.totalAPayer}" type="number" groupingUsed="true"/> Ar
                                </td>
                                <td class="text-end text-success">
                                    <fmt:formatNumber value="${stats.totalPaye}" type="number" groupingUsed="true"/> Ar
                                </td>
                                <td class="text-end text-danger">
                                    <fmt:formatNumber value="${stats.totalReste}" type="number" groupingUsed="true"/> Ar
                                </td>
                                <td class="text-center">
                                    <div class="progress" style="height: 20px;">
                                        <div class="progress-bar ${pctPaye >= 100 ? 'bg-success' : pctPaye >= 50 ? 'bg-warning' : 'bg-danger'}" 
                                             role="progressbar" 
                                             style="width: ${pctPaye}%;" 
                                             aria-valuenow="${pctPaye}" aria-valuemin="0" aria-valuemax="100">
                                            <fmt:formatNumber value="${pctPaye}" type="number" maxFractionDigits="1"/>%
                                        </div>
                                    </div>
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${stats.totalReste <= 0}">
                                            <span class="badge bg-success">Soldé</span>
                                        </c:when>
                                        <c:when test="${stats.totalPaye > 0}">
                                            <span class="badge bg-warning text-dark">Partiel</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Impayé</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Détail des diffusions -->
    <div class="card">
        <div class="card-header bg-secondary text-white">
            <i class="bi bi-list-ul"></i> Détail des Diffusions par Société
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Société</th>
                            <th>Publicité</th>
                            <th>Trajet</th>
                            <th>Période</th>
                            <th class="text-center">Diffusions</th>
                            <th class="text-end">Montant Unité</th>
                            <th class="text-end">Total à Payer</th>
                            <th class="text-end">Payé</th>
                            <th class="text-end">Reste</th>
                            <th class="text-center">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty vmPaiements}">
                                <tr>
                                    <td colspan="11" class="text-center text-muted py-4">
                                        <i class="bi bi-inbox fs-1"></i><br>
                                        Aucune diffusion trouvée
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="vm" items="${vmPaiements}">
                                    <tr>
                                        <td>${vm.idDiffusion}</td>
                                        <td><strong>${vm.nomSociete}</strong></td>
                                        <td>${vm.descriptionPublicite}</td>
                                        <td>Trajet #${vm.idTrajet}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${vm.mois == 1}">Janvier</c:when>
                                                <c:when test="${vm.mois == 2}">Février</c:when>
                                                <c:when test="${vm.mois == 3}">Mars</c:when>
                                                <c:when test="${vm.mois == 4}">Avril</c:when>
                                                <c:when test="${vm.mois == 5}">Mai</c:when>
                                                <c:when test="${vm.mois == 6}">Juin</c:when>
                                                <c:when test="${vm.mois == 7}">Juillet</c:when>
                                                <c:when test="${vm.mois == 8}">Août</c:when>
                                                <c:when test="${vm.mois == 9}">Septembre</c:when>
                                                <c:when test="${vm.mois == 10}">Octobre</c:when>
                                                <c:when test="${vm.mois == 11}">Novembre</c:when>
                                                <c:when test="${vm.mois == 12}">Décembre</c:when>
                                            </c:choose> ${vm.annee}
                                        </td>
                                        <td class="text-center">
                                            <span class="badge bg-info">${vm.nombre}</span>
                                        </td>
                                        <td class="text-end">
                                            <fmt:formatNumber value="${vm.montantUnite}" type="number" groupingUsed="true"/> Ar
                                        </td>
                                        <td class="text-end">
                                            <strong><fmt:formatNumber value="${vm.montantAPayer}" type="number" groupingUsed="true"/> Ar</strong>
                                        </td>
                                        <td class="text-end text-success">
                                            <fmt:formatNumber value="${vm.montantPaye}" type="number" groupingUsed="true"/> Ar
                                        </td>
                                        <td class="text-end">
                                            <c:choose>
                                                <c:when test="${vm.montantReste > 0}">
                                                    <span class="text-danger fw-bold">
                                                        <fmt:formatNumber value="${vm.montantReste}" type="number" groupingUsed="true"/> Ar
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-success">
                                                        <i class="bi bi-check-circle"></i> Soldé
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <c:if test="${vm.montantReste > 0}">
                                                <button class="btn btn-sm btn-success" 
                                                        data-bs-toggle="modal" 
                                                        data-bs-target="#paiementModal"
                                                        data-diffusion-id="${vm.idDiffusion}"
                                                        data-societe-id="${vm.idSociete}"
                                                        data-societe-nom="${vm.nomSociete}"
                                                        data-reste="${vm.montantReste}">
                                                    <i class="bi bi-plus-circle"></i> Payer
                                                </button>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Modal Nouveau Paiement -->
<div class="modal fade" id="paiementModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <form method="post" action="${pageContext.request.contextPath}/paiements">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title"><i class="bi bi-cash-coin"></i> Enregistrer un Paiement</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="modalIdDiffusion" name="idDiffusion">
                    <input type="hidden" id="modalIdSociete" name="idSociete">
                    
                    <div class="mb-3">
                        <label class="form-label">Société</label>
                        <input type="text" class="form-control" id="modalSocieteNom" readonly>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label">Reste à payer</label>
                        <input type="text" class="form-control" id="modalReste" readonly>
                    </div>
                    
                    <div class="mb-3">
                        <label for="montant" class="form-label">Montant du paiement *</label>
                        <input type="number" step="0.01" class="form-control" id="montant" 
                               name="montant" required placeholder="Ex: 500000">
                    </div>
                    
                    <div class="mb-3">
                        <label for="datePaiement" class="form-label">Date du paiement</label>
                        <input type="datetime-local" class="form-control" id="datePaiement" 
                               name="datePaiement" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new java.util.Date()) %>">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                    <button type="submit" class="btn btn-success">
                        <i class="bi bi-check-lg"></i> Enregistrer le paiement
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    var paiementModal = document.getElementById('paiementModal');
    if (paiementModal) {
        paiementModal.addEventListener('show.bs.modal', function(event) {
            var button = event.relatedTarget;
            var diffusionId = button.getAttribute('data-diffusion-id');
            var societeId = button.getAttribute('data-societe-id');
            var societeNom = button.getAttribute('data-societe-nom');
            var reste = button.getAttribute('data-reste');
            
            document.getElementById('modalIdDiffusion').value = diffusionId;
            document.getElementById('modalIdSociete').value = societeId;
            document.getElementById('modalSocieteNom').value = societeNom;
            document.getElementById('modalReste').value = reste + ' Ar';
            document.getElementById('montant').max = reste;
            document.getElementById('montant').placeholder = 'Max: ' + reste + ' Ar';
        });
    }
});
</script>
