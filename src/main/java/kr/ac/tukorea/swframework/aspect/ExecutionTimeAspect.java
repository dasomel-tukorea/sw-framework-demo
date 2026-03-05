// Week 05 — AOP & Bean 생명주기
// ExecutionTimeAspect.java — 메서드 실행 시간 측정 Aspect
package kr.ac.tukorea.swframework.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 메서드 실행 시간 측정 Aspect
 *
 * - @Aspect: 이 클래스가 AOP 관점(Aspect)임을 선언
 * - @Component: Spring Bean으로 등록하여 컨테이너가 관리하도록 함
 * - @Slf4j: Lombok이 제공하는 로깅 어노테이션 (Logger 자동 생성)
 *
 * 동작 원리:
 *   1. Service 패키지 하위의 모든 메서드 호출을 가로챈다 (Pointcut)
 *   2. 메서드 실행 전 시작 시간을 기록한다
 *   3. joinPoint.proceed()로 실제 메서드를 실행한다
 *   4. 메서드 실행 후 종료 시간을 기록하고, 소요 시간을 로그로 출력한다
 */
@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    /**
     * @Around 어드바이스: 메서드 실행 전후를 모두 감싼다 (가장 강력한 Advice)
     *
     * Pointcut 표현식 분석:
     *   execution(* kr.ac.tukorea.swframework.service..*.*(..))
     *   |         | |                                    | | +- 파라미터: 모든 파라미터
     *   |         | |                                    | +--- 메서드: 모든 메서드
     *   |         | |                                    +----- 클래스: 모든 클래스
     *   |         | +----------------------------------------- 패키지: service 및 하위
     *   |         +------------------------------------------- 반환타입: 모든 타입 (*)
     *   +----------------------------------------------------- Advice 유형: Around
     *
     * @param joinPoint 대상 메서드의 정보를 담고 있는 객체
     * @return 대상 메서드의 원래 반환값
     * @throws Throwable 대상 메서드에서 발생할 수 있는 예외
     */
    @Around("execution(* kr.ac.tukorea.swframework.service..*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1. 메서드 실행 전 — 시작 시간 기록
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        log.info(">>> [시작] {}", methodName);

        try {
            // 2. 실제 메서드 실행 (반드시 proceed() 호출 필요!)
            Object result = joinPoint.proceed();

            // 3. 메서드 실행 후 — 종료 시간 기록 및 소요 시간 계산
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("<<< [종료] {} | 실행 시간: {}ms", methodName, executionTime);

            return result; // 원래 메서드의 반환값을 그대로 전달

        } catch (Throwable ex) {
            // 예외 발생 시에도 실행 시간을 기록
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.error("<!> [예외] {} | 실행 시간: {}ms | 예외: {}",
                    methodName, executionTime, ex.getMessage());
            throw ex; // 예외를 다시 던져서 정상적인 예외 처리 흐름 유지
        }
    }
}
