"use client";

import { apiFetch } from "@/lib/backend/client";
import Image from "next/image";
import { useEffect, useState } from "react";

// 주문 상태 enum
enum OrderStatus {
  ORDERED = "ORDERED",
  CANCELLED = "CANCELLED"
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
  deliveryStatus: boolean; // boolean으로 통일
  orderItems: OrderItemDto[];
}

export default function FindOrdersPage() {
  const [orders, setOrders] = useState<OrderDto[]>([]);
  const [loading, setLoading] = useState(true);

  // 주문 내역을 불러오기 위한 GET 요청청
  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      // 서버로 GET 요청
      const response = await apiFetch("/grid/orders/findOrder") as OrderDto[];
      setOrders(response);
    } catch (error) {
      console.error("주문 내역을 불러오는데 실패했습니다:", error);
    } finally {
      setLoading(false);
    }
  };

  // 전날 오후 2시 ~ 오늘 오후 2시 사이 주문만 취소 가능 (+ 오후 2시가 지나면 취소 불가)
  const canCancelOrder = (createdDate: string): boolean => {
    const orderDate = new Date(createdDate);
    const now = new Date();
    const yesterday2PM = new Date(now);
    yesterday2PM.setDate(yesterday2PM.getDate() - 1);
    yesterday2PM.setHours(14, 0, 0, 0);
    const today2PM = new Date(now);
    today2PM.setHours(14, 0, 0, 0);
    // 주문이 전날 오후 2시~오늘 오후 2시 사이에 생성됐고 지금이 오늘 오후 2시 이전이면 취소가능
    return orderDate >= yesterday2PM && orderDate <= today2PM && now <= today2PM;
  };

  // 주문 취소 처리를 위한 로직직
  const handleCancelOrder = async (orderId: number) => {
    if (!confirm("해당 주문을 정말 취소하시겠습니까?")) {
      return;
    }
    try {
      // 사용자가 주문 취소버튼 클릭하면 서버로 PUT 요청
      await apiFetch(`/grid/orders/${orderId}/cancel`, {
        method: "PUT"
      });
      await fetchOrders();
      alert("주문이 취소되었습니다."); // 주문 취소 시 팝업창 뜸. 사용자가 ok 버튼 누르면 팝업 닫히고, 갱신된 주문 내역이 화면에 보임임
    } catch (error) {
      console.error("주문 취소에 실패했습니다:", error);
      alert("주문 취소에 실패했습니다.");
    }
  };


  // 날짜 포멧 함수 (25.07.17 12:42:00 형태로 화면에 보이기 위해)
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

  // orderStatus(주문상태)를 한글 텍스트로 변환.
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

  // 배송 상태 텍스트
  const getDeliveryStatusText = (order: OrderDto): string => {
    if (order.orderStatus === OrderStatus.CANCELLED) {
      return "배송 불가";
    }
    return order.deliveryStatus ? "배송 시작" : "배송 시작 전";
  };

  // 데이터 로딩 중에는 로딩중... 이라는 메시지 표시시
  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-lg">로딩중...</div>
      </div>
    );
  }

  // 주문 내역 화면 렌더링링
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
              {orders.map((order) => {
                const isCancelled = order.orderStatus === OrderStatus.CANCELLED;
                return (
                  <div
                    key={order.id}
                    className={`rounded-lg p-6 mb-8 shadow-sm ${isCancelled ? "bg-gray-300" : "bg-[#f5f5fa]"}`}
                  >
                    <div className="flex flex-wrap justify-between items-center mb-2">
                      <div className="font-semibold text-base flex items-center gap-2">
                        {formatDate(order.createdDate)}
                        {isCancelled && (
                          <span className="ml-2 text-red-600 font-bold">취소된 주문</span>
                        )}
                      </div>
                      <div className="flex gap-8 items-center">
                        <span>
                          <b>주문 상태 :</b> {getOrderStatusText(order.orderStatus)}
                        </span>
                        <span>
                          <b>배송 상태 :</b> {getDeliveryStatusText(order)}
                        </span>
                        {/* 주문 취소 버튼 노출 조건 */}
                        {!isCancelled && canCancelOrder(order.createdDate) && (
                          <button
                            className="border border-black px-4 py-1 rounded hover:bg-gray-100"
                            onClick={() => handleCancelOrder(order.id)}
                          >
                            주문 취소
                          </button>
                        )}
                      </div>
                    </div>
                    <div className="mb-2 text-sm">주소 : {order.address}</div>
                    <div className="space-y-4">
                      {order.orderItems.map((item) => (
                        <div
                          key={item.id}
                          className="flex items-center bg-white border border-gray-300 rounded-lg p-4 gap-4"
                        >
                          <Image
                            src={item.productImage}
                            alt={item.productName}
                            width={80}
                            height={80}
                            className="rounded-lg object-cover"
                          />
                          <div className="flex-1">
                            <div className="font-bold text-lg">
                              {item.productName}
                            </div>
                            <div className="text-gray-600 text-sm">
                              {item.productPrice.toLocaleString()}원 / {item.orderCount}개
                            </div>
                            <div className="font-bold text-gray-800">
                              {(item.productPrice * item.orderCount).toLocaleString()}원
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>
    </div>
  );
} 