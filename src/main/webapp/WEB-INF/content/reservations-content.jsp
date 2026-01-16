<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%@ page import="com.mdgtaxi.dto.StatusObjectDto" %>
<%@ page import="java.math.BigDecimal" %>

<%
    TrajetReservation reservation = (TrajetReservation) request.getAttribute("reservation");
    List<TrajetReservationDetails> reservationDetails = (List<TrajetReservationDetails>) request.getAttribute("reservationDetails");
    List<VehiculeTarifTypePlace> tarifPlaces = (List<VehiculeTarifTypePlace>) request.getAttribute("tarifPlaces"); // Added for TypePlace logic
    List<TrajetReservationPaiement> payments = (List<TrajetReservationPaiement>) request.getAttribute("payments");
    List<TrajetReservationMouvementStatut> statusHistory = (List<TrajetReservationMouvementStatut>) request.getAttribute("statusHistory");
    List<TypeObjectDTO> modePaiements = (List<TypeObjectDTO>) request.getAttribute("modePaiements");
    List<StatusObjectDto> reservationStatuts = (List<StatusObjectDto>) request.getAttribute("reservationStatuts");
    BigDecimal montantTotal = (BigDecimal) request.getAttribute("montantTotal");
    BigDecimal montantPaye = (BigDecimal) request.getAttribute("montantPaye");
    BigDecimal soldeRestant = (BigDecimal) request.getAttribute("soldeRestant");
    String error = (String) request.getAttribute("error");
    TrajetReservationDetails editingDetail = (TrajetReservationDetails) request.getAttribute("editingDetail"); // For edit mode
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">Détails de la Réservation #<%= reservation.getId() %></h1>
        <a href="<%= request.getContextPath() %>/reservations" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Retour
        </a>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <!-- Informations principales -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Informations de la Réservation</h6>
                </div>
                <div class="card-body">
                    <p><strong>Trajet:</strong> <%= reservation.getTrajet().getLigne().getVilleDepart().getNom() %> → <%= reservation.getTrajet().getLigne().getVilleArrivee().getNom() %></p>
                    <p><strong>Date du trajet:</strong> <%= reservation.getTrajet().getDatetimeDepart() %></p>
                    <p><strong>Client:</strong> <%= reservation.getClient().getNomClient() %></p>
                    <p><strong>Passager:</strong> <%= reservation.getNomPassager() %></p>
                    <p><strong>Date de réservation:</strong> <%= reservation.getDateReservation() %></p>
                    <p><strong>Statut:</strong> <span class="badge badge-primary"><%= reservation.getReservationStatut().getLibelle() %></span></p>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-success">Informations Financières</h6>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-6">
                            <p class="text-muted mb-1">Montant Total</p>
                            <h4 class="text-dark"><%= String.format("%,.2f", montantTotal) %> Ar</h4>
                        </div>
                        <div class="col-6">
                            <p class="text-muted mb-1">Déjà Payé</p>
                            <h4 class="text-success"><%= String.format("%,.2f", montantPaye) %> Ar</h4>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-6">
                            <p class="text-muted mb-1">Non Payé</p>
                            <h4 class="text-warning"><%= String.format("%,.2f", soldeRestant) %> Ar</h4>
                        </div>
                        <div class="col-6">
                            <p class="text-muted mb-1">Reste</p>
                            <h4 class="text-danger"><%= String.format("%,.2f", soldeRestant) %> Ar</h4>
                        </div>
                    </div>
                    <% if (soldeRestant.compareTo(BigDecimal.ZERO) > 0) { %>
                    <span class="badge badge-warning">Partiellement payé</span>
                    <% } else if (soldeRestant.compareTo(BigDecimal.ZERO) == 0) { %>
                    <span class="badge badge-success">Payé intégralement</span>
                    <% } else { %>
                    <span class="badge badge-danger">Remboursement dû</span>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <!-- Détails des Places (TypePlace logic) -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Détails des Places</h6>
        </div>
        <div class="card-body">
            <% if (reservationDetails != null && !reservationDetails.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>Type de Place</th>
                        <th>Nombre</th>
                        <th>Tarif Unitaire</th>
                        <th>Sous-total</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (TrajetReservationDetails d : reservationDetails) { %>
                    <tr>
                        <td><%= d.getTypePlace().getNomTypePlace() %></td>
                        <td><%= String.format("%.1f", d.getNombrePlaces()) %></td>
                        <td><%= String.format("%,.2f", d.getTarifUnitaire()) %> Ar</td>
                        <td><%= String.format("%,.2f", d.getNombrePlaces() * d.getTarifUnitaire()) %> Ar</td>
                        <td>
                            <a href="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>&action=editDetail&detailId=<%= d.getId() %>"
                               class="btn btn-sm btn-info">Modifier</a>
                            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="deleteDetail">
                                <input type="hidden" name="detailId" value="<%= d.getId() %>">
                                <button type="submit" class="btn btn-sm btn-danger">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <div class="alert alert-info">Aucun détail de place ajouté. Ajoutez-en pour spécifier les types de places.</div>
            <% } %>

            <!-- Form to Add/Update Detail -->
            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post">
                <input type="hidden" name="action" value="<%= editingDetail != null ? "updateDetail" : "addDetail" %>">
                <input type="hidden" name="detailId" value="<%= editingDetail != null ? editingDetail.getId() : "" %>">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="idTypePlace" class="form-label">Type de Place</label>
                        <select class="form-control" id="idTypePlace" name="idTypePlace" required>
                            <option value="">Choisir...</option>
                            <% if (tarifPlaces != null) {
                                for (VehiculeTarifTypePlace tp : tarifPlaces) {
                                    double sold = soldPerType.getOrDefault(tp.getTypePlace().getId(), 0.0);
                                    double remaining = tp.getNombrePlace() - sold;
                                    if (remaining > 0 || (editingDetail != null && editingDetail.getTypePlace().getId().equals(tp.getTypePlace().getId()))) { %>
                            <option value="<%= tp.getTypePlace().getId() %>"
                                    <%= (editingDetail != null && editingDetail.getTypePlace().getId().equals(tp.getTypePlace().getId())) ? "selected" : "" %>>
                                <%= tp.getTypePlace().getLibelle() %> (<%= tp.getTarifUnitaire() %> Ar/unité, <%= String.format("%.1f", remaining) %> restantes)
                            </option>
                            <% }
                            }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="nombrePlaces" class="form-label">Nombre de Places</label>
                        <input type="number" step="0.5" min="0.5" class="form-control" id="nombrePlaces" name="nombrePlaces"
                               value="<%= editingDetail != null ? String.format("%.1f", editingDetail.getNombrePlaces()) : "" %>" required>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary"><%= editingDetail != null ? "Mettre à jour" : "Ajouter" %></button>
                <% if (editingDetail != null) { %>
                <a href="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" class="btn btn-secondary">Annuler édition</a>
                <% } %>
            </form>
        </div>
    </div>

    <!-- Ajout de Paiement -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Ajouter un Paiement</h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post">
                <input type="hidden" name="action" value="addPaiement">
                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="montant" class="form-label">Montant *</label>
                        <input type="number" step="0.01" class="form-control" id="montant" name="montant" required>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="idModePaiement" class="form-label">Mode de Paiement *</label>
                        <select class="form-control" id="idModePaiement" name="idModePaiement" required>
                            <option value="">Choisir...</option>
                            <% if (modePaiements != null) {
                                for (TypeObjectDTO m : modePaiements) { %>
                            <option value="<%= m.getId() %>"><%= m.getLibelle() %></option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-4 mb-3">
                        <label for="idCaisse" class="form-label">Caisse</label>
                        <select class="form-control" id="idCaisse" name="idCaisse">
                            <option value="">Choisir...</option>
                            <!-- Assume caisses list is added in servlet if needed -->
                        </select>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-cash"></i> Enregistrer Paiement
                </button>
            </form>
        </div>
    </div>

    <!-- Historique des paiements -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Historique des Paiements</h6>
        </div>
        <div class="card-body">
            <% if (payments != null && !payments.isEmpty()) { %>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Montant</th>
                    <th>Mode</th>
                    <th>Caisse</th>
                </tr>
                </thead>
                <tbody>
                <% for (TrajetReservationPaiement p : payments) { %>
                <tr>
                    <td><%= p.getDatePaiement() %></td>
                    <td><%= String.format("%,.2f", p.getMontant()) %> Ar</td>
                    <td><%= p.getModePaiement().getLibelle() %></td>
                    <td><%= p.getCaisse() != null ? p.getCaisse().getNom() : "-" %></td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } else { %>
            <div class="alert alert-info">Aucun paiement enregistré.</div>
            <% } %>
        </div>
    </div>

    <!-- Changement de statut -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Changer le Statut</h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post">
                <input type="hidden" name="action" value="changeStatut">

                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="idStatut" class="form-label">Nouveau Statut *</label>
                            <select class="form-control" id="idStatut" name="idStatut" required>
                                <option value="">Choisir...</option>
                                <% for (StatusObjectDto s : reservationStatuts) { %>
                                <option value="<%= s.getId() %>"><%= s.getLibelle() %></option>
                                <% } %>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label for="observation" class="form-label">Observation</label>
                            <textarea class="form-control" id="observation" name="observation" rows="2"></textarea>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-arrow-repeat"></i> Changer Statut
                </button>
            </form>
        </div>
    </div>

    <!-- Historique des statuts -->
    <div class="card shadow">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Historique des Statuts</h6>
        </div>
        <div class="card-body">
            <% if (statusHistory != null && !statusHistory.isEmpty()) { %>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Statut</th>
                    <th>Observation</th>
                </tr>
                </thead>
                <tbody>
                <% for (TrajetReservationMouvementStatut m : statusHistory) { %>
                <tr>
                    <td><%= m.getDateMouvement() %></td>
                    <td><span class="badge badge-info"><%= m.getNouveauStatut().getLibelle() %></span></td>
                    <td><%= m.getObservation() != null ? m.getObservation() : "-" %></td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } else { %>
            <div class="alert alert-info">Aucun changement de statut enregistré.</div>
            <% } %>
        </div>
    </div>
</div>