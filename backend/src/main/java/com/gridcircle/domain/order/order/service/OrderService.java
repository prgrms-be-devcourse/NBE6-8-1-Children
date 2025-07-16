package com.gridcircle.domain.order.order.service;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.repository.MemberRepository;
import com.gridcircle.domain.order.order.dto.OrderPageResponseDto;
import com.gridcircle.domain.order.order.dto.OrderRequestDto;
import com.gridcircle.domain.order.order.dto.OrderResponseDto;
import com.gridcircle.domain.order.order.entity.Order;
import com.gridcircle.domain.order.order.entity.OrderItem;
import com.gridcircle.domain.order.order.repository.OrderRepository;
import com.gridcircle.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    //private final ProductRepository productRepository;
    //private final ShoppingBasketRepository shoppingBasketRepository;

    // 장바구니에 있는 데이터를 주문 페이지로 보내주기 위한 메서드 (프론트의 get요청)
    @Transactional(readOnly=true)
    public OrderPageResponseDto getOrderPageData(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 또는 회원가입을 진행해주세요"));
        List<ShoppingBasket> baskets = shoppingBasketRepository.findByMemberId(memberId);

        List<OrderPageItemDto> items = new ArrayList<>();
        for (ShoppingBasket basket : baskets) {
            Product product = productRepository.findById(basket.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
            items.add(new OrderPageItemDto(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    basket.getProductCount()
            ));
        }
        return new OrderPageResponseDto(member.getAddress(), items);
    }

    // 주문페이지에서 결제하기 버튼을 눌렀을 때, 주문 내역을 저장하는 메서드 (프론트의 post요청)
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto, int memberId) {
        Order order = new Order();
        order.setMemberId(memberId);
        order.setTotalPrice(dto.totalPrice());
        order.setAddress(dto.address());
        order.setOrderStatus(dto.orderStatus());
        order.setDeliveryStatus(dto.deliveryStatus());
        order.setCreatedDate(dto.createdDate());
        order.setModifiedDate(dto.modifiedDate());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequestDto.OrderItemRequestDto itemDto : dto.orderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(itemDto.productId());
            orderItem.setProductName(itemDto.productName());
            orderItem.setOrderCount(itemDto.orderCount());
            orderItem.setProductPrice(itemDto.productPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // 응답 DTO로 변환
        List<OrderResponseDto.OrderItemResponseDto> responseItems = new ArrayList<>();
        for (OrderItem item : savedOrder.getOrderItems()) {
            responseItems.add(new OrderResponseDto.OrderItemResponseDto(
                    item.getId(),
                    item.getProductId(),
                    item.getProductName(),
                    item.getOrderCount(),
                    item.getProductPrice()
            ));
        }
        return new OrderResponseDto(
                savedOrder.getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getAddress(),
                savedOrder.getOrderStatus(),
                savedOrder.isDeliveryStatus(),
                savedOrder.getCreatedDate(),
                savedOrder.getModifiedDate(),
                responseItems
        );
    }
}
