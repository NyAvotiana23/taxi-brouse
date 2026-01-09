<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Véhicules</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <c:choose>
                            <c:when test="${not empty vehicule}">Modifier Véhicule</c:when>
                            <c:otherwise>Nouveau Véhicule</c:otherwise>
                        </c:choose>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/vehicules" method="post">
                        <input type="hidden" name="id" value="${vehicule.id}">

                        <div class="mb-3">
                            <label for="marque" class="form-label">Marque</label>
                            <input type="text" class="form-control" id="marque" name="marque" value="${vehicule.marque}" required>
                        </div>
                        <div class="mb-3">
                            <label for="modele" class="form-label">Modèle</label>
                            <input type="text" class="form-control" id="modele" name="modele" value="${vehicule.modele}" required>
                        </div>
                        <div class="mb-3">
                            <label for="immatriculation" class="form-label">Immatriculation</label>
                            <input type="text" class="form-control" id="immatriculation" name="immatriculation" value="${vehicule.immatriculation}" required>
                        </div>
                        <div class="mb-3">
                            <label for="maximumPassager" class="form-label">Max Passagers</label>
                            <input type="number" class="form-control" id="maximumPassager" name="maximumPassager" value="${vehicule.maximumPassager}" required>
                        </div>
                        <div class="mb-3">
                            <label for="capaciteCarburant" class="form-label">Capacité Carburant</label>
                            <input type="number" step="0.01" class="form-control" id="capaciteCarburant" name="capaciteCarburant" value="${vehicule.capaciteCarburant}">
                        </div>
                        <div class="mb-3">
                            <label for="depenseCarburant100km" class="form-label">Dépense / 100km</label>
                            <input type="number" step="0.01" class="form-control" id="depenseCarburant100km" name="depenseCarburant100km" value="${vehicule.depenseCarburant100km}">
                        </div>
                        <div class="mb-3">
                            <label for="idType" class="form-label">Type Véhicule</label>
                            <select class="form-control" id="idType" name="idType" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${vehiculeTypes}" var="type">
                                    <option value="${type.id}" ${vehicule.vehiculeType.id == type.id ? 'selected' : ''}>${type.libelle}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idTypeCarburant" class="form-label">Type Carburant</label>
                            <select class="form-control" id="idTypeCarburant" name="idTypeCarburant" required>
                                <option value="">Choisir...</option>
                                <c:forEach items="${carburantTypes}" var="type">
                                    <option value="${type.id}" ${vehicule.carburantType.id == type.id ? 'selected' : ''}>${type.libelle}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="${pageContext.request.contextPath}/vehicules" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Véhicules</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Immat.</th>
                                    <th>Marque</th>
                                    <th>Modèle</th>
                                    <th>Type</th>
                                    <th>Carburant</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${vehicules}" var="v">
                                    <tr>
                                        <td>${v.immatriculation}</td>
                                        <td>${v.marque}</td>
                                        <td>${v.modele}</td>
                                        <td>${v.vehiculeType.libelle}</td>
                                        <td>${v.carburantType.libelle}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/vehicules?action=edit&id=${v.id}" class="btn btn-sm btn-info">Modifier</a>
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
