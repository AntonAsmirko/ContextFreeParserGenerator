package tmp

import org.junit.jupiter.api.Test
import compile.getCompiler
import compile.runScript

class TmpTest {

    @Test
    fun testLib() {
        val compiler = getCompiler()
        val res = compiler.runScript<(List<String>, String) -> String>(
            """{ children: List<String>, value: String -> 
                    val childrenInt = children.map{ it.toInt() }
                    childrenInt.reduce { acc: Int, i: Int ->  acc + i }.toString() 
                }
            """.trimMargin()
        )
        print(res(listOf("1", "2", "3"), "6"))
    }
}