// Week 07 — 세션 처리 (로그인/로그아웃)
// Week 06 — 다국어 처리 (i18n)
// WebConfig.java — Spring MVC 설정 클래스 (인터셉터 등록)
package kr.ac.tukorea.swframework.config;

import kr.ac.tukorea.swframework.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Spring MVC 설정 클래스
 *
 * WebMvcConfigurer를 구현하여 Spring MVC의 동작을 커스터마이징한다.
 * 여기서는 인터셉터를 등록하여 비로그인 사용자의 접근을 차단한다.
 *
 * 주의사항:
 * - @Configuration 어노테이션 필수 (Spring이 설정 클래스로 인식)
 * - excludePathPatterns에 /login을 반드시 포함 (무한 리다이렉트 방지)
 * - 정적 리소스(CSS/JS/이미지)도 제외해야 페이지가 정상 렌더링됨
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 인터셉터 등록
     *
     * addPathPatterns("/**")     — 모든 경로에 인터셉터 적용
     * excludePathPatterns(...)   — 아래 경로는 인터셉터에서 제외
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")              // 모든 경로에 적용
                .excludePathPatterns(
                        "/", "/login", "/logout",     // 홈, 로그인/로그아웃 페이지
                        "/css/**", "/js/**",          // 정적 리소스 (CSS, JavaScript)
                        "/images/**",                 // 정적 리소스 (이미지)
                        "/error"                      // Spring Boot 기본 에러 페이지
                );
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * 다국어 처리 — LocaleResolver
     * CookieLocaleResolver: 선택된 언어를 쿠키에 저장 (브라우저 재방문 시에도 유지)
     * 대안: SessionLocaleResolver (세션 기반, 탭마다 독립 유지)
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver("lang");
        resolver.setDefaultLocale(Locale.KOREA);
        return resolver;
    }

    /**
     * 다국어 처리 — LocaleChangeInterceptor
     * ?lang=ko 또는 ?lang=en 파라미터로 언어 전환
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /*
     * [참고] 인터셉터 vs 서블릿 필터 vs AOP
     *
     * | 구분 | 서블릿 필터 | 인터셉터 | AOP |
     * |---|---|---|---|
     * | 실행 위치 | DispatcherServlet 이전 | DispatcherServlet 이후 | 메서드 실행 전후 |
     * | 적용 대상 | 모든 요청 | Controller 요청만 | Bean 메서드 |
     * | Spring 활용 | Spring Bean 사용 어려움 | Spring Bean 사용 가능 | Spring Bean 완전 지원 |
     * | 주 용도 | 인코딩, 보안 | 로그인 체크, 로깅 | 트랜잭션, 로깅 |
     *
     * → 이 과목에서는 로그인 체크에 인터셉터를 사용 (Spring MVC에 최적화)
     */
}
