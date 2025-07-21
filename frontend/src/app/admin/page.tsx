"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuthContext } from "@/global/auth/hooks/useAuth";

import Link from "next/link";

export default function AdminMainPage() {
  const { isLoggedIn, role } = useAuthContext();
  const router = useRouter();
  const [showDenied, setShowDenied] = useState(false);

  useEffect(() => {
    console.log("auth 상태:", { isLoggedIn, role });
    let timer;
    if (!isLoggedIn || role !== "ADMIN") {
      setShowDenied(true);
      timer = setTimeout(() => {
        router.replace("/");
      }, 2000);
    } else if (isLoggedIn && role == "ADMIN") {
      setShowDenied(false);
    }
    return () => {
      if (timer) clearTimeout(timer);
    };
  }, [isLoggedIn, role, router]);

  if (showDenied) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gray-50">
        <div className="text-xl font-bold text-red-600">
          권한이 없는 사용자입니다.
        </div>
      </div>
    );
  }

  if (!isLoggedIn || role !== "ADMIN") {
    return null;
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50">
      <div>
        <h1 className="text-3xl font-bold text-center mb-4">백오피스 메인</h1>
        <p className="text-center mb-8">
          GridCircle 백오피스에 오신 것을 환영합니다.
          <br />
          좌측 메뉴에서 관리 기능을 선택하세요.
        </p>
        <div className="flex gap-8 justify-center">
          <div className="bg-white rounded shadow p-8 w-64 text-center">
            <div className="font-bold text-lg mb-2">유저 목록</div>
            <div className="mb-4 text-gray-500 text-sm">
              회원 목록, 권한 관리 등 유저 관련 기능을 제공합니다.
            </div>
            <a
              href="/admin/members"
              className="inline-block px-4 py-2 bg-blue-600 text-white rounded"
            >
              바로가기
            </a>
          </div>
          <div className="bg-white rounded shadow p-8 w-64 text-center">
            <div className="font-bold text-lg mb-2">상품 관리</div>
            <div className="mb-4 text-gray-500 text-sm">
              상품 등록, 수정, 삭제 등 상품 관련 기능을 제공합니다.
            </div>
            <a
              href="/admin/products"
              className="inline-block px-4 py-2 bg-blue-600 text-white rounded"
            >
              바로가기
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}
