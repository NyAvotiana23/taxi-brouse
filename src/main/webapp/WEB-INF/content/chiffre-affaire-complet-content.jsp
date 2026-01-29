<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.view.VmTrajetCaComplet" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%
    List<VmTrajetCaComplet> caComplets = (List<VmTrajetCaComplet>) request.getAttribute("caComplets");
    List<Ville> villes = (List<Ville>) request.getAttribute("villes");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
    String error = (String) request.getAttribute("error");

    // Calculate totals
    BigDecimal totalCaPrevision = BigDecimal.ZERO;
    BigDecimal totalCaReel = BigDecimal.ZERO;
    BigDecimal totalEcart = BigDecimal.ZERO;
    BigDecimal totalPrevisionTicket = BigDecimal.ZERO;
    BigDecimal totalReelTicket = BigDecimal.ZERO;
    BigDecimal totalPrevisionDiffusion = BigDecimal.ZERO;
    BigDecimal totalReelDiffusion = BigDecimal.ZERO;
    BigDecimal totalRestDiffusion = BigDecimal.ZERO;
    BigDecimal totalRestTicket = BigDecimal.ZERO;


    if (caComplets != null) {
        for (VmTrajetCaComplet ca : caComplets) {
            if (ca.getMontantChiffreAffairePrevision() != null) {
                totalCaPrevision = totalCaPrevision.add(ca.getMontantChiffreAffairePrevision());
            }
            if (ca.getMontantChiffreAffaireReel() != null) {
                totalCaReel = totalCaReel.add(ca.getMontantChiffreAffaireReel());
            }
            if (ca.getEcartCa() != null) {
                totalEcart = totalEcart.add(ca.getEcartCa());
            }
            if (ca.getMontantPrevisionTicket() != null) {
                totalPrevisionTicket = totalPrevisionTicket.add(ca.getMontantPrevisionTicket());
            }
            if (ca.getMontantReelTicket() != null) {
                totalReelTicket = totalReelTicket.add(ca.getMontantReelTicket());
            }
            if (ca.getMontantPrevisionDiffusion() != null) {
                totalPrevisionDiffusion = totalPrevisionDiffusion.add(ca.getMontantPrevisionDiffusion());
            }
            if (ca.getMontantReelDiffusion() != null) {
                totalReelDiffusion = totalReelDiffusion.add(ca.getMontantReelDiffusion());
            }

            if (ca.getMontantReelDiffusion() != null) {
                totalRestDiffusion = totalRestDiffusion.add(ca.getMontantRestDiffusion());
            }
            if (ca.getMontantReelDiffusion() != null) {
                totalRestTicket = totalRestTicket.add(ca.getMontantRestTicket());
            }
        }
    }

    double tauxRealisation = totalCaPrevision.compareTo(BigDecimal.ZERO) > 0
            ? totalCaReel.multiply(BigDecimal.valueOf(100)).divide(totalCaPrevision, 2, BigDecimal.ROUND_HALF_UP).doubleValue()
            : 0.0;
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">
            <i class="bi bi-clipboard-data"></i> Chiffre d'Affaires Complet
        </h1>
        <div>
            <a href="<%= request.getContextPath() %>/chiffre-affaire?type=prevision" class="btn btn-warning">
                <i class="bi bi-graph-up-arrow"></i> CA Prévisionnel
            </a>
            <a href="<%= request.getContextPath() %>/chiffre-affaire?type=reel" class="btn btn-info">
                <i class="bi bi-cash-stack"></i> CA Réel
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
                                Total Trajets
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= caComplets != null ? caComplets.size() : 0 %>
                            </div>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-bus-front fs-2 text-gray-300"></i>
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
                                CA Prévisionnel
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", totalCaPrevision) %> Ar
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
                                CA Réel
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%,.2f", totalCaReel) %> Ar
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
            <div class="card border-left-<%= totalEcart.compareTo(BigDecimal.ZERO) >= 0 ? "danger" : "info" %> shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-<%= totalEcart.compareTo(BigDecimal.ZERO) >= 0 ? "danger" : "info" %> text-uppercase mb-1">
                                Écart (Prévu - Réel)
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">
                                <%= String.format("%+,.2f", totalEcart) %> Ar
                            </div>
                            <small class="text-muted">Taux: <%= String.format("%.1f%%", tauxRealisation) %></small>
                        </div>
                        <div class="col-auto">
                            <i class="bi bi-<%= totalEcart.compareTo(BigDecimal.ZERO) >= 0 ? "arrow-down-circle" : "arrow-up-circle" %> fs-2 text-gray-300"></i>
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
            <form action="<%= request.getContextPath() %>/chiffre-affaire" method="get">
                <input type="hidden" name="type" value="complet">
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="idTrajet" class="form-label">ID Trajet</label>
                        <input type="number" class="form-control" id="idTrajet" name="idTrajet"
                               value="<%= request.getParameter("idTrajet") != null ? request.getParameter("idTrajet") : "" %>">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="villeDepart" class="form-label">Ville Départ</label>
                        <select class="form-control" id="villeDepart" name="villeDepart">
                            <option value="">Toutes</option>
                            <% if (villes != null) {
                                for (Ville v : villes) { %>
                            <option value="<%= v.getNom() %>"
                                    <%= request.getParameter("villeDepart") != null && request.getParameter("villeDepart").equals(v.getNom()) ? "selected" : "" %>>
                                <%= v.getNom() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="villeArrive" class="form-label">Ville Arrivée</label>
                        <select class="form-control" id="villeArrive" name="villeArrive">
                            <option value="">Toutes</option>
                            <% if (villes != null) {
                                for (Ville v : villes) { %>
                            <option value="<%= v.getNom() %>"
                                    <%= request.getParameter("villeArrive") != null && request.getParameter("villeArrive").equals(v.getNom()) ? "selected" : "" %>>
                                <%= v.getNom() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="vehicule" class="form-label">Véhicule</label>
                        <select class="form-control" id="vehicule" name="vehicule">
                            <option value="">Tous</option>
                            <% if (vehicules != null) {
                                for (Vehicule v : vehicules) { %>
                            <option value="<%= v.getImmatriculation() %>"
                                    <%= request.getParameter("vehicule") != null && request.getParameter("vehicule").equals(v.getImmatriculation()) ? "selected" : "" %>>
                                <%= v.getImmatriculation() %> - <%= v.getMarque() %>
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
                    <a href="<%= request.getContextPath() %>/chiffre-affaire?type=complet" class="btn btn-secondary">
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
                <i class="bi bi-table"></i> Analyse Prévisionnel vs Réel
            </h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover table-sm" width="100%">
                    <thead class="table-light">
                    <tr>
                        <th rowspan="2">ID</th>
                        <th rowspan="2">Départ</th>
                        <th rowspan="2">Arrivée</th>
                        <th rowspan="2">Date</th>
                        <th rowspan="2">Véhicule</th>
                        <th colspan="3" class="text-center bg-warning">Tickets</th>
                        <th colspan="3" class="text-center bg-info">Diffusions</th>
                        <th colspan="2" class="text-center bg-success">CA Total</th>
                        <th rowspan="2">Écart</th>
                        <th rowspan="2">%</th>
                        <th rowspan="2">Actions</th>
                    </tr>
                    <tr>
                        <th class="text-center bg-warning">Prévu</th>
                        <th class="text-center bg-warning">Réel</th>
                        <th class="text-center bg-warning">Reste</th>

                        <th class="text-center bg-info">Prévu</th>
                        <th class="text-center bg-info">Réel</th>
                        <th class="text-center bg-info">Reste</th>

                        <th class="text-center bg-success">Prévu</th>
                        <th class="text-center bg-success">Réel</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (caComplets != null && !caComplets.isEmpty()) {
                        for (VmTrajetCaComplet ca : caComplets) {
                            double tauxTrajet = ca.getMontantChiffreAffairePrevision().compareTo(BigDecimal.ZERO) > 0
                                    ? ca.getMontantChiffreAffaireReel().multiply(BigDecimal.valueOf(100))
                                    .divide(ca.getMontantChiffreAffairePrevision(), 2, BigDecimal.ROUND_HALF_UP).doubleValue()
                                    : 0.0;
                    %>
                    <tr>
                        <td><%= ca.getIdTrajet() %></td>
                        <td><%= ca.getNomVilleDepart() %></td>
                        <td><%= ca.getNomVilleArrive() %></td>
                        <td><%= ca.getDateDepart() %></td>
                        <td><small><%= ca.getImmatriculationVehicule() %></small></td>
                        <td class="text-end"><small><%= String.format("%,.0f", ca.getMontantPrevisionTicket()) %></small></td>
                        <td class="text-end"><small><%= String.format("%,.0f", ca.getMontantReelTicket()) %></small></td>
                        <td class="text-end"><small><%= String.format("%,.0f", ca.getMontantRestTicket()) %></small></td>

                        <td class="text-end"><small><%= String.format("%,.0f", ca.getMontantPrevisionDiffusion()) %></small></td>
                        <td class="text-end"><small><%= String.format("%,.0f", ca.getMontantReelDiffusion()) %></small></td>
                        <td class="text-end"><small><%= String.format("%,.0f", ca.getMontantRestDiffusion()) %></small></td>

                        <td class="text-end"><strong><%= String.format("%,.0f", ca.getMontantChiffreAffairePrevision()) %></strong></td>
                        <td class="text-end"><strong class="text-success"><%= String.format("%,.0f", ca.getMontantChiffreAffaireReel()) %></strong></td>
                        <td class="text-end">
                                <span class="badge bg-<%= ca.getEcartCa().compareTo(BigDecimal.ZERO) >= 0 ? "danger" : "success" %>">
                                    <%= String.format("%+,.0f", ca.getEcartCa()) %>
                                </span>
                        </td>
                        <td class="text-center">
                                <span class="badge bg-<%= tauxTrajet >= 100 ? "success" : (tauxTrajet >= 80 ? "warning" : "danger") %>">
                                    <%= String.format("%.0f%%", tauxTrajet) %>
                                </span>
                        </td>
                        <td class="text-center">
                            <a href="<%= request.getContextPath() %>/trajets/detail?id=<%= ca.getIdTrajet() %>"
                               class="btn btn-sm btn-info" title="Voir détails">
                                <i class="bi bi-eye"></i>
                            </a>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="14" class="text-center text-muted py-4">
                            <i class="bi bi-inbox fs-3 d-block mb-2"></i>
                            Aucun trajet trouvé
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                    <% if (caComplets != null && !caComplets.isEmpty()) { %>
                    <tfoot class="table-light">
                    <tr>
                        <th colspan="5" class="text-end">Total:</th>
                        <th class="text-end"><%= String.format("%,.0f", totalPrevisionTicket) %></th>
                        <th class="text-end"><%= String.format("%,.0f", totalReelTicket) %></th>
                        <th class="text-end"><%= String.format("%,.0f", totalRestTicket) %></th>

                        <th class="text-end"><%= String.format("%,.0f", totalPrevisionDiffusion) %></th>
                        <th class="text-end"><%= String.format("%,.0f", totalReelDiffusion) %></th>
                        <th class="text-end"><%= String.format("%,.0f", totalRestDiffusion) %></th>

                        <th class="text-end"><strong><%= String.format("%,.0f", totalCaPrevision) %></strong></th>
                        <th class="text-end"><strong class="text-success"><%= String.format("%,.0f", totalCaReel) %></strong></th>
                        <th class="text-end">
                                <span class="badge bg-<%= totalEcart.compareTo(BigDecimal.ZERO) >= 0 ? "danger" : "success" %>">
                                    <%= String.format("%+,.0f", totalEcart) %>
                                </span>
                        </th>
                        <th class="text-center">
                                <span class="badge bg-<%= tauxRealisation >= 100 ? "success" : (tauxRealisation >= 80 ? "warning" : "danger") %>">
                                    <%= String.format("%.0f%%", tauxRealisation) %>
                                </span>
                        </th>
                        <th></th>
                    </tr>
                    </tfoot>
                    <% } %>
                </table>
            </div>
        </div>
    </div>
</div>