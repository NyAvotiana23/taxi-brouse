<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container-fluid">
    <div class="row mb-3">
        <div class="col">
            <h2><i class="bi bi-graph-up"></i> Statistiques Globales des Diffusions</h2>
        </div>
    </div>

    <!-- Filtres -->
    <div class="card mb-3">
        <div class="card-body">
            <form method="get" action="${pageContext.request.contextPath}/diffusions-global" class="row g-3">
                <div class="col-md-3">
                    <label for="mois" class="form-label">Mois</label>
                    <select class="form-select" id="mois" name="mois">
                        <option value="">Tous les mois</option>
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
                <div class="col-md-2">
                    <label for="montantMin" class="form-label">Montant Min</label>
                    <input type="number" step="0.01" class="form-control" id="montantMin" 
                           name="montantMin" value="${param.montantMin}" placeholder="0.00">
                </div>
                <div class="col-md-2">
                    <label for="montantMax" class="form-label">Montant Max</label>
                    <input type="number" step="0.01" class="form-control" id="montantMax" 
                           name="montantMax" value="${param.montantMax}" placeholder="10000.00">
                </div>
                <div class="col-md-3 d-flex align-items-end">
                    <button type="submit" class="btn btn-secondary w-100">
                        <i class="bi bi-funnel"></i> Filtrer
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Statistiques globales -->
    <div class="row mb-3">
        <c:set var="totalMontant" value="0"/>
        <c:set var="totalNombre" value="0"/>
        <c:forEach var="stat" items="${diffusionsGlobal}">
            <c:set var="totalMontant" value="${totalMontant + stat.montantTotal}"/>
            <c:set var="totalNombre" value="${totalNombre + stat.nombreTotal}"/>
        </c:forEach>
        
        <div class="col-md-4">
            <div class="card bg-primary text-white">
                <div class="card-body">
                    <h5 class="card-title">Total Montant</h5>
                    <h2><fmt:formatNumber value="${totalMontant}" type="currency" currencySymbol="Ar"/></h2>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-success text-white">
                <div class="card-body">
                    <h5 class="card-title">Total Diffusions</h5>
                    <h2><fmt:formatNumber value="${totalNombre}" pattern="#,##0"/></h2>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-info text-white">
                <div class="card-body">
                    <h5 class="card-title">Périodes</h5>
                    <h2>${diffusionsGlobal.size()}</h2>
                </div>
            </div>
        </div>
    </div>

    <!-- Table des statistiques mensuelles -->
    <div class="card">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Période</th>
                            <th>Mois</th>
                            <th>Année</th>
                            <th class="text-end">Montant Total</th>
                            <th class="text-end">Nombre Total</th>
                            <th class="text-end">Montant Moyen</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="stat" items="${diffusionsGlobal}">
                            <tr>
                                <td>
                                    <strong>
                                        <c:choose>
                                            <c:when test="${stat.mois == 1}">Janvier</c:when>
                                            <c:when test="${stat.mois == 2}">Février</c:when>
                                            <c:when test="${stat.mois == 3}">Mars</c:when>
                                            <c:when test="${stat.mois == 4}">Avril</c:when>
                                            <c:when test="${stat.mois == 5}">Mai</c:when>
                                            <c:when test="${stat.mois == 6}">Juin</c:when>
                                            <c:when test="${stat.mois == 7}">Juillet</c:when>
                                            <c:when test="${stat.mois == 8}">Août</c:when>
                                            <c:when test="${stat.mois == 9}">Septembre</c:when>
                                            <c:when test="${stat.mois == 10}">Octobre</c:when>
                                            <c:when test="${stat.mois == 11}">Novembre</c:when>
                                            <c:when test="${stat.mois == 12}">Décembre</c:when>
                                        </c:choose>
                                        ${stat.annee}
                                    </strong>
                                </td>
                                <td>${stat.mois}</td>
                                <td>${stat.annee}</td>
                                <td class="text-end">
                                    <span class="badge bg-primary">
                                        <fmt:formatNumber value="${stat.montantTotal}" type="currency" currencySymbol="Ar"/>
                                    </span>
                                </td>
                                <td class="text-end">
                                    <span class="badge bg-success">
                                        <fmt:formatNumber value="${stat.nombreTotal}" pattern="#,##0"/>
                                    </span>
                                </td>
                                <td class="text-end">
                                    <c:set var="moyenne" value="${stat.montantTotal / stat.nombreTotal}"/>
                                    <span class="badge bg-info">
                                        <fmt:formatNumber value="${moyenne}" type="currency" currencySymbol="Ar"/>
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty diffusionsGlobal}">
                            <tr>
                                <td colspan="6" class="text-center text-muted">
                                    Aucune donnée disponible pour les critères sélectionnés
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>