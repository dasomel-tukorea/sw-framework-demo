# SW프레임워크 게시판 프로젝트 (완성본)

한국공학대학교 경영학부 IT경영전공 **SW프레임워크 (2026-1학기)** 강의에서 한 학기 동안 만들게 될 **완성된 Spring Boot 게시판 프로젝트**입니다.

## 기술 스택

| 구분 | 기술 |
|---|---|
| 백엔드 | Spring Boot 3.5.x, Java 21 |
| 뷰 엔진 | Thymeleaf |
| ORM | MyBatis |
| 데이터베이스 | MySQL 8.x (운영), H2 (테스트) |
| 빌드 도구 | Gradle (Wrapper) |
| 컨테이너 | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| 보안 | spring-security-crypto (BCrypt 암호화) |
| API 문서 | springdoc-openapi 2.7.0 (Swagger UI) |
| 테스트 커버리지 | JaCoCo (전체 60%, 서비스/컨트롤러 50%) |

## 주요 기능

- 게시글 CRUD (등록/조회/수정/삭제)
- 게시글 검색 (제목/내용)
- 페이징 처리 (LIMIT/OFFSET)
- 정렬 기능 (번호/제목/작성자)
- 세션 기반 로그인/로그아웃 (BCrypt 비밀번호 해시)
- 인터셉터를 통한 접근 제어
- AOP 기반 메서드 실행 시간 측정
- BCrypt 암호화 (spring-security-crypto, PasswordUtil)
- 다국어(i18n) 지원 (한국어 기본, 영어 전환 가능)
- IoC/DI 시연 (GreetingService 인터페이스 + 구현체 2개, @Primary)
- 파일 업로드/다운로드 (UUID 저장명)
- Swagger UI API 문서 자동 생성

## 프로젝트 구조

```
src/main/java/kr/ac/tukorea/swframework/
├── SwFrameworkApplication.java          # 진입점
├── config/
│   ├── AppConfig.java                   # @Bean 수동 등록 설정
│   └── WebConfig.java                   # 인터셉터 등록, CookieLocaleResolver, LocaleChangeInterceptor (i18n)
├── controller/
│   ├── HomeController.java              # 홈 페이지
│   ├── LoginController.java             # 로그인/로그아웃 (BCrypt 인증)
│   ├── BoardController.java             # 게시판 CRUD
│   └── GreetingController.java          # GET /greeting?name= (IoC/DI 시연)
├── service/
│   ├── BoardService.java                # 서비스 인터페이스
│   ├── BoardServiceImpl.java            # 서비스 구현체
│   ├── GreetingService.java             # 인사말 서비스 인터페이스 (IoC/DI 시연)
│   ├── KoreanGreetingService.java       # 한국어 구현체 (@Primary)
│   └── EnglishGreetingService.java      # 영어 구현체
├── repository/
│   └── UserRepository.java             # 인메모리 사용자 저장소 (BCrypt)
├── util/
│   └── PasswordUtil.java               # BCrypt 래퍼 유틸리티
├── dto/
│   ├── BoardDTO.java                    # 게시글 데이터 전달 객체
│   ├── LoginForm.java                   # 로그인 사용자 정보
│   ├── PageDTO.java                     # 페이징/검색/정렬 조건
│   └── SearchDTO.java                   # 검색 조건
├── interceptor/
│   └── LoginInterceptor.java            # 로그인 체크 인터셉터
├── aspect/
│   └── ExecutionTimeAspect.java         # 실행 시간 측정 AOP
└── mapper/
    └── BoardMapper.java                 # MyBatis Mapper 인터페이스
```

## 리소스 구조

```
src/main/resources/
├── application.yml                      # 로컬 개발 환경 설정
├── application-docker.yml               # Docker 환경 설정
├── application-h2.yml                   # H2 인메모리 DB 설정
├── application-test.yml                 # 테스트 프로파일 설정
├── mapper/
│   └── BoardMapper.xml                  # MyBatis SQL 매핑
├── sql/
│   ├── schema-h2.sql                    # H2용 DDL
│   └── data.sql                         # 초기 데이터
├── messages.properties                  # i18n 한국어 메시지 (기본)
├── messages_en.properties               # i18n 영어 메시지
├── static/css/style.css
└── templates/
    ├── index.html
    ├── login.html
    ├── greeting.html                    # IoC/DI 시연 페이지
    ├── fragments/
    │   └── layout.html                  # 공통 레이아웃 프래그먼트
    ├── board/
    │   ├── list.html
    │   ├── detail.html
    │   └── form.html
    └── error/
        └── 404.html
```

## 실행 방법

### 1. 로컬 실행 (개발 환경)

