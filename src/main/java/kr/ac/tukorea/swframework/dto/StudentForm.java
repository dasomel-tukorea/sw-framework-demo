package kr.ac.tukorea.swframework.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 학생 등록/수정 폼 DTO (Data Transfer Object)
 *
 * 설계 원칙:
 * - 폼 입력값 검증 어노테이션을 포함한 전용 DTO
 * - 도메인 클래스(Student)와 분리하여 관리 (현업 관행)
 * - 폼 ↔ Controller 간 데이터 전달 전용; 도메인 로직 없음
 *
 * Bean Validation (jakarta.validation.constraints):
 * - Spring Boot 3.x 기준 → jakarta.validation 사용 (javax.validation 사용 금지)
 * - 의존성: spring-boot-starter-validation
 *
 * @author 이기하 교수
 * @since 2026-04-03 (6주차)
 */
public class StudentForm {

    // 이름: 필수, 2~20자
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Size(min = 2, max = 20, message = "이름은 2~20자 사이로 입력해주세요.")
    private String name;

    // 학번: 필수, 9자리 숫자
    @NotBlank(message = "학번은 필수 입력 항목입니다.")
    @Pattern(regexp = "\\d{9}", message = "학번은 9자리 숫자로 입력해주세요.")
    private String studentId;

    // 이메일: 선택 입력 (@NotBlank 없음), 형식만 검증
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    private String email;

    /**
     * 기본 생성자
     * - Spring의 @ModelAttribute 데이터 바인딩에 필수
     * - Thymeleaf에서 빈 폼을 렌더링할 때도 사용
     */
    public StudentForm() {
    }

    // ============================
    // Getter / Setter
    // - Thymeleaf th:field 데이터 바인딩에 필수
    // - 필드명과 HTML Form의 name 속성이 일치해야 함
    // ============================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 디버깅용 toString
     * - Controller에서 바인딩된 데이터를 로그로 확인할 때 유용
     */
    @Override
    public String toString() {
        return "StudentForm{" +
                "name='" + name + '\'' +
                ", studentId='" + studentId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
