package com.github.christophpickl.kpotpourri.markdown4k.internal

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import org.testng.annotations.Test
import javax.script.ScriptException

@Test class KotlinCompilerTest {

    fun `Given empty code, Should compile`() {
        KotlinCompiler.compile("")
    }

    fun `Given valid code, Should compile`() {
        KotlinCompiler.compile("""val x = 42""")
    }

    fun `Given code with sideffects, Should compile but not eval`() {
        KotlinCompiler.compile("""throw Exception()""")
    }

    fun `Given invalid code, Should throw`() {
        assertThrown<ScriptException> {
            KotlinCompiler.compile("""val x = notDeclared""")
        }
    }

    fun `Given code depending on external lib, Should compile`() {
        KotlinCompiler.compile("""
import org.testng.annotations.Test

@Test class DummyTest {}
""")
    }

    fun `Given code depending on external lib which is not added as dependency, Should throw`() {
        assertThrown<ScriptException> {
            KotlinCompiler.compile("""import org.notexisting.ShouldThrow""")
        }
    }

}
