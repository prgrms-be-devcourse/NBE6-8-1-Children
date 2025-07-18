"use client";

import { use, useEffect, useState } from "react";
import type { ProductDto } from "@/type/product";
import { useRouter } from "next/navigation";

function useProduct(id: number) {
  const [product, setProduct] = useState<ProductDto | null>(null);

  useEffect(() => {
    fetch(`http://localhost:8080/grid/products/${id}`)
      .then(res => res.json())
      .then(setProduct)
      .catch(console.error);
  }, [id]);

  return { product }; // 항상 객체 반환
}

// 썸네일 리스트 컴포넌트
function Thumbnails({
  images,
  mainImage,
  setMainImage,
}: {
  images: string[];
  mainImage: string;
  setMainImage: (img: string) => void;
}) {
  return (
    <div className="flex gap-2 justify-center mt-2">
      {images.map((img, i) => (
        <img
          key={i}
          src={img}
          alt={`썸네일${i + 1}`}
          width={60}
          height={60}
          className={`rounded border ${mainImage === img ? "border-yellow-700" : "border-gray-300"} cursor-pointer`}
          style={{ objectFit: "cover" }}
          onMouseEnter={() => setMainImage(img)}
        />
      ))}
    </div>
  );
}

export default function Page({ params }: { params: Promise<{ id: string }> }) {
  // Next.js app router에서 params는 Promise로 전달됨
  const { id: idStr } = use(params);
  const id = parseInt(idStr);
  const productState = useProduct(id);
  const memberId = 1 // 임시

  return (
    <>
      <ProductInfo productState={productState} />
    </>
  );
}

