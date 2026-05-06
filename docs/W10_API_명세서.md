# API 명세서 — 게시판 프로젝트

> **API Specification**
> SW 프레임워크 · **W10 과제 (Part 2)** · 제출 기한 **W11 수업 시작 전**
> 한국공학대학교 IT경영전공 · 2026학년도 1학기
>
> 화면설계는 별도 문서 → `docs/W10_화면설계서.md` (W10 Part 1)

---

## 0. 프로젝트 기본 정보

| 항목 | 내용 |
|---|---|
| 프로젝트명 | Spring Boot 게시판 (sw-framework-demo) |
| 팀명 / 팀장 | 예시팀 / 홍길동 |
| Base URL | `http://localhost:8080` |
| API 수 | 12개 (페이지 컨트롤러 기준) |
| 본 프로젝트 API 스타일 | 전통적 MVC (페이지 컨트롤러) · `Redirect` 응답 중심 |

---

## 1. API 엔드포인트 목록

| Method | URL | Controller 메서드 | 설명 | 관련 기능 (W08 ID) |
|---|---|---|---|---|
| GET | `/login` | `LoginController.loginForm()` | 로그인 폼 화면 | FR-002 |
| POST | `/login` | `LoginController.login()` | 로그인 처리 · 세션 생성 | FR-002 |
| POST | `/logout` | `LoginController.logout()` | 로그아웃 · 세션 무효화 | FR-003 |
| GET | `/board/list` | `BoardController.list()` | 게시글 목록 (페이징·검색) | FR-007, FR-011, FR-012 |
| GET | `/board/detail/{id}` | `BoardController.detail()` | 게시글 상세 · 조회수 증가 | FR-008 |
| GET | `/board/create` | `BoardController.createForm()` | 글 작성 폼 | FR-006 |
| POST | `/board/create` | `BoardController.create()` | 글 작성 처리 · PRG | FR-006, FR-015 |
| GET | `/board/edit/{id}` | `BoardController.editForm()` | 글 수정 폼 (작성자 제한) | FR-009 |
| POST | `/board/edit/{id}` | `BoardController.edit()` | 글 수정 처리 · PRG | FR-009 |
| POST | `/board/delete/{id}` | `BoardController.delete()` | 글 삭제 · 작성자 또는 관리자 | FR-010, FR-017 |
| GET | `/board/download/{savedName}` | `BoardController.download()` | 첨부파일 다운로드 | FR-016 |
| GET | `/greeting` | `GreetingController.greet()` | IoC/DI 시연 (Should) | FR-024 |

---

## 2. API 상세 명세

### 2-1. POST /login (로그인 처리)

| 항목 | 내용 |
|---|---|
| Method | POST |
| URL | `/login` |
| Content-Type | `application/x-www-form-urlencoded` |
| 인증 | 비로그인 |

**요청 파라미터:**

| 이름 | 타입 | 필수 | 설명 |
|---|---|---|---|
| loginId | string | ✅ | 로그인 ID (users.username) |
| password | string | ✅ | 비밀번호 (BCrypt 검증) |

**응답 (성공):**

```
HTTP/1.1 302 Found
Location: /board/list
Set-Cookie: JSESSIONID=xxx; HttpOnly
```

세션에 `LoginMember(username, name, role)` 저장.

**응답 (실패):**

```
HTTP/1.1 302 Found
Location: /login?error
```

**에러 케이스:**

- 아이디 미존재 또는 비밀번호 불일치 → `redirect:/login?error` (보안상 메시지 동일)
- 빈 값 입력 → 폼 재표시 + 오류 메시지

---

### 2-2. GET /board/list (게시글 목록 — 페이징·검색)

| 항목 | 내용 |
|---|---|
| Method | GET |
| URL | `/board/list?page=1&searchType=title&keyword=` |
| 인증 | 세션 필수 (LoginInterceptor) |

**Query 파라미터:**

| 이름 | 타입 | 필수 | 기본값 | 설명 |
|---|---|---|---|---|
| page | int | ❌ | 1 | 페이지 번호 |
| searchType | string | ❌ | (없음) | `title` · `author` · `titleContent` |
| keyword | string | ❌ | (없음) | 검색어 |

**응답:**

