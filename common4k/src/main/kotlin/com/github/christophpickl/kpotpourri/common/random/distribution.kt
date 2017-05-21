package com.github.christophpickl.kpotpourri.common.random


//    fun <T> RandX.distributed(distribution: Distribution<T>): T

//data class DistributionItem<T>(val percent: Int, val value: T)
//data class Distribution<T>(val items: List<DistributionItem<T>>) {
//    init {
//        if (items.isEmpty()) throw IllegalArgumentException("Distribution must not be empty!")
//    }
//}
//fun <T> distributionOf(vararg pairs: Pair<Int, T>) =
//        Distribution<T>(pairs.map { DistributionItem(it.first, it.second) })

//    override fun <T> distributed(distribution: Distribution<T>): T {
//        val rand = randomBetween(0, 100)
//        return _distributed(distribution, rand)
//    }
//
//    @VisibleForTesting fun <T> _distributed(distribution: Distribution<T>, rand: Int): T {
//        if (distribution.sumOfPercents() != 100)
//            throw IllegalArgumentException("Distribution percent must be sum of 100: $distribution")
//
//        var currentPercent = 0
//        for (item in distribution.items) {
//            currentPercent += item.percent
//            if (rand <= currentPercent) {
//                return item.value
//            }
//        }
//        throw IllegalStateException("Distribution algorithm failed! rand=$rand, distribution=$distribution (currentPercent=$currentPercent)")
//    }
