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
    id("antlr")
}


group = "ru.anton.asmirko"
version = "1"
java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
    implementation(project(":Grammar"))
    implementation(project(":AntlrMetaGrammar"))
    antlr("org.antlr:antlr4:4.11.1")
    runtimeOnly("org.antlr:antlr4-runtime:4.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.mockk:mockk:1.12.1")
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