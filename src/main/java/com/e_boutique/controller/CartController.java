package com.e_boutique.controller;


import com.e_boutique.model.CartItem;
import com.e_boutique.model.Order;
import com.e_boutique.model.Product;
import com.e_boutique.repository.OrderRepository;
import com.e_boutique.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CartController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderRepository orderRepo;

    @GetMapping("")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping("/client/{email}")
    public List<Order> getOrdersByClient(@PathVariable String email) {
        return orderRepo.findByClientEmail(email);
    }


    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) return "Produit introuvable";

        Optional<CartItem> existing = cart.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + 1);
        } else {
            cart.add(new CartItem(product, 1));
        }

        session.setAttribute("cart", cart);
        return "Produit ajouté au panier";
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "Le panier est vide";
        }

        boolean removed = cart.removeIf(item -> item.getProduct().getId().equals(productId));

        if (removed) {
            session.setAttribute("cart", cart); // mettre à jour la session
            return "Produit supprimé du panier";
        } else {
            return "Produit non trouvé dans le panier";
        }
    }

    @PostMapping("/update/{productId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long productId,
                                            @RequestParam int quantity,
                                            HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) return ResponseEntity.badRequest().body("Panier vide");

        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity <= 0) {
                    cart.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                session.setAttribute("cart", cart);
                return ResponseEntity.ok("Quantité mise à jour");
            }
        }

        return ResponseEntity.badRequest().body("Produit introuvable dans le panier");
    }


}
