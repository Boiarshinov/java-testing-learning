group = "dev.boiarshinov"
version = "1.0-SNAPSHOT"
description = "java-testing-learning"

java.targetCompatibility = JavaVersion.VERSION_17
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.compileJava {
    options.encoding = "UTF-8"
}

plugins {
    java
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.awaitility:awaitility:4.1.1")
    implementation("org.assertj:assertj-core:3.22.0")
    implementation("com.jayway.jsonpath:json-path:2.6.0")
    implementation("org.jooq:joox:1.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
}

tasks.test {
    useJUnitPlatform {
        excludeTags("integration")
    }
}
