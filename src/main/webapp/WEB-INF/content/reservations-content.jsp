<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Réservations</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <c:choose>
                            <c:when test="${not empty reservation}">Modifier Réservation</c:when>
                            <c:otherwise>Nouvelle Réservation</c:otherwise>
                        </c:choose>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/reservations" method="post">
                        <input type="hidden" name="id" value="${reservation.id}">

                        <div class="mb-3">
                            <label for="idTrajet" class="form-label">Trajet</label>
                            <select class="form-control" id="idTrajet" name="idTrajet" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${trajets}" var="t">
                                    <option value="${t.id}" ${reservation.trajet.id == t.id ? 'selected' : ''}>
                                        ${t.ligne.villeDepart.nom} -> ${t.ligne.villeArrivee.nom} (${t.datetimeDepart})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idClient" class="form-label">Client</label>
                            <select class="form-control" id="idClient" name="idClient" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${clients}" var="c">
                                    <option value="${c.id}" ${reservation.client.id == c.id ? 'selected' : ''}>${c.nomClient}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="nomPassager" class="form-label">Nom Passager</label>
                            <input type="text" class="form-control" id="nomPassager" name="nomPassager" value="${reservation.nomPassager}" required>
                        </div>
                        <div class="mb-3">
                            <label for="numeroSiege" class="form-label">Numéro Siège</label>
                            <input type="text" class="form-control" id="numeroSiege" name="numeroSiege" value="${reservation.numeroSiege}">
                        </div>
                        <div class="mb-3">
                            <label for="nombrePlaceReservation" class="form-label">Nombre Places</label>
                            <input type="number" class="form-control" id="nombrePlaceReservation" name="nombrePlaceReservation" value="${reservation.nombrePlaceReservation}" required>
                        </div>
                        <div class="mb-3">
                            <label for="montant" class="form-label">Montant</label>
                            <input type="number" step="0.01" class="form-control" id="montant" name="montant" value="${reservation.montant}" required>
                        </div>
                        <div class="mb-3">
                            <label for="idReservationStatut" class="form-label">Statut</label>
                            <select class="form-control" id="idReservationStatut" name="idReservationStatut" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${reservationStatuts}" var="s">
                                    <option value="${s.id}" ${reservation.reservationStatus.id == s.id ? 'selected' : ''}>${s.libelle}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="${pageContext.request.contextPath}/reservations" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Réservations</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Trajet</th>
                                    <th>Client</th>
                                    <th>Passager</th>
                                    <th>Siège</th>
                                    <th>Statut</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${reservations}" var="r">
                                    <tr>
                                        <td>${r.trajet.ligne.villeDepart.nom} -> ${r.trajet.ligne.villeArrivee.nom}</td>
                                        <td>${r.client.nomClient}</td>
                                        <td>${r.nomPassager}</td>
                                        <td>${r.numeroSiege}</td>
                                        <td>${r.reservationStatus.libelle}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/reservations?action=edit&id=${r.id}" class="btn btn-sm btn-info">Modifier</a>
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
