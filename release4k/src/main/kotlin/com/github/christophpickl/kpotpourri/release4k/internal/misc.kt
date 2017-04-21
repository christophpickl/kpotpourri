package com.github.christophpickl.kpotpourri.release4k.internal

import java.io.File


fun kout(message: String) {
    println("[KRELEASE] $message")
}

fun koutCmd(cwd: File, message: String) {
    kout("[${cwd.canonicalPath}] $ $message")
}
