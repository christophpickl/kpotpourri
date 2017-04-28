package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.common.logging.LOG
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract class HttpFilter : Filter {

    override fun init(filterConfig: FilterConfig) {}

    override fun destroy() {}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest && response is HttpServletResponse) {
            doHttpFilter(request, response, chain)
        } else {
            chain.doFilter(request, response)
        }
    }

    abstract fun doHttpFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain)

}

class RequestResponseDumpFilter : HttpFilter() {

    val log = LOG {}

    override fun doHttpFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        log.debug("")
        log.debug("Request Dump ========>")
        log.debug { "\t" + request.method + " " + request.requestURI }
        log.debug { "\tHeaders: " + formatHeaders(request) }
        // request body

        chain.doFilter(request, response)

        log.debug("")
        log.debug("Response Dump ========>")
        log.debug { "\tHeaders: " + formatHeaders(response) }
        log.debug { "\tStatus: " + response.status }
        // response body
        log.debug("")

    }

    private fun formatHeaders(response: HttpServletResponse): String {
        return _formatHeaders(response.headerNames, response::getHeader)
    }

    private fun formatHeaders(request: HttpServletRequest): String {
        return _formatHeaders(request.headerNames.toList(), request::getHeader)

    }

    private fun _formatHeaders(headerNames: Collection<String>, headerValueProvider: (String) -> String): String {
        val headers = mutableListOf<String>()
        headerNames.toList().forEach { headerName ->
            val headerValue = headerValueProvider(headerName)
            headers.add(headerName + "=" + headerValue)
        }
        return headers.joinToString()
    }

}
