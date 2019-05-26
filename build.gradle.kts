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
    maven { url = uri("https://dl.bintray.com/kotlin/exposed") }
}

val ktorVersion = "1.2.0"
val logbackVersion = "1.2.3"
val h2Version = "1.4.199"
val exposedVersion = "0.13.6"
val hikariCPVersion = "3.3.1"

dependencies {
    /* Kotlin */
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    /* Ktor */
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    /* DB */
    implementation("com.h2database:h2:$h2Version")
    implementation("org.jetbrains.exposed:exposed:$exposedVersion")
    implementation("com.zaxxer:HikariCP:$hikariCPVersion")

    /* Log */
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    /* Test */
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}
