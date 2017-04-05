package com.github.christophpickl.kpotpourri.common.collection


fun List<Any>.toPrettyString(prefix: String = "- ") = map { listItem -> "$prefix$listItem" }.joinToString("\n")
fun List<Any>.prettyPrint() {
    println(toPrettyString())
}

fun Array<out Any>.toPrettyString() = map { "- $it" }.joinToString("\n")
fun Array<out Any>.prettyPrint() {
    println(toPrettyString())
}
