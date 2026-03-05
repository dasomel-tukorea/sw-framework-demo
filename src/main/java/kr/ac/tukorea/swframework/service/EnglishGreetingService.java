// Week 04 — IoC/DI 학습용 영어 인사말 서비스
package kr.ac.tukorea.swframework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 영어 인사말 서비스
 *
 * @Primary가 없으므로 기본 주입 대상이 아님
 * → @Qualifier("englishGreetingService")로 명시적 선택 가능
 *
 * 실험: KoreanGreetingService의 @Primary를 제거하고 여기에 붙여보기
 */
@Slf4j
@Service
public class EnglishGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        log.info("English greeting generated for: {}", name);
        return "Hello, " + name + "! Welcome to SW Framework course. \uD83C\uDDFA\uD83C\uDDF8";
    }
}
