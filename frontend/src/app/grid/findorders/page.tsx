"use client";

import { apiFetch } from "@/lib/backend/client";
import Image from "next/image";
import { useEffect, useState } from "react";

// 주문 상태 enum
enum OrderStatus {
  ORDERED = "ORDERED",
  CANCELLED = "CANCELLED"
}

// 배송 상태 enum
enum DeliveryStatus {
  BEFORE_DELIVERY = "BEFORE_DELIVERY",
  DELIVERY_STARTED = "DELIVERY_STARTED",
  DELIVERED = "DELIVERED"
}

// 주문 아이템 타입
interface OrderItemDto {
  id: number;
  productName: string;
  orderCount: number;
  productPrice: number;
  productImage: string;
  totalCount: number;
}

// 주문 타입
interface OrderDto {
  id: number;
  createdDate: string;
  address: string;
  totalPrice: number;
  orderStatus: OrderStatus;
  deliveryStatus: DeliveryStatus;
  orderItems: OrderItemDto[];
}

export default function FindOrdersPage() {
  const [orders, setOrders] = useState<OrderDto[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await apiFetch("/api/orders/history") as OrderDto[];
      setOrders(response);
    } catch (error) {
      console.error("주문 내역을 불러오는데 실패했습니다:", error);
    } finally {
      setLoading(false);
    }
  };

  // 전날 오후 2시 ~ 오늘 오후 2시 사이 주문만 취소 가능
  const canCancelOrder = (createdDate: string): boolean => {
    const orderDate = new Date(createdDate);
    const now = new Date();
    const yesterday2PM = new Date(now);
    yesterday2PM.setDate(yesterday2PM.getDate() - 1);
    yesterday2PM.setHours(14, 0, 0, 0);
    const today2PM = new Date(now);
    today2PM.setHours(14, 0, 0, 0);
    return orderDate >= yesterday2PM && orderDate <= today2PM;
  };

  const handleCancelOrder = async (orderId: number) => {
    if (!confirm("정말로 이 주문을 취소하시겠습니까?")) {
      return;
    }
    try {
      await apiFetch(`/api/orders/${orderId}/cancel`, {
        method: "PUT"
      });
      await fetchOrders();
      alert("주문이 성공적으로 취소되었습니다.");
    } catch (error) {
      console.error("주문 취소에 실패했습니다:", error);
      alert("주문 취소에 실패했습니다.");
    }
  };

  const formatDate = (dateString: string): string => {
    const date = new Date(dateString);
    const year = date.getFullYear().toString().slice(-2);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}.${month}.${day} ${hours}:${minutes}:${seconds}`;
  };

  const getOrderStatusText = (status: OrderStatus): string => {
    switch (status) {
      case OrderStatus.ORDERED:
        return "주문 완료";
      case OrderStatus.CANCELLED:
        return "주문 취소";
      default:
        return status;
    }
  };

  const getDeliveryStatusText = (status: DeliveryStatus): string => {
    switch (status) {
      case DeliveryStatus.BEFORE_DELIVERY:
        return "배송 시작 전";
      case DeliveryStatus.DELIVERY_STARTED:
        return "배송 시작";
      case DeliveryStatus.DELIVERED:
        return "배송 완료";
      default:
        return status;
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-lg">로딩중...</div>
      </div>
    );
  }

  return (
    <div className="bg-gray-100 min-h-screen py-8">
      <div className="max-w-4xl mx-auto px-4">
        <div className="bg-white rounded-lg shadow-md p-8">
          {/* 페이지 제목 */}
          <h1 className="text-3xl font-bold text-gray-800 mb-6">주문 내역</h1>
          <hr className="border-gray-300 mb-8" />
          {orders.length === 0 ? (
            <div className="text-center py-12">
              주문 내역이 없습니다.
            </div>
          ) : (
            <div className="space-y-8">
              {orders.map((order: OrderDto) => (
                <div key={order.id} className="border-b border-gray-200 pb-8 last:border-b-0">
                  {/* 주문 그룹 헤더 */}
                  <div className="mb-4">
                    <div className="text-gray-800 mb-2">
                      {formatDate(order.createdDate)}
                    </div>
                    <div className="text-gray-800 mb-1">
                      주소: {order.address}
                    </div>
                    <div className="text-gray-800 mb-1">
                      주문 상태: {getOrderStatusText(order.orderStatus)}
                    </div>
                    <div className="text-gray-800 mb-4">
                      배송 상태: {getDeliveryStatusText(order.deliveryStatus)}
                    </div>
                    <hr className="border-gray-300" />
                  </div>
                  {/* 주문 아이템들 */}
                  <div className="space-y-4">
                    {order.orderItems.map((item: OrderItemDto) => (
                      <div
                        key={item.id}
                        className="bg-white border border-gray-200 rounded-lg p-4 flex items-center gap-4"
                      >
                        {/* 상품 이미지 */}
                        <div className="flex-shrink-0">
                          <Image
                            src={item.productImage}
                            alt={item.productName}
                            width={80}
                            height={80}
                            className="rounded-lg object-cover"
                          />
                        </div>
                        {/* 상품 정보 */}
                        <div className="flex-1">
                          <div className="font-medium text-gray-800 mb-1">
                            {item.productName}
                          </div>
                          <div className="text-gray-600 text-sm">
                            {item.productPrice.toLocaleString()}원 / {item.orderCount}개
                          </div>
                          <div className="font-bold text-gray-800">
                            {item.totalCount.toLocaleString()}원
                          </div>
                        </div>
                        {/* 액션 버튼 */}
                        <div className="flex-shrink-0">
                          {order.orderStatus === OrderStatus.ORDERED && canCancelOrder(order.createdDate) ? (
                            <button
                              onClick={() => handleCancelOrder(order.id)}
                              className="px-4 py-2 border border-gray-400 rounded text-sm font-medium hover:bg-gray-50"
                            >
                              취소
                            </button>
                          ) : order.orderStatus === OrderStatus.CANCELLED ? (
                            <button
                              className="px-4 py-2 border border-gray-400 rounded text-sm font-medium bg-gray-100 text-gray-500 cursor-not-allowed"
                              disabled
                            >
                              삭제
                            </button>
                          ) : null}
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
} 