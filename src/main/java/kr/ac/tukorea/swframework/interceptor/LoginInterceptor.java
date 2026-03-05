// Week 07 — 세션 처리 (로그인/로그아웃)
// LoginInterceptor.java — 로그인 체크 인터셉터
package kr.ac.tukorea.swframework.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 로그인 체크 인터셉터
 *
 * HandlerInterceptor를 구현하여 Controller 진입 전 로그인 여부를 확인한다.
 *
 * 실행 흐름:
 * 1. 클라이언트 요청 → DispatcherServlet
 * 2. DispatcherServlet → HandlerInterceptor.preHandle()
 * 3. preHandle() 반환값:
 *    - true  → Controller 진입 허용
 *    - false → Controller 진입 차단 (로그인 페이지로 리다이렉트)
 *
 * 등록 방법: WebConfig에서 addInterceptors()로 등록
 *
 * 장점:
 * - 모든 Controller에 세션 체크 코드를 넣지 않아도 됨 (중복 제거)
 * - URL 패턴으로 보호 범위를 유연하게 설정 가능
 * - AOP(5주차)와 유사하게 횡단 관심사를 분리하는 효과
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * Controller 진입 전 실행되는 메서드
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param handler  실행될 Handler(Controller 메서드) 정보
     * @return true: Controller 진입 허용 / false: 진입 차단
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        // getSession(false) — 세션이 없으면 새로 생성하지 않고 null 반환
        // getSession(true) 또는 getSession()은 세션이 없으면 새로 생성
        HttpSession session = request.getSession(false);

        // 세션이 없거나, 세션에 로그인 사용자 정보가 없으면 → 비로그인 상태
        if (session == null || session.getAttribute("loginUser") == null) {
            log.info("비로그인 사용자 접근 차단: {}", requestURI);
            // 로그인 페이지로 리다이렉트
            response.sendRedirect("/login");
            return false; // Controller 진입 차단
        }

        // 로그인 상태 확인 완료 → Controller 진입 허용
        return true;
    }
}
