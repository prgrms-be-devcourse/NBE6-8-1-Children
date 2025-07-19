package com.gridcircle.domain.shoppingbasket.shoppingbasket.controller;

import com.gridcircle.domain.shoppingbasket.shoppingbasket.dto.ShoppingBasketRequestDto;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.dto.ShoppingBasketResponseDto;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.service.ShoppingBasketService;
import com.gridcircle.global.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/grid/shoppingbasket")
@RequiredArgsConstructor
public class ShoppingBasketController {
    private final ShoppingBasketService shoppingBasketService;

    // 장바구니 등록 요청
    @PostMapping("/create")
    @Transactional
    @Operation(summary = "주문 데이터 등록")
    public void createShoppingBasket(@RequestBody ShoppingBasketRequestDto dto, @AuthenticationPrincipal SecurityUser userDetails) {
        shoppingBasketService.createShoppingBasket(dto, userDetails.getId());
    }

    // 장바구니 다건조회
    @GetMapping
    @Transactional
    @Operation(summary = "장바구니 다건조회")
    public List<ShoppingBasketResponseDto> getShoppingBasket(@AuthenticationPrincipal SecurityUser userDetails) {
        int memberId = userDetails.getId(); // 현재 로그인한 사용자의 id를 가져옴
        return shoppingBasketService.getShoppingBasket(memberId);
    }

    // 장바구니 삭제 요청
    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "장바구니 삭제")
    public void deleteShoppingBasket(@PathVariable int id, @AuthenticationPrincipal SecurityUser userDetails) {
        shoppingBasketService.deleteShoppingBasket(id, userDetails.getId());
    }
}


