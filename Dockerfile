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
RUN ./gradlew --no-daemon dependencies

# 소스코드 전체 복사
COPY . .

# 지정한 모듈 bootJar 실행
RUN ./gradlew :${MODULE_NAME}:bootJar --parallel

# Layer Tools를 사용하여 Jar 파일에서 계층 분리
WORKDIR /app/${MODULE_NAME}/build/libs
RUN java -Djarmode=layertools -jar ${MODULE_NAME}-*.jar extract

# 실제 실행용 이미지
FROM eclipse-temurin:21-jdk-alpine
ARG MODULE_NAME=brake-api
WORKDIR /app

RUN apk --no-cache add curl

# Layer 별 복사
COPY --from=build /app/${MODULE_NAME}/build/libs/dependencies/ ./
COPY --from=build /app/${MODULE_NAME}/build/libs/spring-boot-loader/ ./
COPY --from=build /app/${MODULE_NAME}/build/libs/snapshot-dependencies/ ./
COPY --from=build /app/${MODULE_NAME}/build/libs/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
