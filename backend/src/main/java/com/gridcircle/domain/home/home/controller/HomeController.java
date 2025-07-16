package com.gridcircle.domain.home.home.controller;


import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/grid")
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<Product> mainPage() {
        List<Product> products = productService.findAll();
        return products;
    }
}
