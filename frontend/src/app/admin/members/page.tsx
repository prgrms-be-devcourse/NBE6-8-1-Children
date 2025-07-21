// src/app/admin/users/page.tsx
"use client";
import { useEffect, useState } from "react";

interface User {
  id: number;
  name: string;
  email: string;
  address: string;
}

export default function AdminUsersPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 실제 백엔드 API 주소로 변경 필요
    fetch("http://localhost:8080/grid/admin/members", {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => {
        setUsers(data);
        setLoading(false);
      });
  }, []);

  return (
    <div className="w-full max-w-3xl mx-auto mt-10">
      <h2 className="text-2xl font-bold mb-6">유저 관리</h2>
      {loading ? (
        <div>로딩 중...</div>
      ) : (
        <table className="w-full bg-white rounded shadow">
          <thead>
            <tr className="bg-gray-100">
              <th className="py-2 px-4 text-left">ID</th>
              <th className="py-2 px-4 text-left">이름</th>
              <th className="py-2 px-4 text-left">이메일</th>
              <th className="py-2 px-4 text-left">주소</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className="border-t">
                <td className="py-2 px-4 text-left">{user.id}</td>
                <td className="py-2 px-4 text-left">{user.name}</td>
                <td className="py-2 px-4 text-left">{user.email}</td>
                <td className="py-2 px-4 text-left">{user.address}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
