<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.view.VmChauffeurDetail" %>
<%@ page import="com.mdgtaxi.view.VmChauffeurActivite" %>
<%@ page import="com.mdgtaxi.dto.MouvementStatusDto" %>
<%@ page import="com.mdgtaxi.dto.StatusObjectDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.Period" %>
<%@ page import="java.time.LocalDate" %>
<%
  VmChauffeurDetail detail = (VmChauffeurDetail) request.getAttribute("detail");
  VmChauffeurActivite activite = (VmChauffeurActivite) request.getAttribute("activite");
  MouvementStatusDto currentStatus = (MouvementStatusDto) request.getAttribute("currentStatus");
  List<MouvementStatusDto> statusHistory = (List<MouvementStatusDto>) request.getAttribute("statusHistory");
  List<StatusObjectDto> availableStatuts = (List<StatusObjectDto>) request.getAttribute("availableStatuts");
  String error = (String) request.getAttribute("error");

  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>

<div class="container-fluid">
  <!-- Header -->
  <div class="d-sm-flex align-items-center justify-content-between mb-4">
    <h1 class="h3 mb-0 text-gray-800">
      <i class="fas fa-user-tie"></i> Détails du Chauffeur
    </h1>
    <a href="<%= request.getContextPath() %>/chauffeures" class="btn btn-secondary">
      <i class="fas fa-arrow-left"></i> Retour à la liste
    </a>
  </div>

  <% if (error != null) { %>
  <div class="alert alert-danger alert-dismissible fade show" role="alert">
    <i class="fas fa-exclamation-triangle"></i> <%= error %>
    <button type="button" class="close" data-dismiss="alert">
      <span>&times;</span>
    </button>
  </div>
  <% } %>

  <% if (detail != null) { %>
  <!-- Chauffeur Information Cards -->
  <div class="row">
    <!-- Personal Information -->
    <div class="col-xl-8 col-lg-7">
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">
            Informations Personnelles
          </h6>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <div class="info-group mb-3">
                <label class="text-muted small">Nom Complet</label>
                <h5 class="mb-0"><%= detail.getNomComplet() %></h5>
              </div>
            </div>
            <div class="col-md-6">
              <div class="info-group mb-3">
                <label class="text-muted small">Numéro de Permis</label>
                <h5 class="mb-0">
                                        <span class="badge badge-info">
                                            <%= detail.getNumeroPermis() %>
                                        </span>
                </h5>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-4">
              <div class="info-group mb-3">
                <label class="text-muted small">Date de Naissance</label>
                <p class="mb-0">
                  <i class="fas fa-birthday-cake text-info"></i>
                  <strong><%= detail.getDateNaissance().format(dateFormatter) %></strong>
                </p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="info-group mb-3">
                <label class="text-muted small">Âge</label>
                <p class="mb-0">
                  <% if (detail.getAge() != null) { %>
                  <strong><%= detail.getAge() %></strong> ans
                  <% } else { %>
                  <span class="text-muted">N/A</span>
                  <% } %>
                </p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="info-group mb-3">
                <label class="text-muted small">Statut Actuel</label>
                <p class="mb-0">
                  <% if (detail.getLibelleStatut() != null) { %>
                  <span class="badge badge-primary">
                                                <%= detail.getLibelleStatut() %>
                                            </span>
                  <% } else { %>
                  <span class="badge badge-secondary">Aucun statut</span>
                  <% } %>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Current Status & Activity Stats -->
    <div class="col-xl-4 col-lg-5">
      <!-- Current Status Card -->
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">Statut Actuel</h6>
        </div>
        <div class="card-body text-center">
          <% if (currentStatus != null) { %>
          <div class="mb-3">
            <%= currentStatus.getSpanHtmlNouveauStatut() %>
          </div>
          <small class="text-muted">
            Depuis le <%= currentStatus.getDateMouvement().format(dateTimeFormatter) %>
          </small>
          <% if (currentStatus.getObservation() != null && !currentStatus.getObservation().isEmpty()) { %>
          <p class="mt-3 text-muted">
            <em>"<%= currentStatus.getObservation() %>"</em>
          </p>
          <% } %>
          <% } else { %>
          <span class="badge badge-secondary">Aucun statut</span>
          <% } %>
        </div>
      </div>

      <!-- Activity Stats Card -->
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">Statistiques d'Activité</h6>
        </div>
        <div class="card-body">
          <% if (activite != null) { %>
          <div class="mb-3">
            <label class="text-muted small">Nombre de Trajets</label>
            <h4 class="mb-0 text-primary">
              <i class="fas fa-road"></i> <%= activite.getNombreTrajets() != null ? activite.getNombreTrajets() : 0 %>
            </h4>
          </div>
          <div class="mb-3">
            <label class="text-muted small">Trajets Terminés</label>
            <h5 class="mb-0 text-success">
              <i class="fas fa-check-circle"></i> <%= activite.getTrajetsTermines() != null ? activite.getTrajetsTermines() : 0 %>
            </h5>
          </div>
          <% if (activite.getDernierTrajet() != null) { %>
          <div class="mb-3">
            <label class="text-muted small">Dernier Trajet</label>
            <p class="mb-0">
              <small><%= activite.getDernierTrajet().format(dateTimeFormatter) %></small>
            </p>
          </div>
          <% } %>
          <% if (activite.getPremierTrajet() != null) { %>
          <div class="mb-0">
            <label class="text-muted small">Premier Trajet</label>
            <p class="mb-0">
              <small><%= activite.getPremierTrajet().format(dateTimeFormatter) %></small>
            </p>
          </div>
          <% } %>
          <% } else { %>
          <p class="text-muted text-center">
            <i class="fas fa-info-circle"></i> Aucune activité
          </p>
          <% } %>
        </div>
      </div>
    </div>
  </div>

  <!-- Change Status & History Section -->
  <div class="row">
    <!-- Change Status Form -->
    <div class="col-lg-6">
      <div class="card shadow mb-4">
        <div class="card-header py-3 bg-warning">
          <h6 class="m-0 font-weight-bold text-white">
            <i class="fas fa-exchange-alt"></i> Changer le Statut
          </h6>
        </div>
        <div class="card-body">
          <form action="<%= request.getContextPath() %>/chauffeures/detail" method="POST">
            <input type="hidden" name="id" value="<%= detail.getIdChauffeur() %>">
            <input type="hidden" name="action" value="changeStatut">

            <div class="mb-3">
              <label for="idStatut" class="form-label">Nouveau Statut <span class="text-danger">*</span></label>
              <select class="form-control" id="idStatut" name="idStatut" required>
                <option value="">-- Choisir un statut --</option>
                <% if (availableStatuts != null) {
                  for (StatusObjectDto statut : availableStatuts) { %>
                <option value="<%= statut.getId() %>">
                  <%= statut.getLibelle() %> (Score: <%= statut.getScore() %>)
                </option>
                <% }
                } %>
              </select>
            </div>

            <div class="mb-3">
              <label for="dateChangement" class="form-label">Date du Changement</label>
              <input type="datetime-local"
                     class="form-control"
                     id="dateChangement"
                     name="dateChangement">
              <small class="form-text text-muted">Laisser vide pour la date actuelle</small>
            </div>

            <div class="mb-3">
              <label for="observation" class="form-label">Observation</label>
              <textarea class="form-control"
                        id="observation"
                        name="observation"
                        rows="3"
                        placeholder="Raison du changement de statut..."></textarea>
            </div>

            <button type="submit" class="btn btn-warning btn-block">
              <i class="fas fa-check"></i> Mettre à jour le statut
            </button>
          </form>
        </div>
      </div>
    </div>

    <!-- Status History -->
    <div class="col-lg-6">
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">
            <i class="fas fa-history"></i> Historique des Statuts
          </h6>
        </div>
        <div class="card-body">
          <% if (statusHistory != null && !statusHistory.isEmpty()) { %>
          <div class="timeline">
            <% for (MouvementStatusDto mvt : statusHistory) { %>
            <div class="timeline-item mb-3">
              <div class="d-flex justify-content-between align-items-start">
                <div>
                  <%= mvt.getSpanHtmlNouveauStatut() %>
                  <% if (mvt.getObservation() != null && !mvt.getObservation().isEmpty()) { %>
                  <p class="mb-0 mt-1 text-muted small">
                    <em><%= mvt.getObservation() %></em>
                  </p>
                  <% } %>
                </div>
                <small class="text-muted">
                  <%= mvt.getDateMouvement().format(dateTimeFormatter) %>
                </small>
              </div>
              <hr class="mt-2">
            </div>
            <% } %>
          </div>
          <% } else { %>
          <p class="text-muted text-center">
            <i class="fas fa-info-circle"></i> Aucun historique de statut
          </p>
          <% } %>
        </div>
      </div>
    </div>
  </div>
  <% } else { %>
  <div class="alert alert-warning">
    <i class="fas fa-exclamation-triangle"></i>
    Chauffeur introuvable
  </div>
  <% } %>
</div>

<style>
  .info-group label {
    font-weight: 600;
    text-transform: uppercase;
    font-size: 0.75rem;
    letter-spacing: 0.5px;
  }

  .timeline-item {
    padding-left: 1rem;
    border-left: 2px solid #e3e6f0;
  }

  .timeline-item:last-child hr {
    display: none;
  }
</style>