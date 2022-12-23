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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.mockk:mockk:1.12.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")

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

val compileJava: JavaCompile by tasks
compileJava.source(tasks.generateGrammarSource)
compileJava.dependsOn(tasks.generateGrammarSource)

//val compileKotlin: KotlinCompile by tasks
//compileKotlin.kotlinOptions {
//    jvmTarget = "11"
//}

