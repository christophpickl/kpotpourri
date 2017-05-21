package com.github.christophpickl.kpotpourri.common.random

import com.github.christophpickl.kpotpourri.test4k.assertThrown
import com.github.christophpickl.kpotpourri.test4k.hamkrest_matcher.shouldMatchValue
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThanOrEqualTo
import com.natpryce.hamkrest.lessThanOrEqualTo
import com.natpryce.hamkrest.should.shouldNotMatch
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import java.lang.IllegalArgumentException

@Test class RandXImplTest {

    private lateinit var generator: RandGenerator

    private val anyFrom = 1
    private val anyTo = 9

    @BeforeMethod
    fun `init mocks`() {
        generator = mock()
    }

    private fun whenGeneratorThenReturn(generatedRand: Int) {
        whenever(generator.rand(any())).thenReturn(generatedRand)
    }

    //<editor-fold desc="randomBetween">

    private fun testee() = RandXImpl(generator)
    private fun realTestee() = RandXImpl(RealRandGenerator)

    private fun _randomBetween(from: Int, to: Int, except: Int? = null) =
            testee().randomBetween(from, to, except)

    fun `randomBetween - Given from 0 and to 1, When rand returns 0, Then 0 is returned`() {
        whenGeneratorThenReturn(0)

        _randomBetween(0, 1) shouldMatchValue 0
    }

    fun `randomBetween - Given from 0 and to 1, When rand returns 1, Then 1 is returned`() {
        whenGeneratorThenReturn(1)

        _randomBetween(0, 1) shouldMatchValue 1
    }


    fun `randomBetween - Given from 0 and to 1 and except to 0, When rand returns 1, Then 1 is returned`() {
        whenGeneratorThenReturn(1)

        _randomBetween(0, 1, except = 0) shouldMatchValue 1
    }

    fun `randomBetween - Given from 0 and to 1 and except to 0, When rand returns 0 followed by 1, Then 1 is returned`() {
        whenGeneratorThenReturn(0)
        whenGeneratorThenReturn(1)

        _randomBetween(0, 1, except = 0) shouldMatchValue 1
    }


    fun `randomBetween - When from bigger to, Then throw`() {
        assertThrown<IllegalArgumentException> {
            _randomBetween(1, 0)
        }
    }

    fun `randomBetween - When from equals to, Then throw`() {
        assertThrown<IllegalArgumentException> {
            _randomBetween(0, 0)
        }
    }

    fun `randomBetween - When from is negative, Then throw`() {
        assertThrown<IllegalArgumentException> {
            _randomBetween(-1, anyTo)
        }
    }

    fun `randomBetween - When to is negative, Then throw`() {
        assertThrown<IllegalArgumentException> {
            _randomBetween(anyFrom, -1)
        }
    }

    fun `randomBetween - couple of times`() {
        doCoupleOfTimes {
            assertThat(realTestee().randomBetween(0, 10),
                    greaterThanOrEqualTo(0) and lessThanOrEqualTo(10))
        }
        doCoupleOfTimes {
            assertThat(realTestee().randomBetween(50, 99),
                    greaterThanOrEqualTo(50) and lessThanOrEqualTo(99))
        }
    }

    fun `randomBetween - couple of times with except set`() {
        doCoupleOfTimes {
            realTestee().randomBetween(0, 10, except = 5) shouldNotMatch equalTo(5)
        }
    }

    //</editor-fold>

    /*


    @Test fun randomElementsExcept() {
        val abc = listOf("a", "b", "c")
        assertThat(RandXImpl.randomElementsExcept(abc, 2, "a"),
                hasElement("b") and hasElement("c")
        )

        assertThat(RandXImpl.randomElementsExcept(abc, 1, "a"),
                hasElement("b") or hasElement("c")
        )
    }
     */

}
