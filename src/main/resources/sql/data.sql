-- data.sql — 초기 테스트 데이터
-- MySQL / H2 공통 사용

INSERT INTO users (username, password, name) VALUES
    ('admin', '1234', '관리자'),
    ('guest', '1234', '게스트');

INSERT INTO board (title, content, author) VALUES
    ('첫 번째 게시글', 'Spring Boot 게시판 프로젝트의 첫 번째 글입니다.', 'admin'),
    ('두 번째 게시글', 'MyBatis를 활용한 CRUD 실습 중입니다.', 'guest'),
    ('세 번째 게시글', 'Thymeleaf 템플릿 엔진으로 화면을 구성합니다.', 'admin'),
    ('Spring Boot 자동 설정 원리', 'spring.factories와 @Conditional 어노테이션을 통해 자동 설정이 동작하는 원리를 알아봅니다.', 'admin'),
    ('MyBatis와 JPA 비교', 'SQL 직접 제어가 필요한 프로젝트에서는 MyBatis가 유리하고, 도메인 중심 설계에서는 JPA가 유리합니다.', 'guest'),
    ('인터셉터와 AOP의 차이', '인터셉터는 Controller 레벨, AOP는 Bean 메서드 레벨에서 동작합니다. 용도에 따라 적절히 선택하세요.', 'admin'),
    ('페이징 처리 구현 방법', 'LIMIT과 OFFSET을 활용한 MySQL 페이징 쿼리와 PageDTO 설계 방법을 정리합니다.', 'guest'),
    ('Docker Compose 실습 후기', 'docker-compose.yml 하나로 Spring Boot와 MySQL을 동시에 실행하니 개발 환경 세팅이 훨씬 편해졌습니다.', 'admin'),
    ('Git 브랜치 전략 정리', 'feature 브랜치에서 개발 후 PR을 통해 main에 병합하는 전략을 팀 프로젝트에 적용했습니다.', 'guest'),
    ('세션 기반 인증 구현', 'HttpSession을 활용하여 로그인/로그아웃을 구현하고 LoginInterceptor로 접근 제어를 적용했습니다.', 'admin'),
    ('Thymeleaf 문법 정리', 'th:text, th:each, th:if, th:href 등 자주 쓰는 Thymeleaf 표현식을 정리합니다.', 'guest'),
    ('생성자 주입 vs 필드 주입', 'Spring DI에서는 불변성 보장과 테스트 용이성 때문에 생성자 주입이 권장됩니다.', 'admin'),
    ('MyBatis Dynamic SQL 활용', '<if>, <choose>, <where>, <foreach> 태그를 활용하면 복잡한 동적 쿼리를 깔끔하게 작성할 수 있습니다.', 'guest'),
    ('Spring AOP @Around 어드바이스', '@Around를 사용하면 메서드 실행 전후를 모두 제어할 수 있어 실행 시간 측정에 유용합니다.', 'admin'),
    ('파일 업로드 구현 정리', 'MultipartFile과 UUID를 활용하여 파일명 충돌 없이 서버에 안전하게 저장하는 방법을 정리합니다.', 'guest'),
    ('PRG 패턴이란', 'POST 요청 후 Redirect하여 새로고침 시 중복 등록을 방지하는 Post-Redirect-Get 패턴을 설명합니다.', 'admin'),
    ('Bean Validation 활용', '@NotBlank, @Size 어노테이션으로 서버 측 입력값 검증을 간결하게 구현할 수 있습니다.', 'guest'),
    ('application.yml 프로파일 분리', 'application.yml, application-docker.yml, application-h2.yml로 환경별 설정을 분리하면 관리가 편리합니다.', 'admin'),
    ('JUnit 5 단위 테스트 작성', '@ExtendWith(SpringExtension.class)와 @MockBean을 활용하여 Service 계층 단위 테스트를 작성합니다.', 'guest'),
    ('SQL Injection 방지 방법', 'MyBatis #{} 파라미터 바인딩을 사용하면 PreparedStatement로 처리되어 SQL Injection이 방지됩니다.', 'admin'),
    ('XSS 방지 with Thymeleaf', 'th:text는 HTML 이스케이프를 자동으로 수행하므로 XSS 공격을 기본적으로 방어합니다.', 'guest'),
    ('GitHub Actions CI 파이프라인', 'push 이벤트 발생 시 JDK 21 설정 → Gradle 빌드 → 테스트 순으로 자동 실행되는 워크플로우를 작성합니다.', 'admin'),
    ('H2 인메모리 DB 테스트 활용', 'application-h2.yml 프로파일로 별도 MySQL 없이 빠른 통합 테스트 환경을 구성할 수 있습니다.', 'guest'),
    ('lombok 어노테이션 정리', '@Getter, @Setter, @Slf4j, @Builder, @RequiredArgsConstructor를 적절히 활용하면 보일러플레이트를 줄일 수 있습니다.', 'admin'),
    ('팀 프로젝트 최종 발표 준비', '기능 시연 시나리오를 미리 작성하고, 오류 상황 대비책도 준비해 두면 발표가 훨씬 안정적입니다.', 'guest');
