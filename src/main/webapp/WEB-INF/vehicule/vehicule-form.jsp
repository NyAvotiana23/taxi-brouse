<%@ page import="com.mdgtaxi.service.VehiculeService" %>
<%@ page import="com.mdgtaxi.entity.Vehicule" %>
<%@ page import="com.mdgtaxi.entity.VehiculeType" %>
<%@ page import="com.mdgtaxi.entity.CarburantType" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Initialisation du service et de l'objet véhicule
    VehiculeService service = new VehiculeService();
    String idParam = request.getParameter("id");
    Vehicule v = new Vehicule();

    // Si un ID est fourni, on récupère les données existantes pour la modification
    if (idParam != null && !idParam.isEmpty()) {
        try {
            v = service.getVehiculeById(Long.parseLong(idParam));
        } catch (Exception e) {
            // Gérer l'erreur si l'ID est invalide
        }
    }

    // Note : Dans un cas réel, ces listes devraient être récupérées via des services dédiés
    // List<VehiculeType> types = typeService.getAllTypes();
    // List<CarburantType> carburants = carburantService.getAllCarburants();
%>

<div class="container mt-4">
    <div class="card">
        <div class="card-header">
            <h3><%= (v.getId() == null) ? "Ajouter un nouveau véhicule" : "Modifier le véhicule " + v.getImmatriculation() %></h3>
        </div>
        <div class="card-body">
            <form action="vehicle-save" method="POST">

                <%-- Champ caché pour l'ID en cas de modification --%>
                <% if (v.getId() != null) { %>
                <input type="hidden" name="id" value="<%= v.getId() %>">
                <% } %>

                <div class="row">
                    <%-- Informations de base --%>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Immatriculation</label>
                        <input type="text" name="immatriculation" class="form-control"
                               value="<%= (v.getImmatriculation() != null) ? v.getImmatriculation() : "" %>" required>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label">Marque</label>
                        <input type="text" name="marque" class="form-control"
                               value="<%= (v.getMarque() != null) ? v.getMarque() : "" %>" required>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label">Modèle</label>
                        <input type="text" name="modele" class="form-control"
                               value="<%= (v.getModele() != null) ? v.getModele() : "" %>" required>
                    </div>

                    <%-- Caractéristiques techniques --%>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Nombre maximum de passagers</label>
                        <input type="number" name="maximumPassager" class="form-control"
                               value="<%= (v.getMaximumPassager() != 0) ? v.getMaximumPassager() : "" %>">
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label">Capacité réservoir (Litres)</label>
                        <input type="number" step="0.1" name="capaciteCarburant" class="form-control"
                               value="<%=  v.getCapaciteCarburant()  %>">
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label">Consommation (L/100km)</label>
                        <input type="number" step="0.1" name="depenseCarburant100km" class="form-control"
                               value="<%=  v.getDepenseCarburant100km()  %>">
                    </div>

                    <%-- Types (Listes déroulantes) --%>
                    <div class="col-md-6 mb-3">
                        <label class="form-label">Type de véhicule</label>
                        <select name="idVehiculeType" class="form-select">
                            <%-- Logique pour sélectionner l'option actuelle si modification --%>
                            <option value="1" <%= (v.getVehiculeType() != null && v.getVehiculeType().getId() == 1) ? "selected" : "" %>>Légère</option>
                            <option value="2" <%= (v.getVehiculeType() != null && v.getVehiculeType().getId() == 2) ? "selected" : "" %>>Sprinter</option>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label class="form-label">Type de carburant</label>
                        <select name="idCarburantType" class="form-select">
                            <option value="1" <%= (v.getCarburantType() != null && v.getCarburantType().getId() == 1) ? "selected" : "" %>>Essence</option>
                            <option value="2" <%= (v.getCarburantType() != null && v.getCarburantType().getId() == 2) ? "selected" : "" %>>Gasoil</option>
                        </select>
                    </div>
                </div>

                <div class="mt-4">
                    <button type="submit" class="btn btn-success">
                        <i class="fas fa-save"></i> Enregistrer
                    </button>
                    <a href="vehicule-liste.jsp" class="btn btn-secondary">Annuler</a>
                </div>
            </form>
        </div>
    </div>
</div>