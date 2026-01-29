<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.mdgtaxi.entity.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%
    ProduitExtraVente vente = (ProduitExtraVente) request.getAttribute("vente");
    List<ProduitCategorie> categories = (List<ProduitCategorie>) request.getAttribute("categories");
    List<ProduitExtra> produits = (List<ProduitExtra>) request.getAttribute("produits");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
    String error = (String) request.getAttribute("error");

    boolean isEdit = vente != null && vente.getId() != null;
    String pageTitle = isEdit ? "Modifier la Vente #" + vente.getId() : "Nouvelle Vente";
%>

<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 text-gray-800">
            <i class="bi bi-<%= isEdit ? "pencil" : "plus-lg" %>"></i> <%= pageTitle %>
        </h1>
        <a href="<%= request.getContextPath() %>/produit-extra-ventes" class="btn btn-secondary">
            <i class="bi bi-arrow-left"></i> Retour
        </a>
    </div>

    <% if (error != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle"></i> <%= error %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">
                <i class="bi bi-box-seam"></i> Informations de la Vente
            </h6>
        </div>
        <div class="card-body">
            <form action="<%= request.getContextPath() %>/produit-extra-ventes" method="post">
                <input type="hidden" name="action" value="save">
                <% if (isEdit) { %>
                <input type="hidden" name="id" value="<%= vente.getId() %>">
                <% } %>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="categorieId" class="form-label">Catégorie *</label>
                        <select class="form-control" id="categorieId" name="categorieId" onchange="filterProduits()">
                            <option value="">Sélectionner une catégorie</option>
                            <% if (categories != null) {
                                for (ProduitCategorie c : categories) {
                                    boolean selected = isEdit && vente.getProduitExtra() != null && 
                                                       vente.getProduitExtra().getProduitCategorie() != null &&
                                                       vente.getProduitExtra().getProduitCategorie().getId().equals(c.getId());
                            %>
                            <option value="<%= c.getId() %>" data-id="<%= c.getId() %>" <%= selected ? "selected" : "" %>>
                                <%= c.getLibelle() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label for="produitId" class="form-label">Produit *</label>
                        <select class="form-control" id="produitId" name="produitId" required onchange="updatePrix()">
                            <option value="">Sélectionner un produit</option>
                            <% if (produits != null) {
                                for (ProduitExtra p : produits) {
                                    boolean selected = isEdit && vente.getProduitExtra() != null && 
                                                       vente.getProduitExtra().getId().equals(p.getId());
                            %>
                            <option value="<%= p.getId() %>" 
                                    data-categorie="<%= p.getProduitCategorie() != null ? p.getProduitCategorie().getId() : "" %>"
                                    data-prix="<%= p.getPrixUnitaire() %>"
                                    <%= selected ? "selected" : "" %>>
                                <%= p.getNom() %> - <%= String.format("%,.0f", p.getPrixUnitaire()) %> Ar
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label for="clientId" class="form-label">Client *</label>
                        <select class="form-control" id="clientId" name="clientId" required>
                            <option value="">Sélectionner un client</option>
                            <% if (clients != null) {
                                for (Client c : clients) {
                                    boolean selected = isEdit && vente.getClient() != null && 
                                                       vente.getClient().getId().equals(c.getId());
                            %>
                            <option value="<%= c.getId() %>" <%= selected ? "selected" : "" %>>
                                <%= c.getNomClient() %>
                            </option>
                            <% }
                            } %>
                        </select>
                    </div>

                    <div class="col-md-6 mb-3">
                        <label for="date" class="form-label">Date *</label>
                        <input type="date" class="form-control" id="date" name="date" required
                               value="<%= isEdit && vente.getDate() != null ? vente.getDate().toLocalDate() : LocalDate.now() %>">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="quantite" class="form-label">Quantité *</label>
                        <input type="number" class="form-control" id="quantite" name="quantite" required min="1"
                               value="<%= isEdit && vente.getQuantite() != null ? vente.getQuantite() : 1 %>"
                               onchange="calculateTotal()">
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="prixUnitaire" class="form-label">Prix Unitaire (Ar)</label>
                        <input type="number" class="form-control" id="prixUnitaire" name="prixUnitaire" step="0.01"
                               value="<%= isEdit && vente.getPrixUnitaire() != null ? vente.getPrixUnitaire() : "" %>"
                               onchange="calculateTotal()">
                        <small class="text-muted">Laisser vide pour utiliser le prix du produit</small>
                    </div>

                    <div class="col-md-4 mb-3">
                        <label for="remise" class="form-label">Remise (%)</label>
                        <input type="number" class="form-control" id="remise" name="remise" step="0.1" min="0" max="100"
                               value="<%= isEdit && vente.getRemise() != null ? vente.getRemise() : 0 %>"
                               onchange="calculateTotal()">
                    </div>
                </div>

                <!-- Affichage du montant calculé -->
                <div class="row mb-3">
                    <div class="col-md-12">
                        <div class="alert alert-info">
                            <strong>Montant Total Estimé: </strong>
                            <span id="montantTotal" class="h5">0,00</span> Ar
                        </div>
                    </div>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-check-lg"></i> <%= isEdit ? "Modifier" : "Enregistrer" %>
                    </button>
                    <a href="<%= request.getContextPath() %>/produit-extra-ventes" class="btn btn-secondary">
                        <i class="bi bi-x-lg"></i> Annuler
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function filterProduits() {
    const categorieId = document.getElementById('categorieId').value;
    const produitSelect = document.getElementById('produitId');
    const options = produitSelect.querySelectorAll('option');
    
    options.forEach(option => {
        if (option.value === '') {
            option.style.display = '';
        } else if (categorieId === '' || option.dataset.categorie === categorieId) {
            option.style.display = '';
        } else {
            option.style.display = 'none';
        }
    });
    
    // Reset selection if current selection is hidden
    if (produitSelect.selectedOptions[0] && produitSelect.selectedOptions[0].style.display === 'none') {
        produitSelect.value = '';
    }
}

function updatePrix() {
    const produitSelect = document.getElementById('produitId');
    const prixInput = document.getElementById('prixUnitaire');
    const selectedOption = produitSelect.selectedOptions[0];
    
    if (selectedOption && selectedOption.dataset.prix) {
        prixInput.value = selectedOption.dataset.prix;
    }
    calculateTotal();
}

function calculateTotal() {
    const quantite = parseFloat(document.getElementById('quantite').value) || 0;
    const prixUnitaire = parseFloat(document.getElementById('prixUnitaire').value) || 0;
    const remise = parseFloat(document.getElementById('remise').value) || 0;
    
    const montantBrut = quantite * prixUnitaire;
    const montantRemise = montantBrut * (remise / 100);
    const montantTotal = montantBrut - montantRemise;
    
    document.getElementById('montantTotal').textContent = montantTotal.toLocaleString('fr-FR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    filterProduits();
    calculateTotal();
});
</script>
