"use client";

import { apiFetch } from "@/lib/backend/client";
import Image from "next/image";
import Link from "next/link";
import { useEffect, useState, useRef } from "react";
import type { ProductDto } from "@/type/product";

export default function Home() {
  const [products, setProducts] = useState<ProductDto[]>([]);
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    apiFetch("/grid/products/") // 여러 상품을 반환하는 API 엔드포인트로 수정
      .then(setProducts);
  }, []);

  if (products == null) return <div>로딩중...</div>;

  // 상품 리스트 좌우 스크롤 함수
  const scrollByAmount = (amount: number) => {
    if (scrollRef.current) {
      scrollRef.current.scrollBy({ left: amount, behavior: "smooth" });
    }
  };

  return (
    <div className="bg-white flex flex-col gap-30 w-full max-w-7xl mx-auto px-4 sm:px-8 text-black" style={{ paddingBottom: 64 }}>
      {/* 메인 섹션 */}
      <section className="flex flex-col md:flex-row items-center justify-between gap-12" style={{ marginTop: 128 }}>
        {/* 왼쪽: 타이틀 */}
        <div className="flex-1 flex flex-col gap-12 items-start">
          <h1 className="text-5xl sm:text-6xl font-extrabold leading-tight text-black">
            Grid & Circle
          </h1>
          <h2 className="text-3xl sm:text-4xl font-bold text-black mt-2">
            신선함의<br />기준을 바꾸다.
          </h2>
          <div className="flex gap-16 mt-8">
            <div className="flex flex-col items-center">
              <span className="text-3xl font-bold text-black">50+</span>
              <span className="text-lg text-gray-800">Plant Species</span>
            </div>
            <div className="w-px bg-gray-300 h-10 self-center" />
            <div className="flex flex-col items-center">
              <span className="text-3xl font-bold text-black">100+</span>
              <span className="text-lg text-gray-800">Customers</span>
            </div>
          </div>
        </div>
        {/* 오른쪽: 메인 이미지 */}
        <div className="flex-1 flex justify-center">
          <Image
            src="https://i.postimg.cc/brwfdft4/Chat-GPT-Image.png"
            alt="커피 beans"
            width={450}
            height={300}
            className="rounded-2xl w-[500px] h-[350px] bg-white"
            style={{ objectFit: "contain", backgroundColor: "#fff" }} // objectFit: "contain" 스타일이 적용되어 이미지의 위아래가 잘리지 않고, 그림 전체가 보이도록 수정
            priority
          />
        </div>
      </section>

      {/* 상품 리스트 섹션 */}
      <section id="products" className="w-full text-black mt-16 relative">
        <h2 className="text-4xl font-extrabold mb-10 text-black text-center tracking-tight">PRODUCTS</h2>
        <div className="relative">
          {/* 좌측 스크롤 버튼 */}
          <button
            className="absolute left-0 top-1/2 -translate-y-1/2 z-10 bg-white/80 rounded-full shadow px-3 py-2 text-3xl font-bold hover:bg-yellow-100 transition"
            style={{ left: -32 }}
            onClick={() => scrollByAmount(-350)}
            aria-label="왼쪽으로 스크롤"
          >
            〈
          </button>
          {/* 우측 스크롤 버튼 */}
          <button
            className="absolute right-0 top-1/2 -translate-y-1/2 z-10 bg-white/80 rounded-full shadow px-3 py-2 text-3xl font-bold hover:bg-yellow-100 transition"
            style={{ right: -32 }}
            onClick={() => scrollByAmount(350)}
            aria-label="오른쪽으로 스크롤"
          >
            〉
          </button>
          <div
            className="overflow-x-auto overflow-y-hidden scrollbar-hide"
            ref={scrollRef}
          >
            <div className="flex flex-nowrap gap-12 py-4">
              {products.map((product) => {
                // productImage를 배열로 변환
                const imageArray = product.productImage.split("|");
                return (
                  <Link
                    key={product.id}
                    href={`grid/products/${product.id}`}
                    className="flex flex-col items-center w-72 cursor-pointer hover:scale-105 transition-transform flex-shrink-0" // 마우스를 올리면 확대 효과
                  >
                    <Image
                      src={imageArray[0]}
                      alt={product.productName}
                      width={300}
                      height={200}
                      className="rounded-lg object-cover mb-6 w-[300px] h-[200px]"
                    />
                    <div className="font-bold text-xl text-black mb-2">
                      {product.productName}
                    </div>
                    <div className="text-xl text-gray-800 font-semibold">
                      ₩ {product.price.toLocaleString()}
                    </div>
                  </Link>
                );
              })}
            </div>
          </div>
        </div>
      </section>

      {/* About Us(회사 소개) 섹션 */}
      <section
        id="about"
        className="w-full flex flex-col items-center mt-12 text-black"
      >
        <h2 className="text-4xl font-extrabold mb-6 text-black text-center tracking-tight">About us</h2>
        <div className="text-xl text-gray-800 mb-10 text-center">
          지금 주문하고, 집에서 편하게 신선함을 경험하세요!
        </div>
        <div className="flex flex-col md:flex-row gap-12 w-full justify-center">
          {/* 특징 1 */}
          <div className="flex flex-col items-center flex-1 min-w-[200px]">
            <div className="bg-[#a98c6c] text-white rounded-full w-20 h-20 flex items-center justify-center text-4xl mb-6">
              🌱
            </div>
            <div className="font-bold mb-2 text-black text-2xl">다양한 상품 구성</div>
            <div className="text-center text-gray-800 text-lg">
              엄선된 인기 상품을 한 곳에서 만나보세요.
            </div>
          </div>
          {/* 특징 2 */}
          <div className="flex flex-col items-center flex-1 min-w-[200px]">
            <div className="bg-[#a98c6c] text-white rounded-full w-20 h-20 flex items-center justify-center text-4xl mb-6">
              🚚
            </div>
            <div className="font-bold mb-2 text-black text-2xl">빠르고 무료인 배송</div>
            <div className="text-center text-gray-800 text-lg">
              전국 어디든 빠르고 안전하게, 무료로 배송해드립니다.
            </div>
          </div>
          {/* 특징 3 */}
          <div className="flex flex-col items-center flex-1 min-w-[200px]">
            <div className="bg-[#a98c6c] text-white rounded-full w-20 h-20 flex items-center justify-center text-4xl mb-6">
              📞
            </div>
            <div className="font-bold mb-2 text-black text-2xl">24시간 고객 지원</div>
            <div className="text-center text-gray-800 text-lg">
              언제든 궁금한 점이 있다면, 연중무휴 실시간 상담으로 도와드립니다.
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}