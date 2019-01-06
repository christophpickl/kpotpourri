package com.github.christophpickl.kpotpourri.common.random

import kotlin.random.Random

interface RandomService {
    fun nextInt(fromInclusive: Int, toExclusive: Int): Int
    fun nextLong(fromInclusive: Long, toExclusive: Long): Long
    fun nextDouble(fromInclusive: Double, toExclusive: Double): Double
    fun randomize(base: Int, from: Double, to: Double): Int
    fun randomize(base: Long, from: Double, to: Double): Long
}

object RealRandomService : RandomService {

    override fun randomize(base: Int, from: Double, to: Double) =
        (base * Random.nextDouble(from, to)).toInt()

    override fun randomize(base: Long, from: Double, to: Double) =
        (base * Random.nextDouble(from, to)).toLong()

    override fun nextInt(fromInclusive: Int, toExclusive: Int) =
        Random.nextInt(fromInclusive, toExclusive)

    override fun nextLong(fromInclusive: Long, toExclusive: Long) =
        Random.nextLong(fromInclusive, toExclusive)

    override fun nextDouble(fromInclusive: Double, toExclusive: Double) =
        Random.nextDouble(fromInclusive, toExclusive)
}


object RandomServiceAlwaysMinimum : RandomService {
    override fun randomize(base: Int, from: Double, to: Double): Int =
        (base * from).toInt()

    override fun randomize(base: Long, from: Double, to: Double): Long =
        (base * from).toLong()

    override fun nextInt(fromInclusive: Int, toExclusive: Int) =
        fromInclusive

    override fun nextLong(fromInclusive: Long, toExclusive: Long): Long =
        fromInclusive

    override fun nextDouble(fromInclusive: Double, toExclusive: Double) =
        fromInclusive
}
