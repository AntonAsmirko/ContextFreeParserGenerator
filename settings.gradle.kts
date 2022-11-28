pluginManagement {
    repositories {
        mavenCentral()
        google()
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
    "GraphViz"
)