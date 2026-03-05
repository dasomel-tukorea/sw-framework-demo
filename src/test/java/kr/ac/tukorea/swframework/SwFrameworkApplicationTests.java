// SwFrameworkApplicationTests.java — Spring Boot 통합 테스트
// Week 02 — 개발환경 설정 (프로젝트 생성 시 자동 포함)
// Week 13 — H2 테스트 프로파일 적용 (MySQL 없이 테스트 가능)
package kr.ac.tukorea.swframework;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring Boot 애플리케이션 통합 테스트
 *
 * @SpringBootTest: 전체 애플리케이션 컨텍스트를 로드하여 테스트
 * - 모든 Bean이 정상적으로 생성되는지 확인
 * - 설정 파일 로딩, 의존성 주입이 올바른지 검증
 *
 * @ActiveProfiles("test"): application-test.yml을 로드하여 H2 인메모리 DB 사용
 * - MySQL이 없어도 테스트 통과 가능
 * - CI/CD 파이프라인(GitHub Actions)에서 MySQL 없이 실행 가능
 */
@SpringBootTest
@ActiveProfiles("test")
class SwFrameworkApplicationTests {

    /**
     * 컨텍스트 로딩 테스트
     * - Spring IoC 컨테이너가 정상적으로 초기화되는지 확인
     * - 이 테스트가 실패하면 설정 또는 Bean 등록에 문제가 있는 것
     */
    @Test
    void contextLoads() {
        // 컨텍스트 로딩만 검증 (빈 메서드)
        // 예외가 발생하면 테스트 실패 → Bean 등록 또는 설정 오류 확인
    }
}
