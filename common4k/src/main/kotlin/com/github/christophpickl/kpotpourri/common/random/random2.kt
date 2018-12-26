package com.github.christophpickl.kpotpourri.common.random

import kotlin.random.Random

interface RandomService {
    fun nextInt(fromInclusive: Int, toExclusive: Int): Int
    fun nextDouble(fromInclusive: Double, toExclusive: Double): Double
    fun randomize(base: Int, from: Double, to: Double): Int
}

object RealRandomService : RandomService {

    override fun randomize(base: Int, from: Double, to: Double) =
        (base * Random.nextDouble(from, to)).toInt()

    override fun nextInt(fromInclusive: Int, toExclusive: Int) =
        Random.nextInt(fromInclusive, toExclusive)

    override fun nextDouble(fromInclusive: Double, toExclusive: Double) =
        Random.nextDouble(fromInclusive, toExclusive)
}
