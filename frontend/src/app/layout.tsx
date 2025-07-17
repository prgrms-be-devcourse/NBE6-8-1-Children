import type { Metadata } from "next";
import { ReactNode } from "react";

import { Geist, Geist_Mono } from "next/font/google";
import Link from "next/link";

import "./globals.css";
import { AuthProvider } from "@/global/auth/hooks/useAuth";

import HeaderLayout from "@/app/headerLayout"; 

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

export default function RootLayout({ children }: { children: ReactNode }) {
  return (
    <html lang="ko">
      <body className="min-h-screen flex flex-col bg-white font-sans">
        <AuthProvider>
        {/* Header */}
        <HeaderLayout />
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
        </AuthProvider>
      </body>
    </html>
  );
}
