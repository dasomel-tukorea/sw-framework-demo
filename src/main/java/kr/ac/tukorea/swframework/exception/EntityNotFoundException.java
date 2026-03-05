// Week 10 — 전역 예외 처리
// EntityNotFoundException.java — 요청한 데이터가 없을 때 발생하는 비즈니스 예외
// 프로젝트 경로: src/main/java/kr/ac/tukorea/swframework/exception/EntityNotFoundException.java
package kr.ac.tukorea.swframework.exception;

/**
 * 요청한 데이터가 DB에 존재하지 않을 때 발생하는 비즈니스 예외
 *
 * RuntimeException을 상속받아 @Transactional 롤백 대상이 된다.
 *
 * 사용 예:
 *   if (board == null) {
 *       throw new EntityNotFoundException(id + "번 게시글을 찾을 수 없습니다.");
 *   }
 *
 * GlobalExceptionHandler가 이 예외를 잡아 error/404.html을 렌더링한다.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * @param message 사용자에게 전달할 에러 메시지
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
