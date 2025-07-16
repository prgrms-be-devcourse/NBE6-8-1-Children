package com.gridcircle.domain.order.order.controller;

import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.domain.order.order.dto.OrderPageResponseDto;
import com.gridcircle.domain.order.order.dto.OrderRequestDto;
import com.gridcircle.domain.order.order.dto.OrderResponseDto;
import com.gridcircle.domain.order.order.service.OrderService;
import com.gridcircle.global.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public OrderPageResponseDto getOrderPageDataByToken(@AuthenticationPrincipal SecurityUser userDetails){
        // @AuthenticationPrincipal은 현재 로그인한 사용자 정보를 메서드 파라미터로 주입해주는 어노테이션
        // SecurityUser userDetails는 현재 로그인한 사용자 정보를 담고있는 객체
        int memberId = userDetails.getId(); //userDetails=현재 로그인한 사용자 정보가 담긴 객체에서, id값을 얻음
        return orderService.getOrderPageData(memberId);
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