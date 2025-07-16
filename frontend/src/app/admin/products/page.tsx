<<<<<<< HEAD
// src/app/admin/products/page.tsx
"use client";
import { useEffect, useState } from "react";

export default function AdminProductsPage() {
  const [products, setProducts] = useState([]);
=======
"use client";
import { useEffect, useState } from "react";

export default function AdminUsersPage() {
  const [users, setUsers] = useState([]);
>>>>>>> 33bb721 (feat: 백오피스 페이지 UI 구현)
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 실제 백엔드 API 주소로 변경 필요
<<<<<<< HEAD
    fetch("http://localhost:8080/grid/admin/products")
      .then((res) => res.json())
      .then((data) => {
        setProducts(data);
=======
    fetch("http://localhost:8080/api/admin/users")
      .then((res) => res.json())
      .then((data) => {
        setUsers(data);
>>>>>>> 33bb721 (feat: 백오피스 페이지 UI 구현)
        setLoading(false);
      });
  }, []);

  return (
<<<<<<< HEAD
    <div className="w-full max-w-4xl mx-auto mt-10">
      <h2 className="text-2xl font-bold mb-6">상품 관리</h2>
=======
    <div className="w-full max-w-3xl mx-auto mt-10">
      <h2 className="text-2xl font-bold mb-6">유저 관리</h2>
>>>>>>> 33bb721 (feat: 백오피스 페이지 UI 구현)
      {loading ? (
        <div>로딩 중...</div>
      ) : (
        <table className="w-full bg-white rounded shadow">
          <thead>
            <tr className="bg-gray-100">
              <th className="py-2 px-4">ID</th>
<<<<<<< HEAD
              <th className="py-2 px-4">상품명</th>
              <th className="py-2 px-4">가격</th>
              <th className="py-2 px-4">재고</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.id} className="border-t">
                <td className="py-2 px-4">{product.id}</td>
                <td className="py-2 px-4">{product.name}</td>
                <td className="py-2 px-4">{product.price}</td>
                <td className="py-2 px-4">{product.stock}</td>
=======
              <th className="py-2 px-4">이름</th>
              <th className="py-2 px-4">이메일</th>
              <th className="py-2 px-4">권한</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className="border-t">
                <td className="py-2 px-4">{user.id}</td>
                <td className="py-2 px-4">{user.name}</td>
                <td className="py-2 px-4">{user.email}</td>
                <td className="py-2 px-4">{user.role}</td>
>>>>>>> 33bb721 (feat: 백오피스 페이지 UI 구현)
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
