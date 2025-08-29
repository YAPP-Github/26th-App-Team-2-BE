import org.springframework.boot.gradle.tasks.bundling.BootJar

dependencies {
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // test
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    withType<Jar> { enabled = true }
    withType<BootJar> { enabled = false }
}
