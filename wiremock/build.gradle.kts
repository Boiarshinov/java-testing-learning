plugins {
    id("java")
}

group = "dev.boiarshinov"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.okhttp3)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.wiremock)
    testImplementation(libs.slf4j)
    testImplementation(libs.logback)
}

tasks.test {
    useJUnitPlatform()
}
