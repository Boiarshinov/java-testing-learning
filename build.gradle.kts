import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

plugins {
    java
    id("io.qameta.allure") version "2.11.2"
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.awaitility:awaitility:4.1.1")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("com.jayway.jsonpath:json-path:2.6.0")
    testImplementation("org.jooq:joox:1.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
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
