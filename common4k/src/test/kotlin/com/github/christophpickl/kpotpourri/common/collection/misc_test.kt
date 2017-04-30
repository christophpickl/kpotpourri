package com.github.christophpickl.kpotpourri.common.collection

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import org.testng.annotations.Test


@Test class IterableExtensionsTest {

    fun `toMutableMap`() {
        assertThat(listOf(1 to "einz").toMutableMap(),
                equalTo(mapOf(1 to "einz")))
        assertThat(listOf(1 to "einz").toMutableMap(), isA<MutableMap<Int, String>>())
    }

}
