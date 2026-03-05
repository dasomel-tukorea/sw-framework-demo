// Week 10 — 전역 예외 처리
// GlobalExceptionHandler.java — 모든 Controller의 예외를 한 곳에서 처리
// 프로젝트 경로: src/main/java/kr/ac/tukorea/swframework/exception/GlobalExceptionHandler.java
package kr.ac.tukorea.swframework.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 전역 예외 처리 클래스
 *
 * - @ControllerAdvice: 모든 @Controller에서 발생하는 예외를 이 클래스에서 처리한다
 * - @ExceptionHandler: 처리할 예외 타입을 지정한다
 * - @Slf4j: Lombok 로깅 어노테이션 (log 변수 자동 생성)
 *
 * 현업 포인트:
 *   - 보안: 서버 내부 구조(파일 경로, DB 정보, 스택트레이스)를 사용자에게 노출하면 안 된다
 *   - UX:   개발자용 에러 메시지를 일반 사용자가 이해하기 어렵다
 *   → 전역 예외 처리로 친절한 에러 페이지를 제공하고, 상세 로그는 서버 로그에만 기록한다
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 예외 처리 — 요청한 데이터가 없을 때 (404)
     *
     * 처리 흐름:
     *   Service에서 EntityNotFoundException 발생
     *   → DispatcherServlet이 예외를 전파
     *   → 이 메서드가 처리
     *   → error/404.html 렌더링
     *
     * @param ex    발생한 예외 객체
     * @param model View에 전달할 에러 메시지
     * @return templates/error/404.html 렌더링
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleNotFound(EntityNotFoundException ex, Model model) {
        // 경고 수준 로그 — 서버 로그에 기록 (스택트레이스는 생략)
        log.warn("데이터 없음: {}", ex.getMessage());
        // View에 에러 메시지 전달 — templates/error/404.html의 ${message}에 바인딩
        model.addAttribute("message", ex.getMessage());
        return "error/404";  // templates/error/404.html
    }

    /**
     * 일반 예외 처리 — 예상치 못한 서버 오류 (500)
     *
     * 처리 흐름:
     *   Controller/Service에서 예상치 못한 Exception 발생
     *   → DispatcherServlet이 예외를 전파
     *   → 이 메서드가 처리
     *   → error/500.html 렌더링
     *
     * @param ex    발생한 예외 객체
     * @param model View에 전달할 에러 메시지
     * @return templates/error/500.html 렌더링
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        // 에러 수준 로그 — 스택트레이스 포함하여 서버 로그에 기록
        log.error("서버 오류 발생: {}", ex.getMessage(), ex);
        // 사용자에게는 일반 메시지만 전달 (상세 오류 정보 노출 금지)
        model.addAttribute("message", "서버 내부 오류가 발생했습니다.");
        return "error/500";  // templates/error/500.html
    }
}
