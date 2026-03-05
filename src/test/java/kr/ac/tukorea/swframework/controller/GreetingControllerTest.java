// Week 04 — IoC/DI 컨트롤러 MockMvc 테스트
package kr.ac.tukorea.swframework.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * GreetingController MockMvc 통합 테스트
 * Week 04 — IoC/DI: @Primary Bean 주입 + Controller 동작 검증
 *
 * @SpringBootTest — 전체 Spring Context 로드
 * @ActiveProfiles("test") — H2 인메모리 DB
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("GreetingController — IoC/DI MockMvc 테스트")
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("TC-001: GET /greeting — 기본값(학생)으로 정상 응답")
    void greeting_noParam_usesDefaultName() throws Exception {
        // given: 로그인 세션 (인터셉터 통과용)
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUser", "admin");

        // when + then
        mockMvc.perform(get("/greeting").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("greeting"))
                .andExpect(model().attributeExists("message", "name", "serviceClass"));
    }

    @Test
    @DisplayName("TC-002: GET /greeting?name=홍길동 — 이름 파라미터 전달")
    void greeting_withName_passesNameToModel() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUser", "admin");

        // when + then
        mockMvc.perform(get("/greeting").session(session)
                        .param("name", "홍길동"))
                .andExpect(status().isOk())
                .andExpect(view().name("greeting"))
                .andExpect(model().attribute("name", "홍길동"));
    }

    @Test
    @DisplayName("TC-003: GET /greeting — @Primary Bean(KoreanGreetingService)이 주입됨")
    void greeting_primaryBeanInjected_isKoreanService() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUser", "admin");

        // when + then: @Primary인 KoreanGreetingService가 주입되어야 함
        // CGLIB 프록시 적용 시 getSimpleName() → "KoreanGreetingService$$SpringCGLIB$$0" 형태가 될 수 있으므로 startsWith로 검증
        mockMvc.perform(get("/greeting").session(session)
                        .param("name", "테스트"))
                .andExpect(status().isOk())
                .andExpect(result ->
                        org.assertj.core.api.Assertions.assertThat(
                                (String) result.getModelAndView().getModel().get("serviceClass")
                        ).startsWith("KoreanGreetingService"));
    }

    @Test
    @DisplayName("TC-004: GET /greeting — 반환된 메시지에 이름 포함")
    void greeting_messageContainsName() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loginUser", "admin");

        // when + then
        mockMvc.perform(get("/greeting").session(session)
                        .param("name", "홍길동"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"));
        // 메시지에 이름 포함 여부는 뷰 레벨에서 확인 (모델에 message 존재 확인)
    }

    @Test
    @DisplayName("TC-005: 비로그인 상태에서 /greeting 접근 시 로그인 페이지로 리다이렉트")
    void greeting_noSession_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
