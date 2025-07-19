package com.gridcircle.domain.shoppingbasket.shoppingbasket.controller;

import com.gridcircle.domain.shoppingbasket.shoppingbasket.dto.ShoppingBasketRequestDto;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.service.ShoppingBasketService;
import com.gridcircle.global.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

//    // 장바구니 다건조회 Controller
//    @Transactional(readOnly = true)
//    @GetMapping("/create/{memberId}")
//    @Operation(summary = "장바구니 다건조회")
//    public List<ShoppingBasketDto> getShoppingBasket(@AuthenticationPrincipal SecurityUser userDetails) {
//        int memberId = userDetails.getId(); // 현재 로그인한 사용자의 id를 가져옴
//        return shoppingBasketService.getShoppingBasket(memberId);
//    }
}


