package com.github.christophpickl.kpotpourri.common.enum

inline fun <reified T : Enum<T>> printAllValues() {
    println(enumValues<T>().joinToString { it.name })
}
