package com.github.christophpickl.kpotpourri.common.math


object KMath {

    fun minButNotNegative(first: Int, second: Int, vararg others: Int): Int =
        min(first, second, *others).coerceAtLeast(0)

    fun min(first: Int, second: Int, vararg others: Int): Int =
        listOf(first, second).plus(others.toList()).min()!!

}
