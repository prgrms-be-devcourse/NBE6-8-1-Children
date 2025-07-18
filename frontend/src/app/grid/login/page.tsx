"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { useAuthContext } from "@/global/auth/hooks/useAuth";

export default function LoginPage() {
  const router = useRouter();
  const auth = useAuthContext();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [keepLoggedIn, setKeepLoggedIn] = useState(false);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const isValidEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validateField = (name: string, value: string) => {
    let error = "";
    switch (name) {
      case "email":
        if (!value) {
          error = "이메일을 입력해주세요.";
        } else if (!isValidEmail(value)) {
          error = "유효한 이메일 형식이 아닙니다.";
        }
        break;
      case "password":
        if (!value) {
          error = "비밀번호를 입력해주세요.";
        }
        break;
    }
    return error;
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    const error = validateField(name, value);
    setErrors((prev) => ({
      ...prev,
      [name]: error,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/grid/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
        }),
        credentials: "include", // 필요한 경우
      });

      if (!response.ok) {
        throw new Error("로그인 실패");
      }

      const result = await response.json();
      const name = result.data.item.name;
      const role = result.data.item.role;
      const accessToken = result.data.accessToken;

      // ✅ 로그인 상태 저장 (이름 + 로그인 여부)
      localStorage.setItem("isLoggedIn", "true");
      localStorage.setItem("name", name);
      localStorage.setItem("role", role);

      // ✅ accessToken 저장 방식: 유지 여부에 따라 다르게
      if (keepLoggedIn) {
        localStorage.setItem("accessToken", accessToken);
      } else {
        sessionStorage.setItem("accessToken", accessToken);
      }
      auth.login(name, role); // result.token이 실제 토큰 필드명인지 확인 필요
      console.log("로그인 성공:", result);
    } catch (err) {
      console.error("로그인 실패:", err);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex justify-center items-center py-12">
      <div className="max-w-md w-full">
        <div className="bg-white rounded-lg shadow-md p-8 border border-solid border-gray-200">
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* 이메일 */}
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                이메일
              </label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                className={`w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.email ? "border-red-500" : "border-gray-300"
                }`}
                placeholder="이메일을 입력하세요"
              />
              {errors.email && (
                <p className="mt-1 text-sm text-red-600">{errors.email}</p>
              )}
            </div>

            {/* 비밀번호 */}
            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                비밀번호
              </label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                className={`w-full px-3 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.password ? "border-red-500" : "border-gray-300"
                }`}
                placeholder="비밀번호를 입력하세요"
              />
              {errors.password && (
                <p className="mt-1 text-sm text-red-600">{errors.password}</p>
              )}
            </div>

            {/* 로그인 상태 유지 */}
            <div className="flex items-center">
              <input
                type="checkbox"
                id="keepLoggedIn"
                checked={keepLoggedIn}
                onChange={(e) => setKeepLoggedIn(e.target.checked)}
                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              />
              <label
                htmlFor="keepLoggedIn"
                className="ml-2 block text-sm text-gray-900"
              >
                로그인 상태 유지
              </label>
            </div>

            {/* 로그인 버튼 */}
            <button
              type="submit"
              className="w-full bg-black text-white py-3 rounded-md font-medium hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 transition-colors"
            >
              로그인
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
