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
}


group = "ru.anton.asmirko"
version = "1"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(project(":Grammar"))
    implementation(project(":Tree"))
    implementation(kotlin("stdlib-jdk8"))
}
tasks.withType<Test> {
    useJUnitPlatform()
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}