package com.gridcircle.domain.product.product.service;

import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(int id){
        return productRepository.findById(id)
                .orElseThrow(()-> new ExpressionException("상품을 찾을 수 없습니다."));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public long count() {
        return productRepository.count();
    }

    public Product write(String productName, String description, String productImage, int price, int stock){
        Product product = new Product(productName, description, productImage, price, stock);

        return productRepository.save(product);
    }


}
