<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Lignes</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <c:choose>
                            <c:when test="${not empty ligne}">Modifier Ligne</c:when>
                            <c:otherwise>Nouvelle Ligne</c:otherwise>
                        </c:choose>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/lignes" method="post">
                        <input type="hidden" name="id" value="${ligne.id}">

                        <div class="mb-3">
                            <label for="idVilleDepart" class="form-label">Ville Départ</label>
                            <select class="form-control" id="idVilleDepart" name="idVilleDepart" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${villes}" var="v">
                                    <option value="${v.id}" ${ligne.villeDepart.id == v.id ? 'selected' : ''}>${v.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idVilleArrivee" class="form-label">Ville Arrivée</label>
                            <select class="form-control" id="idVilleArrivee" name="idVilleArrivee" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${villes}" var="v">
                                    <option value="${v.id}" ${ligne.villeArrivee.id == v.id ? 'selected' : ''}>${v.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="distanceKm" class="form-label">Distance (km)</label>
                            <input type="number" step="0.01" class="form-control" id="distanceKm" name="distanceKm" value="${ligne.distanceKm}" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="${pageContext.request.contextPath}/lignes" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Lignes</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Départ</th>
                                    <th>Arrivée</th>
                                    <th>Distance</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${lignes}" var="l">
                                    <tr>
                                        <td>${l.villeDepart.nom}</td>
                                        <td>${l.villeArrivee.nom}</td>
                                        <td>${l.distanceKm} km</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/lignes?action=edit&id=${l.id}" class="btn btn-sm btn-info">Modifier</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
