// Week 10 — Spring MVC 패턴 게시판 CRUD
// Week 11 — 페이징 처리
// BoardServiceImpl.java — 게시판 서비스 구현체
package kr.ac.tukorea.swframework.service;

import kr.ac.tukorea.swframework.dto.BoardDTO;
import kr.ac.tukorea.swframework.dto.PageDTO;
import kr.ac.tukorea.swframework.exception.EntityNotFoundException;
import kr.ac.tukorea.swframework.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시판 비즈니스 로직 구현체
 *
 * - @Service: Spring이 이 클래스를 서비스 빈으로 등록한다 (Week 04 DI)
 * - @Transactional: 메서드 실행 중 예외 발생 시 자동 롤백한다
 * - @Slf4j: Lombok 로깅 어노테이션 (log 변수 자동 생성)
 *
 * ExecutionTimeAspect(Week 05)에 의해 모든 메서드의 실행 시간이 자동 측정된다.
 */
@Slf4j
@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    // 생성자 주입 (DI) — @Autowired 생략 가능 (생성자가 1개일 때)
    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    @Override
    @Transactional(readOnly = true)  // 읽기 전용 트랜잭션 (성능 최적화)
    public List<BoardDTO> getListWithPaging(PageDTO pageDTO) {
        return boardMapper.findAllWithPaging(pageDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalCount(PageDTO pageDTO) {
        return boardMapper.countAll(pageDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDTO getDetail(Long id) {
        BoardDTO board = boardMapper.findById(id);
        if (board == null) {
            // DB에 해당 ID가 없으면 비즈니스 예외 발생
            // → GlobalExceptionHandler.handleNotFound()가 error/404.html을 렌더링
            throw new EntityNotFoundException(id + "번 게시글을 찾을 수 없습니다.");
        }
        return board;
    }

    @Override
    public void create(BoardDTO boardDTO) {
        boardMapper.insert(boardDTO);
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        boardMapper.update(boardDTO);
    }

    @Override
    public void remove(Long id) {
        boardMapper.delete(id);
    }
}
