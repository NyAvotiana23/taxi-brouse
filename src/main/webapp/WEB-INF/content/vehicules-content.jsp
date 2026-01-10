<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%
    Vehicule vehicule = (Vehicule) request.getAttribute("vehicule");
    List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
    List<TypeObjectDTO> vehiculeTypes = (List<TypeObjectDTO>) request.getAttribute("vehiculeTypes");
    List<TypeObjectDTO> carburantTypes = (List<TypeObjectDTO>) request.getAttribute("carburantTypes");
%>

<div class="container-fluid">
    <h1 class="h3 mb-4 text-gray-800">Gestion des Véhicules</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">
                        <% if (vehicule != null) { %>
                            Modifier Véhicule
                        <% } else { %>
                            Nouveau Véhicule
                        <% } %>
                    </h6>
                </div>
                <div class="card-body">
                    <form action="<%= request.getContextPath() %>/vehicules" method="post">
                        <input type="hidden" name="id" value="<%= vehicule != null ? vehicule.getId() : "" %>">

                        <div class="mb-3">
                            <label for="marque" class="form-label">Marque</label>
                            <input type="text" class="form-control" id="marque" name="marque" 
                                value="<%= vehicule != null ? vehicule.getMarque() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="modele" class="form-label">Modèle</label>
                            <input type="text" class="form-control" id="modele" name="modele" 
                                value="<%= vehicule != null ? vehicule.getModele() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="immatriculation" class="form-label">Immatriculation</label>
                            <input type="text" class="form-control" id="immatriculation" name="immatriculation" 
                                value="<%= vehicule != null ? vehicule.getImmatriculation() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="maximumPassager" class="form-label">Max Passagers</label>
                            <input type="number" class="form-control" id="maximumPassager" name="maximumPassager" 
                                value="<%= vehicule != null ? vehicule.getMaximumPassager() : "" %>" required>
                        </div>
                        <div class="mb-3">
                            <label for="capaciteCarburant" class="form-label">Capacité Carburant</label>
                            <input type="number" step="0.01" class="form-control" id="capaciteCarburant" name="capaciteCarburant" 
                                value="<%= vehicule != null && vehicule.getCapaciteCarburant() != null ? vehicule.getCapaciteCarburant() : "" %>">
                        </div>
                        <div class="mb-3">
                            <label for="depenseCarburant100km" class="form-label">Dépense / 100km</label>
                            <input type="number" step="0.01" class="form-control" id="depenseCarburant100km" name="depenseCarburant100km" 
                                value="<%= vehicule != null && vehicule.getDepenseCarburant100km() != null ? vehicule.getDepenseCarburant100km() : "" %>">
                        </div>
                        <div class="mb-3">
                            <label for="idType" class="form-label">Type Véhicule</label>
                            <select class="form-control" id="idType" name="idType" required>
                                <option value="">Choisir...</option>
                                <% if (vehiculeTypes != null) {
                                    for (TypeObjectDTO type : vehiculeTypes) { %>
                                        <option value="<%= type.getId() %>" 
                                            <%= (vehicule != null && vehicule.getVehiculeType() != null && vehicule.getVehiculeType().getId().equals(type.getId())) ? "selected" : "" %>>
                                            <%= type.getLibelle() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="idTypeCarburant" class="form-label">Type Carburant</label>
                            <select class="form-control" id="idTypeCarburant" name="idTypeCarburant" required>
                                <option value="">Choisir...</option>
                                <% if (carburantTypes != null) {
                                    for (TypeObjectDTO type : carburantTypes) { %>
                                        <option value="<%= type.getId() %>" 
                                            <%= (vehicule != null && vehicule.getCarburantType() != null && vehicule.getCarburantType().getId().equals(type.getId())) ? "selected" : "" %>>
                                            <%= type.getLibelle() %>
                                        </option>
                                    <% }
                                } %>
                            </select>
                        </div>

                        <button type="submit" class="btn btn-primary">Enregistrer</button>
                        <a href="<%= request.getContextPath() %>/vehicules" class="btn btn-secondary">Annuler</a>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <h6 class="m-0 font-weight-bold text-primary">Liste des Véhicules</h6>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>Immat.</th>
                                    <th>Marque</th>
                                    <th>Modèle</th>
                                    <th>Type</th>
                                    <th>Carburant</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (vehicules != null) {
                                    for (Vehicule v : vehicules) { %>
                                        <tr>
                                            <td><%= v.getImmatriculation() %></td>
                                            <td><%= v.getMarque() %></td>
                                            <td><%= v.getModele() %></td>
                                            <td><%= v.getVehiculeType().getLibelle() %></td>
                                            <td><%= v.getCarburantType().getLibelle() %></td>
                                            <td>
                                                <a href="<%= request.getContextPath() %>/vehicules?action=edit&id=<%= v.getId() %>" class="btn btn-sm btn-info">Modifier</a>
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
