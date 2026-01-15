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

    <!-- Form: Create / Edit -->
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
                                <% for (Ligne l : lignes) { %>
                                <option value="<%= l.getId() %>"
                                        <%= (trajet != null && trajet.getLigne() != null && trajet.getLigne().getId().equals(l.getId())) ? "selected" : "" %>>
                                    <%= l.getVilleDepart().getNom() %> → <%= l.getVilleArrivee().getNom() %>
                                </option>
                                <% } %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="idChauffeur" class="form-label">Chauffeur</label>
                            <select class="form-control" id="idChauffeur" name="idChauffeur" required>
                                <option value="">Choisir...</option>
                                <% for (Chauffeur c : chauffeurs) { %>
                                <option value="<%= c.getId() %>"
                                        <%= (trajet != null && trajet.getChauffeur() != null && trajet.getChauffeur().getId().equals(c.getId())) ? "selected" : "" %>>
                                    <%= c.getNom() %> <%= c.getPrenom() %>
                                </option>
                                <% } %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="idVehicule" class="form-label">Véhicule</label>
                            <select class="form-control" id="idVehicule" name="idVehicule" required>
                                <option value="">Choisir...</option>
                                <% for (Vehicule v : vehicules) { %>
                                <option value="<%= v.getId() %>"
                                        <%= (trajet != null && trajet.getVehicule() != null && trajet.getVehicule().getId().equals(v.getId())) ? "selected" : "" %>>
                                    <%= v.getImmatriculation() %> - <%= v.getMarque() %> <%= v.getModele() %>
                                </option>
                                <% } %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="datetimeDepart" class="form-label">Date Heure Départ</label>
                            <input type="datetime-local" class="form-control" id="datetimeDepart" name="datetimeDepart"
                                   value="<%= trajet != null && trajet.getDatetimeDepart() != null ? trajet.getDatetimeDepart().toString().substring(0, 16) : "" %>" required>
                        </div>

                        <div class="mb-3">
                            <label for="datetimeArrivee" class="form-label">Date Heure Arrivée (optionnel)</label>
                            <input type="datetime-local" class="form-control" id="datetimeArrivee" name="datetimeArrivee"
                                   value="<%= trajet != null && trajet.getDatetimeArrivee() != null ? trajet.getDatetimeArrivee().toString().substring(0, 16) : "" %>">
                        </div>

                        <div class="mb-3">
                            <label for="idTrajetStatut" class="form-label">Statut</label>
                            <select class="form-control" id="idTrajetStatut" name="idTrajetStatut" required>
                                <option value="">Choisir...</option>
                                <% for (TypeObjectDTO s : trajetStatuts) { %>
                                <option value="<%= s.getId() %>"
                                        <%= (trajet != null && trajet.getTrajetStatut() != null && trajet.getTrajetStatut().getId().equals(s.getId())) ? "selected" : "" %>>
                                    <%= s.getLibelle() %>
                                </option>
                                <% } %>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="fraisUnitaire" class="form-label">Frais Unitaire (Ar)</label>
                            <input type="number" step="0.01" class="form-control" id="fraisUnitaire" name="fraisUnitaire"
                                   value="<%= trajet != null ? trajet.getFraisUnitaire() : "" %>" required>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="<%= request.getContextPath() %>/trajets" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <!-- List + Multi-Filter -->
        <div class="col-md-8">
            <!-- Multi-Filter Form (using Map in servlet) -->
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Filtrer les Trajets</h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/trajets" method="get">
                        <div class="row">
                            <div class="col-md-3 mb-3">
                                <label for="filterLigne" class="form-label">Ligne</label>
                                <select class="form-control" id="filterLigne" name="idLigne">
                                    <option value="">Tous</option>
                                    <% for (Ligne l : lignes) { %>
                                    <option value="<%= l.getId() %>"><%= l.getVilleDepart().getNom() %> → <%= l.getVilleArrivee().getNom() %></option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="col-md-3 mb-3">
                                <label for="filterChauffeur" class="form-label">Chauffeur</label>
                                <select class="form-control" id="filterChauffeur" name="idChauffeur">
                                    <option value="">Tous</option>
                                    <% for (Chauffeur c : chauffeurs) { %>
                                    <option value="<%= c.getId() %>"><%= c.getNom() %> <%= c.getPrenom() %></option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="col-md-3 mb-3">
                                <label for="filterVehicule" class="form-label">Véhicule</label>
                                <select class="form-control" id="filterVehicule" name="idVehicule">
                                    <option value="">Tous</option>
                                    <% for (Vehicule v : vehicules) { %>
                                    <option value="<%= v.getId() %>"><%= v.getImmatriculation() %></option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="col-md-3 mb-3">
                                <label for="filterStatut" class="form-label">Statut</label>
                                <select class="form-control" id="filterStatut" name="idTrajetStatut">
                                    <option value="">Tous</option>
                                    <% for (TypeObjectDTO s : trajetStatuts) { %>
                                    <option value="<%= s.getId() %>"><%= s.getLibelle() %></option>
                                    <% } %>
                                </select>
                            </div>
                            <div class="col-md-3 mb-3">
                                <label for="filterStartDate" class="form-label">Départ Après</label>
                                <input type="date" class="form-control" id="filterStartDate" name="startDate">
                            </div>
                            <div class="col-md-3 mb-3">
                                <label for="filterEndDate" class="form-label">Départ Avant</label>
                                <input type="date" class="form-control" id="filterEndDate" name="endDate">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">Filtrer</button>
                        <a href="<%= request.getContextPath() %>/trajets" class="btn btn-secondary">Réinitialiser</a>
                    </form>
                </div>
            </div>

            <!-- List Table -->
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Trajets</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Ligne</th>
                                <th>Chauffeur</th>
                                <th>Véhicule</th>
                                <th>Départ</th>
                                <th>Arrivée</th>
                                <th>Statut</th>
                                <th>Frais Unitaire</th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% if (trajets != null && !trajets.isEmpty()) {
                                for (Trajet t : trajets) { %>
                            <tr>
                                <td><%= t.getId() %></td>
                                <td><%= t.getLigne().getVilleDepart().getNom() %> → <%= t.getLigne().getVilleArrivee().getNom() %></td>
                                <td><%= t.getChauffeur().getNom() %> <%= t.getChauffeur().getPrenom() %></td>
                                <td><%= t.getVehicule().getImmatriculation() %></td>
                                <td><%= t.getDatetimeDepart() %></td>
                                <td><%= t.getDatetimeArrivee() != null ? t.getDatetimeArrivee() : "-" %></td>
                                <td><%= t.getTrajetStatut().getLibelle() %></td>
                                <td><%= t.getFraisUnitaire() %> Ar</td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/trajets?action=edit&id=<%= t.getId() %>" class="btn btn-sm btn-info">Modifier</a>
                                    <a href="<%= request.getContextPath() %>/trajets/detail?id=<%= t.getId() %>" class="btn btn-sm btn-primary">Détails</a>
                                </td>
                            </tr>
                            <% }
                            } else { %>
                            <tr>
                                <td colspan="9" class="text-center">Aucun trajet trouvé.</td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>