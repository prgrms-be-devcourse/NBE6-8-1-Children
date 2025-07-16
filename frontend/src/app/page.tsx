"use client";

import { apiFetch } from "@/lib/backend/client";
import Image from "next/image";
import Link from "next/link";
import { useEffect, useState } from "react";
import type { ProductDto } from "@/type/product";

export default function Home() {
  const [products, setProducts] = useState<ProductDto[]>([]);

  useEffect(() => {
    apiFetch("/grid/products/") // 여러 상품을 반환하는 API 엔드포인트로 수정
      .then(setProducts);
  }, []);

  if (products == null) return <div>로딩중...</div>;

  return (
    <div className="bg-white flex flex-col gap-24 w-full max-w-7xl mx-auto px-4 sm:px-8 text-black">
      {/* Hero Section */}
      <section className="flex flex-col md:flex-row items-center justify-between gap-12 mt-12">
        {/* Left: Title & Stats */}
        <div className="flex-1 flex flex-col gap-6 items-start">
          <h1 className="text-4xl sm:text-5xl font-extrabold leading-tight text-black">
            Grid & Circle
          </h1>
          <h2 className="text-2xl sm:text-3xl font-bold text-black mt-2">
            신선함의
            <br />
            기준을 바꾸다.
          </h2>
          <div className="flex gap-12 mt-6">
            <div className="flex flex-col items-center">
              <span className="text-2xl font-bold text-black">50+</span>
              <span className="text-sm text-gray-800">Plant Species</span>
            </div>
            <div className="w-px bg-gray-300 h-8 self-center" />
            <div className="flex flex-col items-center">
              <span className="text-2xl font-bold text-black">100+</span>
              <span className="text-sm text-gray-800">Customers</span>
            </div>
          </div>
        </div>
        {/* Right: Hero Image */}
        <div className="flex-1 flex justify-center">
          <Image
            src="/main/main_coffee.jpg" // public 폴더에 coffee.jpg 추가 필요
            alt="Coffee beans"
            width={450}
            height={260}
            className="rounded-2xl object-cover w-[420px] h-[260px]"
            priority
          />
        </div>
      </section>

      {/* Products Section */}
      <section id="products" className="w-full text-black">
        <h3 className="text-xl font-bold mb-6 text-black">PRODUCTS</h3>
        <div className="flex flex-wrap gap-8 justify-around">
          {products.map((product) => {
            // productImage를 배열로 변환
            const imageArray = product.productImage.split("|");
            return (
              <Link
                key={product.id}
                href={`grid/products/${product.id}`}
                className="flex flex-col items-center w-48 cursor-pointer hover:scale-105 transition-transform"
              >
                <Image
                  src={imageArray[0]} // 첫 번째 이미지를 사용
                  alt={product.productName}
                  width={300}
                  height={200}
                  className="rounded-lg object-cover mb-4"
                />
                <div className="font-medium text-black">
                  {product.productName}
                </div>
                <div className="text-gray-800 text-sm">
                  ₩ {product.price.toLocaleString()}
                </div>
              </Link>
            );
          })}
        </div>
      </section>

      {/* About Us Section */}
      <section
        id="about"
        className="w-full flex flex-col items-center mt-12 text-black"
      >
        <h3 className="text-xl font-bold mb-2 text-black">About us</h3>
        <div className="text-gray-800 mb-8">
          Order now and appreciate the beauty of nature
        </div>
        <div className="flex flex-col md:flex-row gap-8 w-full justify-center">
          {/* Feature 1 */}
          <div className="flex flex-col items-center flex-1 min-w-[200px]">
            <div className="bg-[#a98c6c] text-white rounded-full w-16 h-16 flex items-center justify-center text-3xl mb-4">
              🌱
            </div>
            <div className="font-bold mb-1 text-black">Large Assortment</div>
            <div className="text-center text-gray-800 text-sm">
              we offer many different types of products with fewer variations in
              each category.
            </div>
          </div>
          {/* Feature 2 */}
          <div className="flex flex-col items-center flex-1 min-w-[200px]">
            <div className="bg-[#a98c6c] text-white rounded-full w-16 h-16 flex items-center justify-center text-3xl mb-4">
              🚚
            </div>
            <div className="font-bold mb-1 text-black">
              Fast & Free Shipping
            </div>
            <div className="text-center text-gray-800 text-sm">
              4-day or less delivery time, free shipping and an expedited
              delivery option.
            </div>
          </div>
          {/* Feature 3 */}
          <div className="flex flex-col items-center flex-1 min-w-[200px]">
            <div className="bg-[#a98c6c] text-white rounded-full w-16 h-16 flex items-center justify-center text-3xl mb-4">
              📞
            </div>
            <div className="font-bold mb-1 text-black">24/7 Support</div>
            <div className="text-center text-gray-800 text-sm">
              answers to any business related inquiry 24/7 and in real-time.
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
