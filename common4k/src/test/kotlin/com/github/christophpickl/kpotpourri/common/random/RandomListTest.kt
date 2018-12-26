package com.github.christophpickl.kpotpourri.common.random

import com.github.christophpickl.kpotpourri.common.isBetween
import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test
class RandomListTest {

    fun `Given single element When random Then return it`() {
        val list = randomListOf("a" to 100)

        assertThat(list.randomElement(), equalTo("a"))
    }

    fun `Given two elements one is bigger When random Then return mostly one`() {
        val list = randomListOf("a" to 90, "b" to 10)

        val randoms = randomSample(list)

        assertThat(randoms["a"], isBetween(70, 100))
        assertThat(randoms["b"], isBetween(0, 30))
    }

    fun `Given two elements both are same When random Then return both equally`() {
        val list = randomListOf("a" to 50, "b" to 50)

        val randoms = randomSample(list)

        assertThat(randoms["a"], isBetween(30, 70))
        assertThat(randoms["b"], isBetween(30, 70))
    }

    fun `When too less percentage Then fail`() {
        assertThrown<IllegalArgumentException> {
            randomListOf("a" to 0)
        }
    }

    fun `When too much percentage Then fail`() {
        assertThrown<IllegalArgumentException> {
            randomListOf("a" to 110)
        }
    }

    private val timesHundred = 100
    private fun <E> randomSample(list: RandomList<E>) = (1..(100 * timesHundred)).map {
        list.randomElement()
    }.groupingBy { it }.eachCount().mapValues { it.value / timesHundred }.toMutableMap().fillMissing(list)

    private fun <K> MutableMap<K, Int>.fillMissing(list: RandomList<K>) = apply {
        list.filter { !containsKey(it) }.forEach { put(it, 0) }
    }

}
