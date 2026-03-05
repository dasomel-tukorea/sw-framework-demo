// Week 07 — BCrypt 암호화 단위 테스트
package kr.ac.tukorea.swframework.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PasswordUtil 단위 테스트
 * Week 07 — BCrypt 암호화 학습 포인트 검증
 *
 * BCrypt 핵심 특성:
 * 1. 단방향 해시 — 해시에서 원문 복원 불가
 * 2. Salt — 같은 입력도 매번 다른 해시값 생성
 * 3. matches() — Salt를 추출해 동일 조건으로 재해시 후 비교
 */
@DisplayName("PasswordUtil — BCrypt 암호화 단위 테스트")
class PasswordUtilTest {

    @Test
    @DisplayName("TC-001: encode() — 평문을 BCrypt 해시로 변환")
    void encode_returnsHashedPassword() {
        // given
        String rawPassword = "1234";

        // when
        String encoded = PasswordUtil.encode(rawPassword);

        // then
        assertThat(encoded).isNotNull();
        assertThat(encoded).isNotEqualTo(rawPassword);       // 원문과 다름
        assertThat(encoded).startsWith("$2a$");              // BCrypt 해시 형식
    }

    @Test
    @DisplayName("TC-002: encode() — 같은 입력도 매번 다른 해시값 생성 (Salt)")
    void encode_sameInputProducesDifferentHash() {
        // given
        String rawPassword = "1234";

        // when
        String hash1 = PasswordUtil.encode(rawPassword);
        String hash2 = PasswordUtil.encode(rawPassword);

        // then: Salt 때문에 매번 다른 해시값 — equals()로 비교하면 항상 false!
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    @DisplayName("TC-003: matches() — 올바른 비밀번호 검증 성공")
    void matches_correctPassword_returnsTrue() {
        // given
        String rawPassword = "1234";
        String encoded = PasswordUtil.encode(rawPassword);

        // when
        boolean result = PasswordUtil.matches(rawPassword, encoded);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("TC-004: matches() — 틀린 비밀번호 검증 실패")
    void matches_wrongPassword_returnsFalse() {
        // given
        String rawPassword = "1234";
        String encoded = PasswordUtil.encode(rawPassword);

        // when
        boolean result = PasswordUtil.matches("wrong", encoded);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("TC-005: matches() — 해시가 달라도 같은 원문이면 true (Salt 내포)")
    void matches_differentHashSameInput_returnsTrue() {
        // given: 같은 원문으로 두 번 인코딩 → 다른 해시값
        String rawPassword = "myPassword";
        String hash1 = PasswordUtil.encode(rawPassword);
        String hash2 = PasswordUtil.encode(rawPassword);
        assertThat(hash1).isNotEqualTo(hash2);  // 다른 해시값 확인

        // when + then: 두 해시 모두 원문과 매칭 성공
        assertThat(PasswordUtil.matches(rawPassword, hash1)).isTrue();
        assertThat(PasswordUtil.matches(rawPassword, hash2)).isTrue();
    }

    @Test
    @DisplayName("TC-006: 빈 문자열 비밀번호도 해시/검증 가능")
    void encode_emptyPassword_canBeHashedAndVerified() {
        // given
        String emptyPassword = "";

        // when
        String encoded = PasswordUtil.encode(emptyPassword);

        // then
        assertThat(PasswordUtil.matches(emptyPassword, encoded)).isTrue();
        assertThat(PasswordUtil.matches("notEmpty", encoded)).isFalse();
    }
}
