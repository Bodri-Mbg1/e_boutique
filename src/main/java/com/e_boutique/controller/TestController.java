package com.e_boutique.controller;

import com.e_boutique.model.Product;
import com.e_boutique.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/add")
    public Product addProductManually() {
        Product product = new Product();
        product.setNom("Produit Test");
        product.setDescription("Produit créé manuellement");
        product.setPrix(12.99);
        product.setImageUrl("https://test.com/image.jpg");

        return productRepository.save(product);
    }
}
