package com.gridcircle.domain.member.member.service;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.entity.Product;
import com.gridcircle.domain.member.member.repository.MemberRepository;
import com.gridcircle.domain.member.member.repository.ProductRepository;
import com.gridcircle.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public long count() {
        return productRepository.count();
    }

    public Product write(String productName, String description, String productImage, int price, int stock){
        Product product = new Product(productName, description, productImage, price, stock);

        return productRepository.save(product);
    }
}
