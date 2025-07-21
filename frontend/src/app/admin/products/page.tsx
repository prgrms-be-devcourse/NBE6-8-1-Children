// src/app/admin/products/page.tsx
"use client";
// ... 기존 import ...
import { useState, useEffect } from "react";
import ProductModal from "./ProductModal";
import type { ProductDto } from "@/type/product";

export default function AdminProductsPage() {
  const [products, setProducts] = useState<ProductDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [editProduct, setEditProduct] = useState<ProductDto | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  useEffect(() => {
    fetchProducts();
  }, []);

  function fetchProducts() {
    setLoading(true);
    fetch("http://localhost:8080/grid/admin/products", {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          // 에러 응답일 때, 메시지 파싱해서 throw
          return res.json().then((errorData) => {
            throw errorData;
          });
        }
        return res.json();
      })
      .then((data) => {
        setProducts(data);
        setLoading(false);
      })
      .catch((error) => {
        // 백엔드에서 내려준 에러 메시지 사용
        alert(`${error.resultCode} : ${error.msg}`);
        setLoading(false);
      });
  }

  function handleCreateOrEdit(
    data: Omit<ProductDto, "id" | "createdDate" | "modifiedDate"> | ProductDto
  ) {
    let method, url;
    if (editProduct) {
      // 수정
      method = "PUT";
      url = `http://localhost:8080/grid/admin/product/${editProduct.id}`;
    } else {
      // 등록
      method = "POST";
      url = "http://localhost:8080/grid/admin/createProduct";
    }
    fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          return res.json().then((errorData) => {
            throw errorData;
          });
        }
        return res.json();
      })
      .then(() => {
        setModalOpen(false);
        setEditProduct(null);
        fetchProducts();
      })
      .catch((error) => {
        alert(`${error.resultCode} : ${error.msg}`); // 백엔드 메시지 띄우기
      });
  }

  function handleDelete(id: number) {
    fetch(`http://localhost:8080/grid/admin/product/${id}`, {
      method: "DELETE",
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          return res.json().then((errorData) => {
            throw errorData;
          });
        }
        return res.json();
      })
      .then(() => {
        setDeleteId(null);
        fetchProducts();
      })
      .catch((error) => {
        alert(`${error.resultCode} : ${error.msg}`);
      });
  }

  return (
    <div className="w-full max-w-4xl mx-auto mt-10">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold">상품 관리</h2>
        <button
          className="px-4 py-2 bg-blue-600 text-white rounded"
          onClick={() => {
            setEditProduct(null);
            setModalOpen(true);
          }}
        >
          상품 등록
        </button>
      </div>
      {loading ? (
        <div>로딩 중...</div>
      ) : (
        <table className="w-full bg-white rounded shadow">
          <thead>
            <tr className="bg-gray-100">
              <th className="py-2 px-4 text-center w-16">ID</th>
              <th className="py-2 px-4 text-left">상품명</th>
              <th className="py-2 px-4 text-left">상품 설명</th>
              <th className="py-2 px-4 text-right w-32">가격</th>
              <th className="py-2 px-4 text-right w-32">재고</th>
              <th className="py-2 px-4 text-center w-32">관리</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product: ProductDto) => (
              <tr key={product.id} className="border-t">
                <td className="py-2 px-4 text-center">{product.id}</td>
                <td className="py-2 px-4 text-left">{product.productName}</td>
                <td className="py-2 px-4 text-left">{product.description}</td>
                <td className="py-2 px-4 text-right">{product.price}</td>
                <td className="py-2 px-4 text-right">{product.stock}</td>
                <td className="py-2 px-4 text-center flex gap-2">
                  <button
                    className="px-2 py-1 bg-green-500 text-white rounded"
                    onClick={() => {
                      setEditProduct(product);
                      setModalOpen(true);
                    }}
                  >
                    수정
                  </button>
                  <button
                    className="px-2 py-1 bg-red-500 text-white rounded"
                    onClick={() => setDeleteId(product.id)}
                  >
                    삭제
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {/* 등록/수정 모달 */}
      <ProductModal
        open={modalOpen}
        onClose={() => {
          setModalOpen(false);
          setEditProduct(null);
        }}
        onSubmit={handleCreateOrEdit}
        initialData={editProduct}
      />

      {/* 삭제 확인 모달 */}
      {deleteId && (
        <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
          <div className="bg-white rounded shadow-lg p-8 w-full max-w-sm">
            <div className="mb-4">정말 삭제하시겠습니까?</div>
            <div className="flex justify-end gap-2">
              <button
                className="px-4 py-2 bg-gray-200 rounded"
                onClick={() => setDeleteId(null)}
              >
                취소
              </button>
              <button
                className="px-4 py-2 bg-red-600 text-white rounded"
                onClick={() => handleDelete(deleteId)}
              >
                삭제
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
