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

    // 주문 페이지 GET 요청에 대한 응답 DTO 생성 메서드
    @Transactional(readOnly=true)
    public OrderPageResponseDto getOrderPageData(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 또는 회원가입을 진행해주세요"));
        List<ShoppingBasket> baskets = shoppingBasketRepository.findByMemberId(memberId);
        Map<Integer, OrderPageResponseDto.OrderPageItemDto> productMap = new HashMap<>(); // 상품별로 수량을 합치는 Map
        for (ShoppingBasket basket : baskets) {
            Product product = productRepository.findById(basket.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
            Integer productId = product.getId();
            productMap.compute(productId, (key, existingItem) -> {
                if (existingItem != null) { // 장바구니에 이미 존재하는 상품이면 수량만 증가
                    return new OrderPageResponseDto.OrderPageItemDto(
                            existingItem.productId(),
                            existingItem.productName(),
                            existingItem.productPrice(),
                            existingItem.orderCount() + basket.getProductCount(), // 이 부분
                            existingItem.productImage()
                    );
                } else {// 장바구니에 존재하지 않는 상품이면 새로 추가
                    return new OrderPageResponseDto.OrderPageItemDto(
                            product.getId(),
                            product.getProductName(),
                            product.getPrice(),
                            basket.getProductCount(), // 이 부분
                            product.getProductImage().split("\\|")[0]
                    );
                }
            });
        }
        List<OrderPageResponseDto.OrderPageItemDto> items = new ArrayList<>(productMap.values());
        return new OrderPageResponseDto(member.getAddress(), items);
    }

    // 주문 페이지 POST 요청에 대한 주문 내역을 저장하는 메서드
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto reqDto, int memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("로그인 또는 회원가입을 진행해주세요."));

        Orders orders = new Orders();
        orders.setTotalPrice(reqDto.totalPrice());
        orders.setAddress(reqDto.address());
        orders.setOrderStatus(OrderStatus.valueOf(reqDto.orderStatus())); // String을 enum으로 변환
        orders.setDeliveryStatus(reqDto.deliveryStatus());
        orders.setMember(member);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderRequestDto.OrderItemRequestDto itemDto : reqDto.orderItems()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
            // 주문 완료 시, 해당 Product의 stock 차감
            if(product.getStock() < itemDto.orderCount()){
                throw new IllegalArgumentException(product.getProductName()+"의 재고가 부족합니다.");
            }
            product.updateStock(product.getStock() - itemDto.orderCount()); // product의 stock 값 업데이트

            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(itemDto.productName());
            orderItem.setOrderCount(itemDto.orderCount());
            orderItem.setProductPrice(itemDto.productPrice());
            orderItem.setProductImage(itemDto.productImage());
            orderItem.setOrders(orders);
            orderItem.setProduct(product);

            orderItems.add(orderItem); // orderItems 리스트에 orderItem 엔티티 저장
        }
        // orders 엔티티에 orderItems 리스트를 orderItems 리스트 값으로 set
        orders.setOrderItems(orderItems);
        // orders 엔티티를 orderRepository에 저장 (이때, CasecadeType.PERSIST로 인해, OrderItem도 함께 저장됨)
        Orders savedOrders = orderRepository.save(orders);
        // 주문 완료 후 해당 사용자의 장바구니 비우기
        shoppingBasketRepository.deleteByMemberId(memberId);
        // 주문 내역 db에 등록한 데이터를 dto로 변환하여 응답
        return new OrderResponseDto(savedOrders);
    }

    // 주문 내역 조회 페이지 GET 요청에 대한 응답 DTO 생성 메서드
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrderHistory(int memberId) {
        List<Orders> orders = orderRepository.findByMemberId(memberId);
        return orders.stream()
                .map(order -> new OrderResponseDto(order))
                .toList();
    }

    // 주문 내역 조회 페이지 PUT 취소 요청에 대한 메서드
    @Transactional
    public void cancelOrder(int orderId, int memberId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        if (orders.getMember().getId() != memberId){
            throw new IllegalArgumentException("본인 주문만 취소할 수 있습니다.");
        }
        // 각 주문에 따라 취소 가능 여부 체크
        LocalDateTime orderDate = orders.getCreatedDate(); // 주문 시간
        LocalDateTime now = LocalDateTime.now(); // 현재 시간
        LocalDateTime cancelDeadline; // 취소 마감 시간
        if (orderDate.getHour() < 14) { // 주문이 14시 이전이면
            cancelDeadline = orderDate.withHour(14).withMinute(0).withSecond(0).withNano(0); // 당일 14시 전까지 주문 취소 가능
        } else { // 주문이 14시 이후면
            cancelDeadline = orderDate.plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0); // 다음날 14시 전까지 주문 취소 가능
        }
        if (now.isAfter(cancelDeadline)) { // 현재시간이 취소 마감 시간 이후라면
            throw new IllegalArgumentException("배송이 시작되어 주문을 취소할 수 없습니다.");
        }

        // @Transactional이 붙어 있어서, 메서드 실행 중 수정한 엔티티 변경사항은 트랜젝션 종료 시 DB에 자동 반영됨 (더티체킹)
        orders.setOrderStatus(OrderStatus.CANCELLED);
        //modifiedDate는 @LastModifiedDate에 의해 자동 갱신됨
    }
}
