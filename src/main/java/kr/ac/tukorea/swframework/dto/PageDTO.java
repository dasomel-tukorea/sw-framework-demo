// Week 11 — 페이징 처리
// PageDTO.java — 페이징 + 검색 + 정렬 조건 전달 객체
package kr.ac.tukorea.swframework.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 페이징 + 검색 + 정렬 조건을 담는 DTO
 *
 * Controller에서 파라미터를 바인딩받고, MyBatis에서 SQL 파라미터로 사용한다.
 *
 * 핵심 공식: offset = (page - 1) * size
 * 예) page=1, size=10 → offset=0  (1~10번 게시글)
 *     page=2, size=10 → offset=10 (11~20번 게시글)
 */
@Getter
@Setter
public class PageDTO {

    private int page = 1;          // 현재 페이지 (기본값 1)
    private int size = 10;         // 페이지당 게시글 수 (기본값 10)
    private int totalCount;        // 전체 게시글 수
    private String searchType;     // 검색 유형 (title / content)
    private String keyword;        // 검색어
    private String sortBy;         // 정렬 기준 (id / title / author)

    // 기본 생성자
    public PageDTO() {}

    /**
     * 오프셋 계산 — MyBatis에서 #{offset}으로 접근 가능
     * Getter 메서드이므로 Thymeleaf에서도 ${page.offset}으로 사용 가능
     */
    public int getOffset() {
        return (page - 1) * size;
    }

    /**
     * 전체 페이지 수 계산 — 올림 처리
     */
    public int getTotalPages() {
        return (int) Math.ceil((double) totalCount / size);
    }

    /**
     * 이전 페이지 존재 여부
     */
    public boolean hasPrev() {
        return page > 1;
    }

    /**
     * 다음 페이지 존재 여부
     */
    public boolean hasNext() {
        return page < getTotalPages();
    }

    /**
     * 페이지 값 설정 시 최소 1페이지를 보장
     */
    public void setPage(int page) {
        this.page = Math.max(page, 1);
    }
}
