// Week 04 — IoC/DI
// AppConfig.java — 애플리케이션 설정 클래스 (@Bean 수동 등록)
package kr.ac.tukorea.swframework.config;

import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션 설정 클래스
 *
 * - @Configuration: 이 클래스가 Spring 설정 파일임을 선언
 * - @Bean: 메서드의 반환 객체를 Spring Bean으로 수동 등록
 *
 * 사용 시점:
 * - 외부 라이브러리 객체를 Bean으로 등록할 때
 * - 복잡한 초기화 로직이 필요한 객체를 등록할 때
 * - 내가 직접 만든 클래스는 @Component/@Service를 사용하는 것이 일반적
 *
 * 완성 프로젝트에서는 게시판 도메인 위주이므로
 * 추가적인 @Bean 등록이 필요하면 여기에 작성한다.
 */
@Configuration
public class AppConfig {

    // 필요 시 외부 라이브러리 Bean 등록
    // 예: RestTemplate, ObjectMapper 커스터마이징 등
}
