package com.gridcircle.domain.member.member.controller;

import com.gridcircle.domain.member.member.dto.MemberWithUserEmailDto;
import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.domain.product.product.dto.ProductDto;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.service.ProductService;
import com.gridcircle.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grid/admin")
@RequiredArgsConstructor
public class AdmMemberController {
    private final MemberService memberService;
    private final ProductService productService;



    @GetMapping("/members")
    @Transactional(readOnly = true)
    @Operation(summary = "Admin - 회원 목록 다건 조회")
    public List<MemberWithUserEmailDto> getMembers() {
        List<Member> members = memberService.findAll();

        return members.stream()

                .map(MemberWithUserEmailDto::new)
                .toList();
    }

    @GetMapping("/member/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Admin - 회원 목록 단건 조회")
    public MemberWithUserEmailDto getMember(
            @PathVariable int id
    ) {
        Member member = memberService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));

        return new MemberWithUserEmailDto(member);
    }

    record ProductJoinReqBody(

            @NotBlank
            @Size(min = 2, max = 30, message = "상품 이름은 최소 2자 이상 30자 미만이어야 합니다.")
            String productName,
            @NotBlank
            @Size(min = 5, max = 100, message = "상품 설명은 최소 5자 이상 100자 미만이어야 합니다.")
            String description,
            @NotBlank
            String productImage,
            @NotNull
            @Min(value = 1000, message = "상품 가격은 최소 1000원 이상 입니다.")
            int price,
            @NotNull
            @Min(value = 1, message = "상품 재고는 최소 2개 이상 1000개 미만이어야 합니다.")
            int stock
    ) {
    }


    @PostMapping("/createProduct")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "상품 생성")
    public RsData<ProductDto> createProduct(@Valid @RequestBody ProductJoinReqBody reqBody) {
        Product product = productService.write(
                reqBody.productName(),
                reqBody.description(),
                reqBody.productImage(),
                reqBody.price(),
                reqBody.stock()
        );

        return new RsData<>(
                "201-1",
                "%s 상품이 등록되었습니다.".formatted(product.getProductName()),
                new ProductDto(product)
        );
    }



    record ProductModifyReqBody(

            @NotBlank
            @Size(min = 2, max = 30, message = "상품 이름은 최소 2자 이상 30자 미만이어야 합니다.")
            String productName,
            @NotBlank
            @Size(min = 5, max = 100, message = "상품 설명은 최소 5자 이상 100자 미만이어야 합니다.")
            String description,
            @NotBlank
            String productImage,
            @NotNull
            @Min(value = 1000, message = "상품 가격은 최소 1000원 이상 입니다.")
            int price,
            @NotNull
            @Min(value = 1, message = "상품 재고는 최소 2개 이상 1000개 미만이어야 합니다.")
            int stock
    ) {
    }


    @PutMapping("/product/{id}")
    @Transactional
    @Operation(summary = "상품 수정")
    public RsData<ProductDto> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductModifyReqBody reqBody
    ) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        productService.update(
                product,
                reqBody.productName(),
                reqBody.description(),
                reqBody.productImage(),
                reqBody.price(),
                reqBody.stock()
        );

        return new RsData<>(
                "200-1",
                "%d번 상품이 수정되었습니다.".formatted(product.getId()),
                new ProductDto(product)
        );
    }

    @GetMapping("/products")
    @Transactional(readOnly = true)
    @Operation(summary = "Admin - 상품 목록 다건 조회")
    public List<ProductDto> getProducts() {
        List<Product> products = productService.findAllProducts();

        return products.stream()
                .map(ProductDto::new)
                .toList();
    }

    @GetMapping("/product/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Admin - 상품 목록 단건 조회")
    public ProductDto getProduct(@PathVariable int id) {
        Product product = productService.getProductById(id);

        return new ProductDto(product);
    }

    @DeleteMapping("/product/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "Admin - 상품 삭제")
    public RsData<Void> delete(@PathVariable int id) {
        productService.delete(id);

        return new RsData<>(
                "200-1",
                "%d번 상품이 삭제되었습니다.".formatted(id)
        );
    }
}
