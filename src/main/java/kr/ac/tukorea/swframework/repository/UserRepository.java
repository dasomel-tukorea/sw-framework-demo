// Week 07 — 메모리 기반 사용자 저장소 (BCrypt 암호화 적용)
// 실무에서는 DB + UserMapper로 대체됨
package kr.ac.tukorea.swframework.repository;

import kr.ac.tukorea.swframework.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 메모리 기반 사용자 저장소
 *
 * 학습 포인트:
 * 1. @Repository: Spring 데이터 접근 계층 Bean 등록
 * 2. 생성자에서 테스트 계정을 BCrypt 해시로 등록 (평문 저장 금지!)
 * 3. authenticate(): 입력 비밀번호를 BCrypt matches()로 검증
 *
 * 실무에서는:
 * - UserMapper.findByUsername(loginId) 로 DB 조회
 * - BCryptPasswordEncoder.matches(rawPw, user.getHashedPw()) 로 검증
 * - Spring Security 사용 시 이 클래스 전체가 UserDetailsService로 대체됨
 */
@Slf4j
@Repository
public class UserRepository {

    // username → BCrypt 해시 비밀번호 (평문 저장 금지!)
    private final Map<String, String> users = new HashMap<>();

    /**
     * 애플리케이션 시작 시 테스트 계정을 BCrypt 해시로 등록
     * 실무에서는 DB에 이미 저장된 해시값을 조회함
     */
    public UserRepository() {
        // 평문 "1234"를 BCrypt 해시로 변환하여 저장
        users.put("admin", PasswordUtil.encode("1234"));
        users.put("guest", PasswordUtil.encode("1234"));
        log.info("UserRepository 초기화 완료 — 테스트 계정 2개 등록 (BCrypt 암호화 적용)");
    }

    /**
     * 로그인 인증: loginId + rawPassword 검증
     *
     * @param loginId     입력한 아이디
     * @param rawPassword 입력한 평문 비밀번호
     * @return 인증 성공 여부
     */
    public boolean authenticate(String loginId, String rawPassword) {
        String encodedPassword = users.get(loginId);
        if (encodedPassword == null) {
            log.debug("존재하지 않는 사용자: {}", loginId);
            return false;
        }
        // BCrypt.matches(): 평문 vs 해시 비교 (equals() 사용 금지!)
        boolean result = PasswordUtil.matches(rawPassword, encodedPassword);
        log.debug("인증 결과 — loginId: {}, 성공: {}", loginId, result);
        return result;
    }

    /**
     * 아이디 존재 여부 확인
     */
    public boolean existsByLoginId(String loginId) {
        return users.containsKey(loginId);
    }

    /**
     * 신규 사용자 등록 (비밀번호 BCrypt 해시 후 저장)
     */
    public void register(String loginId, String rawPassword) {
        if (existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 존재하는 아이디: " + loginId);
        }
        users.put(loginId, PasswordUtil.encode(rawPassword));
        log.info("신규 사용자 등록: {}", loginId);
    }
}
