package com.gridcircle.domain.order.order.service;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.repository.MemberRepository;
import com.gridcircle.domain.order.order.dto.OrderRequestDto;
import com.gridcircle.domain.order.order.dto.OrderResponseDto;
import com.gridcircle.domain.order.order.entity.Order;
import com.gridcircle.domain.order.order.entity.OrderItem;
import com.gridcircle.domain.order.order.entity.OrderStatus;
import com.gridcircle.domain.order.order.repository.OrderRepository;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    //private final ShoppingBasketRepository shoppingBasketRepository;

/*
    // 장바구니에 있는 데이터 + 사용자 주소를 주문 페이지로 보내주기 위한 dto를 생성하는 메서드 (프론트의 get요청)
    @Transactional(readOnly=true)
    public OrderPageResponseDto getOrderPageData(int memberId) {
        // 현재 로그인중인 member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 또는 회원가입을 진행해주세요"));
        // 현재 로그인중인 member의 장바구니 데이터를 모아서 baskets 객체에 저장
        List<ShoppingBasket> baskets = shoppingBasketRepository.findByMemberId(memberId);

        // OrderPageItemDto 타입의 데이터를 담는 items 리스트 생성
        List<OrderPageResponseDto.OrderPageItemDto> items = new ArrayList<>();
        for (ShoppingBasket basket : baskets) {
            // 장바구니에서 상품 객체 조회
            Product product = productRepository.findById(basket.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
            // items 리스트에 장바구니 내용(상품id, 상품이름, 상품가격, 담은 상품갯수, 상품이미지)을 dto로 저장
            items.add(new OrderPageResponseDto.OrderPageItemDto(
                    product.getId(),
                    product.getProductName(),
                    product.getPrice(),
                    basket.getProductCount(),
                    product.getProductImage().split("\\|")[0] // 첫번째 이미지 url만 프론트로 전송
            ));
        }
        return new OrderPageResponseDto(member.getAddress(), items); // 장바구니 항목 + 현재 로그인한 사용자의 주소를 Dto로 생성 후 return
    }
*/ // 장바구니가 없어서 오류나는 부분 주석 처리



    // 주문페이지에서 결제하기 버튼을 눌렀을 때, 주문 내역을 저장하는 메서드 (프론트의 post요청)
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto reqDto, int memberId) { // reqDto는 프론트가 보내온 데이터, memberId는 사용자 id
        // 사용자 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 또는 회원가입을 진행해주세요."));
        // Order 엔티티 생성
        Order order = new Order();
        // 생성한 order에 값 설정
        order.setTotalPrice(reqDto.totalPrice());
        order.setAddress(reqDto.address());
        order.setOrderStatus(OrderStatus.valueOf(reqDto.orderStatus()));
        order.setDeliveryStatus(reqDto.deliveryStatus());
        order.setMember(member);

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
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            // orderItems 리스트에 orderItem 엔티티 저장
            orderItems.add(orderItem);
        }
        // order 엔티티에 orderItems 리스트 저장
        order.setOrderItems(orderItems);

        // order 엔티티를 orderRepository에 저장 (이때, CasecadeType.PERSIST로 인해, OrderItem도 함께 저장됨)
        Order savedOrder = orderRepository.save(order);

        // 주문 내역 db에 등록한 데이터를 dto로 변환하여 응답
        return new OrderResponseDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrderHistory(int memberId) {
        // 해당 사용자의 모든 주문(Order) 조회
        List<Order> orders = orderRepository.findByMemberId(memberId);
        return orders.stream()
                .map(OrderResponseDto::new)
                .toList(); //주문 리스트를 dto 리스트로 변환 (프론트에 응답 보내기 위해)
    }

    @Transactional
    public void cancelOrder(int orderId, int memberId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        // 추가로 본인 주문이 맞는지 체크 (혹시나해서)
        if (order.getMember().getId() != memberId){
            throw new IllegalArgumentException("본인 주문만 취소할 수 있습니다.");
        }
        //현재 시각이 오늘 오후 2시를 지난 시간이라면 취소 불가능
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayTwoPM = now.withHour(14).withMinute(0).withSecond(0).withNano(0);
        if(now.isAfter(todayTwoPM)){
            throw new IllegalArgumentException();
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        //modifiedDate는 @LastModifiedDate에 의해 자동 갱신되므로 생략
    }
}
