plugins {
    kotlin("jvm") version "1.3.31"
    application
}

group = "jp.ac.titech.itsp"
version = "0.0.1"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}

val ktorVersion = "1.2.0"
val logbackVersion = "1.2.3"

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}
