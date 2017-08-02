package com.github.christophpickl.kpotpourri.common.random

internal fun doCoupleOfTimes(code: () -> Unit) {
    1.rangeTo(100).forEach { code() }
}
