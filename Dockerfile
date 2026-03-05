# Dockerfile — Spring Boot 애플리케이션 컨테이너화
# Week 12 — Docker 컨테이너화 배포
# 사용법: ./gradlew bootJar && docker build -t swframework:1.0 .

# 1단계: 베이스 이미지 — Java 21 런타임 (JRE만 포함하여 이미지 크기 최소화)
FROM eclipse-temurin:21-jre

# 2단계: 작업 디렉토리 설정 — 이후 명령의 기준 경로
WORKDIR /app

# 3단계: 빌드된 JAR 파일을 컨테이너 내부로 복사
COPY build/libs/*.jar app.jar

# 4단계: 컨테이너가 사용할 포트 명시 (Spring Boot 기본 8080)
# 실제 포트 연결은 docker run -p 에서 수행한다
EXPOSE 8080

# 5단계: 컨테이너 시작 시 실행할 명령어
# Spring 프로파일을 docker로 지정하여 application-docker.yml 사용
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
