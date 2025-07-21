"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

// 백엔드 DTO에 맞춘 타입 정의
type BasketItem = {
  id: number; // ShoppingBasket PK
  productName: string;
  productImage: string;
  productCount: number;
  productPrice: number;
};

export default function ShoppingBasketPage() {
  const [basket, setBasket] = useState<BasketItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const router = useRouter();

  // 장바구니 페이지에 서버에서 데이터 불러오기
  useEffect(() => {
    fetch(`http://localhost:8080/grid/shoppingbasket`, {
      credentials: "include"
    })
      .then(res => {
        if (!res.ok) throw new Error("장바구니 데이터를 불러오지 못했습니다.");
        return res.json();
      })
      .then(data => {
        setBasket(data); // 받아온 장바구니 배열을 상태에 저장
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <div>로딩중...</div>;
  if (error) return <div>에러: {error}</div>;

  // 장바구니 총액 계산
  const total = basket.reduce((sum, item) => sum + item.productPrice * item.productCount, 0);

  // 삭제 기능
  const handleDelete = (id: number) => {
    fetch(`http://localhost:8080/grid/shoppingbasket/${id}`, {
      method: "DELETE",
      credentials: "include"
    })

    .then(res => {
      if(!res.ok) throw new Error("삭제 실패");
      // 삭제 후, 화면 갱신
      setBasket(basket => basket.filter(item => item.id !== id))
    })
    .catch(err => alert(err.message))
  };

  return (
    <div className="min-h-screen bg-white flex flex-col items-center justify-center">
      <h1 className="text-4xl font-bold mb-10">장바구니</h1>
      <div className="w-full max-w-3xl rounded-2xl p-5 bg-[#f5e7d4]">
        {/* 장바구니 상품 목록, 스크롤 적용 */}
        <div className="max-h-[600px] overflow-y-auto p-3">
        {basket.map((item, idx) => (
          <div
            key={idx}
            className="flex items-center justify-between mb-5 border border-gray-300 rounded-xl bg-white p-3 shadow-sm"
          >
            <div className="flex items-center gap-6">
              <img src={item.productImage} alt={item.productName} className="w-24 h-24 rounded-lg object-cover" />
              <div>
                <div className="text-lg font-semibold mb-1">{item.productName}</div>
                <div className="text-xl font-bold mb-1">{(item.productPrice * item.productCount).toLocaleString()}원</div>
                <div className="border px-3 py-1 rounded text-sm inline-block">총 수량 : {item.productCount}개</div>
              </div>
            </div>
            {/* 삭제 버튼 */}
            <button
              className="text-2xl text-gray-400 hover:text-black font-bold"
              onClick={() => handleDelete(item.id)}
              >
            ×    
            </button>
          </div>
        ))}
        </div>
        {/* 총 가격 */}
        <hr className="my-8" />
        <div className="flex justify-between items-center text-xl font-bold mb-8">
          <span>Total</span>
          <span>{total.toLocaleString()}원</span>
        </div>
        <button className="w-full py-4 bg-black hover:bg-gray-800 text-white text-lg font-bold rounded transition"
                onClick={() => {router.push('/grid/orders');}}
        >
          결제하러 가기
        </button>       
      </div>
    </div>
  );
}