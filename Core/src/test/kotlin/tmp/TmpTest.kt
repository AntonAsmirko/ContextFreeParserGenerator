package tmp

import org.junit.jupiter.api.Test
import compile.getCompiler
import compile.runScript

class TmpTest {

    @Test
    fun testLib() {
        val compiler = getCompiler()
        compiler.runScript<Any?>("val x = mutableMapOf<String, String>()")
        compiler.runScript<Any?>("x[\"u\"] = \"aaaaaaaaaaaaaaaaaa\"")
        val res = compiler.runScript<(List<String>, String) -> String>(
            """{ children: List<String>, value: String -> x["u"]}
            """.trimMargin()
        )
        print(res(listOf("1", "2", "3"), "6"))
    }
}