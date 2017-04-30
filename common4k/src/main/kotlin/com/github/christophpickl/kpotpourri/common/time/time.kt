package com.github.christophpickl.kpotpourri.common.time

import com.github.christophpickl.kpotpourri.common.numbers.format

/**
 * Create a readable representation of milliseconds in a given format.
 */
fun Long.timify(format: MsTimification) = format.timify(this)

/**
 * Available formats for milliseconds representation.
 */
enum class MsTimification {

    /**
     * Format as "1.234 seconds".
     */
    Seconds {
        override fun timify(ms: Long) = (ms / 1000.0).format(3) + " seconds"
    },
    /**
     * Format as "1 minute and 23 seconds".
     */
    MinutesAndSeconds {
        override fun timify(ms: Long): String {
            val totalSeconds = (ms / 1000.0).toInt()
            val mins = (totalSeconds / 60)
            val secs = totalSeconds - (mins * 60)

            val pluralSeconds = if (secs == 1) "" else "s"
            val pluralMinutes = if (mins == 1) "" else "s"
            return "$mins minute$pluralMinutes and $secs second$pluralSeconds"
        }
    }
    ;

    internal abstract fun timify(ms: Long): String

}
