package com.github.christophpickl.kpotpourri.common.random

/**
 * Wrapper interface for real random calculation.
 */
interface RandGenerator {
    /**
     * Generates a number between zero and given upper limit.
     */
    fun rand(upperLimit: Int): Int
}

/**
 * Delegates to real random generation.
 */
object RealRandGenerator : RandGenerator {
    override fun rand(upperLimit: Int) = Math.round(Math.random() * upperLimit).toInt()
}
