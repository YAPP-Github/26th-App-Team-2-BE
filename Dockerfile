FROM eclipse-temurin:21-jdk-alpine AS build
ARG MODULE_NAME=brake-api
WORKDIR /app

# Gradle Wrapper 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
COPY ${MODULE_NAME}/build.gradle.kts ./${MODULE_NAME}/

RUN chmod +x ./gradlew

# 의존성 파일 복사
COPY build.gradle.kts settings.gradle.kts ./

# 의존성 다운로드 (캐시용)
RUN ./gradlew :${MODULE_NAME}:dependencies --no-daemon

# 소스코드 전체 복사
COPY . .

# 지정한 모듈 bootJar 실행
RUN ./gradlew :${MODULE_NAME}:bootJar --parallel

FROM eclipse-temurin:21-jdk-alpine
ARG MODULE_NAME=brake-api
WORKDIR /app

RUN apk --no-cache add curl

COPY --from=build /app/${MODULE_NAME}/build/libs/${MODULE_NAME}-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Xms512m", "-Xmx1g", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
