plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint").version("12.3.0")
    id("jacoco")
}

repositories {
    mavenCentral()
}

group = "com.brake"
version = "0.0.1-SNAPSHOT"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    ktlint {
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.JSON)
        }
    }

    dependencies {
        // Kotlin-Logging
        implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
    }
}

val jwtVersion = "0.12.6"
extra["springCloudVersion"] = "2025.0.0"
val flywayVersion = "10.16.0"

// dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
//    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
//
//    // feign
//    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
//
//    // JPA
//    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//
//    // redis
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")
//
//    // security
//    implementation("org.springframework.boot:spring-boot-starter-security")
//
//    // Mysql
//    runtimeOnly("com.mysql:mysql-connector-j")
//
//    // H2
//    testImplementation("com.h2database:h2")
//
//    // Kotlin-Logging
//    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
//
//    // jwt
//    implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
//    implementation("io.jsonwebtoken:jjwt-impl:$jwtVersion")
//    implementation("io.jsonwebtoken:jjwt-jackson:$jwtVersion")
//
//    // validation
//    implementation("org.springframework.boot:spring-boot-starter-validation")
//
//    // flyway
//    implementation("org.flywaydb:flyway-core:$flywayVersion")
//    implementation("org.flywaydb:flyway-mysql:$flywayVersion")
//
//    // monitoring
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
//    implementation("io.micrometer:micrometer-registry-prometheus")
//
//    // test
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
//    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
//
//    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
//    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.4")
// }
//
// dependencyManagement {
//    imports {
//        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
//    }
// }

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

tasks.withType<Test> {
    finalizedBy(tasks.jacocoTestReport)
    useJUnitPlatform()
}

tasks.named<ProcessResources>("processResources") {
    dependsOn("copy main env")
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
