<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.LocalDate" %>
<%
    ProduitExtraVente vente = (ProduitExtraVente) request.getAttribute("vente");
    BigDecimal montantTotal = (BigDecimal) request.getAttribute("montantTotal");
    BigDecimal montantPaye = (BigDecimal) request.getAttribute("montantPaye");
    BigDecimal montantReste = (BigDecimal) request.getAttribute("montantReste");
    String error = (String) request.getAttribute("error");

    if (montantTotal == null) montantTotal = BigDecimal.ZERO;
    if (montantPaye == null) montantPaye = BigDecimal.ZERO;
    if (montantReste == null) montantReste = BigDecimal.ZERO;
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">
            <i class="bi bi-plus-lg"></i> Nouveau Paiement - Vente #<%= vente != null ? vente.getId() : "" %>
        </h1>
        <a href="<%= request.getContextPath() %>/produit-extra-ventes?action=detail&id=<%= vente != null ? vente.getId() : "" %>" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Retour
        </a>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i> <%= error %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <% if (vente != null) { %>
    
    <!-- Info Cards -->
    <div class="row mb-4">
        <div class="col-md-4">
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
                                Déjà Payé
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", montantPaye) %> Ar
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card border-left-danger shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-danger text-uppercase mb-1">
                                Reste à Payer
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", montantReste) %> Ar
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Vente Info -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-info-circle"></i> Informations de la Vente
            </h6>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <p><strong>Produit:</strong> <%= vente.getProduitExtra() != null ? vente.getProduitExtra().getNom() : "-" %></p>
                    <p><strong>Client:</strong> <%= vente.getClient() != null ? vente.getClient().getNomClient() : "-" %></p>
                </div>
                <div class="col-md-6">
                    <p><strong>Quantité:</strong> <%= vente.getQuantite() %></p>
                    <p><strong>Date:</strong> <%= vente.getDate() != null ? vente.getDate().toLocalDate() : "-" %></p>
                </div>
            </div>
        </div>
    </div>

    <!-- Payment Form -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-cash-stack"></i> Enregistrer un Paiement
            </h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/produit-extra-ventes" method="post">
                <input type="hidden" name="action" value="savePayement">
                <input type="hidden" name="venteId" value="<%= vente.getId() %>">

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="montant" class="form-label">Montant (Ar) *</label>
                        <input type="number" class="form-control" id="montant" name="montant" required 
                               step="0.01" min="0.01" max="<%= montantReste %>"
                               value="<%= montantReste %>">
                        <small class="text-muted">Maximum: <%= String.format("%,.2f", montantReste) %> Ar</small>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label for="datePayement" class="form-label">Date de Paiement *</label>
                        <input type="date" class="form-control" id="datePayement" name="datePayement" required
                               value="<%= LocalDate.now() %>">
                    </div>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-success">
                        <i class="bi bi-check-lg"></i> Enregistrer le Paiement
                    </button>
                    <a href="<%= request.getContextPath() %>/produit-extra-ventes?action=detail&id=<%= vente.getId() %>" class="btn btn-secondary">
                        <i class="bi bi-x-lg"></i> Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>

    <% } else { %>
    <div class="alert alert-warning">
        <i class="bi bi-exclamation-triangle"></i> Vente non trouvée
    </div>
    <% } %>
</div>
