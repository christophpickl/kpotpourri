package com.github.christophpickl.kpotpourri.web4k

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestResponseDumpFilter : HttpFilter() {

    override fun doHttpFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        println()
        println("Request Dump ========>")
        println("\t" + request.method + " " + request.requestURI)
        println("\tHeaders: " + formatHeaders(request))
        // request body

        chain.doFilter(request, response)

        println()
        println("Response Dump ========>")
        println("\tHeaders: " + formatHeaders(response))
        println("\tStatus: " + response.status)
        // response body
        println()

    }

    private fun formatHeaders(response: HttpServletResponse): String {
        return _formatHeaders(response.headerNames, { response.getHeader(it) })
    }

    private fun formatHeaders(request: HttpServletRequest): String {
        return _formatHeaders(request.headerNames.toList(), { request.getHeader(it) })

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
