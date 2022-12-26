package compile

import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import javax.script.ScriptEngineManager

fun getCompiler(): KotlinJsr223JvmLocalScriptEngine {
    val scriptEngine = ScriptEngineManager()
    val factory = scriptEngine.getEngineByExtension("kts").factory
    return factory.scriptEngine as KotlinJsr223JvmLocalScriptEngine
}

inline fun <reified T> KotlinJsr223JvmLocalScriptEngine.runScript(script: String): T = eval(script) as T
