package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.mapContainsExactly
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test


@Test class MapExtensionsTest {

    private val map1 = mapOf(1 to true)
    private val map2 = mapOf(2 to true)
    private val map12 = mapOf(1 to true, 2 to true)

    fun `hasIntersection`() {
        assertThat(map1.hasIntersection(map2), equalTo(false))
        assertThat(map1.hasIntersection(map12), equalTo(true))
    }

    fun `verifyNoIntersection does not throw exception when has no intersection`() {
        map1.verifyNoIntersection(map2)
    }

    @Test(expectedExceptions = arrayOf(KPotpourriException::class))
    fun `verifyNoIntersection throws exception when has intersection`() {
        map1.verifyNoIntersection(map12)
    }

    fun `put a Pair`() {
        assertThat(mutableMapOf<String, String>().apply { put("a" to "b") },
                mapContainsExactly("a" to "b"))
    }

}
