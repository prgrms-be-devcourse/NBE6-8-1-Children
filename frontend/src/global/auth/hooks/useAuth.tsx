'use client';

import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useRouter } from 'next/navigation';

interface AuthContextType {
  isLoggedIn: boolean;
  login: (cb?: () => void) => void;
  logout: (cb?: () => void) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const router = useRouter();

  // ✅ 새로고침 시 로그인 상태 유지
  useEffect(() => {
    const stored = localStorage.getItem('isLoggedIn');
    // console.log('초기 로그인 상태 (localStorage):', stored); // ✅ 확인용

    if (stored === 'true') setIsLoggedIn(true);
  }, []);

  // ✅ 로그인 시 localStorage 저장
  const login = (cb?: () => void) => {
    setIsLoggedIn(true);
    localStorage.setItem('isLoggedIn', 'true');
    cb && cb();
  };

  // ✅ 로그아웃 시 localStorage 제거
  const logout = async (cb?: () => void) => {
    try {
      await fetch('http://localhost:8080/grid/logout', {
        method: 'POST',
        credentials: 'include',
      });
      setIsLoggedIn(false);
      localStorage.removeItem('isLoggedIn');
      cb && cb();
      alert('로그아웃 되었습니다.');
    } catch (e) {
      alert('로그아웃 실패');
    }
  };

  return (
    <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuthContext() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuthContext는 AuthProvider 안에서만 사용해야 합니다.');
  }
  return context;
}