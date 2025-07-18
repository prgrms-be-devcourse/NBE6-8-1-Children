"use client";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";

type BasketItem = {
  id: number;
  name: string;
  price: number;
  image: string;
  description: string;
  quantity: number;
};

export default function ShoppingBasketPage() {
  const params = useParams();
  const memberId = params.id; // URL의 [id] 파라미터

  const [basket, setBasket] = useState<BasketItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // [1] 장바구니 데이터 fetch
    fetch(`http://localhost:8080/grid/shoppingbasket/${memberId}`)
      .then(res => {
        if (!res.ok) throw new Error("장바구니 데이터를 불러오지 못했습니다.");
        return res.json();
      })
      .then(data => {
        setBasket(data); // 백엔드에서 basket 배열 형태로 내려준다고 가정
        setLoading(false);
      })
      .catch(err => {
        setError(err.message);
        setLoading(false);
      });
  }, [memberId]);

  if (loading) return <div>로딩중...</div>;
  if (error) return <div>에러: {error}</div>;

  const total = basket.reduce((sum, item) => sum + item.price * item.quantity, 0);

  return (
    <div className="min-h-screen  bg-white flex flex-col items-center justify-center">
      <h1 className="text-4xl font-bold mb-12">장바구니</h1>
      <div className="w-full max-w-2xl">
        {basket.map((item) => (
          <div key={item.id} className="flex items-center justify-between mb-10">
            <div className="flex items-center gap-6">
              <img src={item.image} alt={item.name} className="w-24 h-24 rounded-lg object-cover" />
              <div>
                <div className="text-lg font-semibold mb-1">{item.name}</div>
                <div className="text-xl font-bold mb-1">{item.price.toLocaleString()}원</div>
                <div className="text-gray-600 mb-2">{item.description}</div>
                <div className="border px-3 py-1 rounded text-sm inline-block">총 수량 : {item.quantity}개</div>
              </div>
            </div>
            <button className="text-2xl text-gray-400 hover:text-black font-bold">×</button>
          </div>
        ))}
        <hr className="my-8" />
        <div className="flex justify-between items-center text-xl font-bold mb-8">
          <span>Total</span>
          <span>{total.toLocaleString()}원</span>
        </div>
        <button className="w-full py-4 bg-yellow-600 hover:bg-yellow-700 text-white text-lg font-bold rounded transition">
          결제하러 가기
        </button>
      </div>
    </div>
  );
}
