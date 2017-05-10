package com.github.christophpickl.kpotpourri.markdown4k.internal

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.isA
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

@Test class KompilerTest {

    fun `Given empty code, Should compile`() {
        assertKompileSuccess("")
    }

    fun `Given valid code, Should compile`() {
        assertKompileSuccess("""val x = 42""")
    }

    fun `Given code with sideffects, Should compile but not eval`() {
        assertKompileSuccess("""throw Exception()""")
    }

    fun `Given invalid code, Should throw`() {
        assertKompileFailure("""val x = notDeclared""")
    }

    fun `Given code depending on external lib, Should compile`() {
        assertKompileSuccess("""
import org.testng.annotations.Test

@Test class DummyTest {}
""")
    }

    fun `Given code depending on external lib which is not added as dependency, Should throw`() {
        assertKompileFailure("""import org.notexisting.ShouldThrow""")
    }

}

private fun assertKompileSuccess(code: String) {
    assertThat(Kompiler.kompile(code), isA(InternalKompilationResult.Success::class))
}

private fun assertKompileFailure(code: String) {
    assertThat(Kompiler.kompile(code), isA(InternalKompilationResult.Failure::class))
}
