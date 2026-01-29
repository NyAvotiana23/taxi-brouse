<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.view.VmCAComplet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.time.LocalDate" %>
<%
    List<VmCAComplet> caComplets = (List<VmCAComplet>) request.getAttribute("caComplets");
    String error = (String) request.getAttribute("error");

    // Calculate totals
    BigDecimal totalCaPrevisionTicket = BigDecimal.ZERO;
    BigDecimal totalCaPrevisionDiffusion = BigDecimal.ZERO;
    BigDecimal totalCaPrevisionProduit = BigDecimal.ZERO;
    BigDecimal totalCaPrevisionTotal = BigDecimal.ZERO;
    BigDecimal totalCaReelTicket = BigDecimal.ZERO;
    BigDecimal totalCaReelDiffusion = BigDecimal.ZERO;
    BigDecimal totalCaReelProduit = BigDecimal.ZERO;
    BigDecimal totalCaReelTotal = BigDecimal.ZERO;

    if (caComplets != null) {
        for (VmCAComplet ca : caComplets) {
            if (ca.getCaPrevisionTicket() != null) {
                totalCaPrevisionTicket = totalCaPrevisionTicket.add(ca.getCaPrevisionTicket());
            }
            if (ca.getCaPrevisionDiffusion() != null) {
                totalCaPrevisionDiffusion = totalCaPrevisionDiffusion.add(ca.getCaPrevisionDiffusion());
            }
            if (ca.getCaPrevisionProduit() != null) {
                totalCaPrevisionProduit = totalCaPrevisionProduit.add(ca.getCaPrevisionProduit());
            }
            if (ca.getCaPrevisionTotal() != null) {
                totalCaPrevisionTotal = totalCaPrevisionTotal.add(ca.getCaPrevisionTotal());
            }
            if (ca.getCaReelTicket() != null) {
                totalCaReelTicket = totalCaReelTicket.add(ca.getCaReelTicket());
            }
            if (ca.getCaReelDiffusion() != null) {
                totalCaReelDiffusion = totalCaReelDiffusion.add(ca.getCaReelDiffusion());
            }
            if (ca.getCaReelProduit() != null) {
                totalCaReelProduit = totalCaReelProduit.add(ca.getCaReelProduit());
            }
            if (ca.getCaReelTotal() != null) {
                totalCaReelTotal = totalCaReelTotal.add(ca.getCaReelTotal());
            }
        }
    }

    BigDecimal ecartTotal = totalCaPrevisionTotal.subtract(totalCaReelTotal);
    double tauxRealisation = totalCaPrevisionTotal.compareTo(BigDecimal.ZERO) > 0
            ? totalCaReelTotal.multiply(BigDecimal.valueOf(100)).divide(totalCaPrevisionTotal, 2, BigDecimal.ROUND_HALF_UP).doubleValue()
            : 0.0;

    // Génération des années pour le filtre (5 ans avant et après l'année courante)
    int currentYear = LocalDate.now().getYear();
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">
            <i class="bi bi-bar-chart-fill"></i> CA Complet Mensuel
        </h1>
        <div>
            <a href="<%= request.getContextPath() %>/chiffre-affaire?type=prevision" class="btn btn-warning">
                <i class="bi bi-graph-up-arrow"></i> CA Prévisionnel
            </a>
            <a href="<%= request.getContextPath() %>/chiffre-affaire?type=reel" class="btn btn-info">
                <i class="bi bi-cash-stack"></i> CA Réel
            </a>
            <a href="<%= request.getContextPath() %>/chiffre-affaire?type=complet" class="btn btn-success">
                <i class="bi bi-clipboard-data"></i> CA Complet Trajet
            </a>
        </div>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i> <%= error %>
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
                                Nombre de Mois
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= caComplets != null ? caComplets.size() : 0 %>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-calendar3 fs-2 text-gray-300"></i>
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
                                CA Prévisionnel Total
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", totalCaPrevisionTotal) %> Ar
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-graph-up fs-2 text-gray-300"></i>
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
                                CA Réel Total
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", totalCaReelTotal) %> Ar
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
            <div class="card border-left-<%= ecartTotal.compareTo(BigDecimal.ZERO) >= 0 ? "danger" : "info" %> shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-<%= ecartTotal.compareTo(BigDecimal.ZERO) >= 0 ? "danger" : "info" %> text-uppercase mb-1">
                                Écart (Prévu - Réel)
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%+,.2f", ecartTotal) %> Ar
                            </div>
                            <small class="text-muted">Taux: <%= String.format("%.1f%%", tauxRealisation) %></small>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-<%= ecartTotal.compareTo(BigDecimal.ZERO) >= 0 ? "arrow-down-circle" : "arrow-up-circle" %> fs-2 text-gray-300"></i>
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
            <form action="<%= request.getContextPath() %>/ca-complet" method="get">
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="mois" class="form-label">Mois</label>
                        <select class="form-control" id="mois" name="mois">
                            <option value="">Tous les mois</option>
                            <% 
                            String[] moisNoms = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", 
                                                  "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
                            for (int i = 1; i <= 12; i++) { 
                                String selected = request.getParameter("mois") != null && 
                                                   request.getParameter("mois").equals(String.valueOf(i)) ? "selected" : "";
                            %>
                            <option value="<%= i %>" <%= selected %>><%= moisNoms[i-1] %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="annee" class="form-label">Année</label>
                        <select class="form-control" id="annee" name="annee">
                            <option value="">Toutes les années</option>
                            <% for (int y = currentYear + 2; y >= currentYear - 5; y--) { 
                                String selected = request.getParameter("annee") != null && 
                                                   request.getParameter("annee").equals(String.valueOf(y)) ? "selected" : "";
                            %>
                            <option value="<%= y %>" <%= selected %>><%= y %></option>
                            <% } %>
                        </select>
                    </div>
                </div>
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-search"></i> Rechercher
                    </button>
                    <a href="<%= request.getContextPath() %>/ca-complet" class="btn btn-secondary">
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
                <i class="bi bi-table"></i> CA Complet par Mois
            </h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover" width="100%">
                    <thead class="table-light">
                        <tr>
                            <th rowspan="2">Mois</th>
                            <th rowspan="2">Année</th>
                            <th colspan="4" class="text-center bg-warning">CA Prévisionnel</th>
                            <th colspan="4" class="text-center bg-success">CA Réel</th>
                            <th rowspan="2">Écart</th>
                            <th rowspan="2">%</th>
                        </tr>
                        <tr>
                            <th class="bg-warning">Tickets</th>
                            <th class="bg-warning">Diffusion</th>
                            <th class="bg-warning">Produits</th>
                            <th class="bg-warning">Total</th>
                            <th class="bg-success">Tickets</th>
                            <th class="bg-success">Diffusion</th>
                            <th class="bg-success">Produits</th>
                            <th class="bg-success">Total</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% if (caComplets != null && !caComplets.isEmpty()) {
                        String[] moisLabels = {"", "Jan", "Fév", "Mar", "Avr", "Mai", "Jun", "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc"};
                        for (VmCAComplet ca : caComplets) {
                            BigDecimal caPrev = ca.getCaPrevisionTotal() != null ? ca.getCaPrevisionTotal() : BigDecimal.ZERO;
                            BigDecimal caReel = ca.getCaReelTotal() != null ? ca.getCaReelTotal() : BigDecimal.ZERO;
                            BigDecimal ecart = caPrev.subtract(caReel);
                            double taux = caPrev.compareTo(BigDecimal.ZERO) > 0
                                    ? caReel.multiply(BigDecimal.valueOf(100)).divide(caPrev, 2, BigDecimal.ROUND_HALF_UP).doubleValue()
                                    : 0.0;
                    %>
                    <tr>
                        <td><%= ca.getMois() != null && ca.getMois() > 0 && ca.getMois() <= 12 ? moisLabels[ca.getMois()] : ca.getMois() %></td>
                        <td><%= ca.getAnnee() %></td>
                        <td class="text-end"><%= String.format("%,.0f", ca.getCaPrevisionTicket() != null ? ca.getCaPrevisionTicket() : BigDecimal.ZERO) %></td>
                        <td class="text-end"><%= String.format("%,.0f", ca.getCaPrevisionDiffusion() != null ? ca.getCaPrevisionDiffusion() : BigDecimal.ZERO) %></td>
                        <td class="text-end"><%= String.format("%,.0f", ca.getCaPrevisionProduit() != null ? ca.getCaPrevisionProduit() : BigDecimal.ZERO) %></td>
                        <td class="text-end fw-bold"><%= String.format("%,.0f", caPrev) %></td>
                        <td class="text-end"><%= String.format("%,.0f", ca.getCaReelTicket() != null ? ca.getCaReelTicket() : BigDecimal.ZERO) %></td>
                        <td class="text-end"><%= String.format("%,.0f", ca.getCaReelDiffusion() != null ? ca.getCaReelDiffusion() : BigDecimal.ZERO) %></td>
                        <td class="text-end"><%= String.format("%,.0f", ca.getCaReelProduit() != null ? ca.getCaReelProduit() : BigDecimal.ZERO) %></td>
                        <td class="text-end fw-bold"><%= String.format("%,.0f", caReel) %></td>
                        <td class="text-end <%= ecart.compareTo(BigDecimal.ZERO) > 0 ? "text-danger" : "text-success" %>">
                            <%= String.format("%+,.0f", ecart) %>
                        </td>
                        <td class="text-center">
                            <span class="badge <%= taux >= 100 ? "bg-success" : (taux >= 75 ? "bg-warning" : "bg-danger") %>">
                                <%= String.format("%.1f%%", taux) %>
                            </span>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="12" class="text-center text-muted">
                            <i class="bi bi-inbox fs-1"></i>
                            <p>Aucune donnée disponible</p>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                    <tfoot class="table-secondary fw-bold">
                        <tr>
                            <td colspan="2">TOTAL</td>
                            <td class="text-end"><%= String.format("%,.0f", totalCaPrevisionTicket) %></td>
                            <td class="text-end"><%= String.format("%,.0f", totalCaPrevisionDiffusion) %></td>
                            <td class="text-end"><%= String.format("%,.0f", totalCaPrevisionProduit) %></td>
                            <td class="text-end"><%= String.format("%,.0f", totalCaPrevisionTotal) %></td>
                            <td class="text-end"><%= String.format("%,.0f", totalCaReelTicket) %></td>
                            <td class="text-end"><%= String.format("%,.0f", totalCaReelDiffusion) %></td>
                            <td class="text-end"><%= String.format("%,.0f", totalCaReelProduit) %></td>
                            <td class="text-end"><%= String.format("%,.0f", totalCaReelTotal) %></td>
                            <td class="text-end <%= ecartTotal.compareTo(BigDecimal.ZERO) > 0 ? "text-danger" : "text-success" %>">
                                <%= String.format("%+,.0f", ecartTotal) %>
                            </td>
                            <td class="text-center">
                                <span class="badge <%= tauxRealisation >= 100 ? "bg-success" : (tauxRealisation >= 75 ? "bg-warning" : "bg-danger") %>">
                                    <%= String.format("%.1f%%", tauxRealisation) %>
                                </span>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</div>
