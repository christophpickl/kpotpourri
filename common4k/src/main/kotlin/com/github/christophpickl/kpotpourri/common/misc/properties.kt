package com.github.christophpickl.kpotpourri.common.misc

import java.util.*

fun propertiesOf(vararg data: Pair<String, Any>) = Properties().apply {
    data.forEach { (k, v) ->
        setProperty(k, v.toString())
    }
}

