import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation(project(":brake-internal"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation(kotlin("test"))
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
