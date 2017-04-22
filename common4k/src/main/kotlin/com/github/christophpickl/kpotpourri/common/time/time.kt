package com.github.christophpickl.kpotpourri.common.time

import com.github.christophpickl.kpotpourri.common.numbers.format

fun Long.timify(format: MsTimification) = format.timify(this)

enum class MsTimification {
    Seconds {
        override fun timify(ms: Long) = (ms / 1000.0).format(3) + " seconds"
    },
    MinutesAndSeconds {
        override fun timify(ms: Long): String {
            val secsTmp = (ms / 1000.0).toInt()
            val mins = (secsTmp / 60)
            val secs = secsTmp - (mins * 60)

            val sSecs = if(secs == 1) "" else "s"
            val sMins = if(mins == 1) "" else "s"
            return "$mins minute$sMins and $secs second$sSecs"
        }
    }
    ;
    abstract fun timify(ms: Long): String
}
