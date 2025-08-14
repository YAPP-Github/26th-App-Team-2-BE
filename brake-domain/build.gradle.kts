import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":brake-internal"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    // validation
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testImplementation(kotlin("test"))
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
