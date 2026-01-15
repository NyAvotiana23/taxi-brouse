<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.Vehicule" %>
<%@ page import="com.mdgtaxi.dto.TypeObjectDTO" %>
<%@ page import="java.util.List" %>
<%
    Vehicule vehicule = (Vehicule) request.getAttribute("vehicule");
    List<TypeObjectDTO> vehiculeTypes = (List<TypeObjectDTO>) request.getAttribute("vehiculeTypes");
    List<TypeObjectDTO> carburantTypes = (List<TypeObjectDTO>) request.getAttribute("carburantTypes");
    Double premiumPlaces = (Double) request.getAttribute("premiumPlaces");
    Double premiumTarif = (Double) request.getAttribute("premiumTarif");
    Double standardPlaces = (Double) request.getAttribute("standardPlaces");
    Double standardTarif = (Double) request.getAttribute("standardTarif");
    String error = (String) request.getAttribute("error");
%>

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
        <% if (error != null) { %>
        <div class="alert alert-danger" role="alert">
            <%= error %>
        </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/vehicules" method="post">
            <input type="hidden" name="id" value="<%= vehicule != null ? vehicule.getId() : "" %>">

            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="marque" class="form-label">Marque <span class="text-danger">*</span></label>
                        <input type="text"
                               class="form-control"
                               id="marque"
                               name="marque"
                               value="<%= vehicule != null ? vehicule.getMarque() : "" %>"
                               required>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="modele" class="form-label">Modèle <span class="text-danger">*</span></label>
                        <input type="text"
                               class="form-control"
                               id="modele"
                               name="modele"
                               value="<%= vehicule != null ? vehicule.getModele() : "" %>"
                               required>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="immatriculation" class="form-label">Immatriculation <span class="text-danger">*</span></label>
                        <input type="text"
                               class="form-control"
                               id="immatriculation"
                               name="immatriculation"
                               value="<%= vehicule != null ? vehicule.getImmatriculation() : "" %>"
                               required>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="maximumPassager" class="form-label">Nombre de Passagers Max <span class="text-danger">*</span></label>
                        <input type="number"
                               class="form-control"
                               id="maximumPassager"
                               name="maximumPassager"
                               min="1"
                               value="<%= vehicule != null ? vehicule.getMaximumPassager() : "" %>"
                               required>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="idType" class="form-label">Type de Véhicule <span class="text-danger">*</span></label>
                        <select class="form-control" id="idType" name="idType" required>
                            <option value="">-- Choisir un type --</option>
                            <% if (vehiculeTypes != null) {
                                for (TypeObjectDTO type : vehiculeTypes) { %>
                            <option value="<%= type.getId() %>"
                                    <%= (vehicule != null && vehicule.getVehiculeType() != null &&
                                            vehicule.getVehiculeType().getId().equals(type.getId())) ? "selected" : "" %>>
                                <%= type.getLibelle() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="idTypeCarburant" class="form-label">Type de Carburant <span class="text-danger">*</span></label>
                        <select class="form-control" id="idTypeCarburant" name="idTypeCarburant" required>
                            <option value="">-- Choisir un carburant --</option>
                            <% if (carburantTypes != null) {
                                for (TypeObjectDTO type : carburantTypes) { %>
                            <option value="<%= type.getId() %>"
                                    <%= (vehicule != null && vehicule.getCarburantType() != null &&
                                            vehicule.getCarburantType().getId().equals(type.getId())) ? "selected" : "" %>>
                                <%= type.getLibelle() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="capaciteCarburant" class="form-label">Capacité Carburant (L)</label>
                        <input type="number"
                               step="0.01"
                               class="form-control"
                               id="capaciteCarburant"
                               name="capaciteCarburant"
                               min="0"
                               value="<%= vehicule != null && vehicule.getCapaciteCarburant() != null ?
                                          vehicule.getCapaciteCarburant() : "" %>">
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="depenseCarburant100km" class="form-label">Consommation (L/100km)</label>
                        <input type="number"
                               step="0.01"
                               class="form-control"
                               id="depenseCarburant100km"
                               name="depenseCarburant100km"
                               min="0"
                               value="<%= vehicule != null && vehicule.getDepenseCarburant100km() != null ?
                                          vehicule.getDepenseCarburant100km() : "" %>">
                    </div>
                </div>
            </div>

            <!-- New section for seat types -->
            <div class="card mb-4">
                <div class="card-header">
                    <h6 class="m-0 font-weight-bold text-primary">Configuration des Types de Places</h6>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="premiumPlaces" class="form-label">Nombre de Places Premium</label>
                                <input type="number"
                                       step="1"
                                       class="form-control"
                                       id="premiumPlaces"
                                       name="premiumPlaces"
                                       min="0"
                                       value="<%= premiumPlaces != null ? premiumPlaces : "0" %>">
                            </div>
                            <div class="mb-3">
                                <label for="premiumTarif" class="form-label">Tarif Unitaire Premium (Ar)</label>
                                <input type="number"
                                       step="0.01"
                                       class="form-control"
                                       id="premiumTarif"
                                       name="premiumTarif"
                                       min="0"
                                       value="<%= premiumTarif != null ? premiumTarif : "140000.0" %>">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="standardPlaces" class="form-label">Nombre de Places Standard</label>
                                <input type="number"
                                       step="1"
                                       class="form-control"
                                       id="standardPlaces"
                                       name="standardPlaces"
                                       min="0"
                                       value="<%= standardPlaces != null ? standardPlaces : "0" %>">
                            </div>
                            <div class="mb-3">
                                <label for="standardTarif" class="form-label">Tarif Unitaire Standard (Ar)</label>
                                <input type="number"
                                       step="0.01"
                                       class="form-control"
                                       id="standardTarif"
                                       name="standardTarif"
                                       min="0"
                                       value="<%= standardTarif != null ? standardTarif : "80000.0" %>">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="mt-4">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Enregistrer
                </button>
                <a href="<%= request.getContextPath() %>/vehicules" class="btn btn-secondary">
                    <i class="fas fa-times"></i> Annuler
                </a>
            </div>
        </form>
    </div>
</div>