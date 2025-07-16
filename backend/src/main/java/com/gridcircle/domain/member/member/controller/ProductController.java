package com.gridcircle.domain.member.member.controller;

import com.gridcircle.domain.member.member.dto.MemberWithUserEmailDto;
import com.gridcircle.domain.member.member.dto.ProductDto;
import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.entity.Product;
import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.domain.member.member.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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
}
