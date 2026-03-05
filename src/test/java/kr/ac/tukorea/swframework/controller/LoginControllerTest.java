// LoginControllerTest.java — 로그인/로그아웃 컨트롤러 MockMvc 테스트
// Week 13 — 테스트 코드 기초
package kr.ac.tukorea.swframework.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 로그인/로그아웃 컨트롤러 MockMvc 테스트
 * Week 13 — 테스트 코드 기초
 *
 * 테스트 대상:
 * - GET  /login  — 로그인 폼 페이지
 * - POST /login  — 로그인 처리 (하드코딩 인증: admin/1234, guest/1234)
 * - POST /logout — 로그아웃 처리 (세션 무효화)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ============================================================
    // 로그인 폼 테스트
    // ============================================================

    @Test
    @DisplayName("TC-001: 로그인 페이지 정상 접속 — login 뷰 반환")
    void 로그인_페이지_접속() throws Exception {
        // when + then: GET /login → 200 OK + login 뷰
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // ============================================================
    // 로그인 처리 테스트
    // ============================================================

    @Test
    @DisplayName("TC-002: admin 계정으로 정상 로그인 — 게시판 목록으로 리다이렉트")
    void admin_정상_로그인() throws Exception {
        // when + then: POST /login (admin/1234) → /board/list 리다이렉트
        mockMvc.perform(post("/login")
                        .param("loginId", "admin")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"));
    }

    @Test
    @DisplayName("TC-003: guest 계정으로 정상 로그인 — 게시판 목록으로 리다이렉트")
    void guest_정상_로그인() throws Exception {
        // when + then: POST /login (guest/1234) → /board/list 리다이렉트
        mockMvc.perform(post("/login")
                        .param("loginId", "guest")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"));
    }

    @Test
    @DisplayName("TC-004: 잘못된 비밀번호로 로그인 실패 — login 뷰와 에러 메시지 반환")
    void 잘못된_비밀번호_로그인_실패() throws Exception {
        // when + then: 잘못된 비밀번호 → 로그인 페이지로 돌아오고 error 속성 존재
        mockMvc.perform(post("/login")
                        .param("loginId", "admin")
                        .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error")); // 에러 메시지 모델 속성 확인
    }

    @Test
    @DisplayName("TC-005: 존재하지 않는 아이디로 로그인 실패 — login 뷰와 에러 메시지 반환")
    void 존재하지않는_아이디_로그인_실패() throws Exception {
        // when + then: 없는 아이디 → login 뷰 반환 + error 속성
        mockMvc.perform(post("/login")
                        .param("loginId", "nouser")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @DisplayName("TC-006: 빈 아이디로 로그인 시도 — login 뷰와 에러 메시지 반환")
    void 빈_아이디_로그인_실패() throws Exception {
        mockMvc.perform(post("/login")
                        .param("loginId", "")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    // ============================================================
    // 로그아웃 테스트
    // ============================================================

    @Test
    @DisplayName("TC-007: 로그아웃 처리 — 세션 무효화 후 로그인 페이지로 리다이렉트")
    void 로그아웃_처리() throws Exception {
        // given: 로그인 세션 준비
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUser", "admin");

        // when + then: POST /logout → 세션 무효화 + /login 리다이렉트
        mockMvc.perform(post("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("TC-008: 로그아웃 후 게시판 접근 시 로그인 페이지로 리다이렉트")
    void 로그아웃_후_게시판_접근_차단() throws Exception {
        // given: 로그인 후 로그아웃
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUser", "admin");

        // 로그아웃
        mockMvc.perform(post("/logout").session(session));

        // when + then: 로그아웃된 세션으로 /board/list 접근 → /login 리다이렉트
        // 세션이 invalidate() 되었으므로 loginUser 속성이 없음
        mockMvc.perform(get("/board/list").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
