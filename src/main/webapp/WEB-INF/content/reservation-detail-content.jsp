<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%@ page import="com.mdgtaxi.dto.StatusObjectDto" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Map" %>

<%
    TrajetReservation reservation = (TrajetReservation) request.getAttribute("reservation");
    List<TrajetReservationDetails> reservationDetails = (List<TrajetReservationDetails>) request.getAttribute("reservationDetails");
    List<VehiculeTarifTypePlace> tarifPlaces = (List<VehiculeTarifTypePlace>) request.getAttribute("tarifPlaces");
    Map<Long, Double> soldPerType = (Map<Long, Double>) request.getAttribute("soldPerType");
    List<TrajetReservationPaiement> payments = (List<TrajetReservationPaiement>) request.getAttribute("payments");
    List<TrajetReservationMouvementStatut> statusHistory = (List<TrajetReservationMouvementStatut>) request.getAttribute("statusHistory");
    List<TypeObjectDTO> modePaiements = (List<TypeObjectDTO>) request.getAttribute("modePaiements");
    List<StatusObjectDto> reservationStatuts = (List<StatusObjectDto>) request.getAttribute("reservationStatuts");
    List<TypeObjectDTO> categories = (List<TypeObjectDTO>) request.getAttribute("categories");
    BigDecimal montantTotal = (BigDecimal) request.getAttribute("montantTotal");
    BigDecimal montantPaye = (BigDecimal) request.getAttribute("montantPaye");
    BigDecimal soldeRestant = (BigDecimal) request.getAttribute("soldeRestant");
    String error = (String) request.getAttribute("error");
    TrajetReservationDetails editingDetail = (TrajetReservationDetails) request.getAttribute("editingDetail");


