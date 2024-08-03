val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kmongo_version: String by project

plugins {
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

group = "com.ihcl.qwikcilver"
version = "0.0.1"
application {
    mainClass.set("com.ihcl.qwikcilver.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-client-core:2.2.1")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-gson-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.intuit.karate:karate-core:1.2.0")
    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("io.insert-koin:koin-core:3.2.0")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.konform:konform:0.4.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:4.5.1")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers-jvm")
    //redis config
    implementation("ch.qos.logback:logback-classic:1.2.6")
    implementation("io.ktor:ktor-server-default-headers-jvm")
    implementation("redis.clients:jedis:5.0.2")

    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20160810")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")

     implementation ("ch.qos.logback:logback-classic:$logback_version")
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")




}