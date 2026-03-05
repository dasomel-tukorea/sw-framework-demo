// PageDTOTest.java — PageDTO 단위 테스트
// Week 13 — 테스트 코드 기초
package kr.ac.tukorea.swframework.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PageDTO 단위 테스트
 *
 * Spring 컨텍스트 없이 순수 Java 객체 테스트 (빠른 실행)
 * getOffset(), getTotalPages(), hasPrev(), hasNext(), setPage() 검증
 */
class PageDTOTest {

    // ============================================================
    // offset 계산 테스트
    // ============================================================

    @Test
    @DisplayName("TC-001: 1페이지 offset은 0이어야 한다")
    void 첫페이지_offset_계산() {
        // given
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(1);
        pageDTO.setSize(10);

        // when + then: (1-1) * 10 = 0
        assertEquals(0, pageDTO.getOffset(), "1페이지 offset은 0");
    }

    @Test
    @DisplayName("TC-002: 2페이지 offset은 10이어야 한다 (size=10)")
    void 두번째페이지_offset_계산() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(2);
        pageDTO.setSize(10);

        // (2-1) * 10 = 10
        assertEquals(10, pageDTO.getOffset(), "2페이지 offset은 10");
    }

    @Test
    @DisplayName("TC-003: 3페이지 offset은 20이어야 한다 (size=10)")
    void 세번째페이지_offset_계산() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(3);
        pageDTO.setSize(10);

        assertEquals(20, pageDTO.getOffset());
    }

    @Test
    @DisplayName("TC-004: size=5, page=3이면 offset은 10이어야 한다")
    void 사이즈5_3페이지_offset() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(3);
        pageDTO.setSize(5);

        // (3-1) * 5 = 10
        assertEquals(10, pageDTO.getOffset());
    }

    // ============================================================
    // totalPages 계산 테스트
    // ============================================================

    @Test
    @DisplayName("TC-005: totalCount=25, size=10이면 totalPages=3이어야 한다")
    void 전체페이지수_올림계산() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setSize(10);
        pageDTO.setTotalCount(25);

        // ceil(25 / 10.0) = ceil(2.5) = 3
        assertEquals(3, pageDTO.getTotalPages());
    }

    @Test
    @DisplayName("TC-006: totalCount=30, size=10이면 totalPages=3이어야 한다")
    void 전체페이지수_나누어떨어지는경우() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setSize(10);
        pageDTO.setTotalCount(30);

        assertEquals(3, pageDTO.getTotalPages());
    }

    @Test
    @DisplayName("TC-007: totalCount=0이면 totalPages=0이어야 한다")
    void 전체페이지수_데이터없는경우() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setSize(10);
        pageDTO.setTotalCount(0);

        assertEquals(0, pageDTO.getTotalPages());
    }

    @Test
    @DisplayName("TC-008: totalCount=1이면 totalPages=1이어야 한다")
    void 전체페이지수_게시글1건() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setSize(10);
        pageDTO.setTotalCount(1);

        assertEquals(1, pageDTO.getTotalPages());
    }

    // ============================================================
    // hasPrev / hasNext 테스트
    // ============================================================

    @Test
    @DisplayName("TC-009: 1페이지에서는 이전 페이지가 없어야 한다")
    void 첫페이지_이전페이지_없음() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(1);
        pageDTO.setSize(10);
        pageDTO.setTotalCount(25);

        assertFalse(pageDTO.hasPrev(), "1페이지에는 이전 페이지 없음");
    }

    @Test
    @DisplayName("TC-010: 2페이지에서는 이전 페이지가 있어야 한다")
    void 두번째페이지_이전페이지_있음() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(2);
        pageDTO.setSize(10);
        pageDTO.setTotalCount(25);

        assertTrue(pageDTO.hasPrev(), "2페이지에는 이전 페이지 있음");
    }

    @Test
    @DisplayName("TC-011: 마지막 페이지에서는 다음 페이지가 없어야 한다")
    void 마지막페이지_다음페이지_없음() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(3);       // 3페이지 = 마지막
        pageDTO.setSize(10);
        pageDTO.setTotalCount(25); // totalPages = 3

        assertFalse(pageDTO.hasNext(), "마지막 페이지에는 다음 페이지 없음");
    }

    @Test
    @DisplayName("TC-012: 중간 페이지에서는 다음 페이지가 있어야 한다")
    void 중간페이지_다음페이지_있음() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(2);
        pageDTO.setSize(10);
        pageDTO.setTotalCount(25); // totalPages = 3

        assertTrue(pageDTO.hasNext(), "2페이지에는 다음 페이지 있음");
    }

    // ============================================================
    // setPage 최소값 보장 테스트
    // ============================================================

    @Test
    @DisplayName("TC-013: setPage(0) 호출 시 page는 1로 보정되어야 한다")
    void setPage_0이하_보정() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(0);

        // Math.max(0, 1) = 1
        assertEquals(1, pageDTO.getPage(), "0 입력 시 1로 보정");
    }

    @Test
    @DisplayName("TC-014: setPage(-5) 호출 시 page는 1로 보정되어야 한다")
    void setPage_음수_보정() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(-5);

        assertEquals(1, pageDTO.getPage(), "음수 입력 시 1로 보정");
    }

    @Test
    @DisplayName("TC-015: 기본값 확인 — page=1, size=10")
    void 기본값_확인() {
        PageDTO pageDTO = new PageDTO();

        assertEquals(1, pageDTO.getPage(), "기본 page는 1");
        assertEquals(10, pageDTO.getSize(), "기본 size는 10");
        assertEquals(0, pageDTO.getOffset(), "기본 offset은 0");
    }
}