- `200 OK + board/list.html`
- Model: `List<BoardDTO> boards`, `PageDTO page`, `SearchDTO search`

**페이징 규칙:**

| 항목 | 값 |
|---|---|
| 페이지당 게시글 수 | 10건 |
| 페이지 블록 크기 | 5개 |

---

### 2-3. POST /board/create (게시글 등록)

| 항목 | 내용 |
|---|---|
| Method | POST |
| URL | `/board/create` |
| Content-Type | `multipart/form-data` (파일 첨부 지원) |
| 인증 | 세션 필수 |

**요청 파라미터:**

| 이름 | 타입 | 필수 | 설명 |
|---|---|---|---|
| title | string | ✅ | 제목 (최대 200자) |
| content | string | ✅ | 본문 |
| file | MultipartFile | ❌ | 첨부파일 1개 (UUID 저장명으로 변환) |

**자동 설정 데이터 (서버 측):**

- `author` ← 세션의 `loginMember.username`
- `created_at` ← `CURRENT_TIMESTAMP`
- `view_count` ← `0`

**응답:**

- 성공: `302 Redirect → /board/list`
- 실패(검증 오류): `200 OK + board/form.html` (오류 메시지 포함)

---

### 2-4. POST /board/delete/{id} (게시글 삭제)

| 항목 | 내용 |
|---|---|
| Method | POST |
| URL | `/board/delete/{id}` |
| 인증 | 세션 필수 + (작성자 본인 OR 관리자) |

**경로 변수:**

- `id` (long, 필수): 게시글 ID

**응답:**

- 성공: `302 Redirect → /board/list`
- 권한 부족: `302 Redirect → /board/detail/{id}?error=permission`

---

### 2-5. GET /board/download/{savedName} (파일 다운로드)

| 항목 | 내용 |
|---|---|
| Method | GET |
| URL | `/board/download/{savedName}` |
| 인증 | 세션 필수 |

**경로 변수:**

- `savedName` (string, 필수): 서버 저장 파일명 (UUID)

**응답:**

```
HTTP/1.1 200 OK
Content-Type: application/octet-stream
Content-Disposition: attachment; filename="원본파일명.확장자"
```

본문은 파일 바이트 스트림.

---

## 3. 공통 응답 / 에러 처리

| 상황 | HTTP 상태 | 동작 |
|---|---|---|
| 비로그인 접근 | 302 Redirect | `/login` 으로 이동 (LoginInterceptor) |
| 권한 부족 | 302 Redirect | 상세 페이지로 + `?error=permission` |
| 자원 없음 | 404 Not Found | `error/404.html` |
| 서버 오류 | 500 Internal Server Error | `error/500.html` |
| 유효성 실패 | 200 OK | 폼 재표시 + Bean Validation 오류 메시지 |
| XSS 방지 | — | Thymeleaf `th:text` (HTML 이스케이프) · `XssEscapeFilter` |
| SQL 인젝션 방지 | — | MyBatis `#{}` 바인딩만 사용 |

---

## 4. 인증·세션 관리

| 항목 | 내용 |
|---|---|
| 인증 방식 | HTTP 세션 (`HttpSession`) |
| 세션 키 | `loginMember` (LoginMember 객체) |
| 세션 타임아웃 | 30분 (application.yml `server.servlet.session.timeout`) |
| 쿠키 속성 | `HttpOnly` |
| 비로그인 차단 | `LoginInterceptor` (`/login`, `/css/**`, `/js/**` 제외) |
| 비밀번호 | BCrypt 해시 저장 (`spring-security-crypto`) |

---

## ✅ 제출 전 체크리스트

- [x] API 엔드포인트 10개 이상 정의 (현재 12개)
- [x] HTTP 메서드 정확히 표기 (GET / POST)
- [x] URL 패턴 일관성 (RESTful 원칙 가급적 준수)
- [x] PRG 패턴 적용 (POST 성공 후 redirect)
- [x] 주요 POST API 2~3개의 상세 명세 작성 (요청·응답·에러)
- [x] W08 요구사항 정의서 기능 ID와 매칭
- [x] 공통 응답·에러 처리 방침 명시
- [x] GitHub 저장소 `docs/W10_API_명세서.md`로 업로드
