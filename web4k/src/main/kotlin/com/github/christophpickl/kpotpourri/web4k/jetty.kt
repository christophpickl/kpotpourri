package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.common.collection.plusIf
import com.github.christophpickl.kpotpourri.common.logging.LOG
import com.github.christophpickl.kpotpourri.common.string.combineUrlParts
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.Configuration
import org.eclipse.jetty.webapp.WebAppContext
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap
import org.jboss.resteasy.plugins.spring.SpringContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import java.net.BindException
import java.util.*
import javax.servlet.Filter
import kotlin.reflect.KClass

val DEFAULT_JETTY_PORT = 8442

data class JettyConfig(
        val springConfig: KClass<*>,
        val port: Int = DEFAULT_JETTY_PORT,
        val servletPrefix: String = "/rest",
        val useStdOut: Boolean = false,
        // could pass Data structure which also allows changing the path
        val filters: List<KClass<out Filter>> = emptyList(),
        val enableRequestResponseFilter: Boolean = true,
        val exposeExceptions: Boolean = false
)

class JettyServer(private val config: JettyConfig) {

    companion object {
        private val RESOURCE_BASE = "src/main/webapp"
        private val CONTEXT_PATH = "/"
    }

    val fullUrl = combineUrlParts("http://localhost:${config.port}", CONTEXT_PATH, config.servletPrefix)
    private val log = LOG {}

    private var server: Server = Server(config.port)
    val isRunning get() = server.isRunning

    fun start(suppressOutput: Boolean = false) {
        if (server.isRunning || server.isStarting || server.isStarted) {
            throw IllegalStateException("Jetty was already started!")
        }
        if (!suppressOutput) logOrPrintln("Starting Jetty ...")

        server.handler = newContext()
        try {
            server.start()
        } catch(e: BindException) {
            throw KPotpourriException("Address 'localhost:${config.port}' is already in use.")
        }

        if (!suppressOutput) logOrPrintln("Jetty started at $fullUrl :)")
    }

    fun startInteractively() {
        println("Starting jetty at $fullUrl")
        start(suppressOutput = true)
        println("Hit ENTER to quit Jetty.")
        readLine()
        stop()
        println("Jetty stopped. Bye bye.")
    }

    fun stop() {
        if (!server.isRunning) {
            throw IllegalStateException("Jetty was not yet started!")
        }
        logOrPrintln("Stopping Jetty...")
        server.stop()
        server.join()
    }

    private fun newContext(): WebAppContext {
        val context = WebAppContext()
        context.resourceBase = RESOURCE_BASE
        context.contextPath = CONTEXT_PATH
        context.isParentLoaderPriority = true
        context.configurations = arrayOf<Configuration>(WebAppInitializingConfiguration())
        context.errorHandler = JsonErrorHandler(config.exposeExceptions)
        initReasEasyAndSpring(context)

        config.filters.plusIf(config.enableRequestResponseFilter, RequestResponseDumpFilter::class).forEach {
            context.addFilter(it.java, "${config.servletPrefix}/*", null)
        }

        return context
    }

    private fun initReasEasyAndSpring(context: WebAppContext) {
        log.info { "Registering RestEasy servlet mapping prefix: ${config.servletPrefix}" }
        context.setInitParameter("resteasy.servlet.mapping.prefix", config.servletPrefix)

        context.addEventListener(ResteasyBootstrap())
        context.addServlet(HttpServletDispatcher::class.java, "${config.servletPrefix}/*")

        context.addEventListener(SpringContextLoaderListener() as EventListener) // he got confused ;)
        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext::class.java.name)
        log.info { "Registering spring config: ${config.springConfig.java.name}" }
        context.setInitParameter("contextConfigLocation", config.springConfig.java.name)
    }

    private fun logOrPrintln(message: String) {
        if (config.useStdOut) {
            println(message)
        } else {
            log.debug(message)
        }
    }

}
