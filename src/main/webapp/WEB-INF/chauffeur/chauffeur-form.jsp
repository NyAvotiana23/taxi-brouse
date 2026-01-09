<%@ page import="com.mdgtaxi.service.ChauffeurService" %>
<%@ page import="com.mdgtaxi.entity.Chauffeur" %>
<%
    String idParam = request.getParameter("id");
    Chauffeur c = new Chauffeur();
    if(idParam != null) {
        c = new ChauffeurService().getChauffeurById(Long.parseLong(idParam));
    }
%>
<form action="../../chauffeur-save" method="POST">
    <input type="hidden" name="id" value="<%= (c.getId() != null) ? c.getId() : "" %>">

    <label>Nom :</label>
    <input type="text" name="nom" value="<%= (c.getNom() != null) ? c.getNom() : "" %>">

    <label>Prénom :</label>
    <input type="text" name="prenom" value="<%= (c.getPrenom() != null) ? c.getPrenom() : "" %>">

    <label>Numéro Permis :</label>
    <input type="text" name="numeroPermis" value="<%= (c.getNumeroPermis() != null) ? c.getNumeroPermis() : "" %>">

    <button type="submit">Enregistrer</button>
</form>