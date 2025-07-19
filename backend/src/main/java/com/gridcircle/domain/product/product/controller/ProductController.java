package com.gridcircle.domain.product.product.controller;

import com.gridcircle.domain.product.product.dto.ProductDto;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grid/products/")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "상품 단건 조회")
    public ProductDto getProducts(@PathVariable int id){
        Product product = productService.getProductById(id);
        return new ProductDto(product);
    }

    @GetMapping()
    @Transactional(readOnly = true)
    @Operation(summary = "다건 조회")
    public List<ProductDto> getItems() {
        List<Product> items = productService.findAll();

        return items
                .stream()
                .map(ProductDto::new) // PostDto로 변환
                .toList();
    }
}
