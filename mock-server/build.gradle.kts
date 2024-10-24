plugins {
    id("java")
}

group = "dev.boiarshinov"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":test-http-client"))

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockserver)
}

tasks.test {
    useJUnitPlatform()
}
