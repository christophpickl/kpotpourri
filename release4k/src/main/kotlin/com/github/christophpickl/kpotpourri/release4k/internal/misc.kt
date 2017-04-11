package com.github.christophpickl.kpotpourri.release4k.internal


fun kout(message: String) {
    println("[KRELEASE] $message")
}

fun koutCmd(message: String) {
    kout("$ $message")
}
