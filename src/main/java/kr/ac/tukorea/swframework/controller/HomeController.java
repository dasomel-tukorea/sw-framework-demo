// Week 03 — SW 프레임워크 이론 / Week 07 — 세션 처리
// HomeController.java — 홈 페이지 컨트롤러
package kr.ac.tukorea.swframework.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 홈 페이지 컨트롤러
 *
 * - "/" 루트 경로 요청을 처리한다
 * - 로그인 여부에 따라 사용자 이름을 표시한다
 */
@Controller
public class HomeController {

    /**
     * 홈 페이지 표시
     * GET /
     *
     * - 로그인한 사용자가 있으면 세션에서 사용자 이름을 가져온다
     * - WebConfig 인터셉터의 excludePathPatterns에 "/" 포함
     */
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        // 세션에서 로그인 사용자 정보 조회
        String loginUser = (String) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
        }
        return "index"; // templates/index.html 렌더링
    }
}
