import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint").version("12.3.0") // ktlint
    id("jacoco") // jacoco
    id("com.epages.restdocs-api-spec") version "0.19.4"
}

group = "com.brake"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val jwtVersion = "0.12.6"
extra["springCloudVersion"] = "2025.0.0"
val flywayVersion = "10.16.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Kotlin-Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jwtVersion")

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.4")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports.xml.required.set(true)

    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "**/common/**",
                    )
                }
            },
        ),
    )
}

openapi3 {
    setServer("/")
    title = "Brake API"
    description = "Brake API description"
    version = "0.1.0"
    format = "yaml"
}

tasks.register("generateSwagger") {
    dependsOn("openapi3")
    doFirst {
        val swaggerUIFile = file("build/api-spec/openapi3.yaml")
        val securitySchemesContent =
            """
              securitySchemes:
                bearerAuth:
                  type: http
                  scheme: bearer
                  bearerFormat: JWT
                  name: Authorization
                  in: header
                  description: "액세스 토큰을 기입해 주세요"
            security:
              - bearerAuth: []
            """.trimIndent()

        swaggerUIFile.appendText(securitySchemesContent)
    }
}

// Swagger 문서 복사
tasks.register<Copy>("copyToSwagger") {
    dependsOn("generateSwagger")
    delete("src/main/resources/static/swagger/openapi3.yaml")
    copy {
        from("build/api-spec/openapi3.yaml")
        into("src/main/resources/static/swagger/")
    }
}

tasks.named("bootJar") {
    dependsOn("copyToSwagger")
}

tasks.withType<Test> {
    finalizedBy(tasks.jacocoTestReport)
    useJUnitPlatform()
}

ktlint {
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
    }
}

tasks.named<ProcessResources>("processResources") {
    dependsOn("copy main env")
}

tasks.named<BootJar>("bootJar") {
    dependsOn("generateSwagger")

    from("build/api-spec/openapi3.yaml") {
        into("static/swagger")
    }
}

tasks.named<ProcessResources>("processTestResources") {
    dependsOn("copy test env")
}

tasks.register<Copy>("copy main env") {
    from("YAPP-ENV") {
        include("*")
        exclude("application-test.yml", "README.md")
    }
    into("src/main/resources")

    onlyIf {
        gradle.startParameter.taskNames.none { it.contains("test") }
    }

    doLast {
        logger.lifecycle("✅ 메인 환경변수 복사 완료")
    }
}

tasks.register<Copy>("copy test env") {
    from("YAPP-ENV") {
        include("application-test.yml")
        rename("application-test.yml", "application.yml")
    }
    into("src/test/resources")

    doLast {
        logger.lifecycle("✅ 테스트 환경변수 복사 완료")
    }
}
