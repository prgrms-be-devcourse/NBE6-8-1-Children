"use client";

import Link from "next/link";
import { useAuthContext } from "@/global/auth/hooks/useAuth";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function Header() {
  const router = useRouter();
  const auth = useAuthContext();
  const [loggedIn, setLoggedIn] = useState(auth.isLoggedIn);
  useEffect(() => {
    setLoggedIn(auth.isLoggedIn);
  }, [auth.isLoggedIn]);

  console.log("auth : ", auth);
  const handleLogout = () => {
    auth.logout(() => {
      window.location.href = "/"; // 이게 가장 확실함
    });
  };

  const handleLogin = () => {
    router.push("/grid/login");
  };

  return (
    // Header
    <header className="w-full flex items-center justify-between px-12 py-6 border-b border-gray-200">
      {/* 로고 영역 */}
      <div className="font-extrabold text-3xl">Grid & Circle</div>
      {/* 네비게이션 메뉴 */}
      <nav className="flex gap-10 text-xl font-bold">
        <Link href="/" className="text-black hover:font-extrabold">
          Home
        </Link>
        <Link href="/#products" className="text-gray-500 hover:text-black">
          Products
        </Link>
        <Link href="/#about" className="text-gray-500 hover:text-black">
          Contacts
        </Link>
      </nav>
      {/* 액션 버튼 영역 (로그인, 로그아웃, 장바구니, 주문, 이름) */}
      <div className="flex items-center gap-3">
        {/* 회원가입 버튼 (비로그인 시) */}
        {!auth.isLoggedIn && (
          <Link href="/grid/signup">
            <button className="px-4 py-1 border rounded-full text-sm font-medium border-gray-400 hover:bg-gray-100">
              signup
            </button>
          </Link>
        )}
        {/* 로그인/로그아웃 버튼 */}
        <button
          onClick={auth.isLoggedIn ? handleLogout : handleLogin}
          className="px-4 py-1 rounded-full text-sm font-medium bg-black text-white hover:bg-gray-800"
        >
          {auth.isLoggedIn ? "logout" : "login"}
        </button>
        {/* 장바구니 버튼 */}
        <span
          className="ml-3 text-xl cursor-pointer"
          title="Cart"
          onClick={() => {
            if (!auth.isLoggedIn) {
              router.push("/grid/login");
            } else {
              router.push("/grid/shoppingbasket");
            }
          }}
        >
          🛒
        </span>
        {/* 주문 버튼 */}
        <span
          className="ml-3 text-2xl cursor-pointer"
          title="Order"
          onClick={() => {
            if (!auth.isLoggedIn) {
              router.push("/grid/login");
            } else {
              router.push("/grid/findorders");
            }
          }}
        >
          📦
        </span>
        {/* 이름 부분 */}
        <span className="ml-2 text-lg font-medium flex items-center h-6">
          {auth.name}
        </span>
      </div>
    </header>
  );
}
