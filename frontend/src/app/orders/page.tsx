'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';

// 상품(Product) 타입
interface Product {
  id: number;
  name: string;
  price: number;
  image?: string;
}

// 장바구니(ShoppingBasket) 타입
interface BasketItem {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  price: number;
}

// 회원(Member) 타입
interface Member {
  id: number;
  address: string;
}

export default function OrderPage() {
  // 상품 목록
  const [products, setProducts] = useState<Product[]>([]);
  // 장바구니 목록
  const [basketItems, setBasketItems] = useState<BasketItem[]>([]);
  // 주소
  const [address, setAddress] = useState('');
  // 회원 정보
  const [member, setMember] = useState<Member | null>(null);

  // 상품 목록 GET (product 테이블)
  useEffect(() => {
    // TODO: 서버 연동 시 axios.get('/api/products') 등으로 교체
    const mockProducts: Product[] = [
      { id: 1, name: '커피콩 (브라질)', price: 5000 },
      { id: 2, name: '커피콩 (에티오피아)', price: 5000 },
      { id: 3, name: '커피콩 (베트남)', price: 5000 },
      { id: 4, name: '커피콩 (케냐)', price: 5000 },
    ];
    setProducts(mockProducts);
  }, []);

  // 장바구니 목록 GET (shoppingbasket 테이블)
  useEffect(() => {
    // TODO: 서버 연동 시 axios.get('/api/shoppingbasket?memberId=...') 등으로 교체
    const mockBasket: BasketItem[] = [
      { id: 1, productId: 1, productName: '커피콩 (브라질)', quantity: 2, price: 5000 },
      { id: 2, productId: 2, productName: '커피콩 (에티오피아)', quantity: 1, price: 5000 },
      { id: 3, productId: 4, productName: '커피콩 (케냐)', quantity: 1, price: 5000 },
    ];
    setBasketItems(mockBasket);
  }, []);

  // 회원 정보 GET (member 테이블)
  useEffect(() => {
    // TODO: 서버 연동 시 axios.get('/api/member/me') 등으로 교체
    const mockMember: Member = { id: 1, address: '서울시 강남구 테헤란로 123' };
    setMember(mockMember);
    setAddress(mockMember.address);
  }, []);

  // 총 금액 계산
  const getTotal = () => basketItems.reduce((sum, item) => sum + item.price * item.quantity, 0);

  // 결제하기 버튼 클릭
  const handleOrder = async () => {
    if (!member) return;
    const now = new Date();
    const isoString = now.toISOString();
    const orderPayload = {
      memberId: member.id, // Order 테이블: member FK
      totalPrice: getTotal(),
      address,
      orderStatus: '주문 완료',
      deliveryStatus: '배송 전',
      createdDate: isoString,
      modifiedDate: isoString,
      orderItems: basketItems.map(item => ({
        productId: item.productId, // OrderItem: product FK
        productName: item.productName,
        orderCount: item.quantity,
        productPrice: item.price,
      })),
    };
    // TODO: 서버 연동 시 axios.post('/api/orders', orderPayload)
    // 임시로 alert
    alert('주문 데이터 전송!\n' + JSON.stringify(orderPayload, null, 2));
  };

  return (
    <div style={{ background: '#f7f6fb', minHeight: '100vh', padding: '0 0 3rem 0' }}>
      {/* 상단 네비게이션 */}
      <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '2rem 3rem 1.5rem 3rem' }}>
        <div style={{ fontWeight: 'bold', fontSize: '2rem' }}>Grid & Circle</div>
        <div style={{ display: 'flex', gap: '0.7rem', alignItems: 'center' }}>
          <button style={{ border: '1px solid #bbb', background: 'white', borderRadius: '6px', padding: '0.4rem 1.2rem', fontWeight: 500 }}>signup</button>
          <button style={{ border: 'none', background: '#222', color: 'white', borderRadius: '6px', padding: '0.4rem 1.2rem', fontWeight: 500 }}>login</button>
          <span style={{ fontSize: '1.3rem', marginLeft: '0.7rem', cursor: 'pointer' }}>🛒</span>
          <span style={{ fontSize: '1.3rem', marginLeft: '0.3rem', cursor: 'pointer' }}>👤</span>
        </div>
      </nav>

      {/* 본문: 좌우 2단 */}
      <div style={{ display: 'flex', justifyContent: 'space-between', maxWidth: 1100, margin: '0 auto', marginTop: '2rem' }}>
        {/* 좌측: 상품 목록 */}
        <div style={{ width: '54%', background: 'white', border: '1.5px solid #bbb', borderRadius: 6, padding: '2rem 1.5rem', minHeight: 400 }}>
          <div style={{ fontWeight: 'bold', fontSize: '1.1rem', marginBottom: '1.2rem' }}></div>
          {products.map(product => (
            <div key={product.id} style={{ display: 'flex', alignItems: 'center', borderBottom: '1px solid #e5e5ee', padding: '0.7rem 0', minHeight: 60 }}>
              <img
                src={product.image || '/globe.svg'}
                alt={product.name}
                style={{ width: 48, height: 48, objectFit: 'cover', borderRadius: 6, border: '1px solid #eee', background: '#fafafa', marginRight: 18 }}
              />
              <div style={{ flex: 1, fontWeight: 500, fontSize: '1.05rem' }}>{product.name}</div>
              <div style={{ fontWeight: 600 }}>{product.price.toLocaleString()}원</div>
            </div>
          ))}
        </div>

        {/* 우측: 주문 요약 */}
        <div style={{ width: '40%', background: '#f5f5fa', borderRadius: 12, boxShadow: '0 0 10px rgba(0,0,0,0.07)', padding: '2.2rem 2.2rem 2rem 2.2rem', minHeight: 400 }}>
          <div style={{ fontWeight: 'bold', fontSize: '1.15rem', marginBottom: '1.2rem', borderBottom: '2px solid #e5e5ee', paddingBottom: '1rem' }}>
            주문 요약
          </div>
          <div style={{ marginBottom: '1.2rem' }}>
            {basketItems.map(item => (
              <div key={item.id} style={{ fontWeight: 600, marginBottom: 7, display: 'flex', alignItems: 'center' }}>
                {item.productName}
                <span style={{ marginLeft: '0.6rem', background: '#333', color: 'white', padding: '2px 10px', borderRadius: '12px', fontSize: '0.93rem', fontWeight: 500 }}>
                  {item.quantity}개
                </span>
              </div>
            ))}
          </div>
          <div style={{ marginBottom: '1.2rem' }}>
            <div style={{ fontWeight: 500, marginBottom: 5 }}>주소</div>
            <input
              type="text"
              value={address}
              onChange={e => setAddress(e.target.value)}
              style={{ width: '100%', padding: '0.7rem', border: '1.5px solid #ccc', borderRadius: '6px', fontSize: '1rem', marginBottom: 0 }}
            />
          </div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '2rem', fontWeight: 'bold', fontSize: '1.15rem' }}>
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
    </div>
  );
}


