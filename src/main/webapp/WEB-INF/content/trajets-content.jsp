<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Trajets</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <c:choose>
                            <c:when test="${not empty trajet}">Modifier Trajet</c:when>
                            <c:otherwise>Nouveau Trajet</c:otherwise>
                        </c:choose>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/trajets" method="post">
                        <input type="hidden" name="id" value="${trajet.id}">

                        <div class="mb-3">
                            <label for="idLigne" class="form-label">Ligne</label>
                            <select class="form-control" id="idLigne" name="idLigne" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${lignes}" var="l">
                                    <option value="${l.id}" ${trajet.ligne.id == l.id ? 'selected' : ''}>${l.villeDepart.nom} -> ${l.villeArrivee.nom}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idChauffeur" class="form-label">Chauffeur</label>
                            <select class="form-control" id="idChauffeur" name="idChauffeur" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${chauffeurs}" var="c">
                                    <option value="${c.id}" ${trajet.chauffeur.id == c.id ? 'selected' : ''}>${c.nom} ${c.prenom}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idVehicule" class="form-label">Véhicule</label>
                            <select class="form-control" id="idVehicule" name="idVehicule" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${vehicules}" var="v">
                                    <option value="${v.id}" ${trajet.vehicule.id == v.id ? 'selected' : ''}>${v.immatriculation} (${v.marque})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="datetimeDepart" class="form-label">Date Départ</label>
                            <input type="datetime-local" class="form-control" id="datetimeDepart" name="datetimeDepart" value="${trajet.datetimeDepart}" required>
                        </div>
                        <div class="mb-3">
                            <label for="datetimeArrivee" class="form-label">Date Arrivée (Estimée)</label>
                            <input type="datetime-local" class="form-control" id="datetimeArrivee" name="datetimeArrivee" value="${trajet.datetimeArrivee}">
                        </div>
                        <div class="mb-3">
                            <label for="nombrePassager" class="form-label">Nombre Passagers</label>
                            <input type="number" class="form-control" id="nombrePassager" name="nombrePassager" value="${trajet.nombrePassager}" required>
                        </div>
                        <div class="mb-3">
                            <label for="fraisUnitaire" class="form-label">Frais Unitaire</label>
                            <input type="number" step="0.01" class="form-control" id="fraisUnitaire" name="fraisUnitaire" value="${trajet.fraisUnitaire}" required>
                        </div>
                        <div class="mb-3">
                            <label for="idTrajetStatut" class="form-label">Statut</label>
                            <select class="form-control" id="idTrajetStatut" name="idTrajetStatut" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${trajetStatuts}" var="s">
                                    <option value="${s.id}" ${trajet.trajetStatut.id == s.id ? 'selected' : ''}>${s.libelle}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="${pageContext.request.contextPath}/trajets" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Trajets</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Ligne</th>
                                    <th>Chauffeur</th>
                                    <th>Véhicule</th>
                                    <th>Départ</th>
                                    <th>Statut</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${trajets}" var="t">
                                    <tr>
                                        <td>${t.ligne.villeDepart.nom} -> ${t.ligne.villeArrivee.nom}</td>
                                        <td>${t.chauffeur.nom}</td>
                                        <td>${t.vehicule.immatriculation}</td>
                                        <td>${t.datetimeDepart}</td>
                                        <td>${t.trajetStatut.libelle}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/trajets?action=edit&id=${t.id}" class="btn btn-sm btn-info">Modifier</a>
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
