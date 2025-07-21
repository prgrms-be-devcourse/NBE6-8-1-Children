"use client";

import { apiFetch } from "@/lib/backend/client";
import Image from "next/image";
import { useEffect, useState } from "react";
import { useRouter } from 'next/navigation';

enum OrderStatus {
  ORDERED = "ORDERED",
  CANCELLED = "CANCELLED"
}
interface OrderItemDto {
  id: number;
  productName: string;
  orderCount: number;
  productPrice: number;
  productImage: string;
  totalCount: number;
}
interface OrderDto {
  id: number;
  createdDate: string;
  address: string;
  totalPrice: number;
  orderStatus: OrderStatus;
  deliveryStatus: boolean; 
  orderItems: OrderItemDto[];
}

export default function FindOrdersPage() {
  const [orders, setOrders] = useState<OrderDto[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    fetchOrders();
  }, [router]);
  const fetchOrders = async () => {
    try {
      //주문내역조회페이지 들어가면 서버에 데이터 GET요청 
      const response = await apiFetch("/grid/orders/findOrder") as OrderDto[]; 
      setOrders(response);
    } catch (error: any) {
      if (
        error.message?.toLowerCase().includes("forbidden") ||
        error.status === 403 ||
        error.message?.toLowerCase().includes("unauthorized") ||
        error.status === 401
      ) {
        if (window.confirm("로그인 후 이용해주세요. 로그인 페이지로 이동합니다.")) {
          router.push('/grid/login');
        }
        return;
      }
      if (error.message === "Failed to fetch" || error.message === "NetworkError when attempting to fetch resource.") {
        alert("네트워크 연결에 문제가 있습니다. 인터넷 상태를 확인해주세요.");
        return;
      }
      alert("주문 내역을 불러오는데 실패했습니다: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  // 각 주문 시간에 따라 주문 취소 가능 여부 결정
  const canCancelOrder = (createdDate: string): boolean => {
    const orderDate = new Date(createdDate); 
    const now = new Date();
    const cancelDeadline = new Date(orderDate);
    if (orderDate.getHours() < 14) {
      cancelDeadline.setHours(14, 0, 0, 0);
    } else {
      cancelDeadline.setDate(cancelDeadline.getDate() + 1);
      cancelDeadline.setHours(14, 0, 0, 0);
    }
    return now <= cancelDeadline;
  };
  // 사용자가 주문 취소버튼 눌렀을 때 서버로 PUT 요청
  const handleCancelOrder = async (orderId: number, createdDate: string) => {
    if (!confirm("해당 주문을 정말 취소하시겠습니까?")) { 
      return;
    }
    // 오후 2시가 지났으면 취소 불가
    if (!canCancelOrder(createdDate)) {
      alert("배송이 시작되어 주문을 취소할 수 없습니다.");
      return;
    }
    try {
      // 사용자가 주문 취소버튼 클릭하면 서버로 PUT 요청
      await apiFetch(`/grid/orders/${orderId}/cancel`, {
        method: "PUT"
      });
      await fetchOrders();
      alert("주문이 취소되었습니다."); 
    } catch (error: any) { 
      console.error("주문 취소에 실패했습니다:", error);
      // 서버에서 성공했지만 응답이 없어서 에러로 처리되는 경우
      if (error.message?.includes("Unexpected end of JSON input") || 
          error.message?.includes("JSON") ||
          error.status === 200) {
        await fetchOrders();
        alert("주문이 취소되었습니다.");
        return;
      }
      alert("주문 취소에 실패했습니다."); 
      fetchOrders(); //리렌더링
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
  // orderStatus(주문상태) 한글 텍스트로 변환
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
  // deliveryStatus(배송상태) 한글 텍스트 변환 
  const getDeliveryStatusText = (order: OrderDto): string => {
    if (order.orderStatus === OrderStatus.CANCELLED) {
      return "배송 불가";
    }
    return order.deliveryStatus ? "배송 시작" : "배송 시작 전";
  };
  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-lg">로딩중...</div>
      </div>
    );
  }
  // 주문 내역 화면 렌더링
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
                        {/* 주문 취소 버튼 - 취소된 주문이 아니면 항상 표시 */}
                        {!isCancelled && (
                          <button
                            className="border border-black px-4 py-1 rounded hover:bg-gray-100"
                            onClick={() => handleCancelOrder(order.id, order.createdDate)}
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