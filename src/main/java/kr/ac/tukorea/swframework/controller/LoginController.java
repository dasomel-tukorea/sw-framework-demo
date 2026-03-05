// Week 07 — 세션 처리 (로그인/로그아웃)
// LoginController.java — 로그인/로그아웃 요청 처리
package kr.ac.tukorea.swframework.controller;

import jakarta.servlet.http.HttpSession;
import kr.ac.tukorea.swframework.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 로그인/로그아웃 Controller
 *
 * 주요 학습 포인트:
 * 1. HttpSession — 서버 측 세션에 사용자 정보 저장/조회/삭제
 * 2. session.setAttribute() — 세션에 데이터 저장 (로그인 처리)
 * 3. session.getAttribute() — 세션에서 데이터 조회 (로그인 상태 확인)
 * 4. session.invalidate() — 세션 무효화 (로그아웃 처리)
 * 5. PRG 패턴 — 로그인 성공 후 Redirect
 *
 * 실무 참고:
 * - 실제 프로젝트에서는 DB에서 사용자를 조회하고 비밀번호를 BCrypt 등으로 암호화 비교
 * - Spring Security를 사용하면 이 Controller의 대부분 로직이 프레임워크로 대체됨
 */
@Slf4j
@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 로그인 폼 페이지 표시
     * GET /login
     *
     * - 인터셉터의 excludePathPatterns에 포함되어야 함 (무한 리다이렉트 방지)
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // templates/login.html 렌더링
    }

    /**
     * 로그인 처리
     * POST /login
     *
     * @param loginId  폼에서 전송된 아이디 (name="loginId")
     * @param password 폼에서 전송된 비밀번호 (name="password")
     * @param session  HttpSession 객체 — Spring이 자동 주입
     * @param model    에러 메시지 전달용 Model
     * @return 성공 시 redirect:/board/list, 실패 시 login 페이지
     */
    @PostMapping("/login")
    public String login(
            @RequestParam String loginId,    // HTML Form의 name="loginId"와 매핑
            @RequestParam String password,   // HTML Form의 name="password"와 매핑
            HttpSession session,             // Spring이 현재 요청의 세션을 자동 주입
            Model model) {

        // === BCrypt 기반 인증 로직 (Week 07 심화) ===
        // 1. UserRepository에서 loginId로 사용자 조회
        // 2. BCrypt.matches(password, hashedPassword)로 비밀번호 검증
        // 3. 인증 성공 시 세션에 사용자 정보 저장
        if (userRepository.authenticate(loginId, password)) {

            // 로그인 성공 → 세션에 사용자 정보 저장
            // "loginUser" 키로 저장 → 이후 session.getAttribute("loginUser")로 조회
            session.setAttribute("loginUser", loginId);
            log.info("로그인 성공: {}", loginId);

            // PRG 패턴: POST 성공 → Redirect → GET
            return "redirect:/board/list";
        }

        // 로그인 실패 → 에러 메시지와 함께 로그인 페이지 재표시
        log.warn("로그인 실패 시도: {}", loginId);
        model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        return "login";
    }

    /**
     * 로그아웃 처리
     * POST /logout
     *
     * - session.invalidate()로 세션의 모든 데이터를 삭제하고 세션을 무효화
     * - GET이 아닌 POST를 사용하는 이유: CSRF 공격 방지
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        log.info("로그아웃: {}", session.getAttribute("loginUser"));
        session.invalidate(); // 세션 무효화 — 모든 세션 속성 삭제
        return "redirect:/login";
    }
}
