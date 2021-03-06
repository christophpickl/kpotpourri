package com.github.christophpickl.kpotpourri.common.math


object KMath {

    fun max(first: Int, second: Int, vararg others: Int): Int =
        listOf(first, second).plus(others.toList()).max()!!

    fun max(first: Long, second: Long, vararg others: Long): Long =
        listOf(first, second).plus(others.toList()).max()!!

    fun max(first: Double, second: Double, vararg others: Double): Double =
        listOf(first, second).plus(others.toList()).max()!!
    
    fun minButNotNegative(first: Int, second: Int, vararg others: Int): Int =
        min(first, second, *others).coerceAtLeast(0)

    fun min(first: Int, second: Int, vararg others: Int): Int =
        listOf(first, second).plus(others.toList()).min()!!

    fun min(first: Long, second: Long, vararg others: Long): Long =
        listOf(first, second).plus(others.toList()).min()!!
    
    fun min(first: Double, second: Double, vararg others: Double): Double =
        listOf(first, second).plus(others.toList()).min()!!

}
