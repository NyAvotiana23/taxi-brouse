<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%
    Trajet trajet = (Trajet) request.getAttribute("trajet");
    List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
    List<Ligne> lignes = (List<Ligne>) request.getAttribute("lignes");
    List<Chauffeur> chauffeurs = (List<Chauffeur>) request.getAttribute("chauffeurs");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
    List<TypeObjectDTO> trajetStatuts = (List<TypeObjectDTO>) request.getAttribute("trajetStatuts");
%>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Trajets</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <% if (trajet != null) { %>
                            Modifier Trajet
                        <% } else { %>
                            Nouveau Trajet
                        <% } %>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/trajets" method="post">
                        <input type="hidden" name="id" value="<%= trajet != null ? trajet.getId() : "" %>">

                        <div class="mb-3">
                            <label for="idLigne" class="form-label">Ligne</label>
                            <select class="form-control" id="idLigne" name="idLigne" required>
                                <option value="">Choisir...</option>
                                <% if (lignes != null) {
                                    for (Ligne l : lignes) { %>
                                        <option value="<%= l.getId() %>" 
                                            <%= (trajet != null && trajet.getLigne() != null && trajet.getLigne().getId().equals(l.getId())) ? "selected" : "" %>>
                                            <%= l.getVilleDepart().getNom() %> -> <%= l.getVilleArrivee().getNom() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idChauffeur" class="form-label">Chauffeur</label>
                            <select class="form-control" id="idChauffeur" name="idChauffeur" required>
                                <option value="">Choisir...</option>
                                <% if (chauffeurs != null) {
                                    for (Chauffeur c : chauffeurs) { %>
                                        <option value="<%= c.getId() %>" 
                                            <%= (trajet != null && trajet.getChauffeur() != null && trajet.getChauffeur().getId().equals(c.getId())) ? "selected" : "" %>>
                                            <%= c.getNom() %> <%= c.getPrenom() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idVehicule" class="form-label">Véhicule</label>
                            <select class="form-control" id="idVehicule" name="idVehicule" required>
                                <option value="">Choisir...</option>
                                <% if (vehicules != null) {
                                    for (Vehicule v : vehicules) { %>
                                        <option value="<%= v.getId() %>" 
                                            <%= (trajet != null && trajet.getVehicule() != null && trajet.getVehicule().getId().equals(v.getId())) ? "selected" : "" %>>
                                            <%= v.getImmatriculation() %> (<%= v.getMarque() %>)
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="datetimeDepart" class="form-label">Date Départ</label>
                            <input type="datetime-local" class="form-control" id="datetimeDepart" name="datetimeDepart" 
                                value="<%= trajet != null && trajet.getDatetimeDepart() != null ? trajet.getDatetimeDepart() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="datetimeArrivee" class="form-label">Date Arrivée (Estimée)</label>
                            <input type="datetime-local" class="form-control" id="datetimeArrivee" name="datetimeArrivee" 
                                value="<%= trajet != null && trajet.getDatetimeArrivee() != null ? trajet.getDatetimeArrivee() : "" %>">
                        </div>
                        <div class="mb-3">
                            <label for="nombrePassager" class="form-label">Nombre Passagers</label>
                            <input type="number" class="form-control" id="nombrePassager" name="nombrePassager" 
                                value="<%= trajet != null ? trajet.getNombrePassager() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="fraisUnitaire" class="form-label">Frais Unitaire</label>
                            <input type="number" step="0.01" class="form-control" id="fraisUnitaire" name="fraisUnitaire" 
                                value="<%= trajet != null ? trajet.getFraisUnitaire() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="idTrajetStatut" class="form-label">Statut</label>
                            <select class="form-control" id="idTrajetStatut" name="idTrajetStatut" required>
                                <option value="">Choisir...</option>
                                <% if (trajetStatuts != null) {
                                    for (TypeObjectDTO s : trajetStatuts) { %>
                                        <option value="<%= s.getId() %>" 
                                            <%= (trajet != null && trajet.getTrajetStatut() != null && trajet.getTrajetStatut().getId().equals(s.getId())) ? "selected" : "" %>>
                                            <%= s.getLibelle() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="<%= request.getContextPath() %>/trajets" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>


        <div class="col-md-8">
            <!-- Formulaire de filtrage -->
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Filtrer les Trajets</h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/trajets" method="get" class="row g-3">
                        <div class="col-md-4">
                            <label for="filterLigne" class="form-label">Ligne</label>
                            <select class="form-control" id="filterLigne" name="filterLigne">
                                <option value="">Toutes les lignes</option>
                                <% if (lignes != null) {
                                    String selectedLigne = (String) request.getAttribute("filterLigne");
                                    for (Ligne l : lignes) { %>
                                        <option value="<%= l.getId() %>" 
                                            <%= (selectedLigne != null && selectedLigne.equals(String.valueOf(l.getId()))) ? "selected" : "" %>>
                                            <%= l.getVilleDepart().getNom() %> -> <%= l.getVilleArrivee().getNom() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="filterChauffeur" class="form-label">Chauffeur</label>
                            <select class="form-control" id="filterChauffeur" name="filterChauffeur">
                                <option value="">Tous les chauffeurs</option>
                                <% if (chauffeurs != null) {
                                    String selectedChauffeur = (String) request.getAttribute("filterChauffeur");
                                    for (Chauffeur c : chauffeurs) { %>
                                        <option value="<%= c.getId() %>" 
                                            <%= (selectedChauffeur != null && selectedChauffeur.equals(String.valueOf(c.getId()))) ? "selected" : "" %>>
                                            <%= c.getNom() %> <%= c.getPrenom() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="filterVehicule" class="form-label">Véhicule</label>
                            <select class="form-control" id="filterVehicule" name="filterVehicule">
                                <option value="">Tous les véhicules</option>
                                <% if (vehicules != null) {
                                    String selectedVehicule = (String) request.getAttribute("filterVehicule");
                                    for (Vehicule v : vehicules) { %>
                                        <option value="<%= v.getId() %>" 
                                            <%= (selectedVehicule != null && selectedVehicule.equals(String.valueOf(v.getId()))) ? "selected" : "" %>>
                                            <%= v.getImmatriculation() %> (<%= v.getMarque() %>)
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="filterStatut" class="form-label">Statut</label>
                            <select class="form-control" id="filterStatut" name="filterStatut">
                                <option value="">Tous les statuts</option>
                                <% if (trajetStatuts != null) {
                                    String selectedStatut = (String) request.getAttribute("filterStatut");
                                    for (TypeObjectDTO s : trajetStatuts) { %>
                                        <option value="<%= s.getId() %>" 
                                            <%= (selectedStatut != null && selectedStatut.equals(String.valueOf(s.getId()))) ? "selected" : "" %>>
                                            <%= s.getLibelle() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="filterDateDebut" class="form-label">Date Début</label>
                            <input type="datetime-local" class="form-control" id="filterDateDebut" name="filterDateDebut" 
                                value="<%= request.getAttribute("filterDateDebut") != null ? request.getAttribute("filterDateDebut") : "" %>">
                        </div>
                        <div class="col-md-4">
                            <label for="filterDateFin" class="form-label">Date Fin</label>
                            <input type="datetime-local" class="form-control" id="filterDateFin" name="filterDateFin" 
                                value="<%= request.getAttribute("filterDateFin") != null ? request.getAttribute("filterDateFin") : "" %>">
                        </div>
                        <div class="col-12">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-filter"></i> Filtrer
                            </button>
                            <a href="<%= request.getContextPath() %>/trajets" class="btn btn-secondary">
                                <i class="fas fa-redo"></i> Réinitialiser
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Liste des trajets -->
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Trajets</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Ligne</th>
                                    <th>Chauffeur</th>
                                    <th>Véhicule</th>
                                    <th>Départ</th>
                                    <th>Arrivee</th>
                                    <th>Statut</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (trajets != null) {
                                    for (Trajet t : trajets) { %>
                                        <tr>
                                            <td><%= t.getLigne().getVilleDepart().getNom() %> -> <%= t.getLigne().getVilleArrivee().getNom() %></td>
                                            <td><%= t.getChauffeur().getNom() %></td>
                                            <td><%= t.getVehicule().getImmatriculation() %></td>
                                            <td><%= t.getDatetimeDepart() %></td>
                                            <td><%= t.getDatetimeArrivee() %></td>
                                            <td><%= t.getTrajetStatut().getLibelle() %></td>
                                            <td>
                                                <a href="<%= request.getContextPath() %>/trajets/detail?id=<%= t.getId() %>" class="btn btn-sm btn-success">Détails</a>
                                                <a href="<%= request.getContextPath() %>/trajets?action=edit&id=<%= t.getId() %>" class="btn btn-sm btn-info">Modifier</a>
                                            </td>
                                        </tr>
                                    <% }
                                } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
