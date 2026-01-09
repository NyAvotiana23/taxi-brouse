<%@ page import="com.mdgtaxi.service.ChauffeurService" %>
<%@ page import="com.mdgtaxi.view.VmChauffeurDetail" %>
<%@ page import="java.util.*" %>
<%
    ChauffeurService service = new ChauffeurService();
    Map<String, Object> filters = new HashMap<>();

    String nomFilter = request.getParameter("nom");
    if(nomFilter != null && !nomFilter.isEmpty()) filters.put("nom", nomFilter);

    List<VmChauffeurDetail> chauffeurs = filters.isEmpty() ?
            service.getAllChauffeurDetails() : service.searchChauffeurDetailsWithFilters(filters);
%>
<div class="container">
    <h2>Filtres de recherche</h2>
    <form method="GET">
        <input type="text" name="nom" placeholder="Nom du chauffeur" value="<%= (nomFilter!=null)?nomFilter:"" %>">
        <button type="submit">Rechercher</button>
    </form>

    <table class="table">
        <% for(VmChauffeurDetail c : chauffeurs) { %>
        <tr>
            <td><%= c.getNomComplet() %></td>
            <td><%= c.getNumeroPermis() %></td>
            <td><%= c.getAge() %> ans</td>
            <td><%= c.getLibelleStatut() %></td>
            <td><a href="chauffeur-detail.jsp?id=<%= c.getIdChauffeur() %>">Voir Activité</a></td>
        </tr>
        <% } %>
    </table>
</div>