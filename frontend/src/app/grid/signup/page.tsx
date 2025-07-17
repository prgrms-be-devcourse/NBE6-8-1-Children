'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation'; 
import { useAuthContext } from "@/global/auth/hooks/useAuth";

export default function SignupPage() {
  const router = useRouter();
  const auth = useAuthContext();

  const [formData, setFormData] = useState({
    email: '',  
    password: '',
    confirmPassword: '', 
    name: '',
    address: ''});
  const [errors, setErrors] = useState<{[key: string]: string}>({});
  const [agreed, setAgreed] = useState(false);

  // 이메일 형식 검증 함수
  const isValidEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  // 실시간 유효성 검사
  const validateField = (name: string, value: string) => {
    let error = '';

    switch (name) {
      case 'email':
        if (!value) {
          error = '이메일을 입력해주세요.';
        } else if (!isValidEmail(value)) {
          error = '유효한 이메일 형식이 아닙니다.';
        }
        break;
      
      case 'password':
        if (!value) {
          error = '비밀번호를 입력해주세요.';
        } else if (value.length < 10) {
          error = '비밀번호는 10자 이상 이어야 합니다.';
        }
        break;
      
      case 'confirmPassword':
        if (!value) {
          error = '비밀번호 확인을 입력해주세요.';
        } else if (value !== formData.password) {
          error = '비밀번호가 일치하지 않습니다.';
        }
        break;
      
      case 'name':
        if (!value) {
          error = '이름을 입력해주세요.';
        } else if (value.length < 2) {
          error = '이름은 2자 이상이어야 합니다.';
        }
        break;
      
      case 'address':
        if (!value) {
          error = '주소를 입력해주세요.';
        }
        break;
    }

    return error;
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // 실시간 유효성 검사
    const error = validateField(name, value);
    setErrors(prev => ({
      ...prev,
      [name]: error
    }));

    // 비밀번호 확인 필드 특별 처리
    if (name === 'password') {
      if (formData.confirmPassword && value !== formData.confirmPassword) {
        setErrors(prev => ({
          ...prev,
          confirmPassword: '비밀번호가 일치하지 않습니다.'
        }));
      } else if (formData.confirmPassword && value === formData.confirmPassword) {
        setErrors(prev => ({
          ...prev,
          confirmPassword: ''
        }));
      }
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
  
    const newErrors: { [key: string]: string } = {};
  
    // 1. 필드 유효성 검사
    Object.keys(formData).forEach((key) => {
      const error = validateField(key, formData[key as keyof typeof formData]);
      if (error) {
        newErrors[key] = error;
      }
    });
  
    // 2. 약관 동의 체크
    if (!agreed) {
      newErrors.agreed = '약관에 동의해주세요.';
    }
  
    // 3. 에러 존재 시 중단
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
  
    // 4. 회원가입 요청
    try {
      const signupRes = await fetch('http://localhost:8080/grid/members', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
          name: formData.name,
          address: formData.address,
        }),
        credentials: 'include',
      });
  
      if (!signupRes.ok) {
        throw new Error('회원가입 실패');
      }
  
      const signupResult = await signupRes.json();
      console.log('회원가입 성공:', signupResult);
  
      // 5. 로그인 요청
      const loginRes = await fetch('http://localhost:8080/grid/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
        }),
        credentials: 'include',
      });
  
      if (!loginRes.ok) {
        throw new Error('로그인 실패');
      }
  
      const loginResult = await loginRes.json();
      console.log('로그인 성공:', loginResult);
      auth.login(() => {
        router.push('/');
      });
      alert(`${loginResult.data.item.name}님, 환영합니다!`);
  
      // ✅ 로그인 후 라우팅 (예: 메인 페이지 또는 이전 페이지로)
      // router.push('/main') 또는 router.back()
      router.push('/');
    } catch (err) {
      console.error('에러 발생:', err);
      alert('회원가입 또는 로그인에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex justify-center items-center py-12">
      <div className="max-w-md w-full">
        <div className="bg-white rounded-lg shadow-md p-8">
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* 이메일 */}
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                아이디(이메일)
              </label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.email ? 'border-red-500': 'border-gray-300'
                }`}
                placeholder="이메일을 입력하세요"
              />
              {errors.email && (
                <p className="mt-1 text-sm text-red-600">{errors.email}</p>
              )}
            </div>

            {/* 비밀번호 */}
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                비밀번호
              </label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.password ? 'border-red-500': 'border-gray-300'
                }`}
                placeholder="비밀번호를 입력하세요 (10자 이상)"
              />
              {errors.password && (
                <p className="mt-1 text-sm text-red-600">{errors.password}</p>
              )}
            </div>

            {/* 비밀번호 확인 */}
            <div>
              <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 mb-2">
                비밀번호 확인
              </label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleInputChange}
                className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.confirmPassword ? 'border-red-500': 'border-gray-300'
                }`}
                placeholder="비밀번호를 다시 입력하세요"
              />
              {errors.confirmPassword && (
                <p className="mt-1 text-sm text-red-600">{errors.confirmPassword}</p>
              )}
            </div>

            {/* 이름 */}
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-2">
                이름
              </label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.name ? 'border-red-500': 'border-gray-300'
                }`}
                placeholder="이름을 입력하세요 (2자 이상)"
              />
              {errors.name && (
                <p className="mt-1 text-sm text-red-600">{errors.name}</p>
              )}
            </div>

            {/* 주소 */}
            <div>
              <label htmlFor="address" className="block text-sm font-medium text-gray-700 mb-2">
                주소
              </label>
              <input
                type="text"
                id="address"
                name="address"
                value={formData.address}
                onChange={handleInputChange}
                className={`w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500 ${
                  errors.address ? 'border-red-500': 'border-gray-300'
                }`}
                placeholder="주소를 입력하세요"
              />
              {errors.address && (
                <p className="mt-1 text-sm text-red-600">{errors.address}</p>
              )}
            </div>

            {/* 약관 동의 */}
            <div className="space-y-3">
              <h3 className="text-lg font-medium text-gray-900">Grid & Circle 이용 약관</h3>
              <p className="text-sm text-gray-600">본인은 위 약관 내용을 확인하였습니다.</p>
              <div className="flex items-center">
                <input
                  type="checkbox"
                  id="agreed"
                  checked={agreed}
                  onChange={(e) => setAgreed(e.target.checked)}
                  className="h-4 w-4 text-blue-600 focus:ring-blue-500 focus:ring-gray-300 rounded"
                />
                <label htmlFor="agreed" className="ml-2 block text-sm text-gray-900">
                  약관에 동의합니다
                </label>
              </div>
              {errors.agreed && (
                <p className="mt-1 text-sm text-red-600">{errors.agreed}</p>
              )}
            </div>

            {/* 가입 버튼 */}
            <button
              type="submit"
              className="w-full bg-gray-800 text-white py-3-4ded-md font-medium hover:bg-gray-700 focus:outline-none focus:ring-2ocus:ring-gray-500 focus:ring-offset-2 transition-colors"
            >
              동의하고 가입하기
            </button>
          </form>
        </div>
      </div>
    </div>
  );
} 