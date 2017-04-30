package com.github.christophpickl.kpotpourri.common

import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.nullValue
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test class NullnessTest {

    fun `nullOrWith - Given non null instance`() {
        assertThat(2.nullOrWith { it + 40 }, equalTo(42))
    }

    fun `nullOrWith - Given null instance`() {
        val maybeNull: Int? = null
        assertThat(maybeNull.nullOrWith { it + 40 }, nullValue())
    }

}
