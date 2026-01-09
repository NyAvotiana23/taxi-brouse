<%@ page import="com.mdgtaxi.service.ChauffeurService" %>
<%@ page import="com.mdgtaxi.view.VmChauffeurDetail" %>
<%@ page import="com.mdgtaxi.view.VmChauffeurActivite" %>
<%
    Long id = Long.parseLong(request.getParameter("id"));
    ChauffeurService service = new ChauffeurService();
    VmChauffeurDetail info = service.getChauffeurDetail(id);
    VmChauffeurActivite activite = service.getActivite(id);
%>
<div class="container">
    <h2>Fiche Chauffeur : <%= info.getNomComplet() %></h2>

    <div class="row">
        <div class="col-md-6">
            <h4>Statistiques d'activité</h4>
            <ul>
                <li>Nombre de trajets : <%= (activite != null) ? activite.getNombreTrajets() : 0 %></li>
                <li>Dernier mouvement : <%= (activite != null) ? activite.getDernierTrajet() : "N/A" %></li>
            </ul>
        </div>

        <div class="col-md-6">
            <h4>Action : Changement de Statut</h4>
            <form action="../../chauffeur-action" method="POST">
                <input type="hidden" name="idChauffeur" value="<%= id %>">
                <select name="statut">
                    <option value="1">Disponible</option>
                    <option value="2">En Trajet</option>
                    <option value="3">En Congé</option>
                </select>
                <button type="submit">Valider</button>
            </form>
        </div>
    </div>
</div>