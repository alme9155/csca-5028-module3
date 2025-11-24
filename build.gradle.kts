import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "2.2.21"
val ktorVersion = "3.3.2"
val kmongoVersion = "4.11.0"

plugins {
    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "cu.csca5028.alme9155"
version = "1.0.0"

application {
    mainClass.set("cu.csca5028.alme9155.collector.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")

    implementation("org.litote.kmongo:kmongo:$kmongoVersion")

    testImplementation(kotlin("test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName.set("mvp-data-collector")
    archiveVersion.set("1.0")
    archiveClassifier.set("")
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}

