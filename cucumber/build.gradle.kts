plugins {
    id("java")
//    id("io.qameta.allure") version "2.11.2"
}

group = "dev.boiarshinov"
version = "1.0-SNAPSHOT"

dependencies {
    testImplementation(libs.cucumber)
    testImplementation(libs.cucumber.junit)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platform.suite)
}

tasks.test {
    useJUnitPlatform()
    systemProperty("cucumber.junit-platform.naming-strategy", "long")
}

//allure {
//    adapter {
//        frameworks {
//            junit5 {//used by default
//                // configuration if needed
//            }
//        }
//    }
//}
