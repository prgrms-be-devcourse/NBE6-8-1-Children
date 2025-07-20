package com.gridcircle.domain.order.order.controller;

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

import java.util.List;

@RestController
@RequestMapping("/grid/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 페이지 GET 요청
    @GetMapping("/basket/me")
    @Transactional(readOnly=true)
    @Operation(summary = "주문 페이지 데이터 조회")
    public OrderPageResponseDto getOrderPageDataByToken(@AuthenticationPrincipal SecurityUser userDetails){
        int memberId = userDetails.getId(); // 현재 로그인한 사용자 정보 찾기
        return orderService.getOrderPageData(memberId);
    }

    // 주문 페이지 POST 요청
    @PostMapping("/basket/me/order")
    @Transactional
    @Operation(summary = "주문 데이터 등록")
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto, @AuthenticationPrincipal SecurityUser user) {
        OrderResponseDto responseDto = orderService.createOrder(orderRequestDto, user.getId());
        return responseDto;
    }

    // 주문 내역 조회 페이지 GET 요청
    @GetMapping("findOrder")
    @Transactional(readOnly = true)
    @Operation(summary = "주문 내역 조회 페이지 데이터 조회")
    public List<OrderResponseDto> getOrderHistory(@AuthenticationPrincipal SecurityUser user){
        int memberId = user.getId();
        return orderService.getOrderHistory(memberId);
    }

    // 주문 취소 PUT 요청
    @PutMapping("/{orderId}/cancel")
    @Transactional
    @Operation(summary = "주문 취소 요청")
    public void cancelOrder(@PathVariable int orderId, @AuthenticationPrincipal SecurityUser user) {
        orderService.cancelOrder(orderId, user.getId());
    }

}
