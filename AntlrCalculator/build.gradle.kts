import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation(project(":Tree"))
    implementation(project(":Grammar"))
    implementation(project(":GraphViz"))
    implementation(kotlin("stdlib-jdk8"))
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf(
        "-package",
        "ru.anton.asmirko.antlrcalculator",
        "-visitor",
        "-no-listener",
        "-long-messages"
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
}
repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}

