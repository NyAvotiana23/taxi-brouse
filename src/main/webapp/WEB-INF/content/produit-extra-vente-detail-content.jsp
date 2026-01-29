<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%
    ProduitExtraVente vente = (ProduitExtraVente) request.getAttribute("vente");
    List<ProduitExtraVentePayement> payements = (List<ProduitExtraVentePayement>) request.getAttribute("payements");
    BigDecimal montantTotal = (BigDecimal) request.getAttribute("montantTotal");
    BigDecimal montantPaye = (BigDecimal) request.getAttribute("montantPaye");
    BigDecimal montantReste = (BigDecimal) request.getAttribute("montantReste");
    
    String error = request.getParameter("error");
    String success = request.getParameter("success");

    if (montantTotal == null) montantTotal = BigDecimal.ZERO;
    if (montantPaye == null) montantPaye = BigDecimal.ZERO;
    if (montantReste == null) montantReste = BigDecimal.ZERO;

    double tauxPaiement = montantTotal.compareTo(BigDecimal.ZERO) > 0
            ? montantPaye.multiply(BigDecimal.valueOf(100)).divide(montantTotal, 2, BigDecimal.ROUND_HALF_UP).doubleValue()
            : 0.0;
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">
            <i class="bi bi-box-seam"></i> Détail Vente #<%= vente != null ? vente.getId() : "" %>
        </h1>
        <div>
            <a href="<%= request.getContextPath() %>/produit-extra-ventes" class="btn btn-secondary">
                <i class="bi bi-arrow-left"></i> Retour
            </a>
            <a href="<%= request.getContextPath() %>/produit-extra-ventes?action=addPayement&venteId=<%= vente != null ? vente.getId() : "" %>" class="btn btn-success">
                <i class="bi bi-plus-lg"></i> Ajouter Paiement
            </a>
        </div>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i> <%= error %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <% if (success != null) { %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle"></i> <%= success %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <% if (vente != null) { %>
    
    <!-- Summary Cards -->
    <div class="row mb-4">
        <div class="col-md-3">
            <div class="card border-left-primary shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                Montant Total
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", montantTotal) %> Ar
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-currency-exchange fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-left-success shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                Montant Payé
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", montantPaye) %> Ar
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-cash-coin fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-left-<%= montantReste.compareTo(BigDecimal.ZERO) > 0 ? "danger" : "info" %> shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-<%= montantReste.compareTo(BigDecimal.ZERO) > 0 ? "danger" : "info" %> text-uppercase mb-1">
                                Reste à Payer
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", montantReste) %> Ar
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-exclamation-triangle fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card border-left-warning shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                Progression
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%.1f%%", tauxPaiement) %>
                            </div>
                            <div class="progress progress-sm mt-2">
                                <div class="progress-bar bg-<%= tauxPaiement >= 100 ? "success" : (tauxPaiement >= 50 ? "warning" : "danger") %>" 
                                     role="progressbar" style="width: <%= tauxPaiement %>%" 
                                     aria-valuenow="<%= tauxPaiement %>" aria-valuemin="0" aria-valuemax="100"></div>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-percent fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Vente Info -->
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <i class="bi bi-info-circle"></i> Informations de la Vente
                    </h6>
                </div>
                <div class="card-body">
                    <table class="table table-borderless">
                        <tr>
                            <th width="40%">Date:</th>
                            <td><%= vente.getDate() != null ? vente.getDate().toLocalDate() : "-" %></td>
                        </tr>
                        <tr>
                            <th>Produit:</th>
                            <td><%= vente.getProduitExtra() != null ? vente.getProduitExtra().getNom() : "-" %></td>
                        </tr>
                        <tr>
                            <th>Catégorie:</th>
                            <td><%= vente.getProduitExtra() != null && vente.getProduitExtra().getProduitCategorie() != null ? vente.getProduitExtra().getProduitCategorie().getLibelle() : "-" %></td>
                        </tr>
                        <tr>
                            <th>Client:</th>
                            <td><%= vente.getClient() != null ? vente.getClient().getNomClient() : "-" %></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <i class="bi bi-calculator"></i> Détails Financiers
                    </h6>
                </div>
                <div class="card-body">
                    <table class="table table-borderless">
                        <tr>
                            <th width="40%">Prix Unitaire:</th>
                            <td><%= String.format("%,.2f", vente.getPrixUnitaire()) %> Ar</td>
                        </tr>
                        <tr>
                            <th>Quantité:</th>
                            <td><%= vente.getQuantite() %></td>
                        </tr>
                        <tr>
                            <th>Remise:</th>
                            <td><%= vente.getRemise() != null ? String.format("%.1f%%", vente.getRemise()) : "0%" %></td>
                        </tr>
                        <tr class="fw-bold">
                            <th>Montant Net:</th>
                            <td class="text-primary"><%= String.format("%,.2f", montantTotal) %> Ar</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Payments Table -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-cash-stack"></i> Historique des Paiements
            </h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Date Paiement</th>
                            <th>Montant</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (payements != null && !payements.isEmpty()) {
                        for (ProduitExtraVentePayement p : payements) { %>
                    <tr>
                        <td><%= p.getId() %></td>
                        <td><%= p.getDatePayement() != null ? p.getDatePayement().toLocalDate() : "-" %></td>
                        <td class="text-end text-success fw-bold"><%= String.format("%,.2f", p.getMontant()) %> Ar</td>
                        <td>
                            <form action="<%= request.getContextPath() %>/produit-extra-ventes" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="deletePayement">
                                <input type="hidden" name="payementId" value="<%= p.getId() %>">
                                <input type="hidden" name="venteId" value="<%= vente.getId() %>">
                                <button type="submit" class="btn btn-sm btn-danger" 
                                        onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce paiement ?')">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="4" class="text-center text-muted">
                            <i class="bi bi-inbox fs-1"></i>
                            <p>Aucun paiement enregistré</p>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                    <% if (payements != null && !payements.isEmpty()) { %>
                    <tfoot class="table-secondary fw-bold">
                        <tr>
                            <td colspan="2">TOTAL PAYÉ</td>
                            <td class="text-end text-success"><%= String.format("%,.2f", montantPaye) %> Ar</td>
                            <td></td>
                        </tr>
                    </tfoot>
                    <% } %>
                </table>
            </div>
        </div>
    </div>

    <% } else { %>
    <div class="alert alert-warning">
        <i class="bi bi-exclamation-triangle"></i> Vente non trouvée
    </div>
    <% } %>
</div>