// 상품 상세 정보 및 UI를 렌더링
// 페이지 프론트 부분
function ProductInfo({ productState }: { productState: { product: ProductDto | null } }) {
  const memberId = 1; // 임시로 1, 실제론 로그인 정보에서 받아와야 함
  // 데이터 로딩 중 처리
  if (!productState || productState.product == null) return <div>로딩중...</div>;
  const { product } = productState;

  // 이미지 배열 선언
  const images = product.productImage.split("|");

  // 썸네일 이미지 배열 (첫 번째는 상품 대표 이미지, 나머지는 커피 관련 임시 이미지)
  const thumbnailArray = [
    images[0],
    images[1],
    images[2],
  ];

  // 메인 이미지 상태 (썸네일 hover 시 변경)
  const [mainImage, setMainImage] = useState(thumbnailArray[0]);
  // 수량 상태
  const [quantity, setQuantity] = useState(1);
  const [showPopup, setShowPopup] = useState(false);

  // 수량 선택 버튼 (1~9)
  const handleQuantityChange = (delta: number) => {
    setQuantity((prev) => {
      const next = prev + delta;
      if (next < 1) return 1;
      if (next > 9) return 9;
      return next;
    });
  };

  const router = useRouter();
  const currentId = Number(product.id);

  // 페이지 이동 함수
  const goToProduct = (newId: number) => {
    if (newId > 0) {
      router.push(`/grid/products/${newId}`);
    }
  };

  // 장바구니 담기 버튼 클릭 시 실행되는 함수
  const handleAddToCart = async () => {
    
    // 장바구니에 담을 데이터 준비
    const cartData = {
      memberId,
      productId: product.id,
      productName: product.productName,
      productImage: product.productImage.split("|")[0], // 대표 이미지
      orderCount: quantity,
      productPrice: product.price,
    };

    // [1] 장바구니 테이블에 데이터 저장(POST 요청)
    const res = await fetch("http://localhost:8080/grid/shoppingbasket", {
      method: "POST",
      headers: { "Content-Type": "application/json"
        // 인증이 필요하면 아래 Authorization 헤더 추가
        // "Authorization": `Bearer ${accessToken}`, 
      },
      body: JSON.stringify(cartData),
    });

    if (res.ok) {
      // [2] 팝업 띄우기
      setShowPopup(true);
    } else {
      alert("장바구니 담기에 실패했습니다.");
    }
  };


  // 페이지 프론트 부분
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="max-w-7xl w-full mx-auto p-12 bg-[#f5e7d4] rounded-2xl shadow-2xl">
        {/* 상단: 상품명 */}
        <h2 className="text-6xl font-extrabold text-center text-brown-900 mb-12 tracking-tight">
          {product.productName}
        </h2>
        <div className="flex flex-col md:flex-row gap-12 items-stretch">
          {/* 왼쪽: 메인 이미지 + 썸네일 */}
          <div className="flex flex-col items-center justify-center h-full">
            <img
              src={mainImage}
              alt={product.productName}
              width={500}
              height={500}
              className="rounded-2xl object-cover shadow-lg border border-brown-200 mb-5 aspect-square"
            />
            <div className="flex gap-4 mt-4">
              {thumbnailArray.map((img, i) => (
                <img
                  key={i}
                  src={img}
                  alt={`썸네일${i + 1}`}
                  width={150}
                  height={150}
                  className={`rounded-lg border-2 ${mainImage === img ? "border-yellow-700" : "border-gray-300"} cursor-pointer object-cover aspect-square`}
                  onMouseEnter={() => setMainImage(img)}
                />
              ))}
            </div>
          </div>
          {/* 오른쪽: 상품 정보 */}
          <div className="flex-[2] flex flex-col items-center justify-center text-center bg-white rounded-xl p-12 shadow-lg ">
            <div className="text-4xl font-bold text-yellow-800 mb-4">
              {product.price.toLocaleString()}원
            </div>
            <div className="mb-4 text-2xl text-gray-800">{product.description}</div>
            <div className="mb-4 text-xl text-gray-600">재고: {product.stock}개</div>
            {/* 수량 선택 + 장바구니 */}
            <div className="flex items-center gap-6 mt-8">
              <button onClick={() => setQuantity(q => Math.max(1, q - 1))} className="px-6 py-3 text-2xl border rounded bg-yellow-600 hover:bg-yellow-700 text-white">-</button>
              <span className="font-bold text-3xl">{quantity}</span>
              <button onClick={() => setQuantity(q => Math.min(9, q + 1))} className="px-6 py-3 text-2xl border rounded bg-yellow-600 hover:bg-yellow-700 text-white">+</button>
              {/* 장바구니 버튼 클릭 이벤트 */}
              <button
                className="ml-8 px-10 py-4 bg-yellow-600 hover:bg-yellow-700 text-white rounded-xl font-semibold shadow-lg text-2xl transition"
                onClick={handleAddToCart}
              >
                장바구니 담기
              </button>
            </div>
          </div>
        </div>
        {/* 하단: 커피 상세 설명 */}
        <div className="mt-16 bg-white/90 rounded-xl p-10 shadow text-center">
          <h3 className="text-4xl font-extrabold mb-6 text-yellow-900">커피 상세 설명</h3>
          <p className="text-2xl text-gray-800 leading-relaxed">
            <strong>원산지:</strong> 브라질 산토스 지역<br />
            <strong>풍미:</strong> 고소하고 부드러운 맛, 은은한 산미와 달콤한 초콜릿 향<br />
            <strong>로스팅:</strong> 미디엄 로스트<br />
            <strong>설명:</strong> 이 커피는 브라질의 청정 고지대에서 자란 원두만을 엄선하여, 깊고 진한 풍미와 깔끔한 뒷맛이 특징입니다.
          </p>
        </div>
        {/* 최하단: 커피 페이지 이동 */}
        <div className="flex justify-center items-center gap-8 mt-10">
          <button
            onClick={() => goToProduct(currentId - 1)}
            className="text-4xl px-6 py-2 rounded-full bg-yellow-600 hover:bg-yellow-700 transition text-white"
            aria-label="이전 커피"
            disabled={currentId <= 1}
          >
            &#8592;
          </button>
          <span className="text-xl font-bold text-gray-700">다른 커피 보기</span>
          <button
            onClick={() => goToProduct(currentId + 1)}
            className="text-4xl px-6 py-2 rounded-full bg-yellow-600 hover:bg-yellow-700 transition text-white"
            aria-label="다음 커피"
          >
            &#8594;
          </button>
        </div>
      </div>
      {/* 팝업 모달 */}
      {showPopup && (
        <div
          className="fixed inset-0 flex items-center justify-center bg-transparent z-50"
          onClick={() => setShowPopup(false)} // 바깥 클릭 시 팝업 닫기
        >
          <div
            className="bg-white rounded-xl p-8 shadow-lg flex flex-col items-center"
            onClick={e => e.stopPropagation()} // 내부 클릭 시 닫히지 않게
          >
            <div className="mb-6 text-lg font-semibold">
              상품 {quantity}개를 장바구니에 담았습니다.
            </div>
            {/* [3] 장바구니 페이지로 이동하는 버튼 */}
            <button
              onClick={() => {
                setShowPopup(false); // 팝업 닫기
                router.push(`/grid/shoppingbasket/${memberId}`); // 장바구니 페이지로 이동
              }}
              className="px-6 py-2 bg-yellow-600 text-white rounded-lg font-bold hover:bg-yellow-700"
            >
              장바구니 바로가기 &gt;
            </button>
          </div>
        </div>
      )}
    </div>
  );
}