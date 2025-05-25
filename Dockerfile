FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Gradle Wrapper 복사
COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew

# 의존성 파일 복사 및 다운로드
COPY build.gradle.kts .
COPY settings.gradle.kts .

# 의존성 다운로드 (캐시용)
RUN ./gradlew --no-daemon dependencies

# 소스코드 복사 및 애플리케이션 빌드
# Layered Jar 활성화를 위해 bootJar 태스크를 사용
COPY . .
RUN ./gradlew --no-daemon clean build

# Layer Tools를 사용하여 Jar 파일에서 계층 분리
WORKDIR /app/build/libs
RUN java -Djarmode=layertools -jar *.jar extract

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Layer 별 복사 변경되지 않았다면 복사 안 함
COPY --from=build /app/build/libs/dependencies/ ./
COPY --from=build /app/build/libs/spring-boot-loader/ ./
COPY --from=build /app/build/libs/snapshot-dependencies/ ./
COPY --from=build /app/build/libs/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
