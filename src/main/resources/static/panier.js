const panierContainer = document.getElementById('panier');
const totalDisplay = document.getElementById('total');

fetch('http://localhost:8080/api/cart', {
    credentials: 'include'
})
    .then(res => res.json())
    .then(data => {
        panierContainer.innerHTML = '';
        let total = 0;

        if (data.length === 0) {
            panierContainer.innerHTML = "<p>Votre panier est vide.</p>";
            return;
        }

        data.forEach(item => {
            const { product, quantity } = item;
            const subtotal = product.prix * quantity;
            total += subtotal;

            panierContainer.innerHTML += `
        <div class="carte">
          <img src="${product.imageUrl}" alt="${product.nom}">
          <h3>${product.nom}</h3>
          <p>${quantity} x ${product.prix.toLocaleString()} ₣</p>
          <p class="prix">Sous-total : ${subtotal.toLocaleString()} ₣</p>
        </div>
      `;
        });

        totalDisplay.textContent = `Total : ${total.toLocaleString()} ₣`;
    })
    .catch(err => {
        panierContainer.innerHTML = "<p>Erreur de chargement du panier</p>";
        console.error(err);
    });

function commander() {
    const clientNom = prompt("Votre nom ?");
    const clientEmail = prompt("Votre email ?");
    const clientAdresse = prompt("Votre adresse ?");

    if (!clientNom || !clientEmail || !clientAdresse) {
        alert("Tous les champs sont requis.");
        return;
    }

    fetch("http://localhost:8080/api/orders/place", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({
            clientNom,
            clientEmail,
            clientAdresse
        })
    })
        .then(res => res.text())
        .then(msg => {
            alert(msg);
            window.location.href = "index.html";
        })
        .catch(err => alert("Erreur lors de la commande."));
}
