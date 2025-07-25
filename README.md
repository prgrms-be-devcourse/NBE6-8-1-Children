# NBE6-8-1-Children
Grid &amp; Circle 커피 원두 주문 시스템
우리는 작은 로컬 카페 Grids & Circles 입니다. 고객들이 Coffe Bean package를 온라인 웹 사이트로 주문을 합니다. 매일 전날 오후 2시부터 오늘 오후 2시까지의 주문을 모아서 처리합니다.

## 🛠️ 주요 기능

### 고객
- 회원가입 및 로그인
- 원두 상품 목록 조회
- 장바구니 담기, 수정, 삭제
- 주문 및 결제 진행
- 마이페이지를 통한 주문 내역 확인

### 관리자
- 상품 추가/수정/삭제
- 주문 현황 관리
- 사용자 관리
  
---

## ⚙️ 개발 환경

- Frontend: Next.js , TypeScript, React, Tailwinds CSS
- Backend: Spring Boot, Spring Security + JWT, Layered Architecture 
- Database: JPA/H2 Database

---

## ▶️ 실행 방법  


### 1. 클론하기
```bash
git clone https://github.com/prgrms-be-devcourse/NBE6-8-1-Children.git
cd NBE6-8-1-Children
```
### 2. 백엔드 실행

### 3. 프론트엔드 실행
```bash
cd frontend
npm install
npm run dev
```
.env 파일은 루트 디렉토리에 따로 생성해서 환경 변수 세팅이 필요합니다.  

<br><br>
  
## 💬 커밋 컨벤션 (Commit Convention)

- 커밋 태그는 **소문자**로 작성합니다.
- 예시:  
  ```
  feat: 회원가입 API 구현
  ```

### ✅ 커밋 태그 종류

| 태그       | 설명                                      |
|------------|-------------------------------------------|
| `feat`     | 새로운 기능 추가                          |
| `fix`      | 버그 수정                                 |
| `design`   | CSS 등 UI 디자인 변경                     |
| `refactor` | 리팩토링 (기능 변화 없이 코드 구조 개선)  |
| `docs`     | 문서 수정 (README 등)                     |
| `test`     | 테스트 코드 추가 또는 수정                |
| `chore`    | 빌드 설정, 패키지 매니저 설정 등 잡일     |

---
<br>

## 🔄 작업 흐름 (Flow)


### 1. 이슈 생성

- GitHub 이슈 탭에서 템플릿에 맞춰 작성합니다.
- 이슈에는 아래와 같은 **작업 태그**를 지정합니다:
  - `feat` | `fix` | `chore` | `refactor`

### 2. 브랜치 생성

- 브랜치 명명 규칙: 태그/이슈번호
- 예시:
  ```
  feat/#21
  fix/#8
  ```

### 3. 커밋 메시지 작성

- 앞서 정의한 커밋 컨벤션을 따릅니다.
- 예시:
  ```
  feat: 회원가입 API 구현
  fix: 로그인 시 500 에러 수정
  ```

### 4. PR(Pull Request) 작성

- PR 제목은 명확하게 작성하고, 본문에는 아래 내용을 포함합니다:
  - 실행 결과 스크린샷 첨부 (필수)
  - 관련 이슈 자동 종료:  
    ```
    close #{이슈번호}
    ```
- PR 템플릿을 반드시 사용합니다.

### 5. 코드 리뷰 및 머지

- 최소 1명 이상의 팀원에게 **리뷰 승인**을 받은 후 **직접 merge**합니다.

### 6. 브랜치 정리

- `main` 브랜치에 merge된 feature 브랜치는 **삭제**합니다.

---
