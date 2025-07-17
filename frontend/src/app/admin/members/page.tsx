// src/app/admin/users/page.tsx
"use client";
import { useEffect, useState } from "react";

export default function AdminUsersPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 실제 백엔드 API 주소로 변경 필요
    fetch("http://localhost:8080/grid/admin/members", {
      headers: {
        Authorization:
          "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQURNSU4iLCJuYW1lIjoiYWRtaW4iLCJlbWFpbCI6ImFkbWluMTJAZ21haWwuY29tIiwiaWQiOjYsImlhdCI6MTc1MjczODA2MSwiZXhwIjoxNzUyNzM5MjYxfQ.Q4Jk9pC-o7WJ6SX3xYQUjHUtYfnQ_fh9MHFxjdUfGbt3vBGaUck-jEU9zllCBvxdUH4KgLtG2DkwRuyuweonuA",
      },
    })
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
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
              <th className="py-2 px-4">ID</th>
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
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
