// Week 10 — Spring MVC 패턴 게시판 CRUD
// SearchDTO.java — 검색 조건 전달 객체
package kr.ac.tukorea.swframework.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 검색 조건을 담는 DTO
 *
 * Controller에서 URL 파라미터를 자동 바인딩받고,
 * MyBatis Dynamic SQL에서 검색 조건으로 사용한다.
 *
 * 사용 예시:
 * - /board/list?searchType=title&keyword=Spring
 * - /board/list?searchType=content&keyword=게시판
 */
@Getter
@Setter
@NoArgsConstructor
public class SearchDTO {

    private String searchType;  // 검색 유형: "title" 또는 "content"
    private String keyword;     // 검색어
}
