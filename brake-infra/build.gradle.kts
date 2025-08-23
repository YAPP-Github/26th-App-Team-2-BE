import org.springframework.boot.gradle.tasks.bundling.BootJar

extra["springCloudVersion"] = "2025.0.0"

val jwtVersion = "0.12.6"
dependencies {
    implementation(project(":brake-domain"))
    implementation(project(":brake-internal"))
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")

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

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jwtVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jwtVersion")

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

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
