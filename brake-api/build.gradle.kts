import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.epages.restdocs-api-spec") version "0.19.4"
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
    implementation(project(":brake-domain"))
    implementation(project(":brake-infra"))
    implementation(project(":brake-common"))
    implementation(project(":brake-internal"))

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

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

tasks {
    withType<BootJar> {
        enabled = true
        mainClass.set("com.yapp.brake.BrakeApplication")
        dependsOn("copyToSwagger")

        from("build/api-spec/openapi3.yaml") {
            into("static/swagger")
        }
    }
}
