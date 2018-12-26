package com.github.christophpickl.kpotpourri.build

internal object Out {

    fun info(message: String) {
        out("INFO", message)
    }
    
    fun warn(message: String) {
        out("WARN", message)
    }

    fun error(message: String) {
        out("ERROR", message)
    }

    private fun out(level: String, message: String) {
        println("[BUILD4K][$level] $message")
    }

}
