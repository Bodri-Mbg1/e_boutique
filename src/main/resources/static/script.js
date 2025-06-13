const produitsContainer = document.getElementById('produits');

fetch('http://localhost:8080/api/products')
    .then(res => res.json())
    .then(data => {
        produitsContainer.innerHTML = '';
        data.forEach(produit => {
            produitsContainer.innerHTML += `
        <div class="carte">
          <img src="${produit.imageUrl ? produit.imageUrl : 'https://via.placeholder.com/200x140?text=Image'}" alt="${produit.nom || 'Sans nom'}">
<h3>${produit.nom || 'Produit inconnu'}</h3>
<p class="categorie">${produit.description || 'Aucune description'}</p>
<p class="prix">${produit.prix ? produit.prix.toLocaleString() + ' ₣' : 'Prix indisponible'}</p>

          <button onclick="ajouterAuPanier(${produit.id})">Ajouter au panier</button>
        </div>
      `;
        });
    })
    .catch(error => {
        produitsContainer.innerHTML = "<p>Erreur de chargement des produits</p>";
        console.error(error);
    });

function ajouterAuPanier(productId) {
    fetch(`http://localhost:8080/api/cart/add/${productId}`, {
        method: 'POST',
        credentials: 'include' // ⚠️ Pour garder la session active
    })
        .then(res => res.text())
        .then(msg => alert(msg))
        .catch(err => alert("Erreur d'ajout au panier"));
}
