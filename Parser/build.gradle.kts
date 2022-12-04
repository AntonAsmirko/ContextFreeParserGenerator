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

apply(plugin = "org.jetbrains.kotlin.jvm")

plugins {
    id("java")
//    kotlin("jvm") version "1.5.0-release-759"
}


group = "ru.anton.asmirko"
version = "1"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(project(":GrammarUtills"))
    implementation(project(":Core"))
    implementation(project(":Grammar"))
    implementation(project(":Lexer"))
    implementation(project(":Tree"))
    implementation(project(":GraphViz"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.mockk:mockk:1.12.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")
    implementation(kotlin("stdlib-jdk8"))
}
tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xmx8g")
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}
repositories {
    mavenCentral()
}