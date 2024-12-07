plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.vitor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")

    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.register<JavaExec>("run") {
    environment("LANG", "en_US.UTF-8")
    systemProperty("file.encoding", "UTF-8")
    mainClass.set("MainKt")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf("-Dfile.encoding=UTF-8")
}