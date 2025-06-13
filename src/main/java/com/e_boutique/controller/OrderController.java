package com.e_boutique.controller;


import com.e_boutique.model.CartItem;
import com.e_boutique.model.Order;
import com.e_boutique.model.OrderItem;
import com.e_boutique.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @PostMapping("/place")
    public String placeOrder(@RequestBody Order orderDetails, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "Panier vide";
        }

        Order order = new Order();
        order.setClientNom(orderDetails.getClientNom());
        order.setClientEmail(orderDetails.getClientEmail());
        order.setClientAdresse(orderDetails.getClientAdresse());
        order.setDateCommande(new Date());

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrixUnitaire(item.getProduct().getPrix());

            total += item.getProduct().getPrix() * item.getQuantity();
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotal(total);
        orderRepo.save(order);

        session.removeAttribute("cart"); // vider le panier

        return "Commande passée avec succès";
    }
}
