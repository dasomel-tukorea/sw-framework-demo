// Week 10 — Spring MVC 패턴 게시판 CRUD
// Week 11 — 페이징 처리
// BoardService.java — 게시판 서비스 인터페이스
package kr.ac.tukorea.swframework.service;

import kr.ac.tukorea.swframework.dto.BoardDTO;
import kr.ac.tukorea.swframework.dto.PageDTO;

import java.util.List;

/**
 * 게시판 비즈니스 로직 인터페이스
 *
 * 인터페이스로 분리하여 구현체 교체가 용이하도록 한다.
 * (예: 테스트용 Mock 구현체, 다른 DB 연동 구현체 등)
 *
 * Week 04에서 배운 인터페이스 기반 DI의 실전 적용이다.
 */
public interface BoardService {

    /**
     * 게시글 목록 조회 (페이징 + 검색 + 정렬)
     * @param pageDTO 페이징/검색/정렬 조건
     * @return 페이징된 게시글 목록
     */
    List<BoardDTO> getListWithPaging(PageDTO pageDTO);

    /**
     * 전체 게시글 수 조회 (검색 조건 반영)
     * @param pageDTO 검색 조건
     * @return 전체 게시글 수
     */
    int getTotalCount(PageDTO pageDTO);

    /**
     * 게시글 상세 조회
     * @param id 게시글 번호
     * @return 게시글 정보
     */
    BoardDTO getDetail(Long id);

    /**
     * 게시글 등록
     * @param boardDTO 등록할 게시글 정보
     */
    void create(BoardDTO boardDTO);

    /**
     * 게시글 수정
     * @param boardDTO 수정할 게시글 정보
     */
    void modify(BoardDTO boardDTO);

    /**
     * 게시글 삭제
     * @param id 삭제할 게시글 번호
     */
    void remove(Long id);
}
