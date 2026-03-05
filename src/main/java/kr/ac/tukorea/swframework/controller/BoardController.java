// Week 10 — Spring MVC 패턴 게시판 CRUD
// Week 11 — 페이징 처리 + 파일 업로드/다운로드
// BoardController.java — 게시판 요청 처리 컨트롤러
package kr.ac.tukorea.swframework.controller;

import kr.ac.tukorea.swframework.dto.BoardDTO;
import kr.ac.tukorea.swframework.dto.PageDTO;
import kr.ac.tukorea.swframework.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

/**
 * 게시판 요청 처리 컨트롤러
 *
 * - @Controller: Spring MVC 컨트롤러로 등록
 * - @RequestMapping("/board"): 모든 URL에 /board 접두사 적용
 * - @Slf4j: Lombok 로깅 어노테이션 (log 변수 자동 생성)
 *
 * 주요 학습 포인트:
 * 1. @GetMapping / @PostMapping — HTTP 메서드별 매핑
 * 2. @ModelAttribute — Form 데이터를 Java 객체에 자동 바인딩
 * 3. @PathVariable — URL 경로 변수 추출
 * 4. PRG 패턴 — Post-Redirect-Get 중복 등록 방지
 * 5. 페이징 + 검색 파라미터 바인딩
 */
@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    // application.yml의 file.upload-dir 값을 자동 주입 (Week 11)
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 생성자 주입 (DI) — @Autowired 생략 가능 (생성자가 1개일 때)
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시글 목록 + 검색 + 페이징
     * GET /board/list
     *
     * @param pageDTO 페이징/검색 조건이 자동 바인딩된 DTO
     * @param model   View에 전달할 데이터
     * @return board/list.html 렌더링
     */
    @GetMapping("/list")
    public String list(@ModelAttribute("page") PageDTO pageDTO, Model model) {
        // 전체 게시글 수 조회 (검색 조건 반영)
        int totalCount = boardService.getTotalCount(pageDTO);
        pageDTO.setTotalCount(totalCount);

        // 페이징된 게시글 목록 조회
        List<BoardDTO> boardList = boardService.getListWithPaging(pageDTO);
        model.addAttribute("boardList", boardList);
        // pageDTO는 @ModelAttribute("page")로 이미 Model에 "page"라는 이름으로 등록됨

        log.info("게시글 목록 조회 - 페이지: {}, 검색: {}={}, 총 {}건",
                pageDTO.getPage(), pageDTO.getSearchType(), pageDTO.getKeyword(), totalCount);

        return "board/list";  // templates/board/list.html 렌더링
    }

    /**
     * 게시글 상세 조회
     * GET /board/detail/{id}
     *
     * @param id URL 경로에서 추출한 게시글 ID
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        BoardDTO board = boardService.getDetail(id);
        model.addAttribute("board", board);
        return "board/detail";
    }

    /**
     * 게시글 등록 폼 페이지
     * GET /board/create
     *
     * - 빈 BoardDTO 객체를 Model에 담아 전달
     * - th:object="${board}"에서 이 객체를 사용
     */
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("board", new BoardDTO());
        return "board/form";
    }

    /**
     * 게시글 등록 처리 — PRG 패턴 적용 (Post-Redirect-Get)
     * POST /board/create
     *
     * Week 11: 파일 업로드 지원 추가
     * - MultipartFile file: 첨부파일 (선택 항목, 없으면 empty)
     * - enctype="multipart/form-data" 폼에서만 정상 동작
     *
     * @param boardDTO Form 데이터가 자동 바인딩된 객체
     * @param file     첨부파일 (선택 — 미첨부 시 isEmpty() = true)
     * @return redirect URL (PRG 패턴 — 새로고침해도 POST가 재전송되지 않음)
     */
    @PostMapping("/create")
    public String create(@ModelAttribute BoardDTO boardDTO,
                         @RequestParam(value = "file", required = false) MultipartFile file)
            throws IOException {

        // 파일이 첨부된 경우에만 처리 (필수 항목 아님)
        if (file != null && !file.isEmpty()) {
            // UUID로 고유한 저장 파일명 생성 — 파일명 충돌 방지
            String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 저장 경로 생성 (uploads/ 디렉토리가 없으면 자동 생성)
            Path uploadPath = Path.of(uploadDir).resolve(savedName);
            Files.createDirectories(uploadPath.getParent());

            // 파일을 지정 경로에 저장
            file.transferTo(uploadPath);

            // DTO에 파일 정보 설정 (DB에 저장될 값)
            boardDTO.setFileName(file.getOriginalFilename()); // 원본 파일명
            boardDTO.setSavedName(savedName);                  // 서버 저장 파일명 (UUID)
            log.info("파일 업로드: {} → {}", file.getOriginalFilename(), savedName);
        }

        boardService.create(boardDTO);
        log.info("게시글 등록: {}", boardDTO.getTitle());
        return "redirect:/board/list";  // 등록 후 목록으로 이동
    }

    /**
     * 파일 다운로드
     * GET /board/download/{savedName}
     *
     * Week 11: 저장 파일명으로 파일을 찾아 원본 파일명으로 응답
     * - Content-Disposition: attachment → 브라우저가 다운로드 대화상자 표시
     *
     * @param savedName UUID 형식의 서버 저장 파일명
     * @return 파일 리소스 (없으면 404)
     */
    @GetMapping("/download/{savedName}")
    public ResponseEntity<Resource> download(@PathVariable String savedName)
            throws MalformedURLException {

        // 저장 경로에서 파일을 Resource 객체로 로드
        Path filePath = Path.of(uploadDir).resolve(savedName);
        Resource resource = new UrlResource(filePath.toUri());

        // 파일이 존재하지 않으면 404 응답
        if (!resource.exists()) {
            log.warn("파일 없음: {}", savedName);
            return ResponseEntity.notFound().build();
        }

        // Content-Disposition 헤더: "attachment; filename=저장파일명"
        // → 브라우저가 파일을 인라인 표시하지 않고 다운로드 창을 띄움
        // 주의: 파일명이 UUID 형식으로 표시됨 — 원본명 표시는 DB에서 fileName 조회 후 헤더 지정 필요
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * 게시글 수정 폼 페이지
     * GET /board/edit/{id}
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        BoardDTO board = boardService.getDetail(id);
        model.addAttribute("board", board);
        return "board/form";
    }

    /**
     * 게시글 수정 처리
     * POST /board/edit/{id}
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute BoardDTO boardDTO) {
        boardDTO.setId(id);
        boardService.modify(boardDTO);
        log.info("게시글 수정: id={}", id);
        return "redirect:/board/detail/" + id;
    }

    /**
     * 게시글 삭제
     * POST /board/delete/{id}
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.remove(id);
        log.info("게시글 삭제: id={}", id);
        return "redirect:/board/list";
    }
}
