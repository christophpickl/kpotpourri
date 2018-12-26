package com.github.christophpickl.kpotpourri.common.os

import com.github.christophpickl.kpotpourri.common.process.ExecuteContext
import com.github.christophpickl.kpotpourri.common.process.ProcessExecuterImpl
import mu.KotlinLogging

class TextToSpeech {

    private val log = KotlinLogging.logger {}
    private val os = OsSniffer.os
    private var warningPrinted = false

    fun say(text: String) {
        if (os == Os.Mac) {
            ProcessExecuterImpl.execute(
                command = "say",
                args = listOf(text),
                context = ExecuteContext(
                    suppressOutput = true
                )
            )
        } else if (!warningPrinted) {
            warningPrinted = true
            log.warn { "Your operating system is not supported for TTS :(" }
        }
    }
}
