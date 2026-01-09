<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Chauffeurs</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <c:choose>
                            <c:when test="${not empty chauffeur}">Modifier Chauffeur</c:when>
                            <c:otherwise>Nouveau Chauffeur</c:otherwise>
                        </c:choose>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/chauffeures" method="post">
                        <input type="hidden" name="id" value="${chauffeur.id}">

                        <div class="mb-3">
                            <label for="nom" class="form-label">Nom</label>
                            <input type="text" class="form-control" id="nom" name="nom" value="${chauffeur.nom}" required>
                        </div>
                        <div class="mb-3">
                            <label for="prenom" class="form-label">Prénom</label>
                            <input type="text" class="form-control" id="prenom" name="prenom" value="${chauffeur.prenom}" required>
                        </div>
                        <div class="mb-3">
                            <label for="dateNaissance" class="form-label">Date de Naissance</label>
                            <input type="date" class="form-control" id="dateNaissance" name="dateNaissance" value="${chauffeur.dateNaissance}" required>
                        </div>
                        <div class="mb-3">
                            <label for="numeroPermis" class="form-label">Numéro Permis</label>
                            <input type="text" class="form-control" id="numeroPermis" name="numeroPermis" value="${chauffeur.numeroPermis}" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="${pageContext.request.contextPath}/chauffeures" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Chauffeurs</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Nom</th>
                                    <th>Prénom</th>
                                    <th>Date Naissance</th>
                                    <th>Permis</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${chauffeurs}" var="c">
                                    <tr>
                                        <td>${c.nom}</td>
                                        <td>${c.prenom}</td>
                                        <td>${c.dateNaissance}</td>
                                        <td>${c.numeroPermis}</td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/chauffeures?action=edit&id=${c.id}" class="btn btn-sm btn-info">Modifier</a>
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
