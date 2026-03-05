// Week 07 — UserRepository 단위 테스트 (BCrypt 인증 검증)
package kr.ac.tukorea.swframework.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * UserRepository 단위 테스트
 * Week 07 — BCrypt 기반 사용자 인증 학습 포인트 검증
 */
@DisplayName("UserRepository — BCrypt 인증 단위 테스트")
class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 각 테스트 전 새 인스턴스 생성 (생성자에서 admin/guest 등록)
        userRepository = new UserRepository();
    }

    @Test
    @DisplayName("TC-001: admin/1234 로그인 인증 성공")
    void authenticate_adminWithCorrectPassword_returnsTrue() {
        assertThat(userRepository.authenticate("admin", "1234")).isTrue();
    }

    @Test
    @DisplayName("TC-002: guest/1234 로그인 인증 성공")
    void authenticate_guestWithCorrectPassword_returnsTrue() {
        assertThat(userRepository.authenticate("guest", "1234")).isTrue();
    }

    @Test
    @DisplayName("TC-003: 틀린 비밀번호로 인증 실패")
    void authenticate_wrongPassword_returnsFalse() {
        assertThat(userRepository.authenticate("admin", "wrong")).isFalse();
    }

    @Test
    @DisplayName("TC-004: 존재하지 않는 아이디로 인증 실패")
    void authenticate_unknownUser_returnsFalse() {
        assertThat(userRepository.authenticate("unknown", "1234")).isFalse();
    }

    @Test
    @DisplayName("TC-005: existsByLoginId — admin 존재 확인")
    void existsByLoginId_existingUser_returnsTrue() {
        assertThat(userRepository.existsByLoginId("admin")).isTrue();
        assertThat(userRepository.existsByLoginId("guest")).isTrue();
    }

    @Test
    @DisplayName("TC-006: existsByLoginId — 없는 사용자 false 반환")
    void existsByLoginId_unknownUser_returnsFalse() {
        assertThat(userRepository.existsByLoginId("nobody")).isFalse();
    }

    @Test
    @DisplayName("TC-007: register() — 신규 사용자 등록 후 인증 성공")
    void register_newUser_canAuthenticateAfterRegistration() {
        // given
        String loginId = "newUser";
        String password = "securePass";

        // when
        userRepository.register(loginId, password);

        // then
        assertThat(userRepository.existsByLoginId(loginId)).isTrue();
        assertThat(userRepository.authenticate(loginId, password)).isTrue();
        assertThat(userRepository.authenticate(loginId, "wrongPass")).isFalse();
    }

    @Test
    @DisplayName("TC-008: register() — 이미 존재하는 아이디 등록 시 예외 발생")
    void register_duplicateUser_throwsException() {
        assertThatThrownBy(() -> userRepository.register("admin", "newPass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("admin");
    }
}
