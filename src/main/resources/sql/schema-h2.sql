-- schema-h2.sql — H2 인메모리 DB용 테이블 생성
-- 프로파일: h2 (MySQL 호환 모드)
-- 실행: ./gradlew bootRun --args='--spring.profiles.active=h2'
--
-- 주의: 테스트 환경에서 여러 Spring Context가 같은 H2 DB를 공유하는 경우
--       data.sql의 INSERT가 중복 실행될 수 있으므로 DROP으로 매번 초기화한다.

DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS users;

-- ============================================
-- 사용자 테이블
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(100) NOT NULL,
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

-- ============================================
-- 게시판 테이블
-- Week 11 — file_name, saved_name 컬럼 추가
-- ============================================
CREATE TABLE IF NOT EXISTS board (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    title      VARCHAR(200) NOT NULL,
    content    TEXT         NOT NULL,
    author     VARCHAR(100) NOT NULL,
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    file_name  VARCHAR(255) NULL,
    saved_name VARCHAR(255) NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_created_at ON board (created_at DESC);
