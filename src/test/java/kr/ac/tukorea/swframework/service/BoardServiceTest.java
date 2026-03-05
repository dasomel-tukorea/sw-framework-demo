// BoardServiceTest.java — 게시판 서비스 단위 테스트
// Week 13 — 테스트 코드 기초
package kr.ac.tukorea.swframework.service;

import kr.ac.tukorea.swframework.dto.BoardDTO;
import kr.ac.tukorea.swframework.dto.PageDTO;
import kr.ac.tukorea.swframework.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 게시판 서비스 단위 테스트
 * Week 13 — 테스트 코드 기초
 *
 * 테스트 패턴: given-when-then
 * - given: 사전 조건 (테스트 데이터 준비)
 * - when: 실제 실행 (메서드 호출)
 * - then: 결과 검증 (assert)
 *
 * @SpringBootTest: Spring 컨텍스트 전체 로드 (통합 테스트)
 * @ActiveProfiles("test"): H2 인메모리 DB 사용 (MySQL 없이 실행 가능)
 * @Transactional: 각 테스트 메서드 완료 후 DB 롤백 (테스트 간 격리)
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BoardServiceTest {

    @Autowired
    private BoardService boardService;  // 테스트 대상 서비스 (인터페이스 주입)

    // ============================================================
    // 게시글 목록 조회 테스트
    // ============================================================

    @Test
    @DisplayName("TC-001: 게시글 목록 조회 시 null이 아닌 리스트를 반환해야 한다")
    void 게시글_목록_조회_테스트() {
        // given: 기본 페이징 조건 (page=1, size=10)
        PageDTO pageDTO = new PageDTO();

        // when: 게시글 목록 조회
        List<BoardDTO> boardList = boardService.getListWithPaging(pageDTO);

        // then: null이 아닌 리스트 반환 (데이터가 없어도 빈 리스트)
        assertNotNull(boardList, "게시글 목록은 null이 아니어야 한다");
    }

    @Test
    @DisplayName("TC-002: 전체 게시글 수 조회 시 0 이상이어야 한다")
    void 전체_게시글_수_조회_테스트() {
        // given: 기본 검색 조건 (검색어 없음)
        PageDTO pageDTO = new PageDTO();

        // when: 전체 게시글 수 조회
        int totalCount = boardService.getTotalCount(pageDTO);

        // then: 0 이상 (음수 불가)
        assertTrue(totalCount >= 0, "전체 게시글 수는 0 이상이어야 한다");
    }

    // ============================================================
    // 게시글 등록 테스트
    // ============================================================

    @Test
    @DisplayName("TC-003: 게시글 등록 후 전체 게시글 수가 1 증가해야 한다")
    void 게시글_등록_후_조회_테스트() {
        // given: 등록할 게시글 데이터
        BoardDTO newBoard = new BoardDTO();
        newBoard.setTitle("단위 테스트 제목");
        newBoard.setContent("단위 테스트 내용입니다.");
        newBoard.setAuthor("admin");

        PageDTO pageDTO = new PageDTO();
        int beforeCount = boardService.getTotalCount(pageDTO);

        // when: 게시글 등록
        boardService.create(newBoard);

        // then: 등록 후 전체 게시글 수가 1 증가
        int afterCount = boardService.getTotalCount(pageDTO);
        assertEquals(beforeCount + 1, afterCount, "게시글 등록 후 전체 수가 1 증가해야 한다");
    }

    // ============================================================
    // 게시글 상세 조회 테스트
    // ============================================================

    @Test
    @DisplayName("TC-004: 존재하지 않는 게시글 조회 시 EntityNotFoundException이 발생해야 한다")
    void 존재하지_않는_게시글_조회_예외_테스트() {
        // given: 존재하지 않는 게시글 ID
        Long invalidId = 99999L;

        // when + then: EntityNotFoundException 발생 검증
        // assertThrows: 지정한 예외가 발생하면 테스트 통과, 미발생 시 실패
        assertThrows(EntityNotFoundException.class,
                () -> boardService.getDetail(invalidId),
                "존재하지 않는 게시글 조회 시 EntityNotFoundException이 발생해야 한다");
    }

    @Test
    @DisplayName("TC-005: 게시글 등록 후 상세 조회 시 동일한 제목이 반환되어야 한다")
    void 게시글_등록_후_상세조회_테스트() {
        // given: 등록할 게시글
        BoardDTO newBoard = new BoardDTO();
        newBoard.setTitle("상세조회 테스트 제목");
        newBoard.setContent("상세조회 테스트 내용");
        newBoard.setAuthor("admin");

        // when: 등록
        boardService.create(newBoard);

        // then: 등록된 게시글의 ID로 조회 시 제목 일치 확인
        // 가장 최근 등록된 게시글 조회 (목록 최상단)
        PageDTO pageDTO = new PageDTO();
        List<BoardDTO> list = boardService.getListWithPaging(pageDTO);
        assertFalse(list.isEmpty(), "게시글 목록이 비어있지 않아야 한다");

        BoardDTO latest = list.get(0); // 최신순 정렬이므로 첫 번째
        assertEquals("상세조회 테스트 제목", latest.getTitle(), "등록한 제목과 조회된 제목이 일치해야 한다");
        assertEquals("admin", latest.getAuthor(), "등록한 작성자와 조회된 작성자가 일치해야 한다");
    }

    // ============================================================
    // 게시글 수정 테스트
    // ============================================================

    @Test
    @DisplayName("TC-006: 게시글 수정 후 수정된 내용이 반영되어야 한다")
    void 게시글_수정_테스트() {
        // given: 게시글 등록
        BoardDTO newBoard = new BoardDTO();
        newBoard.setTitle("수정 전 제목");
        newBoard.setContent("수정 전 내용");
        newBoard.setAuthor("admin");
        boardService.create(newBoard);

        // 등록된 게시글 ID 조회
        PageDTO pageDTO = new PageDTO();
        List<BoardDTO> list = boardService.getListWithPaging(pageDTO);
        Long createdId = list.get(0).getId();

        // when: 게시글 수정
        BoardDTO updateDTO = new BoardDTO();
        updateDTO.setId(createdId);
        updateDTO.setTitle("수정 후 제목");
        updateDTO.setContent("수정 후 내용");
        boardService.modify(updateDTO);

        // then: 수정된 내용 확인
        BoardDTO updated = boardService.getDetail(createdId);
        assertEquals("수정 후 제목", updated.getTitle(), "수정 후 제목이 반영되어야 한다");
        assertEquals("수정 후 내용", updated.getContent(), "수정 후 내용이 반영되어야 한다");
    }

    // ============================================================
    // 게시글 삭제 테스트
    // ============================================================

    @Test
    @DisplayName("TC-007: 게시글 삭제 후 전체 게시글 수가 1 감소해야 한다")
    void 게시글_삭제_테스트() {
        // given: 게시글 등록
        BoardDTO newBoard = new BoardDTO();
        newBoard.setTitle("삭제 테스트 게시글");
        newBoard.setContent("삭제 테스트 내용");
        newBoard.setAuthor("admin");
        boardService.create(newBoard);

        PageDTO pageDTO = new PageDTO();
        int beforeCount = boardService.getTotalCount(pageDTO);

        // 등록된 게시글 ID 조회
        List<BoardDTO> list = boardService.getListWithPaging(pageDTO);
        Long createdId = list.get(0).getId();

        // when: 게시글 삭제
        boardService.remove(createdId);

        // then: 전체 게시글 수 1 감소
        int afterCount = boardService.getTotalCount(pageDTO);
        assertEquals(beforeCount - 1, afterCount, "삭제 후 전체 게시글 수가 1 감소해야 한다");

        // 삭제된 게시글 조회 시 예외 발생 확인
        Long deletedId = createdId;
        assertThrows(EntityNotFoundException.class,
                () -> boardService.getDetail(deletedId),
                "삭제된 게시글 조회 시 EntityNotFoundException이 발생해야 한다");
    }

    // ============================================================
    // 페이징 + 검색 테스트
    // ============================================================

    @Test
    @DisplayName("TC-008: 페이지당 게시글 수가 size 이하여야 한다")
    void 페이지당_게시글_수_테스트() {
        // given: page=1, size=5 (5개씩)
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(1);
        pageDTO.setSize(5);

        // when: 게시글 목록 조회
        List<BoardDTO> boardList = boardService.getListWithPaging(pageDTO);

        // then: 반환된 목록 크기가 size(5) 이하
        assertTrue(boardList.size() <= 5, "페이지당 게시글 수는 size(5) 이하여야 한다");
    }

    @Test
    @DisplayName("TC-009: 제목 검색 시 검색어를 포함하는 결과만 반환되어야 한다")
    void 제목_검색_테스트() {
        // given: 검색 대상 게시글 등록
        BoardDTO target = new BoardDTO();
        target.setTitle("Spring Boot 검색 테스트 게시글");
        target.setContent("내용");
        target.setAuthor("admin");
        boardService.create(target);

        // given: 검색 조건
        PageDTO pageDTO = new PageDTO();
        pageDTO.setSearchType("title");
        pageDTO.setKeyword("Spring Boot 검색 테스트");

        // when: 검색 조건으로 목록 조회
        List<BoardDTO> result = boardService.getListWithPaging(pageDTO);

        // then: 검색어를 포함한 결과가 1건 이상 반환
        assertFalse(result.isEmpty(), "검색 결과가 1건 이상이어야 한다");
        assertTrue(result.stream().allMatch(b -> b.getTitle().contains("Spring Boot 검색 테스트")),
                "모든 결과의 제목이 검색어를 포함해야 한다");
    }

    @Test
    @DisplayName("TC-010: 내용 검색 시 검색어를 포함하는 결과만 반환되어야 한다")
    void 내용_검색_테스트() {
        // given: 내용에 특정 키워드 포함 게시글 등록
        BoardDTO target = new BoardDTO();
        target.setTitle("내용검색 테스트 제목");
        target.setContent("MyBatis content 검색 테스트 내용입니다.");
        target.setAuthor("guest");
        boardService.create(target);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setSearchType("content");
        pageDTO.setKeyword("content 검색 테스트");

        // when: 내용 검색
        List<BoardDTO> result = boardService.getListWithPaging(pageDTO);

        // then: 1건 이상 반환
        assertFalse(result.isEmpty(), "내용 검색 결과가 1건 이상이어야 한다");
    }

    @Test
    @DisplayName("TC-011: 존재하지 않는 검색어로 검색 시 빈 목록을 반환해야 한다")
    void 검색_결과_없음_테스트() {
        // given: 절대 존재하지 않을 검색어
        PageDTO pageDTO = new PageDTO();
        pageDTO.setSearchType("title");
        pageDTO.setKeyword("ZZZZZZZ절대없는제목ZZZZZZZ");

        // when: 검색
        List<BoardDTO> result = boardService.getListWithPaging(pageDTO);

        // then: 빈 목록 반환
        assertTrue(result.isEmpty(), "존재하지 않는 검색어로 검색 시 빈 목록이어야 한다");

        // 검색 결과 수도 0
        int count = boardService.getTotalCount(pageDTO);
        assertEquals(0, count, "검색 결과 수도 0이어야 한다");
    }
}
