plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.i0.kata"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.kotest.selected)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
