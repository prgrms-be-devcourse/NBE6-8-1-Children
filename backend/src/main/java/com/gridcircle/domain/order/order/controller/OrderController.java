package com.gridcircle.domain.order.order.controller;

import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.domain.order.order.dto.OrderPageResponseDto;
import com.gridcircle.domain.order.order.dto.OrderRequestDto;
import com.gridcircle.domain.order.order.dto.OrderResponseDto;
import com.gridcircle.domain.order.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grid/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;


    // 주문 페이지 (장바구니에서 데이터 조회)
    @GetMapping("/basket/me")
    @Transactional(readOnly=true)
    @Operation(summary = "주문 페이지 데이터 조회")
    public OrderPageResponseDto getOrderPageDataByToken(/*@AuthenticationPrincipal CustomUserDetails userDetails*/){
        //int memberId = userDeatils.getId();
        //return orderService.getOrderPageData(memberId);
        return null; // 오류 때문에 임시로 null
    }

    // 주문 페이지에서 주문 등록 요청
    @PostMapping("/{memberId}")
    public OrderResponseDto createOrder(
            @PathVariable int memberId,
            @RequestBody OrderRequestDto requestDto
    ) {
        return orderService.createOrder(requestDto, memberId);
    }

}