#### 사전 준비
- JDK 21 설치
- MySQL 8.x 실행 중
- IntelliJ IDEA (권장)

#### 데이터베이스 설정
```bash
# MySQL에 접속하여 스키마 생성
mysql -u root -p < sql/schema.sql
```

#### 애플리케이션 실행
```bash
# Gradle Wrapper로 실행
./gradlew bootRun

# 또는 JAR 빌드 후 실행
./gradlew bootJar
java -jar build/libs/sw-framework-1.0.0.jar
```

#### 브라우저 접속
```
http://localhost:8080
http://localhost:8080/swagger-ui/index.html  # Swagger UI API 문서
테스트 계정: admin / 1234
```

#### H2 인메모리 DB로 실행 (MySQL 없이)
```bash
./gradlew bootRun --args='--spring.profiles.active=h2'
```

### 2. Docker 실행 (컨테이너 환경)

```bash
# JAR 빌드
./gradlew bootJar

# Docker Compose로 앱 + DB 동시 실행
docker compose up -d --build

# 로그 확인
docker compose logs -f

# 종료
docker compose down
```

### 3. 설정 파일

| 파일 | 용도 |
|---|---|
| `application.yml` | 로컬 개발 환경 설정 |
| `application-docker.yml` | Docker 환경 설정 (프로파일 활성화) |
| `application-h2.yml` | H2 인메모리 DB 설정 (MySQL 없이 실행) |
| `application-test.yml` | 테스트 전용 설정 (`@ActiveProfiles("test")`) |

## 주차별 학습 내용 매핑

| 주차 | 주제 | 관련 파일 |
|---|---|---|
| 2 | 개발환경 설정 + Git | `build.gradle`, `settings.gradle`, `application.yml` |
| 3 | 프레임워크 이론 | `SwFrameworkApplication.java`, `HomeController.java` |
| 4 | IoC/DI | `AppConfig.java`, `GreetingService.java`, `KoreanGreetingService.java`, `EnglishGreetingService.java`, `GreetingController.java` |
| 5 | AOP & Bean | `ExecutionTimeAspect.java` |
| 6 | Thymeleaf Form | `board/form.html`, `board/list.html`, `style.css`, `layout.html` |
| 7 | 세션 로그인 + BCrypt | `LoginController.java`, `LoginInterceptor.java`, `WebConfig.java`, `UserRepository.java`, `PasswordUtil.java` |
| 9 | MyBatis | `BoardMapper.java`, `BoardMapper.xml`, `schema-h2.sql` |
| 10 | Spring MVC + Swagger | `BoardController.java`, `BoardService.java`, `BoardServiceImpl.java`, Swagger UI (`/swagger-ui/index.html`) |
| 11 | 페이징 처리 | `PageDTO.java`, `BoardMapper.xml` (LIMIT/OFFSET), `list.html` (페이징 UI) |
| 12 | Docker | `Dockerfile`, `docker-compose.yml`, `.dockerignore`, `application-docker.yml` |
| 13 | CI/CD + 테스트 | `.github/workflows/ci.yml`, JaCoCo 커버리지, H2 테스트 프로파일 |
| 14 | 다국어(i18n) | `messages.properties`, `messages_en.properties`, `WebConfig.java` (CookieLocaleResolver) |

## 테스트 계정

| 아이디 | 비밀번호 | 권한 |
|---|---|---|
| admin | 1234 | 관리자 |
| guest | 1234 | 게스트 |

> **참고**: 비밀번호는 BCrypt로 해시 처리되어 `UserRepository`에 저장됩니다 (`PasswordUtil` 사용).

## 테스트

| 테스트 파일 | TC 수 | 설명 |
|---|---|---|
| `BoardServiceTest.java` | - | 게시판 서비스 단위 테스트 |
| `BoardControllerTest.java` | 10 | 게시판 컨트롤러 통합 테스트 |
| `LoginControllerTest.java` | - | 로그인 컨트롤러 테스트 |
| `PageDTOTest.java` | - | 페이징 DTO 단위 테스트 |
| `PasswordUtilTest.java` | 6 | BCrypt 암호화 테스트 |
| `UserRepositoryTest.java` | 8 | 사용자 저장소 테스트 |
| `GreetingServiceTest.java` | 5 | 인사말 서비스 IoC/DI 테스트 |
| `GreetingControllerTest.java` | 5 | 인사말 컨트롤러 테스트 |

```bash
# 전체 테스트 실행 + JaCoCo 커버리지 리포트
./gradlew test jacocoTestReport
# 리포트 위치: build/reports/jacoco/test/html/index.html
```

## 라이선스

이 프로젝트는 교육 목적으로 작성되었습니다.
