<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%
    List<ProduitExtraVente> ventes = (List<ProduitExtraVente>) request.getAttribute("ventes");
    List<ProduitCategorie> categories = (List<ProduitCategorie>) request.getAttribute("categories");
    List<ProduitExtra> produits = (List<ProduitExtra>) request.getAttribute("produits");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    
    Map<Long, BigDecimal> montantsTotaux = (Map<Long, BigDecimal>) request.getAttribute("montantsTotaux");
    Map<Long, BigDecimal> montantsPayes = (Map<Long, BigDecimal>) request.getAttribute("montantsPayes");
    Map<Long, BigDecimal> montantsRestes = (Map<Long, BigDecimal>) request.getAttribute("montantsRestes");
    
    String error = request.getParameter("error");
    String success = request.getParameter("success");

    // Calculer les totaux
    BigDecimal totalVentes = BigDecimal.ZERO;
    BigDecimal totalPayes = BigDecimal.ZERO;
    BigDecimal totalRestes = BigDecimal.ZERO;

    if (montantsTotaux != null) {
        for (BigDecimal m : montantsTotaux.values()) {
            totalVentes = totalVentes.add(m);
        }
    }
    if (montantsPayes != null) {
        for (BigDecimal m : montantsPayes.values()) {
            totalPayes = totalPayes.add(m);
        }
    }
    if (montantsRestes != null) {
        for (BigDecimal m : montantsRestes.values()) {
            totalRestes = totalRestes.add(m);
        }
    }
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">
            <i class="bi bi-box-seam"></i> Ventes Produits Extra
        </h1>
        <a href="<%= request.getContextPath() %>/produit-extra-ventes?action=add" class="btn btn-primary">
            <i class="bi bi-plus-lg"></i> Nouvelle Vente
        </a>
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

    <!-- Summary Cards -->
    <div class="row mb-4">
        <div class="col-md-3">
            <div class="card border-left-primary shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                Nombre de Ventes
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= ventes != null ? ventes.size() : 0 %>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-cart3 fs-2 text-gray-300"></i>
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
                                Total Ventes
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", totalVentes) %> Ar
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
                                Total Payé
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", totalPayes) %> Ar
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
            <div class="card border-left-danger shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-danger text-uppercase mb-1">
                                Reste à Payer
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", totalRestes) %> Ar
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-exclamation-triangle fs-2 text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Filters -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-funnel"></i> Filtres de Recherche
            </h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/produit-extra-ventes" method="get">
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="categorieId" class="form-label">Catégorie</label>
                        <select class="form-control" id="categorieId" name="categorieId">
                            <option value="">Toutes</option>
                            <% if (categories != null) {
                                for (ProduitCategorie c : categories) { %>
                            <option value="<%= c.getId() %>"
                                    <%= request.getParameter("categorieId") != null && request.getParameter("categorieId").equals(String.valueOf(c.getId())) ? "selected" : "" %>>
                                <%= c.getLibelle() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="produitId" class="form-label">Produit</label>
                        <select class="form-control" id="produitId" name="produitId">
                            <option value="">Tous</option>
                            <% if (produits != null) {
                                for (ProduitExtra p : produits) { %>
                            <option value="<%= p.getId() %>"
                                    <%= request.getParameter("produitId") != null && request.getParameter("produitId").equals(String.valueOf(p.getId())) ? "selected" : "" %>>
                                <%= p.getNom() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="clientId" class="form-label">Client</label>
                        <select class="form-control" id="clientId" name="clientId">
                            <option value="">Tous</option>
                            <% if (clients != null) {
                                for (Client c : clients) { %>
                            <option value="<%= c.getId() %>"
                                    <%= request.getParameter("clientId") != null && request.getParameter("clientId").equals(String.valueOf(c.getId())) ? "selected" : "" %>>
                                <%= c.getNom() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="dateDebut" class="form-label">Date Début</label>
                        <input type="date" class="form-control" id="dateDebut" name="dateDebut"
                               value="<%= request.getParameter("dateDebut") != null ? request.getParameter("dateDebut") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="dateFin" class="form-label">Date Fin</label>
                        <input type="date" class="form-control" id="dateFin" name="dateFin"
                               value="<%= request.getParameter("dateFin") != null ? request.getParameter("dateFin") : "" %>">
                    </div>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-search"></i> Rechercher
                    </button>
                    <a href="<%= request.getContextPath() %>/produit-extra-ventes" class="btn btn-secondary">
                        <i class="bi bi-arrow-counterclockwise"></i> Réinitialiser
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Results Table -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-table"></i> Liste des Ventes
            </h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover" width="100%">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Date</th>
                            <th>Produit</th>
                            <th>Catégorie</th>
                            <th>Client</th>
                            <th>Qté</th>
                            <th>P.U.</th>
                            <th>Remise</th>
                            <th>Montant Total</th>
                            <th>Payé</th>
                            <th>Reste</th>
                            <th>Statut</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (ventes != null && !ventes.isEmpty()) {
                        for (ProduitExtraVente v : ventes) {
                            BigDecimal montantTotal = montantsTotaux.get(v.getId());
                            BigDecimal montantPaye = montantsPayes.get(v.getId());
                            BigDecimal montantReste = montantsRestes.get(v.getId());
                            String statut = montantReste.compareTo(BigDecimal.ZERO) <= 0 ? "Payé" : "En cours";
                            String statutClass = montantReste.compareTo(BigDecimal.ZERO) <= 0 ? "bg-success" : "bg-warning";
                    %>
                    <tr>
                        <td><%= v.getId() %></td>
                        <td><%= v.getDate() != null ? v.getDate().toLocalDate() : "-" %></td>
                        <td><%= v.getProduitExtra() != null ? v.getProduitExtra().getNom() : "-" %></td>
                        <td><%= v.getProduitExtra() != null && v.getProduitExtra().getProduitCategorie() != null ? v.getProduitExtra().getProduitCategorie().getLibelle() : "-" %></td>
                        <td><%= v.getClient() != null ? v.getClient().getNom() : "-" %></td>
                        <td class="text-center"><%= v.getQuantite() %></td>
                        <td class="text-end"><%= String.format("%,.0f", v.getPrixUnitaire()) %></td>
                        <td class="text-center"><%= v.getRemise() != null ? String.format("%.1f%%", v.getRemise()) : "0%" %></td>
                        <td class="text-end fw-bold"><%= String.format("%,.0f", montantTotal) %></td>
                        <td class="text-end text-success"><%= String.format("%,.0f", montantPaye) %></td>
                        <td class="text-end text-danger"><%= String.format("%,.0f", montantReste) %></td>
                        <td class="text-center">
                            <span class="badge <%= statutClass %>"><%= statut %></span>
                        </td>
                        <td>
                            <div class="btn-group btn-group-sm">
                                <a href="<%= request.getContextPath() %>/produit-extra-ventes?action=detail&id=<%= v.getId() %>" 
                                   class="btn btn-info" title="Détails">
                                    <i class="bi bi-eye"></i>
                                </a>
                                <a href="<%= request.getContextPath() %>/produit-extra-ventes?action=edit&id=<%= v.getId() %>" 
                                   class="btn btn-warning" title="Modifier">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                <a href="<%= request.getContextPath() %>/produit-extra-ventes?action=addPayement&venteId=<%= v.getId() %>" 
                                   class="btn btn-success" title="Ajouter Paiement">
                                    <i class="bi bi-plus-circle"></i>
                                </a>
                                <a href="<%= request.getContextPath() %>/produit-extra-ventes?action=delete&id=<%= v.getId() %>" 
                                   class="btn btn-danger" title="Supprimer"
                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette vente ?')">
                                    <i class="bi bi-trash"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="13" class="text-center text-muted">
                            <i class="bi bi-inbox fs-1"></i>
                            <p>Aucune vente trouvée</p>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                    <tfoot class="table-secondary fw-bold">
                        <tr>
                            <td colspan="8">TOTAL</td>
                            <td class="text-end"><%= String.format("%,.0f", totalVentes) %></td>
                            <td class="text-end text-success"><%= String.format("%,.0f", totalPayes) %></td>
                            <td class="text-end text-danger"><%= String.format("%,.0f", totalRestes) %></td>
                            <td colspan="2"></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</div>
