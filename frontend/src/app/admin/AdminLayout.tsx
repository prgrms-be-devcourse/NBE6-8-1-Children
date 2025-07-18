// src/app/admin/AdminLayout.tsx
"use client";
import Link from "next/link";
import { usePathname } from "next/navigation";

const navItems = [
  { name: "상품 관리", href: "/admin/products" },
  { name: "회원 관리", href: "/admin/members" },
  // 필요시 추가
];

export default function AdminLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const pathname = usePathname();
  return (
    <div className="flex min-h-screen">
      <aside className="w-56 bg-gray-800 text-white flex flex-col py-8 px-4">
        <h2 className="text-xl font-bold mb-8">관리자 메뉴</h2>
        <nav className="flex flex-col gap-2">
          {navItems.map((item) => (
            <Link
              key={item.href}
              href={item.href}
              className={`py-2 px-3 rounded ${
                pathname.startsWith(item.href)
                  ? "bg-gray-700 font-bold"
                  : "hover:bg-gray-700"
              }`}
            >
              {item.name}
            </Link>
          ))}
        </nav>
      </aside>
      <main className="flex-1 bg-gray-50">{children}</main>
    </div>
  );
}
