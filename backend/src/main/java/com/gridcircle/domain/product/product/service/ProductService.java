package com.gridcircle.domain.product.product.service;

import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findByProductName(String productName) {
        return productRepository.findByProductName((productName))
                .orElseThrow(() -> new ExpressionException("상품을 찾을 수 없습니다."));


    }

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

//    product,
//            reqBody.productName(),
//            reqBody.description(),
//            reqBody.productImage(),
//            reqBody.price(),
//            reqBody.stock()

    // 상품 정보 수정

    public void update(Product product, String productName, String description, String productImage, int price, int stock) {
        product.update(
                productName,
                description,
                productImage,
                price,
                stock
        );
    }

    // 상품 삭제
    public Optional<Product> delete(int id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return product;
                });
    }

}
