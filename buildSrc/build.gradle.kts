plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("de.undercouch:gradle-download-task:4.1.1")
    implementation("com.github.jengelman.gradle.plugins:shadow:2.0.3")
}
