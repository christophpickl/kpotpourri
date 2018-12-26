package com.github.christophpickl.kpotpourri.common.misc

/**
 * Simplify thread sleep (change long to int).
 */
fun sleep(ms: Int) {
    Thread.sleep(ms.toLong())
}
