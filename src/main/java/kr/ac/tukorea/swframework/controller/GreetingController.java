// Week 04 — IoC/DI 학습용 컨트롤러
// GreetingService 인터페이스에만 의존 → 구현체 교체 가능
package kr.ac.tukorea.swframework.controller;

import kr.ac.tukorea.swframework.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * IoC/DI 학습용 인사말 컨트롤러
 *
 * 주요 학습 포인트 (Week 04):
 * 1. 생성자 주입 — @Autowired 없이 단일 생성자로 DI
 * 2. 인터페이스 타입 의존 — 구현체 교체 시 이 클래스 수정 불필요
 * 3. @Primary — KoreanGreetingService가 기본 주입됨
 *
 * 실험:
 * - KoreanGreetingService에서 @Primary 제거, EnglishGreetingService에 추가
 * - /greeting?name=홍길동 → 출력 언어가 한국어 → 영어로 바뀜!
 * - GreetingController 코드는 전혀 수정하지 않았는데 동작이 바뀜 → OCP 원칙
 */
@Slf4j
@Controller
public class GreetingController {

    // 인터페이스 타입으로 선언 — 어떤 구현체가 주입될지 모름 (느슨한 결합)
    private final GreetingService greetingService;

    // 생성자 주입 — @Autowired 생략 가능 (단일 생성자)
    // Spring이 @Primary Bean (KoreanGreetingService)을 자동으로 주입
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    /**
     * 인사말 페이지
     * GET /greeting?name=홍길동
     *
     * @param name 인사할 대상 (기본값: "학생")
     */
    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(defaultValue = "학생") String name,
            Model model) {

        String message = greetingService.greet(name);
        model.addAttribute("message", message);
        model.addAttribute("name", name);
        model.addAttribute("serviceClass", greetingService.getClass().getSimpleName());

        log.info("인사말 요청 — name: {}, service: {}", name, greetingService.getClass().getSimpleName());
        return "greeting";
    }
}
