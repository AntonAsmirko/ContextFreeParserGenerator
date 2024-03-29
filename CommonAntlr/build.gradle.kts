buildscript {
    dependencies {
        classpath(
            group = "org.jetbrains.kotlin",
            name = "kotlin-gradle-plugin",
            version = "1.6.10"
        )
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "java")

plugins {
    id("java")
    id("antlr")
}


group = "ru.anton.asmirko"
version = "1"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    antlr("org.antlr:antlr4:4.11.1")
    runtimeOnly("org.antlr:antlr4-runtime:4.11.1")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
repositories {
    mavenCentral()
}

