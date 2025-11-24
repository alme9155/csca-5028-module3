//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") 
    id("org.jetbrains.kotlin.plugin.serialization") 
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "cu.csca5028.alme9155"
version = "1.0.0"

application {
    mainClass.set("cu.csca5028.alme9155.collector.AppKt")
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

val ktorVersion = "3.0.0"
val kmongoVersion = "4.11.0"

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")

    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")           // CIO engine (non-blocking)
    implementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")    
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("org.litote.kmongo:kmongo:$kmongoVersion")
    testImplementation(kotlin("test"))
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveBaseName.set("csca-5028-module3")
    archiveVersion.set("1.0.0")
    archiveClassifier.set("")
    manifest {
        attributes["Main-Class"] = "cu.csca5028.alme9155.collector.AppKt"
    }
}

tasks.jar { enabled = false }
tasks.distZip { enabled = false }
tasks.distTar { enabled = false }
tasks.startScripts { enabled = false }

tasks.build {
    dependsOn(tasks.shadowJar)
}