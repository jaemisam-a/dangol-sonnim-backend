# STEP1) Docker에 해당 이미지를 실행시킬 때 jdk11 버전으로 세팅
FROM openjdk:11

# STEP2) 환경변수 설정 (build.gradle, setting.gradle 참고)
# JAR 패키지의 이름과 버전은 항상 프로젝트와 동일하게 설정
ENV NAME=dangol-sonnim-backend
ENV VERSION=0.0.1-SNAPSHOT

# STEP3) gradlew 명령어로 clean
CMD ["./gradlew", "clean", "build"]

# STEP4) JAR 파일의 위치
ARG JAR_FILE=./build/libs/${NAME}-${VERSION}.jar

# STEP5) JAR 파일을 app.jar로 변경
COPY ${JAR_FILE} app.jar

# STEP6) execute java command
ENTRYPOINT ["java", "-jar", "/app.jar"]