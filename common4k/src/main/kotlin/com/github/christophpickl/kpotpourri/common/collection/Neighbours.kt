package com.github.christophpickl.kpotpourri.common.collection

import mu.KotlinLogging.logger

private val log = logger {}

data class Neighbours<T>(
    val previous: T?,
    val next: T?
) {
    companion object {
        fun <T> empty() = Neighbours<T>(null, null)
    }
}

fun <FROM, TO> Pair<FROM?, FROM?>.toNeighbours(transformer: (FROM?) -> TO?): Neighbours<TO> =
    Neighbours(transformer(first), transformer(second))

fun <T, X> List<T>.neighboursFor(pivot: T, matchField: T.() -> X): Pair<T?, T?> {
    val pivotField = pivot.matchField()
    val pivotIndex = indexOfFirst { it.matchField() == pivotField }
    require(pivotIndex != -1) { "Given pivot ($pivotField) could not be found in list! (${joinToString { it.matchField().toString() }})" }

    val prev = if (pivotIndex == 0) null else this[pivotIndex - 1]
    val next = if (pivotIndex == size - 1) null else this[pivotIndex + 1]

    log.trace { "Prev/Next for pivot index ($pivotIndex): ${prev?.matchField()} / ${next?.matchField()}" }
    return prev to next
}
