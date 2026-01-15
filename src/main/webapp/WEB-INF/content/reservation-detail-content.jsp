<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%@ page import="com.mdgtaxi.dto.StatusObjectDto" %>
<%@ page import="java.math.BigDecimal" %>

<%
    TrajetReservation reservation = (TrajetReservation) request.getAttribute("reservation");
    List<TrajetReservationDetails> reservationDetails = (List<TrajetReservationDetails>) request.getAttribute("reservationDetails");
    List<TrajetReservationPaiement> payments = (List<TrajetReservationPaiement>) request.getAttribute("payments");
    List<TrajetReservationMouvementStatut> statusHistory = (List<TrajetReservationMouvementStatut>) request.getAttribute("statusHistory");
    List<TypeObjectDTO> modePaiements = (List<TypeObjectDTO>) request.getAttribute("modePaiements");
    List<StatusObjectDto> reservationStatuts = (List<StatusObjectDto>) request.getAttribute("reservationStatuts");
    BigDecimal montantTotal = (BigDecimal) request.getAttribute("montantTotal");
    BigDecimal montantPaye = (BigDecimal) request.getAttribute("montantPaye");
    BigDecimal soldeRestant = (BigDecimal) request.getAttribute("soldeRestant");
    String error = (String) request.getAttribute("error");
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
                        <div class="col-12">
                            <p class="text-muted mb-1">Solde Restant</p>
                            <h3 class="<%= soldeRestant.compareTo(BigDecimal.ZERO) > 0 ? "text-danger" : "text-success" %>">
                                <%= String.format("%,.2f", soldeRestant) %> Ar
                            </h3>
                            <% if (soldeRestant.compareTo(BigDecimal.ZERO) == 0) { %>
                            <span class="badge badge-success">Payé intégralement</span>
                            <% } else if (montantPaye.compareTo(BigDecimal.ZERO) > 0) { %>
                            <span class="badge badge-warning">Partiellement payé</span>
                            <% } else { %>
                            <span class="badge badge-danger">Non payé</span>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Détails des places réservées -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Places Réservées</h6>
        </div>
        <div class="card-body">
            <% if (reservationDetails != null && !reservationDetails.isEmpty()) { %>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Type de Place</th>
                    <th>Nombre de Places</th>
                    <th>Tarif Unitaire</th>
                    <th>Sous-total</th>
                </tr>
                </thead>
                <tbody>
                <%
                    com.mdgtaxi.service.VehiculeService vehiculeService = new com.mdgtaxi.service.VehiculeService();
                    for (TrajetReservationDetails detail : reservationDetails) {
                        double tarifUnitaire = vehiculeService.getTotalMaxChiffreAffairePossible(
                                reservation.getTrajet().getVehicule().getId(),
                                detail.getTypePlace().getId()
                        ) / vehiculeService.getTarifTypePlaceByVehiculeAndType(
                                reservation.getTrajet().getVehicule().getId(),
                                detail.getTypePlace().getId()
                        ).getNombrePlace();
                        double sousTotal = detail.getNombrePlaces() * tarifUnitaire;
                %>
                <tr>
                    <td><%= detail.getTypePlace().getNomTypePlace() %></td>
                    <td><%= detail.getNombrePlaces() %></td>
                    <td><%= String.format("%,.2f", tarifUnitaire) %> Ar</td>
                    <td><%= String.format("%,.2f", sousTotal) %> Ar</td>
                </tr>
                <% } %>
                <tr class="font-weight-bold">
                    <td colspan="3" class="text-right">TOTAL</td>
                    <td><%= String.format("%,.2f", montantTotal) %> Ar</td>
                </tr>
                </tbody>
            </table>
            <% } else { %>
            <div class="alert alert-info">Aucun détail de place enregistré.</div>
            <% } %>
        </div>
    </div>

    <!-- Formulaire de paiement -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Ajouter un Paiement</h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post">
                <input type="hidden" name="action" value="addPayment">
                <input type="hidden" name="idClient" value="<%= reservation.getClient().getId() %>">

                <div class="row">
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="montant" class="form-label">Montant *</label>
                            <input type="number" step="0.01" class="form-control" id="montant"
                                   name="montant" max="<%= soldeRestant %>"
                                   placeholder="Maximum: <%= String.format("%,.2f", soldeRestant) %>" required>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="idModePaiement" class="form-label">Mode Paiement *</label>
                            <select class="form-control" id="idModePaiement" name="idModePaiement" required>
                                <option value="">Choisir...</option>
                                <% for (TypeObjectDTO t : modePaiements) { %>
                                <option value="<%= t.getId() %>"><%= t.getLibelle() %></option>
                                <% } %>
                            </select>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="mb-3">
                            <label for="idCaisse" class="form-label">Caisse (optionnel)</label>
                            <select class="form-control" id="idCaisse" name="idCaisse">
                                <option value="">Choisir...</option>
                                <% List<Caisse> caisses = (List<Caisse>) request.getAttribute("caisses");
                                    if (caisses != null) {
                                        for (Caisse c : caisses) { %>
                                <option value="<%= c.getId() %>"><%= c.getNom() %></option>
                                <% }
                                } %>
                            </select>
                        </div>
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