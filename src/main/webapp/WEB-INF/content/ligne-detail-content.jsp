<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%
  Ligne ligne = (Ligne) request.getAttribute("ligne");
  List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
%>

<div class="container-fluid">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="h3 text-gray-800">Détails de la Ligne</h1>
    <a href="<%= request.getContextPath() %>/lignes" class="btn btn-secondary">
      <i class="bi bi-arrow-left"></i> Retour
    </a>
  </div>

  <!-- Ligne Information Card -->
  <div class="row mb-4">
    <div class="col-12">
      <div class="card shadow">
        <div class="card-header py-3 bg-primary text-white">
          <h6 class="m-0 font-weight-bold">
            <i class="bi bi-signpost-2-fill"></i> Informations de la Ligne
          </h6>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <h5 class="text-primary">
                <i class="bi bi-geo-fill text-success"></i>
                <%= ligne.getVilleDepart().getNom() %>
              </h5>
              <p class="text-muted">Ville de Départ</p>
            </div>
            <div class="col-md-6">
              <h5 class="text-primary">
                <i class="bi bi-geo-alt-fill text-danger"></i>
                <%= ligne.getVilleArrivee().getNom() %>
              </h5>
              <p class="text-muted">Ville d'Arrivée</p>
            </div>
          </div>
          <hr>
          <div class="row">
            <div class="col-md-12">
              <p class="mb-2">
                <strong><i class="bi bi-rulers text-info"></i> Distance Totale:</strong> <%= ligne.getDistanceKm() %> km
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Statistiques Cards -->
  <div class="row mb-4">
    <div class="col-md-4">
      <div class="card border-left-primary shadow h-100 py-2">
        <div class="card-body">
          <div class="row no-gutters align-items-center">
            <div class="col mr-2">
              <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                Nombre de Trajets
              </div>
              <div class="h5 mb-0 font-weight-bold text-gray-800">
                <%= trajets.size() %>
              </div>
            </div>
            <div class="col-auto">
              <i class="bi bi-list-check fs-2 text-gray-300"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card border-left-success shadow h-100 py-2">
        <div class="card-body">
          <div class="row no-gutters align-items-center">
            <div class="col mr-2">
              <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                Distance Totale
              </div>
              <div class="h5 mb-0 font-weight-bold text-gray-800">
                <%= ligne.getDistanceKm() %> km
              </div>
            </div>
            <div class="col-auto">
              <i class="bi bi-rulers fs-2 text-gray-300"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card border-left-info shadow h-100 py-2">
        <div class="card-body">
          <div class="row no-gutters align-items-center">
            <div class="col mr-2">
              <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                Itinéraire
              </div>
              <div class="h6 mb-0 font-weight-bold text-gray-800">
                <%= ligne.getVilleDepart().getNom() %> → <%= ligne.getVilleArrivee().getNom() %>
              </div>
            </div>
            <div class="col-auto">
              <i class="bi bi-signpost-2 fs-2 text-gray-300"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>