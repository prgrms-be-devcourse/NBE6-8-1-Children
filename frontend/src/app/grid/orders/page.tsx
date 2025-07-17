'use client';

import React, { useEffect, useState } from 'react';
import { apiFetch } from "@/lib/backend/client";
import { useRouter } from 'next/navigation';

interface OrderPageItem {
  productId: number;
  productName: string;
  productPrice: number;
  orderCount: number;
  productImage: string; 
}

interface OrderPageResponse {
  address: string;
  items: OrderPageItem[];
}

export default function OrderPage() {
  const [address, setAddress] = useState('');
  const [items, setItems] = useState<OrderPageItem[]>([]);
  const [isEditingAddress, setIsEditingAddress] = useState(false);
  const [showPopup, setShowPopup] = useState(false);
  const router = useRouter();

  // 주문페이지 진입 시 서버에서 데이터 받아오기
  useEffect(() => {
    apiFetch('/grid/orders/basket/me') // 해당 API로 서버에 GET요청. memberId는 토큰?으로 추출(Rq에 getActor()로)
      .then((res: OrderPageResponse) => {
        setAddress(res.address); //서버가 보낸 응답데이터가 담긴 res.data에서 주소 추출
        setItems(res.items); // 서버가 보낸 응답데이터에서 items 추출(커피관련정보)
      })
      .catch(err => alert('주문 데이터 불러오기 실패: ' + err.message));
  }, []);

  // 총 금액 계산
  const getTotal = () => items.reduce((sum, item) => sum + item.productPrice * item.orderCount, 0);

  // 결제하기 버튼 클릭 시 서버로 POST 요청될 주문페이지의 모든 데이터들
  const handleOrder = () => {
    const orderPayload = { // 서버로 보낼 orderPayload 객체 생성
      totalPrice: getTotal(), // 총 금액
      address, // 주소
      orderStatus: 'ORDERED', // 주문상태
      deliveryStatus: false, // 배송상태
      orderItems: items.map(item => ({ // 주문 상품 목록
        productId: item.productId, // 상품ID
        productName: item.productName, // 상품명
        orderCount: item.orderCount, // 주문 수량
        productPrice: item.productPrice, // 상품 가격
        productImage: item.productImage // 상품 이미지
      }))
    };

    // 서버의 /order 엔드포인트로 orderPayload 객체를 POST 요청
    apiFetch('/grid/orders/basket/me/order', {
      method: "POST",
      body: JSON.stringify(orderPayload),
      headers: {
        "Content-Type": "application/json"
      }
    })
      .then(() => {
        setShowPopup(true); // 서버에서 응답이 오면(성공하면) 팝업창 띄우기
      })
      .catch(err => alert('주문 실패: ' + err.message));  // 서버에서 에러나면 에러메시지 띄우기
  };

  // 결제하기 버튼 눌러서 주문 완료되면 '결제완료' 팝업이 뜨고, 팝업 닫으면 메인화면으로 이동
  const handleClosePopup = () => {
    setShowPopup(false);
    router.push('/'); // 메인화면으로 이동
  };

  return (
    <div style={{ background: '#f7f7fb', minHeight: '100vh', padding: '0 0 3rem 0' }}>
      <div style={{ maxWidth: 900, margin: '0 auto', background: '#f4f4fa', borderRadius: 12, boxShadow: '0 0 10px rgba(0,0,0,0.07)', padding: '2.5rem 2.5rem 2rem 2.5rem' }}>
        <div style={{ fontWeight: 'bold', fontSize: '1.2rem', marginBottom: '1.5rem', borderBottom: '2px solid #e5e5ee', paddingBottom: '1rem' }}>
          주문 요약
        </div>
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '2rem' }}>
          <div style={{ width: 500, background: 'white', border: '1.5px solid #222', borderRadius: 6, boxShadow: '0 2px 6px rgba(0,0,0,0.07)' }}>
            {items.map(item => (
              <div key={item.productId} style={{ display: 'flex', alignItems: 'center', borderBottom: '1px solid #e5e5ee', padding: '0.7rem 1.2rem', minHeight: 60 }}>
                <img
                  src={item.productImage || '/globe.svg'}
                  alt={item.productName}
                  style={{ width: 48, height: 48, objectFit: 'cover', borderRadius: 6, border: '1px solid #eee', background: '#fafafa', marginRight: 18 }}
                />
                <div style={{ flex: 1, fontWeight: 600, fontSize: '1.05rem' }}>{item.productName}</div>
                <div style={{ fontWeight: 600, marginRight: 16 }}>{item.productPrice.toLocaleString()}원</div>
                <span style={{ background: '#333', color: 'white', padding: '2px 12px', borderRadius: '12px', fontSize: '0.93rem', fontWeight: 500 }}>
                  {item.orderCount}개
                </span>
              </div>
            ))}
          </div>
        </div>
        <div style={{ margin: '2rem 0 1.2rem 0', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <div style={{ width: 500 }}>
            <div style={{ fontWeight: 500, marginBottom: 5, textAlign: 'right' }}>주소</div>
            <input
              type="text"
              value={address}
              onChange={e => setAddress(e.target.value)}
              style={{ width: '100%', padding: '0.7rem', border: '1.5px solid #ccc', borderRadius: '6px', fontSize: '1rem', marginBottom: 0 }}
            />
          </div>
        </div>
        <div style={{ width: 500, margin: '0 auto', marginTop: '2rem' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontWeight: 'bold', fontSize: '1.15rem', marginBottom: 12 }}>
            <div>총금액</div>
            <div>{getTotal().toLocaleString()}원</div>
          </div>
          <button
            onClick={handleOrder}
            style={{ width: '100%', marginTop: '1.3rem', padding: '0.9rem', background: 'black', color: 'white', border: 'none', borderRadius: '6px', fontWeight: 600, fontSize: '1.07rem', letterSpacing: 1 }}
          >
            결제하기
          </button>
        </div>
      </div>
      {showPopup && (
        <div style={{
          position: 'fixed', top: 0, left: 0, width: '100vw', height: '100vh',
          background: 'rgba(0,0,0,0.2)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000
        }}>
          <div style={{
            background: 'white', padding: '2rem 3rem', borderRadius: 12, boxShadow: '0 2px 16px rgba(0,0,0,0.15)',
            fontSize: '1.2rem', fontWeight: 600
          }}>
            주문이 완료되었습니다.
            <button
              onClick={handleClosePopup}
              style={{ marginLeft: 20, padding: '0.5rem 1.2rem', borderRadius: 6, border: 'none', background: '#222', color: 'white', fontWeight: 500 }}
            >
              닫기
            </button>
          </div>
        </div>
      )}
    </div>
  );
}