%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">Détails de la Réservation #<%= reservation.getId() %></h1>
        <a href="<%= request.getContextPath() %>/reservations" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Retour
        </a>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i> <%= error %>
        <button type="button" class="close" data-dismiss="alert">
            <span>&times;</span>
        </button>
    </div>
    <% } %>

    <!-- Informations principales -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header py-3 bg-primary text-white">
                    <h6 class="m-0 font-weight-bold">Informations de la Réservation</h6>
                </div>
                <div class="card-body">
                    <p><strong>Trajet:</strong>
                        <%= reservation.getTrajet().getLigne().getVilleDepart().getNom() %> →
                        <%= reservation.getTrajet().getLigne().getVilleArrivee().getNom() %>
                    </p>
                    <p><strong>Véhicule:</strong> <%= reservation.getTrajet().getVehicule().getImmatriculation() %></p>
                    <p><strong>Date du trajet:</strong> <%= reservation.getTrajet().getDatetimeDepart() %></p>
                    <p><strong>Client:</strong> <%= reservation.getClient().getNomClient() %></p>
                    <p><strong>Passager:</strong> <%= reservation.getNomPassager() %></p>
                    <p><strong>Date de réservation:</strong> <%= reservation.getDateReservation() %></p>
                    <p><strong>Statut:</strong>
                        <span class="badge badge-primary"><%= reservation.getReservationStatut().getLibelle() %></span>
                    </p>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card shadow border-left-success">
                <div class="card-header py-3 bg-success text-white">
                    <h6 class="m-0 font-weight-bold">Informations Financières</h6>
                </div>
                <div class="card-body">
                    <div class="row mb-3">
                        <div class="col-6">
                            <p class="text-xs text-uppercase text-muted mb-1">Montant Total</p>
                            <h4 class="text-dark mb-0"><%= String.format("%,.2f", montantTotal) %> Ar</h4>
                        </div>
                        <div class="col-6">
                            <p class="text-xs text-uppercase text-muted mb-1">Déjà Payé</p>
                            <h4 class="text-success mb-0"><%= String.format("%,.2f", montantPaye) %> Ar</h4>
                        </div>
                    </div>
                    <hr>
                    <div class="row mb-3">
                        <div class="col-6">
                            <p class="text-xs text-uppercase text-muted mb-1">Non Payé</p>
                            <h4 class="text-warning mb-0"><%= String.format("%,.2f", soldeRestant) %> Ar</h4>
                        </div>
                        <div class="col-6">
                            <p class="text-xs text-uppercase text-muted mb-1">Reste à Payer</p>
                            <h4 class="<%= soldeRestant.compareTo(BigDecimal.ZERO) > 0 ? "text-danger" : "text-success" %> mb-0">
                                <%= String.format("%,.2f", soldeRestant) %> Ar
                            </h4>
                        </div>
                    </div>
                    <div class="progress mb-2" style="height: 25px;">
                        <%
                            double percentage = montantTotal.compareTo(BigDecimal.ZERO) > 0
                                    ? montantPaye.divide(montantTotal, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue()
                                    : 0;
                        %>
                        <div class="progress-bar bg-success" role="progressbar"
                             style="width: <%= percentage %>%"
                             aria-valuenow="<%= percentage %>" aria-valuemin="0" aria-valuemax="100">
                            <%= String.format("%.1f", percentage) %>%
                        </div>
                    </div>
                    <% if (soldeRestant.compareTo(BigDecimal.ZERO) == 0) { %>
                    <span class="badge badge-success"><i class="bi bi-check-circle"></i> Payé intégralement</span>
                    <% } else if (montantPaye.compareTo(BigDecimal.ZERO) > 0) { %>
                    <span class="badge badge-warning"><i class="bi bi-clock"></i> Partiellement payé</span>
                    <% } else { %>
                    <span class="badge badge-danger"><i class="bi bi-exclamation-circle"></i> Non payé</span>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <!-- Détails des Places -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-ticket-detailed"></i> Détails des Places Réservées
            </h6>
        </div>
        <div class="card-body">
            <% if (reservationDetails != null && !reservationDetails.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
                    <tr>
                        <th>Type de Place</th>
                        <th>Catégorie</th>
                        <th>Nombre</th>
                        <th>Tarif Unitaire</th>
                        <th>Sous-total</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (TrajetReservationDetails d : reservationDetails) {
                        double sousTotal = d.getNombrePlaces() * d.getTarifUnitaire();
                    %>
                    <tr>
                        <td><%= d.getTypePlace().getNomTypePlace() %></td>
                        <td><%= d.getCategoriePersonne().getLibelle() %></td>
                        <td><%= String.format("%.1f", d.getNombrePlaces()) %></td>
                        <td><%= String.format("%,.2f", d.getTarifUnitaire()) %> Ar</td>
                        <td><%= String.format("%,.2f", sousTotal) %> Ar</td>
                        <td>
                            <a href="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>&action=editDetail&detailId=<%= d.getId() %>"
                               class="btn btn-sm btn-info">
                                <i class="bi bi-pencil"></i>
                            </a>
                            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>"
                                  method="post" style="display: inline;"
                                  onsubmit="return confirm('Êtes-vous sûr de vouloir supprimer ce détail?');">
                                <input type="hidden" name="action" value="deleteDetail">
                                <input type="hidden" name="detailId" value="<%= d.getId() %>">
                                <button type="submit" class="btn btn-sm btn-danger">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                    <tr class="font-weight-bold bg-light">
                        <td colspan="4" class="text-right">TOTAL</td>
                        <td colspan="2"><%= String.format("%,.2f", montantTotal) %> Ar</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <div class="alert alert-info" role="alert">
                <i class="bi bi-info-circle"></i> Aucun détail de place ajouté. Ajoutez-en pour spécifier les types de places.
            </div>
            <% } %>

            <!-- Form to Add/Update Detail -->
            <hr>
            <h6 class="font-weight-bold text-primary mb-3">
                <%= editingDetail != null ? "Modifier" : "Ajouter" %> un Détail de Place
            </h6>
            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post">
                <input type="hidden" name="action" value="<%= editingDetail != null ? "updateDetail" : "addDetail" %>">
                <% if (editingDetail != null) { %>
                <input type="hidden" name="detailId" value="<%= editingDetail.getId() %>">
                <% } %>

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="idTypePlace" class="form-label">Type de Place *</label>
                        <select class="form-control" id="idTypePlace" name="idTypePlace" required>
                            <option value="">Choisir...</option>
                            <% if (tarifPlaces != null) {
                                for (VehiculeTarifTypePlace tp : tarifPlaces) {
                                    double sold = soldPerType.getOrDefault(tp.getTypePlace().getId(), 0.0);
                                    double remaining = tp.getNombrePlace() - sold;
                                    // Show if available OR if editing this type
                                    if (remaining > 0 || (editingDetail != null && editingDetail.getTypePlace().getId().equals(tp.getTypePlace().getId()))) {
                            %>
                            <option value="<%= tp.getTypePlace().getId() %>"
                                    <%= (editingDetail != null && editingDetail.getTypePlace().getId().equals(tp.getTypePlace().getId())) ? "selected" : "" %>>
                                <%= tp.getTypePlace().getNomTypePlace() %>
                                (Tarif: <%= String.format("%.0f", tp.getTarifUnitaire()) %> Ar, (Sans remise encore)
                                Restantes: <%= String.format("%.1f", remaining) %>)
                            </option>
                            <%
                                        }
                                    }
                                } %>
                        </select>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="idCategoriePersonne" class="form-label">Catégorie Personne *</label>
                        <select class="form-control" id="idCategoriePersonne" name="idCategoriePersonne" required>
                            <option value="">Choisir...</option>
                            <% if (categories != null) {
                                for (TypeObjectDTO cat : categories) { %>
                            <option value="<%= cat.getId() %>"
                                    <%= (editingDetail != null && editingDetail.getCategoriePersonne().getId().equals(cat.getId())) ? "selected" : "" %>>
                                <%= cat.getLibelle() %>
                            </option>
                            <% }
                            } %>
                        </select>
                        <small class="form-text text-muted">Le tarif sera calculé avec remise si applicable</small>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="nombrePlaces" class="form-label">Nombre de Places *</label>
                        <input type="number" step="0.5" min="0.5" class="form-control"
                               id="nombrePlaces" name="nombrePlaces"
                               value="<%= editingDetail != null ? String.format("%.1f", editingDetail.getNombrePlaces()) : "" %>"
                               required>
                    </div>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-<%= editingDetail != null ? "check" : "plus" %>-circle"></i>
                        <%= editingDetail != null ? "Mettre à jour" : "Ajouter" %>
                    </button>
                    <% if (editingDetail != null) { %>
                    <a href="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>"
                       class="btn btn-secondary">
                        <i class="bi bi-x-circle"></i> Annuler
                    </a>
                    <% } %>
                </div>
            </form>
        </div>
    </div>

    <!-- Ajout de Paiement -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-cash-stack"></i> Ajouter un Paiement
            </h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/reservations/detail?id=<%= reservation.getId() %>" method="post">
                <input type="hidden" name="action" value="addPayment">
                <input type="hidden" name="idClient" value="<%= reservation.getClient().getId() %>">

                <div class="row">
                    <div class="col-md-4 mb-3">
                        <label for="montant" class="form-label">Montant *</label>
                        <input type="number" step="0.01" class="form-control" id="montant" name="montant"
                               max="<%= soldeRestant %>"
                               placeholder="Max: <%= String.format("%,.2f", soldeRestant) %> Ar" required>
                        <small class="form-text text-muted">Solde restant: <%= String.format("%,.2f", soldeRestant) %> Ar</small>
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
                        <label for="idCaisse" class="form-label">Caisse (optionnel)</label>
                        <select class="form-control" id="idCaisse" name="idCaisse">
                            <option value="">Choisir...</option>
                            <!-- Caisses to be loaded if needed -->
                        </select>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary" <%= soldeRestant.compareTo(BigDecimal.ZERO) <= 0 ? "disabled" : "" %>>
                    <i class="bi bi-cash"></i> Enregistrer Paiement
                </button>
            </form>
        </div>
    </div>

    <!-- Historique des paiements -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-clock-history"></i> Historique des Paiements
            </h6>
        </div>
        <div class="card-body">
            <% if (payments != null && !payments.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
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
                        <td class="text-success font-weight-bold"><%= String.format("%,.2f", p.getMontant()) %> Ar</td>
                        <td><span class="badge badge-info"><%= p.getModePaiement().getLibelle() %></span></td>
                        <td><%= p.getCaisse() != null ? p.getCaisse().getNom() : "-" %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <div class="alert alert-info" role="alert">
                <i class="bi bi-info-circle"></i> Aucun paiement enregistré.
            </div>
            <% } %>
        </div>
    </div>

    <!-- Changement de statut -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-arrow-repeat"></i> Changer le Statut
            </h6>
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
                                <% if (reservationStatuts != null) {
                                    for (StatusObjectDto s : reservationStatuts) { %>
                                <option value="<%= s.getId() %>"><%= s.getLibelle() %></option>
                                <% }
                                } %>
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
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-list-check"></i> Historique des Statuts
            </h6>
        </div>
        <div class="card-body">
            <% if (statusHistory != null && !statusHistory.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
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
            </div>
            <% } else { %>
            <div class="alert alert-info" role="alert">
                <i class="bi bi-info-circle"></i> Aucun changement de statut enregistré.
            </div>
            <% } %>
        </div>
    </div>
</div>