// Week 04 — IoC/DI 학습용 한국어 인사말 서비스
package kr.ac.tukorea.swframework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 한국어 인사말 서비스
 *
 * @Primary: 같은 타입의 Bean이 여러 개일 때 기본 주입 대상으로 지정
 * → GreetingController는 코드 수정 없이 한국어/영어 구현체 중 @Primary 선택
 *
 * 실험: @Primary 어노테이션을 제거하고 EnglishGreetingService에 붙여보기
 *       → Controller 코드 수정 없이 출력 언어가 바뀜!
 */
@Slf4j
@Primary  // 기본 구현체: KoreanGreetingService
@Service
public class KoreanGreetingService implements GreetingService {

    @Override
    public String greet(String name) {
        log.info("한국어 인사말 생성: {}", name);
        return name + "님, 안녕하세요! SW프레임워크에 오신 것을 환영합니다. \uD83C\uDDF0\uD83C\uDDF7";
    }
}
