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
      <i class="bi bi-percent"></i> Configuration tarif remise
    </a>

    <!-- Menu Chiffre d'Affaires avec sous-menus -->
    <div class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="caDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
        <i class="bi bi-currency-exchange"></i> Chiffre d'Affaires
      </a>
      <ul class="dropdown-menu" aria-labelledby="caDropdown">
        <li>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/chiffre-affaire?type=prevision">
            <i class="bi bi-graph-up-arrow"></i> CA Prévisionnel
          </a>
        </li>
        <li>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/chiffre-affaire?type=reel">
            <i class="bi bi-cash-stack"></i> CA Réel
          </a>
        </li>
        <li>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/chiffre-affaire?type=complet">
            <i class="bi bi-clipboard-data"></i> CA Complet
          </a>
        </li>
      </ul>
    </div>

    <!-- Menu Diffusion avec sous-menus -->
    <div class="nav-item dropdown">
      <a class="nav-link dropdown-toggle" href="#" id="diffusionDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
        <i class="bi bi-megaphone-fill"></i> Diffusions
      </a>
      <ul class="dropdown-menu" aria-labelledby="diffusionDropdown">
        <li>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/diffusions-global">
            <i class="bi bi-graph-up"></i> Diffusion Globale
          </a>
        </li>
        <li>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/diffusions">
            <i class="bi bi-building"></i> Diffusion par Société
          </a>
        </li>
        <li>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/paiements-dashboard">
            <i class="bi bi-wallet2"></i> Paiement Dashboard
          </a>
        </li>
      </ul>
    </div>
  </nav>
</div>