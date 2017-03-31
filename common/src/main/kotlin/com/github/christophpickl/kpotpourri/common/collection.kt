package com.github.christophpickl.kpotpourri.common


fun List<Any>.toPrettyString() = map { "- $it" }.joinToString("\n")
fun List<Any>.prettyPrint() {
    println(toPrettyString())
}

fun Array<out Any>.toPrettyString() = map { "- $it" }.joinToString("\n")
fun Array<out Any>.prettyPrint() {
    println(toPrettyString())
}
