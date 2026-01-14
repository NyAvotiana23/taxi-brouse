<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.view.VmVehiculeDetail" %>
<%@ page import="com.mdgtaxi.view.VmVehiculeCoutEntretien" %>
<%@ page import="com.mdgtaxi.entity.VehiculeEntretien" %>
<%@ page import="com.mdgtaxi.dto.MouvementStatusDto" %>
<%@ page import="com.mdgtaxi.dto.StatusObjectDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
  VmVehiculeDetail detail = (VmVehiculeDetail) request.getAttribute("detail");
  VmVehiculeCoutEntretien cout = (VmVehiculeCoutEntretien) request.getAttribute("cout");
  List<VehiculeEntretien> entretiens = (List<VehiculeEntretien>) request.getAttribute("entretiens");
  MouvementStatusDto currentStatus = (MouvementStatusDto) request.getAttribute("currentStatus");
  List<MouvementStatusDto> statusHistory = (List<MouvementStatusDto>) request.getAttribute("statusHistory");
  List<StatusObjectDto> availableStatuts = (List<StatusObjectDto>) request.getAttribute("availableStatuts");
  String error = (String) request.getAttribute("error");

  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
  DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
%>

<div class="container-fluid">
  <!-- Header -->
  <div class="d-sm-flex align-items-center justify-content-between mb-4">
    <h1 class="h3 mb-0 text-gray-800">
      <i class="fas fa-car"></i> Détails du Véhicule
    </h1>
    <a href="<%= request.getContextPath() %>/vehicules" class="btn btn-secondary">
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
  <!-- Vehicle Information Card -->
  <div class="row">
    <div class="col-xl-8 col-lg-7">
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">
            Informations du Véhicule
          </h6>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <div class="info-group mb-3">
                <label class="text-muted small">Immatriculation</label>
                <h5 class="mb-0"><%= detail.getImmatriculation() %></h5>
              </div>
            </div>
            <div class="col-md-6">
              <div class="info-group mb-3">
                <label class="text-muted small">Type de Véhicule</label>
                <h5 class="mb-0">
                                        <span class="badge badge-secondary">
                                            <%= detail.getVehiculeTypeLibelle() %>
                                        </span>
                </h5>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-6">
              <div class="info-group mb-3">
                <label class="text-muted small">Marque</label>
                <p class="mb-0"><strong><%= detail.getMarque() %></strong></p>
              </div>
            </div>
            <div class="col-md-6">
              <div class="info-group mb-3">
                <label class="text-muted small">Modèle</label>
                <p class="mb-0"><strong><%= detail.getModele() %></strong></p>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-4">
              <div class="info-group mb-3">
                <label class="text-muted small">Capacité Passagers</label>
                <p class="mb-0">
                  <i class="fas fa-users text-info"></i>
                  <strong><%= detail.getMaximumPassager() %></strong> places
                </p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="info-group mb-3">
                <label class="text-muted small">Type Carburant</label>
                <p class="mb-0">
                                        <span class="badge badge-info">
                                            <%= detail.getCarburantTypeLibelle() %>
                                        </span>
                </p>
              </div>
            </div>
            <div class="col-md-4">
              <div class="info-group mb-3">
                <label class="text-muted small">Consommation</label>
                <p class="mb-0">
                  <% if (detail.getDepenseCarburant100km() != null) { %>
                  <strong><%= detail.getDepenseCarburant100km() %></strong> L/100km
                  <% } else { %>
                  <span class="text-muted">N/A</span>
                  <% } %>
                </p>
              </div>
            </div>
          </div>

          <% if (detail.getCapaciteCarburant() != null) { %>
          <div class="row">
            <div class="col-md-12">
              <div class="info-group mb-3">
                <label class="text-muted small">Capacité Réservoir</label>
                <p class="mb-0">
                  <i class="fas fa-gas-pump text-warning"></i>
                  <strong><%= detail.getCapaciteCarburant() %></strong> Litres
                </p>
              </div>
            </div>
          </div>
          <% } %>
        </div>
      </div>
    </div>

    <!-- Current Status & Stats Card -->
    <div class="col-xl-4 col-lg-5">
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
            Depuis le <%= currentStatus.getDateMouvement().format(dateFormatter) %>
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

      <!-- Maintenance Cost Card -->
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">Coût d'Entretien</h6>
        </div>
        <div class="card-body text-center">
          <h3 class="text-success">
            <%= cout != null && cout.getTotalDepenseEntretien() != null ?
                    cout.getTotalDepenseEntretien() : 0 %> Ar
          </h3>
          <small class="text-muted">Coût total cumulé</small>
        </div>
      </div>
    </div>
  </div>

  <!-- Change Status Section -->
  <div class="row">
    <div class="col-lg-6">
      <div class="card shadow mb-4">
        <div class="card-header py-3 bg-warning">
          <h6 class="m-0 font-weight-bold text-white">
            <i class="fas fa-exchange-alt"></i> Changer le Statut
          </h6>
        </div>
        <div class="card-body">
          <form action="<%= request.getContextPath() %>/vehicules/detail" method="POST">
            <input type="hidden" name="id" value="<%= detail.getIdVehicule() %>">
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

    <!-- Add Maintenance Section -->
    <div class="col-lg-6">
      <div class="card shadow mb-4">
        <div class="card-header py-3 bg-info">
          <h6 class="m-0 font-weight-bold text-white">
            <i class="fas fa-tools"></i> Ajouter un Entretien
          </h6>
        </div>
        <div class="card-body">
          <form action="<%= request.getContextPath() %>/vehicules/detail" method="POST">
            <input type="hidden" name="id" value="<%= detail.getIdVehicule() %>">
            <input type="hidden" name="action" value="addEntretien">

            <div class="mb-3">
              <label for="motif" class="form-label">Motif <span class="text-danger">*</span></label>
              <input type="text"
                     class="form-control"
                     id="motif"
                     name="motif"
                     placeholder="Ex: Vidange, Révision..."
                     required>
            </div>

            <div class="mb-3">
              <label for="montantDepense" class="form-label">Montant (Ar) <span class="text-danger">*</span></label>
              <input type="number"
                     step="0.01"
                     class="form-control"
                     id="montantDepense"
                     name="montantDepense"
                     min="0"
                     required>
            </div>

            <div class="row">
              <div class="col-md-6">
                <div class="mb-3">
                  <label for="dateDebutEntretien" class="form-label">Date Début</label>
                  <input type="datetime-local"
                         class="form-control"
                         id="dateDebutEntretien"
                         name="dateDebutEntretien">
                </div>
              </div>
              <div class="col-md-6">
                <div class="mb-3">
                  <label for="dateFinEntretien" class="form-label">Date Fin</label>
                  <input type="datetime-local"
                         class="form-control"
                         id="dateFinEntretien"
                         name="dateFinEntretien">
                </div>
              </div>
            </div>

            <button type="submit" class="btn btn-info btn-block">
              <i class="fas fa-plus"></i> Ajouter l'entretien
            </button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Status History -->
  <div class="row">
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
                  <%= mvt.getDateMouvement().format(dateFormatter) %>
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

    <!-- Maintenance History -->
    <div class="col-lg-6">
      <div class="card shadow mb-4">
        <div class="card-header py-3">
          <h6 class="m-0 font-weight-bold text-primary">
            <i class="fas fa-wrench"></i> Historique des Entretiens
          </h6>
        </div>
        <div class="card-body">
          <% if (entretiens != null && !entretiens.isEmpty()) { %>
          <div class="table-responsive">
            <table class="table table-sm">
              <thead>
              <tr>
                <th>Date</th>
                <th>Motif</th>
                <th class="text-right">Montant</th>
              </tr>
              </thead>
              <tbody>
              <% for (VehiculeEntretien e : entretiens) { %>
              <tr>
                <td>
                  <small><%= e.getDateDebutEntretien().format(dateFormatter) %></small>
                  <% if (e.getDateFinEntretien() != null) { %>
                  <br>
                  <small class="text-muted">
                    au <%= e.getDateFinEntretien().format(dateFormatter) %>
                  </small>
                  <% } %>
                </td>
                <td><%= e.getMotif() %></td>
                <td class="text-right">
                  <strong class="text-danger">
                    <%= e.getMontantDepense() %> Ar
                  </strong>
                </td>
              </tr>
              <% } %>
              </tbody>
            </table>
          </div>
          <% } else { %>
          <p class="text-muted text-center">
            <i class="fas fa-info-circle"></i> Aucun entretien enregistré
          </p>
          <% } %>
        </div>
      </div>
    </div>
  </div>
  <% } else { %>
  <div class="alert alert-warning">
    <i class="fas fa-exclamation-triangle"></i>
    Véhicule introuvable
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