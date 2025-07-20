package com.gridcircle.domain.order.order.service;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.repository.MemberRepository;
import com.gridcircle.domain.order.order.dto.OrderPageResponseDto;
import com.gridcircle.domain.order.order.dto.OrderRequestDto;
import com.gridcircle.domain.order.order.dto.OrderResponseDto;
import com.gridcircle.domain.order.order.entity.OrderItem;
import com.gridcircle.domain.order.order.entity.OrderStatus;
import com.gridcircle.domain.order.order.entity.Orders;
import com.gridcircle.domain.order.order.repository.OrderRepository;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.repository.ProductRepository;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.entity.ShoppingBasket;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.repository.ShoppingBasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ShoppingBasketRepository shoppingBasketRepository;


    // 장바구니에 있는 데이터 + 사용자 주소를 주문 페이지로 보내주기 위한 dto를 생성하는 메서드 (프론트의 get요청)
    @Transactional(readOnly=true)
    public OrderPageResponseDto getOrderPageData(int memberId) {
        // 현재 로그인중인 member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 또는 회원가입을 진행해주세요"));
        // 현재 로그인중인 member의 장바구니 데이터를 모아서 baskets 객체에 저장
        List<ShoppingBasket> baskets = shoppingBasketRepository.findByMemberId(memberId);

        // 상품별로 수량을 합치는 Map 생성
        Map<Integer, OrderPageResponseDto.OrderPageItemDto> productMap = new HashMap<>();
        for (ShoppingBasket basket : baskets) {
            Product product = productRepository.findById(basket.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
            Integer productId = product.getId();
            productMap.compute(productId, (key, existingItem) -> {
                if (existingItem != null) {// 이미 존재하는 상품이면 수량만 증가
                    return new OrderPageResponseDto.OrderPageItemDto(
                            existingItem.productId(),
                            existingItem.productName(),
                            existingItem.productPrice(),
                            existingItem.orderCount() + basket.getProductCount(),
                            existingItem.productImage()
                    );
                } else {// 장바구니에 존재하지 않는 상품이면 새로 추가
                    return new OrderPageResponseDto.OrderPageItemDto(
                            product.getId(),
                            product.getProductName(),
                            product.getPrice(),
                            basket.getProductCount(),
                            product.getProductImage().split("\\|")[0]
                    );
                }
            });
        }
        List<OrderPageResponseDto.OrderPageItemDto> items = new ArrayList<>(productMap.values());
        return new OrderPageResponseDto(member.getAddress(), items);
    }



    // 주문페이지에서 결제하기 버튼을 눌렀을 때, 주문 내역을 저장하는 메서드 (프론트의 post요청)
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto reqDto, int memberId) { // reqDto는 프론트가 보내온 데이터, memberId는 사용자 id
        // 사용자 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 또는 회원가입을 진행해주세요."));
        // Order 엔티티 생성
        Orders orders = new Orders();
        // 생성한 order에 값 설정
        orders.setTotalPrice(reqDto.totalPrice());
        orders.setAddress(reqDto.address());
        orders.setOrderStatus(OrderStatus.valueOf(reqDto.orderStatus()));
        orders.setDeliveryStatus(reqDto.deliveryStatus());
        orders.setMember(member);

        // 상세 항목을 담을 orderItems 리스트 생성
        List<OrderItem> orderItems = new ArrayList<>();
        // itemDto 객체에
        for (OrderRequestDto.OrderItemRequestDto itemDto : reqDto.orderItems()) {
            // 상품 조회
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
            // OrderItem 엔티티 생성
            OrderItem orderItem = new OrderItem();
            // 생성한 OrderItem에 값 설정
            orderItem.setProductName(itemDto.productName());
            orderItem.setOrderCount(itemDto.orderCount());
            orderItem.setProductPrice(itemDto.productPrice());
            orderItem.setProductImage(itemDto.productImage());
            orderItem.setOrders(orders);
            orderItem.setProduct(product);
            // orderItems 리스트에 orderItem 엔티티 저장
            orderItems.add(orderItem);
        }
        // order 엔티티에 orderItems 리스트 저장
        orders.setOrderItems(orderItems);

        // order 엔티티를 orderRepository에 저장 (이때, CasecadeType.PERSIST로 인해, OrderItem도 함께 저장됨)
        Orders savedOrders = orderRepository.save(orders);

        // 주문 완료 후 해당 사용자의 장바구니 비우기
        shoppingBasketRepository.deleteByMemberId(memberId);

        // 주문 내역 db에 등록한 데이터를 dto로 변환하여 응답
        return new OrderResponseDto(savedOrders);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrderHistory(int memberId) {
        // 해당 사용자의 모든 주문(Order) 조회
        List<Orders> orders = orderRepository.findByMemberId(memberId);
        return orders.stream()
                .map(OrderResponseDto::new)
                .toList(); //주문 리스트를 dto 리스트로 변환 (프론트에 응답 보내기 위해)
    }

    @Transactional
    public void cancelOrder(int orderId, int memberId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        // 추가로 본인 주문이 맞는지 체크 (혹시나해서)
        if (orders.getMember().getId() != memberId){
            throw new IllegalArgumentException("본인 주문만 취소할 수 있습니다.");
        }
        // 각 주문의 배송 주기에 따라 취소 가능 여부 체크
        LocalDateTime orderDate = orders.getCreatedDate();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cancelDeadline;
        if (orderDate.getHour() < 14) {
            cancelDeadline = orderDate.withHour(14).withMinute(0).withSecond(0).withNano(0);
        } else {
            cancelDeadline = orderDate.plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0);
        }
        if (now.isAfter(cancelDeadline)) {
            throw new IllegalArgumentException("배송이 시작되어 주문을 취소할 수 없습니다.");
        }
        orders.setOrderStatus(OrderStatus.CANCELLED);
        //modifiedDate는 @LastModifiedDate에 의해 자동 갱신되므로 생략
    }
}
