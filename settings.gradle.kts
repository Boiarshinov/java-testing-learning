rootProject.name = "java-testing-learning"
include("cucumber")
include("mock-server")
include("wiremock")
include("test-http-client")
include("gatling")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.22"
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
