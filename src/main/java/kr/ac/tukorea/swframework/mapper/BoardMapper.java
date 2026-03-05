// Week 09 — MyBatis / Week 10 — Spring MVC / Week 11 — 페이징
// BoardMapper.java — MyBatis Mapper 인터페이스
package kr.ac.tukorea.swframework.mapper;

import kr.ac.tukorea.swframework.dto.BoardDTO;
import kr.ac.tukorea.swframework.dto.PageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 게시판 CRUD용 MyBatis Mapper 인터페이스
 *
 * - @Mapper: MyBatis가 런타임에 자동으로 구현체를 생성한다
 * - BoardMapper.xml의 SQL과 1:1로 매핑된다
 * - namespace는 이 인터페이스의 FQCN(Fully Qualified Class Name)과 일치해야 한다
 */
@Mapper
public interface BoardMapper {

    /**
     * 게시글 목록 조회 (검색 + 정렬 + 페이징)
     * @param pageDTO 페이징/검색/정렬 조건
     * @return 페이징된 게시글 목록
     */
    List<BoardDTO> findAllWithPaging(PageDTO pageDTO);

    /**
     * 전체 게시글 수 조회 (검색 조건 반영)
     * @param pageDTO 검색 조건
     * @return 전체 게시글 수
     */
    int countAll(PageDTO pageDTO);

    /**
     * 게시글 상세 조회
     * @param id 게시글 번호
     * @return 게시글 정보
     */
    BoardDTO findById(Long id);

    /**
     * 게시글 등록
     * @param boardDTO 등록할 게시글 정보
     */
    void insert(BoardDTO boardDTO);

    /**
     * 게시글 수정
     * @param boardDTO 수정할 게시글 정보
     */
    void update(BoardDTO boardDTO);

    /**
     * 게시글 삭제
     * @param id 삭제할 게시글 번호
     */
    void delete(Long id);

    // ============================================================
    // Dynamic SQL 심화 — Week 09 심화 실습
    // ============================================================

    /**
     * null이 아닌 컬럼만 선택적으로 UPDATE (<set>)
     * @param boardDTO 수정할 게시글 정보 (null 필드는 수정 제외)
     * @return 수정된 행 수
     */
    int updateSelective(BoardDTO boardDTO);

    /**
     * 여러 ID로 한 번에 조회 (<foreach> — IN 절)
     * @param ids 조회할 게시글 번호 목록
     * @return 게시글 목록
     */
    List<BoardDTO> findByIds(@Param("ids") List<Long> ids);

    /**
     * 제목/작성자 조건 복합 검색 (<trim> — 커스텀 접두사/접미사)
     * @param params 검색 조건 Map (title, author)
     * @return 검색된 게시글 목록
     */
    List<BoardDTO> findByCondition(Map<String, Object> params);
}
