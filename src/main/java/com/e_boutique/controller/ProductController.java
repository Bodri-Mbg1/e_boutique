package com.e_boutique.controller;

import com.e_boutique.model.Product;
import com.e_boutique.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000") // si tu fais un front plus tard
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    // 🔹 Voir tous les produits
    @GetMapping("")
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // 🔹 Ajouter un produit
    @PostMapping("")
    public Product addProduct(@RequestBody Product product) {
        return productRepo.save(product);
    }
}
