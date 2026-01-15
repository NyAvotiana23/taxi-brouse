<%-- Update reservation-detail-content.jsp (using correct entity names) --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%@ page import="com.mdgtaxi.dto.StatusObjectDto" %>

<%
    TrajetReservation reservation = (TrajetReservation) request.getAttribute("reservation");
    List<TrajetReservationPaiement> payments = (List<TrajetReservationPaiement>) request.getAttribute("payments");
    List<TrajetReservationMouvementStatut> statusHistory = (List<TrajetReservationMouvementStatut>) request.getAttribute("statusHistory");
    List<TypeObjectDTO> modePaiements = (List<TypeObjectDTO>) request.getAttribute("modePaiements");
    List<StatusObjectDto> reservationStatuts = (List<StatusObjectDto>) request.getAttribute("reservationStatuts");
    String error = (String) request.getAttribute("error");
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">Détails de la Réservation</h1>
        <a href="<%= request.getContextPath() %>/reservations" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Retour
        </a>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <!-- Reservation Information -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Informations</h6>
        </div>
        <div class="card-body">
            <p><strong>Trajet:</strong> <%= reservation.getTrajet().getLigne().getVilleDepart().getNom() %> → <%= reservation.getTrajet().getLigne().getVilleArrivee().getNom() %></p>
            <p><strong>Client:</strong> <%= reservation.getClient().getNomClient() %></p>
            <p><strong>Passager:</strong> <%= reservation.getNomPassager() %></p>
            <p><strong>Siège:</strong> <%= reservation.getNumeroSiege() != null ? reservation.getNumeroSiege() : "-" %></p>
            <p><strong>Places:</strong> <%= reservation.getNombrePlaceReservation() %></p>
            <p><strong>Statut:</strong> <%= reservation.getReservationStatut().getLibelle() %></p>
        </div>
    </div>

    <!-- Paiement Form -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Ajouter un Paiement</h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post">
                <input type="hidden" name="action" value="addPayment">
                <input type="hidden" name="idClient" value="<%= reservation.getClient().getId() %>"> <!-- Use reservation's client -->
                <div class="mb-3">
                    <label for="montant" class="form-label">Montant</label>
                    <input type="number" step="0.01" class="form-control" id="montant" name="montant" required>
                </div>
                <div class="mb-3">
                    <label for="idModePaiement" class="form-label">Mode Paiement</label>
                    <select class="form-control" id="idModePaiement" name="idModePaiement" required>
                        <option value="">Choisir...</option>
                        <% for (TypeObjectDTO t : modePaiements) { %>
                        <option value="<%= t.getId() %>"><%= t.getLibelle() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="idCaisse" class="form-label">Caisse (optionnel)</label>
                    <select class="form-control" id="idCaisse" name="idCaisse">
                        <option value="">Choisir...</option>
                        <!-- Assume caisses list is loaded and set as attribute 'caisses' -->
                        <% List<Caisse> caisses = (List<Caisse>) request.getAttribute("caisses");
                            if (caisses != null) {
                                for (Caisse c : caisses) { %>
                        <option value="<%= c.getId() %>"><%= c.getNom() %></option>
                        <% }
                        } %>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Enregistrer Paiement</button>
            </form>
        </div>
    </div>

    <!-- Liste des Paiements -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Historique des Paiements</h6>
        </div>
        <div class="card-body">
            <% if (payments != null && !payments.isEmpty()) { %>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Montant</th>
                    <th>Date</th>
                    <th>Mode</th>
                    <th>Caisse</th>
                </tr>
                </thead>
                <tbody>
                <% for (TrajetReservationPaiement p : payments) { %>
                <tr>
                    <td><%= p.getMontant() %></td>
                    <td><%= p.getDatePaiement() %></td>
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

    <!-- Change Status Form -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Changer Statut</h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post">
                <input type="hidden" name="action" value="changeStatut">
                <div class="mb-3">
                    <label for="idStatut" class="form-label">Nouveau Statut</label>
                    <select class="form-control" id="idStatut" name="idStatut" required>
                        <option value="">Choisir...</option>
                        <% for (StatusObjectDto s : reservationStatuts) { %>
                        <option value="<%= s.getId() %>"><%= s.getLibelle() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="observation" class="form-label">Observation</label>
                    <textarea class="form-control" id="observation" name="observation"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Changer Statut</button>
            </form>
        </div>
    </div>

    <!-- Liste des Mouvements Statut -->
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
                    <td><%= m.getNouveauStatut().getLibelle() %></td>
                    <td><%= m.getObservation() != null ? m.getObservation() : "-" %></td>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } else { %>
            <div class="alert alert-info">Aucun changement de statut.</div>
            <% } %>
        </div>
    </div>
</div>