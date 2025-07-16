import type { Metadata } from "next";

import { Geist, Geist_Mono } from "next/font/google";
import Link from "next/link";

import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "grid & Circle",
  description: "grid & Circle",
};

export default function RootLayout({ children }) {
  return (
    <html lang="ko">
      <body className="min-h-screen flex flex-col bg-white font-sans">
        {/* Header */}
        <header className="w-full flex items-center justify-between px-12 py-6 border-b border-gray-200">
          {/* Logo */}
          <div className="font-extrabold text-2xl">Grid & Circle</div>
          {/* Navigation */}
          <nav className="flex gap-8 text-base font-medium">
            <Link href="/" className="text-black hover:font-bold">
              Home
            </Link>
            <Link href="/products" className="text-gray-500 hover:text-black">
              Products
            </Link>
            <Link href="/contacts" className="text-gray-500 hover:text-black">
              Contacts
            </Link>
          </nav>
          {/* Actions */}
          <div className="flex items-center gap-4">
            <button className="px-4 py-1 border rounded-full text-sm font-medium border-gray-400 hover:bg-gray-100">
              signup
            </button>
            <button className="px-4 py-1 rounded-full text-sm font-medium bg-black text-white hover:bg-gray-800">
              login
            </button>
            <span className="ml-4 text-xl cursor-pointer" title="Cart">
              🛒
            </span>
            <span className="ml-2 text-xl cursor-pointer" title="User">
              👤
            </span>
          </div>
        </header>
        {/* Main Content */}
        <main className="flex-1 flex flex-col">{children}</main>
        {/* Footer */}
        <footer className="bg-[#a98c6c] text-white mt-12 pt-10 pb-6 px-12">
          <div className="flex flex-col md:flex-row md:justify-between gap-8">
            {/* Left Side */}
            <div className="flex-1 min-w-[220px]">
              <div className="font-extrabold text-xl mb-2">Grid & Circle</div>
              <div className="mb-2">
                신선함의
                <br />
                기준을 바꾸다.
              </div>
              <div className="flex gap-3 mb-2 mt-4">
                <span className="text-2xl cursor-pointer" title="Facebook">
                  🌐
                </span>
                <span className="text-2xl cursor-pointer" title="Instagram">
                  📸
                </span>
                <span className="text-2xl cursor-pointer" title="Twitter">
                  🐦
                </span>
              </div>
              <div className="text-xs mt-4">
                2025 all Right Reserved
                <br />
                Team Children
              </div>
            </div>
            {/* Right Side */}
            <div className="flex-1 flex flex-wrap gap-12 justify-end min-w-[300px]">
              <div>
                <div className="font-bold mb-2">Information</div>
                <div className="text-sm text-white/80">About</div>
              </div>
              <div>
                <div className="font-bold mb-2">Company</div>
                <div className="text-sm text-white/80">Community</div>
              </div>
              <div>
                <div className="font-bold mb-2">Contact</div>
                <div className="text-sm text-white/80">Getting Started</div>
              </div>
            </div>
          </div>
        </footer>
      </body>
    </html>
  );
}
