// Week 07 — 세션 처리 (로그인/로그아웃)
// LoginForm.java — 로그인 사용자 정보 DTO (세션 저장용)
package kr.ac.tukorea.swframework.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 로그인 사용자 정보 DTO
 *
 * 세션에 저장할 객체는 Serializable 구현 권장
 * - 세션을 Redis 등 외부 저장소에 직렬화하여 저장할 수 있음
 * - 서버 재시작 시 세션 복원에도 필요
 *
 * 주의사항:
 * - 비밀번호는 절대 세션에 저장하지 않음!
 * - 세션에는 최소한의 식별 정보만 저장
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm implements Serializable {

    // 직렬화 버전 UID — 클래스 변경 시 역직렬화 호환성 관리
    private static final long serialVersionUID = 1L;

    private String loginId;   // 로그인 아이디
    private String name;      // 사용자 이름 (화면 표시용)
    private String role;      // 권한 — "ADMIN" 또는 "USER"

    /**
     * 관리자 여부 확인 — View에서 권한별 UI 분기에 사용
     * Thymeleaf: th:if="${loginUser.admin}"
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }
}
