export default function AdminLayout({ children }) {
  return (
    <div className="min-h-screen flex bg-gray-100">
      {/* 사이드 메뉴 */}
      <aside className="w-40 bg-white border-r flex flex-col py-0 px-4 min-h-screen justify-start">
        <nav className="flex flex-col gap-2 mt-8">
          <a
            href="/admin/users"
            className="py-2 px-4 rounded hover:bg-gray-200 font-medium text-gray-700 transition"
          >
            유저 관리
          </a>
          <a
            href="/admin/products"
            className="py-2 px-4 rounded hover:bg-gray-200 font-medium text-gray-700 transition"
          >
            상품 관리
          </a>
        </nav>
      </aside>
      {/* 메인 컨텐츠 */}
      <main className="flex-1 flex flex-col items-center justify-center m-0 p-0">
        {children}
      </main>
    </div>
  );
}
