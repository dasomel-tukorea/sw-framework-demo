// Week 04 — IoC/DI 단위 테스트 (GreetingService 구현체 검증)
package kr.ac.tukorea.swframework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * GreetingService 구현체 단위 테스트
 * Week 04 — IoC/DI 학습 포인트 검증
 *
 * 핵심: 구현체를 직접 new로 생성하여 순수 단위 테스트 (Spring Context 불필요)
 * → @SpringBootTest 없음 → 빠른 실행
 */
@DisplayName("GreetingService — IoC/DI 단위 테스트")
class GreetingServiceTest {

    @Test
    @DisplayName("TC-001: KoreanGreetingService — 한국어 인사말 반환")
    void koreanGreeting_containsKoreanMessage() {
        // given: Spring 없이 직접 인스턴스 생성
        GreetingService service = new KoreanGreetingService();

        // when
        String message = service.greet("홍길동");

        // then
        assertThat(message).contains("홍길동");
        assertThat(message).contains("안녕하세요");
    }

    @Test
    @DisplayName("TC-002: EnglishGreetingService — 영어 인사말 반환")
    void englishGreeting_containsEnglishMessage() {
        // given
        GreetingService service = new EnglishGreetingService();

        // when
        String message = service.greet("John");

        // then
        assertThat(message).contains("John");
        assertThat(message).contains("Hello");
    }

    @Test
    @DisplayName("TC-003: KoreanGreetingService — 이름이 빈 문자열일 때도 정상 반환")
    void koreanGreeting_emptyName_returnsMessage() {
        // given
        GreetingService service = new KoreanGreetingService();

        // when
        String message = service.greet("");

        // then
        assertThat(message).isNotNull();
        assertThat(message).isNotEmpty();
    }

    @Test
    @DisplayName("TC-004: EnglishGreetingService — 이름이 빈 문자열일 때도 정상 반환")
    void englishGreeting_emptyName_returnsMessage() {
        // given
        GreetingService service = new EnglishGreetingService();

        // when
        String message = service.greet("");

        // then
        assertThat(message).isNotNull();
        assertThat(message).isNotEmpty();
    }

    @Test
    @DisplayName("TC-005: 두 구현체가 서로 다른 메시지 반환")
    void twoImplementations_returnDifferentMessages() {
        // given
        GreetingService korean = new KoreanGreetingService();
        GreetingService english = new EnglishGreetingService();
        String name = "테스트";

        // when
        String koreanMsg = korean.greet(name);
        String englishMsg = english.greet(name);

        // then: 두 구현체의 메시지는 달라야 함 (다형성 검증)
        assertThat(koreanMsg).isNotEqualTo(englishMsg);
    }
}
