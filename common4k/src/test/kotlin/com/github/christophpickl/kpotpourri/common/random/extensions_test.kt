package com.github.christophpickl.kpotpourri.common.random


import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.containsExactlyInAnyOrder
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.hasSizeOf
import com.natpryce.hamkrest.assertion.assertThat
import org.testng.annotations.Test

@Test class RandomExtensionsTest {

    fun `randomizeElements - all elements sustained`() {
        val list = listOf("a", "b")
        val randomList = list.randomizeElements()
        assertThat(randomList, containsExactlyInAnyOrder("a", "b"))
    }

    fun `randomizeElements - empty list`() {
        assertThat(emptyList<String>().randomizeElements(), hasSizeOf(0))
    }

}
