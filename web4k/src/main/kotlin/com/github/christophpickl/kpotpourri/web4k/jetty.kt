package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.common.logging.LOG
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.webapp.Configuration
import org.eclipse.jetty.webapp.WebAppContext
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap
import org.jboss.resteasy.plugins.spring.SpringContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import kotlin.reflect.KClass

class JettyServer(
        private val springConfig: KClass<*>,
        private val port: Int = DEFAULT_BUFFER_SIZE,
        private val servletPrefix: String = "/rest"
) {

    companion object {
        val DEFAULT_PORT = 8442
    }

    private val log = LOG {}

    private var server: Server? = null

    fun start() {
        println("Starting Jetty ...")

        server = Server(DEFAULT_PORT)
        server!!.handler = newContext()
        server!!.start()

        println()
        println("Jetty started at http://localhost:$port$servletPrefix :)")
    }

    private fun newContext(): WebAppContext {
        val context = WebAppContext()
        context.resourceBase = "src/main/webapp"
        context.contextPath = "/"
        context.isParentLoaderPriority = true

        initReasEasyAndSpring(context)

        context.addFilter(RequestResponseDumpFilter::class.java, "$servletPrefix/*", null)

        val configs = arrayOf<Configuration>(WebAppInitializingConfiguration())
        context.configurations = configs

        // TODO context.setErrorHandler()

        return context
    }

    private fun initReasEasyAndSpring(context: WebAppContext) {
        log.info { "Registering RestEasy servlet mapping prefix: $servletPrefix" }
        context.setInitParameter("resteasy.servlet.mapping.prefix", servletPrefix)

        context.addEventListener(ResteasyBootstrap())
        context.addServlet(HttpServletDispatcher::class.java, "$servletPrefix/*")

        context.addEventListener(SpringContextLoaderListener())
        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext::class.java.name)
        log.info { "Registering spring config: ${springConfig.java.name}" }
        context.setInitParameter("contextConfigLocation", springConfig.java.name)
    }

    fun startInteractively() {
        start()
        println("Hit ENTER to quit Jetty.")
        readLine()
        stop()
        println("Jetty stopped. Bye bye.")
    }

    fun stop() {
        server?.let {
            println("Stopping Jetty...")
            it.stop()
            it.join()
        } ?: throw IllegalStateException("Jetty was not yet started!")
    }

}
