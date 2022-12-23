pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}


rootProject.name = "RegexParser"

include(
    "GrammarUtills",
    "Parser",
    "Core",
    "Grammar",
    "Lexer",
    "Tree",
    "GraphViz",
    "AntlrCalculator"
)