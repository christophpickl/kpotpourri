package com.github.christophpickl.kpotpourri.common.collection

/** Default pretty print item prefix for arrays, lists, etc. */
val KPOT_DEFAULT_PREFIX = "- "

/** Default pretty print item joiner for arrays, lists, etc. */
val KPOT_DEFAULT_JOINER = "\n"

/**
 * Although there is a `toMap` defined on Iterable, there is no `toMutableMap`, so fill this gap :)
 *
 * listOf(1 to "einz").toMutableMap() == mapOf(1 to "einz")
 */
fun <K, V> Iterable<Pair<K, V>>.toMutableMap() = toMap().toMutableMap()
