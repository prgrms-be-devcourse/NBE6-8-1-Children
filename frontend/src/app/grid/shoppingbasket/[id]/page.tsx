"use client";
import { useParams } from "next/navigation";

export default function ShoppingBasketPage() {
  const params = useParams();
  const memberId = params.id; // URL의 [id] 파라미터 (임시로 멤버ID)

  // 임시 장바구니 데이터 (실제론 fetch로 받아와야 함)
  const basket = [
    {
      id: 1,
      name: "커피 이름",
      price: 39.99,
      image: "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=400&q=80",
      description: "커피 설명",
      quantity: 1,
    },
    {
      id: 2,
      name: "커피 이름",
      price: 42.99,
      image: "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=400&q=80",
      description: "커피 설명",
      quantity: 4,
    },
  ];

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
                <div className="text-xl font-bold mb-1">{item.price.toFixed(2)} $</div>
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
          <span>{total.toFixed(2)} $</span>
        </div>
        <button className="w-full py-4 bg-yellow-600 hover:bg-yellow-700 text-white text-lg font-bold rounded transition">
          결제하러 가기
        </button>
      </div>
    </div>
  );
}
