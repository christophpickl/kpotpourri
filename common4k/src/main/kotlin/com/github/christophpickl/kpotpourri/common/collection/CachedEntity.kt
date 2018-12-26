package com.github.christophpickl.kpotpourri.common.collection

class CachedEntity<T>(
    private val loader: () -> T
) {

    private var isValid = false
    private var cached: T? = null

    fun get(): T {
        if (!isValid || cached == null) {
            cached = loader()
            isValid = true
        }
        return cached!!
    }

    fun invalidate() {
        isValid = false
    }
}
