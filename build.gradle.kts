import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint").version("12.3.0")
    id("jacoco")
}

allprojects {
    group = "com.yapp.brake"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

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
        implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
    }
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}

configure(subprojects) {
    jacoco {
        toolVersion = "0.8.13"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

val jacocoMerge =
    tasks.register<JacocoReport>("jacocoMerge") {
        dependsOn(subprojects.map { it.tasks.named("test") })
        executionData.setFrom(
            files(subprojects.map { "${it.projectDir}/build/jacoco/test.exec" }),
        )
    }

tasks.register<JacocoReport>("jacocoRootReport") {
    dependsOn(subprojects.map { it.tasks.named("test") }) // 각 모듈 테스트 먼저
    mustRunAfter(subprojects.map { it.tasks.named("test") }) // 실행 순서 보장

    additionalSourceDirs.setFrom(files(subprojects.map { "${it.projectDir}/src/main/kotlin" }))
    sourceDirectories.setFrom(files(subprojects.map { "${it.projectDir}/src/main/kotlin" }))
    classDirectories.setFrom(
        files(subprojects.map { "${it.projectDir}/build/classes/kotlin/main" }).map {
            fileTree(it) {
                exclude("**/common/**") // 여기서 한 번만 exclude
            }
        },
    )

    executionData.setFrom(jacocoMerge.get().executionData)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.named("test") {
    finalizedBy("jacocoRootReport")
}
