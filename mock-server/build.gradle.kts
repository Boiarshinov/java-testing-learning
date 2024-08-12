plugins {
    id("java")
}

group = "dev.boiarshinov"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.mock-server:mockserver-netty-no-dependencies:5.15.0")
}

tasks.test {
    useJUnitPlatform()
}
