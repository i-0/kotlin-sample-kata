plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.i0.kata"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}