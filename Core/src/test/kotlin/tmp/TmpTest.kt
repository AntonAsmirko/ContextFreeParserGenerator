package tmp

import org.junit.jupiter.api.Test
import compile.getCompiler
import compile.runScript

class TmpTest {

    @Test
    fun testLib() {
        val compiler = getCompiler()
        val fn: (List<String>, String) -> String = {children: List<String>, value: String -> children.joinToString(separator="\n")}
        val res = compiler.runScript<(List<String>, String) -> String>(
            """{ children: List<String>, value: String -> val childrenD = children.map{ it.toDouble() }; (childrenD[0] + childrenD.subList(1, (if(childrenD.subList(1, childrenD.size).isNotEmpty()) childrenD.subList(1, childrenD.size).reduce{acc, i -> acc + i} else 0.0)).toString()}
            """.trimMargin()
        )
        print(res(listOf("1", "2", "3"), "6"))
    }
}