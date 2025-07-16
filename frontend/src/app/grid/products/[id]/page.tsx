"use client";

import { apiFetch } from "@/lib/backend/client";
import type { ProductDto } from "@/type/product";
import { use, useEffect, useState } from "react";

function useProduct(id: number) {
  const [product, setProduct] = useState<ProductDto | null>(null);

  useEffect(() => {
    apiFetch(`/grid/products/${id}`)
      .then(setProduct)
      .catch((error) => {
        alert(`${error.resultCode} : ${error.msg}`);
      });
  }, [id]);

  return { product }; // 항상 객체 반환
}

 // params로 id를 받아 해당 상품 정보를 패칭
export default function Page({ params }: { params: Promise<{ id: string }> }) {
  // Next.js app router에서 params는 Promise로 전달됨
  const { id: idStr } = use(params);
  const id = parseInt(idStr);
  const productState = useProduct(id);

  return (
    <>
      <ProductInfo productState={productState} />
    </>
  );
}

// 상품 상세 정보 및 UI를 렌더링
// 페이지 프론트 부분
function ProductInfo({ productState }: { productState: { product: ProductDto | null } }) {
  // 데이터 로딩 중 처리
  if (!productState || productState.product == null) return <div>로딩중...</div>;
  const { product } = productState;

  // 썸네일 이미지 배열 (첫 번째는 상품 대표 이미지, 나머지는 커피 관련 임시 이미지)
  const thumbnails = [
    product.productImage,
    "https://images.unsplash.com/photo-1511920170033-f8396924c348?auto=format&fit=crop&w=400&q=80",
    "https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=400&q=80",
  ];

  // 메인 이미지 상태 (썸네일 hover 시 변경)
  const [mainImage, setMainImage] = useState(thumbnails[0]);
  // 수량 상태
  const [quantity, setQuantity] = useState(1);

  // 수량 선택 버튼 (1~9)
  const handleQuantityChange = (delta: number) => {
    setQuantity((prev) => {
      const next = prev + delta;
      if (next < 1) return 1;
      if (next > 9) return 9;
      return next;
    });
  };

  // 페이지 프론트 부분
  return (
    <div
      style={{
        maxWidth: 1100,
        margin: "0 auto",
        padding: "5rem 1rem 3rem 1rem",
        background: "#faf7f2",
        borderRadius: 24,
        boxShadow: "0 4px 24px #e0c9a6a0",
        fontFamily: "'Noto Sans KR', sans-serif",
        minHeight: "100vh",
      }}
    >
      {/* 상단: 상품 상세 타이틀 */}
      <h2
        style={{
          fontSize: "2.2rem",
          fontWeight: 900,
          marginBottom: "3.5rem",
          letterSpacing: "0.05em",
          color: "#6b4f27",
          textAlign: "center",
        }}
      >
        PRODUCTS DETAIL
      </h2>
      {/* 상품 이미지 + 정보 영역 */}
      <div style={{ display: "flex", gap: "3rem", alignItems: "stretch", minHeight: 420 }}>
        {/* 왼쪽: 상품 이미지 및 썸네일 */}
        <div style={{ flex: "0 0 400px", display: "flex", flexDirection: "column", justifyContent: "center" }}>
          <div
            style={{
              background: "#fff",
              borderRadius: 18,
              boxShadow: "0 2px 12px #e0c9a6a0",
              padding: 32,
              textAlign: "center",
              height: "100%",
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
            }}
          >
            {/* 메인 상품 이미지 */}
            <img
              src={mainImage}
              alt={product.productName}
              style={{
                width: 320,
                height: 320,
                borderRadius: 16,
                boxShadow: "0 2px 8px #e0c9a6a0",
                margin: "0 auto 20px auto",
                objectFit: "cover",
                display: "block",
                background: "#f5f5f5",
              }}
            />
            {/* 썸네일 리스트 */}
            <div style={{ display: "flex", gap: 12, justifyContent: "center", marginTop: 16 }}>
              {thumbnails.map((thumb, idx) => (
                <img
                  key={idx}
                  src={thumb}
                  alt={`썸네일${idx + 1}`}
                  style={{
                    width: 60,
                    height: 60,
                    borderRadius: 10,
                    border: mainImage === thumb ? "2.5px solid #bfa074" : "1px solid #eee",
                    objectFit: "cover",
                    background: "#fff",
                    cursor: "pointer",
                    transition: "border 0.2s",
                  }}
                  onMouseEnter={() => setMainImage(thumb)}
                />
              ))}
            </div>
          </div>
        </div>
        {/* 오른쪽: 상품 정보 및 장바구니 */}
        <div
          style={{
            flex: 1,
            background: "#fff",
            borderRadius: 18,
            boxShadow: "0 2px 12px #e0c9a6a0",
            padding: "2.5rem 2rem",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center", // 가운데 정렬
            textAlign: "center",  // 가운데 정렬
          }}
        >
          <div style={{ fontSize: "2rem", fontWeight: 700, marginBottom: "1.2rem", color: "#3e2723" }}>
            {product.productName}
          </div>
          <div style={{ color: "#d32f2f", fontSize: "1.7rem", fontWeight: 900, marginBottom: "1.5rem" }}>
            {product.price.toLocaleString()}원
          </div>
          <div style={{ marginBottom: "1.2rem", fontSize: "1.1rem", color: "#5d4037" }}>
            <strong>설명:</strong> {product.description}
          </div>
          <div style={{ marginBottom: "1.5rem", color: "#7b5e36" }}>
            <strong>재고:</strong> {product.stock}개
          </div>
          {/* 수량 선택 + 장바구니 버튼 */}
          <div style={{ display: "flex", alignItems: "center", gap: 16, marginTop: 24, justifyContent: "center" }}>
            <div style={{ display: "flex", alignItems: "center", gap: 4 }}>
              <button
                onClick={() => handleQuantityChange(-1)}
                style={{
                  width: 32,
                  height: 32,
                  borderRadius: 8,
                  border: "1px solid #bfa074",
                  background: "#f5e7d4",
                  color: "#6b4f27",
                  fontSize: "1.3rem",
                  fontWeight: 700,
                  cursor: "pointer",
                }}
              >-</button>
              <span style={{ minWidth: 24, textAlign: "center", fontWeight: 700, fontSize: "1.1rem" }}>{quantity}</span>
              <button
                onClick={() => handleQuantityChange(1)}
                style={{
                  width: 32,
                  height: 32,
                  borderRadius: 8,
                  border: "1px solid #bfa074",
                  background: "#f5e7d4",
                  color: "#6b4f27",
                  fontSize: "1.3rem",
                  fontWeight: 700,
                  cursor: "pointer",
                }}
              >+</button>
            </div>
            <button
              style={{
                background: "#bfa074",
                color: "#fff",
                fontWeight: 700,
                fontSize: "1.1rem",
                border: "none",
                borderRadius: 8,
                padding: "0.9rem 2.5rem",
                cursor: "pointer",
                boxShadow: "0 2px 8px #e0c9a6a0",
                transition: "background 0.2s",
              }}
            >
              장바구니 담기
            </button>
          </div>
        </div>
      </div>
      {/* 하단: 커피 상세 설명 및 원산지 사진 */}
      <div style={{
        marginTop: "4rem",
        background: "#fff",
        borderRadius: 18,
        boxShadow: "0 2px 12px #e0c9a6a0",
        padding: "2.5rem 2rem",
        minHeight: 350,
        textAlign: "center",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}>
        <h3 style={{ fontSize: "1.5rem", fontWeight: 700, color: "#6b4f27", marginBottom: 24 }}>
          커피 상세 설명
        </h3>
        {/* 커피 농장 사진 (상세 설명 위에 위치) */}
        <img
          src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80"
          alt="커피 농장"
          style={{
            width: 225,
            height: 225,
            borderRadius: 20,
            objectFit: "cover",
            boxShadow: "0 2px 8px #e0c9a6a0",
            margin: "0 auto 18px auto",
            display: "block",
          }}
        />
        <div style={{ color: "#7b5e36", fontSize: "1rem", marginBottom: 18 }}>브라질 산토스 커피 농장</div>
        <p style={{ fontSize: "1.1rem", color: "#4e342e", marginBottom: 16 }}>
          <strong>원산지:</strong> 브라질 산토스 지역<br />
          <strong>풍미:</strong> 고소하고 부드러운 맛, 은은한 산미와 달콤한 초콜릿 향<br />
          <strong>로스팅:</strong> 미디엄 로스트<br />
          <strong>설명:</strong> 이 커피는 브라질의 청정 고지대에서 자란 원두만을 엄선하여, 깊고 진한 풍미와 깔끔한 뒷맛이 특징입니다. 아침에 마시기 좋은 부드러운 커피로, 누구나 부담 없이 즐길 수 있습니다.
        </p>
      </div>
    </div>
  );
}