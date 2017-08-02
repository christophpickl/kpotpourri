package com.github.christophpickl.kpotpourri.test4k

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.testng.annotations.Test

@Test class JunitTestTest {

    fun `toParamterized - sunshine`() {
        val params: List<Array<String>> = listOf("a", "b").toParamterized().toList()

        assertThat(params, hasSize(equalTo(2)))
        assertThat(params[0].size, equalTo(1))
        assertThat(params[0][0], equalTo("a"))
        assertThat(params[1].size, equalTo(1))
        assertThat(params[1][0], equalTo("b"))
    }

}
