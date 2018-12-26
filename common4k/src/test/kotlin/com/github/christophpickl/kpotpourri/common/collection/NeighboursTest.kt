package com.github.christophpickl.kpotpourri.common.collection

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.testng.annotations.Test

@Test
class NeighboursTest {

    private val pivot = "pivot"

    fun `Given list with only pivot When find neighbours Then return empty pair`() {
        val actual = listOf(pivot).neighboursFor(pivot) { this }

        assertThat(actual, emptyPair())
    }

    fun `Given full list When find neighbours Then return neighbours`() {
        val actual = listOf("a", pivot, "c").neighboursFor(pivot) { this }

        assertThat(actual, pairOf("a", "c"))
    }

    fun `Given list with previous When find neighbours Then return neighbours`() {
        val actual = listOf("a", pivot).neighboursFor(pivot) { this }

        assertThat(actual, pairOf("a", null))
    }

    fun `Given list with next When find neighbours Then return neighbours`() {
        val actual = listOf(pivot, "b").neighboursFor(pivot) { this }

        assertThat(actual, pairOf(null, "b"))
    }

    fun `Given list  When find neighbours by invalid pivot Then throw`() {
        assertThrown<IllegalArgumentException> { 
            listOf("a", "b").neighboursFor(pivot) { this }
        }
    }

    private fun <T> pairOf(first: T?, second: T?): Matcher<Pair<T?, T?>> = equalTo(Pair<T?, T?>(first, second))
    private fun <T> emptyPair(): Matcher<Pair<T?, T?>> = pairOf(null, null)
}
