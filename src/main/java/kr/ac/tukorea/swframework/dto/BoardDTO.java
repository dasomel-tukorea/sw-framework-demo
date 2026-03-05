// Week 10 — Spring MVC 패턴 게시판 CRUD
// Week 11 — 첨부파일 필드 추가 (fileName, savedName)
// BoardDTO.java — 게시글 데이터 전달 객체 (Data Transfer Object)
package kr.ac.tukorea.swframework.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 게시글 정보를 계층 간에 전달하기 위한 DTO 클래스
 *
 * Controller <-> Service <-> Mapper 사이에서 데이터를 주고받는다.
 *
 * - @Getter: 모든 필드의 Getter 메서드 자동 생성
 * - @Setter: 모든 필드의 Setter 메서드 자동 생성
 * - @NoArgsConstructor: 기본 생성자 자동 생성 (MyBatis 결과 매핑에 필수)
 *
 * DTO 변환 패턴 (Week 10 심화):
 * - of(): 개별 필드로 BoardDTO 생성하는 정적 팩토리 메서드
 * - forUpdate(): 수정용 DTO 생성 — id + 변경할 필드만 포함
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardDTO {

    private Long id;                   // 게시글 번호 (PK, AUTO_INCREMENT)
    private String title;              // 제목
    private String content;            // 내용
    private String author;             // 작성자
    private LocalDateTime createdAt;   // 작성일 (DB에서 자동 생성)
    private LocalDateTime updatedAt;   // 수정일 (DB에서 자동 갱신)

    // === 첨부파일 필드 (Week 11) ===
    // MyBatis map-underscore-to-camel-case=true 설정으로 file_name → fileName 자동 변환
    private String fileName;           // 원본 파일명 (사용자에게 보여줄 이름)
    private String savedName;          // 서버 저장 파일명 (UUID + 원본명 형식, 파일명 충돌 방지)

    // ============================================================
    // DTO 변환 패턴 — Week 10 심화 실습
    // ============================================================

    /**
     * 정적 팩토리 메서드 — 개별 필드로 BoardDTO 생성
     *
     * 변환 로직을 DTO에 집중시켜 Service/Controller의 코드를 단순하게 유지한다.
     *
     * 사용 예:
     *   BoardDTO board = BoardDTO.of(1L, "제목", "내용", "홍길동");
     *
     * @param id      게시글 번호
     * @param title   제목
     * @param content 내용
     * @param author  작성자
     * @return 생성된 BoardDTO 객체
     */
    public static BoardDTO of(Long id, String title, String content, String author) {
        BoardDTO dto = new BoardDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setContent(content);
        dto.setAuthor(author);
        return dto;
    }

    /**
     * 수정용 DTO 생성 — id + 변경할 필드만 포함
     *
     * 수정 처리 시 URL PathVariable에서 오는 id와 Form에서 오는 title/content를 합친다.
     * Controller에서 id 설정을 실수로 빠뜨리는 버그를 방지한다.
     *
     * 사용 예:
     *   BoardDTO updateDTO = BoardDTO.forUpdate(id, boardDTO);
     *   boardService.modify(updateDTO);
     *
     * @param id   URL PathVariable에서 추출한 게시글 번호
     * @param form Form 데이터가 바인딩된 BoardDTO (title, content 포함)
     * @return id가 설정된 수정용 BoardDTO 객체
     */
    public static BoardDTO forUpdate(Long id, BoardDTO form) {
        BoardDTO dto = new BoardDTO();
        dto.setId(id);
        dto.setTitle(form.getTitle());
        dto.setContent(form.getContent());
        return dto;
    }
}
