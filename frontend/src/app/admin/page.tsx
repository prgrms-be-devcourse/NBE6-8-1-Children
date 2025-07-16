import Link from "next/link";

export default function AdminMainPage() {
  return (
    <div className="flex flex-col items-center">
      <h1 className="text-3xl font-bold mb-4 text-gray-800">백오피스 메인</h1>
      <p className="text-gray-600 text-lg mb-8 text-center max-w-xl">
        GridCircle 백오피스에 오신 것을 환영합니다.
        <br />
        좌측 메뉴에서 관리 기능을 선택하세요.
      </p>
      <div className="grid grid-cols-2 gap-6 w-full max-w-2xl">
        <div className="bg-white rounded-lg shadow p-6 flex flex-col items-center">
          <span className="text-xl font-semibold text-gray-700 mb-2">
            유저 관리
          </span>
          <p className="text-gray-500 text-sm text-center mb-4">
            회원 목록, 권한 관리 등 유저 관련 기능을 제공합니다.
          </p>
          <Link
            href="/admin/members"
            className="mt-auto px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
          >
            바로가기
          </Link>
        </div>
        <div className="bg-white rounded-lg shadow p-6 flex flex-col items-center">
          <span className="text-xl font-semibold text-gray-700 mb-2">
            상품 관리
          </span>
          <p className="text-gray-500 text-sm text-center mb-4">
            상품 등록, 수정, 삭제 등 상품 관련 기능을 제공합니다.
          </p>
          <Link
            href="/admin/products"
            className="mt-auto px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition"
          >
            바로가기
          </Link>
        </div>
      </div>
    </div>
  );
}
