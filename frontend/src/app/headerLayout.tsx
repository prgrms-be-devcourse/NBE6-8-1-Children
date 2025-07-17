'use client';

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

  // console.log("auth : "+auth.isLoggedIn);
  const handleLogout = () => {
    auth.logout(() => {
      window.location.href = "/";  // 이게 가장 확실함
    });
  };

  const handleLogin = () => {
    router.push("/grid/login");
  };

  return (
    // Header
    <header className="w-full flex items-center justify-between px-12 py-6 border-b border-gray-200">
      {/* Logo */}
      <div className="font-extrabold text-2xl">Grid & Circle</div>
      {/* Navigation */}
      <nav className="flex gap-8 text-base font-medium">
        <Link href="/" className="text-black hover:font-bold">
        Home
        </Link>
        <Link href="/#products" className="text-gray-500 hover:text-black">
        Products
        </Link>
        <Link href="/#about" className="text-gray-500 hover:text-black">
        Contacts
        </Link>
    </nav>
    {/* Actions */}
    <div className="flex items-center gap-4">
    <button
          onClick={auth.isLoggedIn ? handleLogout : handleLogin}
          className="px-4 py-1 rounded-full text-sm font-medium bg-black text-white hover:bg-gray-800"
        >
          {auth.isLoggedIn ? "logout" : "login"}
        </button>

        <span className="ml-4 text-xl cursor-pointer" title="Cart">
        🛒
        </span>
        <span className="ml-2 text-xl cursor-pointer" title="User">
        👤
        </span>
    </div>
    </header>
  );
}