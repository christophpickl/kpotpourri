package com.github.christophpickl.kpotpourri.markdown4k.internal

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import org.testng.annotations.Test
import javax.script.ScriptException

@Test class KotlinCompilerTest {

    fun `compileKotlin - Given valid code, Should compile`() {
        KotlinCompiler.compile("""val x = 42""")
    }

    fun `compileKotlin - Given invalid code, Should throw`() {
        assertThrown<ScriptException> {
            KotlinCompiler.compile("""val x = y""")
        }
    }

    fun `compileKotlin - Given code depending on external lib, Should compile`() {
        KotlinCompiler.compile("""
import org.testng.annotations.Test

@Test class DummyTest {}
""")
    }

}
