// Week 02 — 개발환경 설정
// SwFrameworkApplication.java — Spring Boot 애플리케이션 진입점
package kr.ac.tukorea.swframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 애플리케이션의 시작점
 *
 * @SpringBootApplication은 다음 3개의 어노테이션을 결합한 것이다:
 * - @Configuration: 이 클래스가 설정 클래스임을 선언
 * - @EnableAutoConfiguration: 클래스패스 기반으로 자동 설정 활성화
 * - @ComponentScan: 현재 패키지 하위의 모든 컴포넌트(@Controller, @Service 등)를 자동 탐색
 */
@SpringBootApplication
public class SwFrameworkApplication {

    public static void main(String[] args) {
        // 내장 톰캣 서버를 시작하고 Spring IoC 컨테이너를 초기화한다
        SpringApplication.run(SwFrameworkApplication.class, args);
    }
}
