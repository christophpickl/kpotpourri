package com.github.christophpickl.kpotpourri.common.web

import mu.KotlinLogging.logger
import java.awt.Desktop
import java.net.URL

object WebsiteOpener {

    private val desktop = Desktop.getDesktop()
    private val log = logger {}

    init {
        require(desktop.isSupported(Desktop.Action.BROWSE))
    }

    fun open(url: URL) {
        log.debug { "open($url)" }
        desktop.browse(url.toURI())
    }

}
