package com.github.christophpickl.kpotpourri.common


fun List<Any>.toPrettyString() = map { "- ${it.toString()}" }.joinToString("\n")

fun List<Any>.prettyPrint() {
    println(toPrettyString())
}
