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

extra["springCloudVersion"] = "2025.0.0"

dependencies {
    implementation(project(":brake-domain"))
    implementation(project(":brake-internal"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")

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

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
