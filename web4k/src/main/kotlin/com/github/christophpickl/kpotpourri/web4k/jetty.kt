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
import java.util.EventListener
import javax.servlet.Filter
import kotlin.reflect.KClass

/**
 * If nothing else defined, 8442 will be default port used by Jetty.
 */
val DEFAULT_JETTY_PORT = 8442

/**
 * Global configuration object for Jetty and the associated web application.
 */
data class JettyConfig(
        val springConfig: KClass<*>,
        val port: Int = DEFAULT_JETTY_PORT,
        val servletPrefix: String = "/rest",
        val useStdOut: Boolean = false,
        // could pass Data structure which also allows changing the path
        val filters: List<KClass<out Filter>> = emptyList(),
        val enableRequestResponseFilter: Boolean = true,
        val exposeExceptions: Boolean = false,
        val errorHandler: ErrorHandlerType = ErrorHandlerType.Default
)

/**
 * Core class to start/stop Jetty server.
 */
class JettyServer(private val config: JettyConfig) {

    companion object {
        private val RESOURCE_BASE = "src/main/webapp"
        private val CONTEXT_PATH = "/"
    }

    private val log = LOG {}
    private var server: Server = Server(config.port)

    /** Check if Jetty server is in RUNNING state. */
    val isRunning get() = server.isRunning

    /** Full URL containing protocol, host, port, context and servlet path. */
    val fullUrl = combineUrlParts("http://localhost:${config.port}", CONTEXT_PATH, config.servletPrefix)

    /**
     * Starts the server, or throws an error if it's already running/starting/started.
     */
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

    /**
     * Starts the server and waits for user input to stop Jetty.
     */
    fun startInteractively() {
        println("Starting jetty at $fullUrl")
        start(suppressOutput = true)
        println("Hit ENTER to quit Jetty.")
        readLine()
        stop()
        println("Jetty stopped. Bye bye.")
    }

    /**
     * Will throw an error if server is not (yet) running.
     */
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
        // always override the error handler (could also disable explicit setting via config though)
        context.errorHandler = RootErrorHandler(config.errorHandler, config.exposeExceptions)
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
