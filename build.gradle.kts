import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.qameta.allure") version "2.11.2"
    kotlin("jvm")
}

group = "dev.boiarshinov"
version = "1.0-SNAPSHOT"
description = "java-testing-learning"

java.targetCompatibility = JavaVersion.VERSION_17
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.compileJava {
    options.encoding = "UTF-8"
    System.setProperty("file.encoding", "UTF-8")
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)

    testImplementation(libs.awaitility)
    testImplementation(libs.assertj)
    testImplementation(libs.json.path)
    testImplementation(libs.joox)
    testImplementation(libs.hamcrest)
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform {
        excludeTags("integration")
    }
}

allure {
    adapter {
        frameworks {
            junit5 {//used by default
                // configuration if needed
            }
        }
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}
