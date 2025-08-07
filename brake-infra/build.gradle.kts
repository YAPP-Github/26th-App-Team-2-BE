import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint").version("12.3.0") // ktlint
    id("jacoco") // jacoco
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(project(":brake-domain"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // feign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.5")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.5")

    // Mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // H2
    testImplementation("com.h2database:h2")

    // Kotlin-Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
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
tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
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
