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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("io.mockk:mockk:1.12.1")
}
tasks.withType<Test> {
    useJUnitPlatform()
}
