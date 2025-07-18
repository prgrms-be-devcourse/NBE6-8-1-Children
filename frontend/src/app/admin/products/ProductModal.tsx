// src/app/admin/products/ProductModal.tsx
"use client";
import { useState, useEffect } from "react";

export default function ProductModal({
  open,
  onClose,
  onSubmit,
  initialData,
}: {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: any) => void;
  initialData?: any;
}) {
  const [form, setForm] = useState({
    productName: "",
    description: "",
    productImage: "",
    price: "",
    stock: "",
  });

  useEffect(() => {
    if (initialData) {
      setForm({
        productName: initialData.productName || "",
        description: initialData.description || "",
        productImage: initialData.productImage || "",
        price: initialData.price || "",
        stock: initialData.stock || "",
      });
    } else {
      setForm({
        productName: "",
        description: "",
        productImage: "",
        price: "",
        stock: "",
      });
    }
  }, [initialData, open]);

  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
      <div className="bg-white rounded shadow-lg p-8 w-full max-w-md">
        <h2 className="text-xl font-bold mb-4">
          {initialData ? "상품 수정" : "상품 등록"}
        </h2>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            onSubmit(form);
          }}
        >
          <div className="mb-4">
            <label className="block mb-1">상품명</label>
            <input
              className="w-full border px-3 py-2 rounded"
              value={form.productName}
              onChange={(e) =>
                setForm({ ...form, productName: e.target.value })
              }
              required
            />
          </div>
          <div className="mb-4">
            <label className="block mb-1">설명</label>
            <textarea
              className="w-full border px-3 py-2 rounded"
              value={form.description}
              onChange={(e) =>
                setForm({ ...form, description: e.target.value })
              }
              required
            />
          </div>
          <div className="mb-4">
            <label className="block mb-1">이미지 (URL 또는 파일명)</label>
            <input
              className="w-full border px-3 py-2 rounded"
              value={form.productImage}
              onChange={(e) =>
                setForm({ ...form, productImage: e.target.value })
              }
              required
            />
          </div>
          <div className="mb-4">
            <label className="block mb-1">가격</label>
            <input
              type="number"
              className="w-full border px-3 py-2 rounded"
              value={form.price}
              onChange={(e) => setForm({ ...form, price: e.target.value })}
              required
            />
          </div>
          <div className="mb-6">
            <label className="block mb-1">재고</label>
            <input
              type="number"
              className="w-full border px-3 py-2 rounded"
              value={form.stock}
              onChange={(e) => setForm({ ...form, stock: e.target.value })}
              required
            />
          </div>
          <div className="flex justify-end gap-2">
            <button
              type="button"
              className="px-4 py-2 bg-gray-200 rounded"
              onClick={onClose}
            >
              취소
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded"
            >
              {initialData ? "수정" : "등록"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
