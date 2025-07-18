"use client";

import React, {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
} from "react";
import { useRouter } from "next/navigation";

interface AuthContextType {
  isLoggedIn: boolean;
  name: string | null;
  login: (name?: string, cb?: () => void) => void;
  logout: (cb?: () => void) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [name, setName] = useState<string | null>(null);
  const router = useRouter();

  // ✅ 새로고침 시 로그인 상태 유지
  useEffect(() => {
    const stored = localStorage.getItem("isLoggedIn");
    // console.log('초기 로그인 상태 (localStorage):', stored); // ✅ 확인용
    const storedName = localStorage.getItem('name');

<<<<<<< HEAD
    if (stored === 'true') setIsLoggedIn(true);
    if (storedName) setName(storedName); // ✅ 이름 세팅
=======
    if (stored === "true") setIsLoggedIn(true);
>>>>>>> 77417f5 (유저 정보 추가 전)
  }, []);


  // ✅ 로그인 시 localStorage 저장
  const login = (name?: string, cb?: () => void) => {
    setIsLoggedIn(true);
<<<<<<< HEAD
    console.log("이름 ", name);
    if (name) {
      setName(name);
      localStorage.setItem('name', name);
    }
    localStorage.setItem('isLoggedIn', 'true');
    router.push('/');
=======
    localStorage.setItem("isLoggedIn", "true");
>>>>>>> 77417f5 (유저 정보 추가 전)
    cb && cb();
  };

  // ✅ 로그아웃 시 localStorage 제거
  const logout = async (cb?: () => void) => {
    try {
      await fetch("http://localhost:8080/grid/logout", {
        method: "POST",
        credentials: "include",
      });
      setIsLoggedIn(false);
<<<<<<< HEAD
      setName(null);
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('name');
=======
      localStorage.removeItem("isLoggedIn");
>>>>>>> 77417f5 (유저 정보 추가 전)
      cb && cb();
      alert("로그아웃 되었습니다.");
    } catch (e) {
      alert("로그아웃 실패");
    }
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, name, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuthContext() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuthContext는 AuthProvider 안에서만 사용해야 합니다.");
  }
  return context;
}
