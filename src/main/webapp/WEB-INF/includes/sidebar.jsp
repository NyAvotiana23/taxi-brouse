<div class="sidebar p-3" style="width: 250px;">
  <h6 class="text-muted text-uppercase mb-3">Management</h6>
  <nav class="nav flex-column">
    <a class="nav-link" href="<%= request.getContextPath() %>/index.jsp">
      <i class="bi bi-speedometer2"></i> Dashboard
    </a>
    <a class="nav-link" href="<%= request.getContextPath() %>/chauffeures">
      <i class="bi bi-person-fill"></i> Chauffeurs
    </a>
    <a class="nav-link" href="<%= request.getContextPath() %>/vehicules">
      <i class="bi bi-car-front-fill"></i> Véhicules
    </a>
    <a class="nav-link" href="<%= request.getContextPath() %>/lignes">
      <i class="bi bi-signpost-2-fill"></i> Lignes
    </a>
    <a class="nav-link" href="<%= request.getContextPath() %>/trajets-a-venir">
      <i class="bi bi-geo-alt-fill"></i> Trajets à venir
    </a>
    <a class="nav-link" href="<%= request.getContextPath() %>/trajets">
      <i class="bi bi-geo-alt-fill"></i> Trajets
    </a>
    <a class="nav-link" href="<%= request.getContextPath() %>/reservations">
      <i class="bi bi-calendar-check-fill"></i> Réservations
    </a>
    <a class="nav-link" href="<%= request.getContextPath() %>/tarif-remises">
      <i class="bi bi-calendar-check-fill"></i> Configuration tarif remise
    </a>

  </nav>
</div>