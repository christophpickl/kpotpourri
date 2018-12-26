package com.github.christophpickl.kpotpourri.common.os

object OsSniffer {
    
    val os by lazy {
        sniff()
    }
    
    private fun sniff(): Os? {
        val name = System.getProperty("os.name").toLowerCase()
        return Os.values().firstOrNull { name.contains(it.osNameFragment) }
    }

}

enum class Os(val osNameFragment: String) {
    Win("win"),
    Mac("mac")
}
