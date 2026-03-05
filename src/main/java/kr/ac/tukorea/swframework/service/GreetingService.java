// Week 04 — IoC/DI (의존성 주입) 학습용 인터페이스
// Controller는 이 인터페이스에만 의존 → 구현체 교체 가능 (OCP)
package kr.ac.tukorea.swframework.service;

/**
 * 인사말 서비스 인터페이스
 *
 * 학습 포인트 (Week 04):
 * 1. 인터페이스 타입으로 의존성 선언 → 구현체 교체 시 Controller 수정 불필요
 * 2. @Primary로 기본 구현체 지정
 * 3. @Qualifier("beanName")으로 특정 구현체 선택 가능
 */
public interface GreetingService {
    /**
     * 이름을 받아 인사말 문자열 반환
     * @param name 인사할 대상 이름
     * @return 인사말 문자열
     */
    String greet(String name);
}
