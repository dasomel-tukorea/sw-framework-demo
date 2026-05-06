# API 명세서 — [프로젝트명]

> **API Specification**
> SW 프레임워크 · **W10 과제 (Part 2)** · 제출 기한 **W11 수업 시작 전**
> 한국공학대학교 IT경영전공 · 2026학년도 1학기

---

## 📋 작성 가이드

REST API 엔드포인트와 요청/응답 형식을 정의합니다. **W10~W11 Controller 구현의 청사진**이 됩니다.

### 작성 원칙

- API 엔드포인트는 **HTTP 메서드 + URL 패턴 + 요청/응답 형식** 모두 명시
- 이번 수업은 전통적 MVC 중심이므로 **페이지 컨트롤러** 기준 API 작성 (REST API는 W10 이후 선택)
- **PRG 패턴** 적용 (POST 성공 후 redirect)
- W08 요구사항 정의서 기능 ID와 **매칭**되도록 작성

---

## 1. 프로젝트 기본 정보

| 항목 | 내용 |
|---|---|
| 프로젝트명 | [W08 정의서와 동일] |
| 팀명 / 팀장 | [예시] Framework Masters / 홍길동 |
| Base URL | [예시] `http://localhost:8080` |
| API 수 | [예시] 10개 |

---

## 2. API 엔드포인트 목록

> Must 기능 위주로 10개 이상의 API를 정의합니다.

| Method | URL | Controller 메서드 | 설명 | 관련 기능 (W08 ID) |
|---|---|---|---|---|
| GET | `/login` | `loginForm()` | 로그인 폼 화면 | F-002 |
| POST | `/login` | `login()` | 로그인 처리 · 세션 생성 | F-002 |
| POST | `/logout` | `logout()` | 로그아웃 · 세션 무효화 | F-002 |
| GET | `/signup` | `signupForm()` | 회원가입 폼 화면 | F-001 |
| POST | `/signup` | `signup()` | 회원가입 처리 | F-001 |
| GET | `/` | `studyList()` | 스터디 목록 (페이징·검색) | F-003, F-006, F-007 |
| GET | `/study/{id}` | `studyDetail()` | 스터디 상세 | F-003 |
| GET | `/study/new` | `studyForm()` | 스터디 생성 폼 | F-003 |
| POST | `/study` | `createStudy()` | 스터디 생성 처리 · PRG 패턴 | F-003 |
| POST | `/study/{id}/join` | `joinStudy()` | 스터디 참여 신청 | F-004 |

---

## 3. API 상세 명세

> 주요 POST API는 **요청 · 응답 · 에러 케이스**를 상세 기록.

### 3-1. POST /login (로그인 처리)

| 항목 | 내용 |
|---|---|
| Method | POST |
| URL | `/login` |
| Content-Type | `application/x-www-form-urlencoded` |

**요청 파라미터:**

| 이름 | 타입 | 필수 | 설명 |
|---|---|---|---|
| email | string | ✅ | 이메일 (form name) |
| password | string | ✅ | 비밀번호 |

**응답 (성공):**

```
HTTP/1.1 302 Found
Location: /
Set-Cookie: JSESSIONID=xxx; HttpOnly
```

**응답 (실패):**

```
HTTP/1.1 302 Found
Location: /login?error
```

**에러 케이스:**

- 이메일 미존재 / 비밀번호 불일치 → `redirect:/login?error`
- 유효성 검사 실패 → 폼 재표시

---

### 3-2. POST /signup (회원가입 처리)

| 항목 | 내용 |
|---|---|
| Method | POST |
| URL | `/signup` |
| Content-Type | `application/x-www-form-urlencoded` |

**요청 파라미터:**

| 이름 | 타입 | 필수 | 설명 |
|---|---|---|---|
| email | string | ✅ | 이메일 (중복 불가) |
| password | string | ✅ | 비밀번호 (8자 이상) |
| name | string | ✅ | 이름 |

**응답:**

- 성공: `302 Redirect → /login?signup=success`
- 실패: `200 OK + signup.html (오류 메시지 포함)`

---

### 3-3. GET / (스터디 목록 — 페이징·검색)

| 항목 | 내용 |
|---|---|
| Method | GET |
| URL | `/?page=1&size=10&keyword=&category=` |
| 인증 | 세션 필수 |

**요청 파라미터 (Query):**

| 이름 | 타입 | 필수 | 기본값 | 설명 |
|---|---|---|---|---|
| page | int | ❌ | 1 | 페이지 번호 |
| size | int | ❌ | 10 | 페이지당 항목 수 |
| keyword | string | ❌ | — | 검색어 (제목·설명) |
| category | string | ❌ | — | 카테고리 필터 |

**응답:**

- `200 OK + study/list.html`
- Model: `List<Study> studies`, `int totalPages`, `int currentPage`, `String keyword`

> 나머지 주요 API도 동일한 형식으로 작성합니다.

---

## 4. 공통 응답 / 에러 처리

| 상황 | HTTP 상태 | 동작 |
|---|---|---|
| 비로그인 접근 | 302 Redirect | `/login` 으로 이동 (인터셉터) |
| 권한 부족 | 403 Forbidden | 에러 페이지 |
| 자원 없음 | 404 Not Found | 에러 페이지 |
| 서버 오류 | 500 Internal Server Error | 에러 페이지 |
| 유효성 실패 | 200 OK | 폼 재표시 + 오류 메시지 |

---

## ✅ 제출 전 체크리스트

- [ ] API 엔드포인트 10개 이상 정의
- [ ] HTTP 메서드 정확히 표기 (GET / POST / PUT / DELETE)
- [ ] URL 패턴 일관성 (RESTful 원칙 가급적 준수)
- [ ] PRG 패턴 적용 (POST 성공 후 redirect)
- [ ] 주요 POST API 2~3개의 상세 명세 작성 (요청·응답·에러)
- [ ] W08 요구사항 정의서 기능 ID와 매칭
- [ ] 공통 응답·에러 처리 방침 명시
- [ ] 파일명: `화면설계서_API명세_팀명.docx` (Part 2)
- [ ] GitHub 저장소 `docs/W10_API_명세서.md`로 업로드
