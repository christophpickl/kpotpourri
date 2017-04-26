package com.github.christophpickl.kpotpourri.web4k

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
        private val contextPath: String = "/rest"
) {

    companion object {
        val DEFAULT_PORT = 8442
    }
    private lateinit var server: Server

    fun start() {
        println("Starting Jetty ...")

        server = Server(DEFAULT_PORT)
        server.handler = newContext()
        server.start()

        println()
        println("Jetty started at http://localhost:$port$contextPath :)")
    }

    private fun newContext(): WebAppContext {
        val context = WebAppContext()
        context.resourceBase = "src/main/webapp"
        context.contextPath = "/"
        context.isParentLoaderPriority = true

        initReasEasyAndSpring(context)

        context.addFilter(RequestResponseDumpFilter::class.java, "$contextPath/*", null)

        val configs = arrayOf<Configuration>(WebAppInitializingConfiguration())
        context.configurations = configs

        // TODO context.setErrorHandler()

        return context
    }

    private fun initReasEasyAndSpring(context: WebAppContext) {
        context.setInitParameter("resteasy.servlet.mapping.prefix", contextPath)

        context.addEventListener(ResteasyBootstrap())
        context.addServlet(HttpServletDispatcher::class.java, "$contextPath/*")

        context.addEventListener(SpringContextLoaderListener())
        context.setInitParameter("contextClass", AnnotationConfigWebApplicationContext::class.java.name)
        context.setInitParameter("contextConfigLocation", springConfig.java.name)

    }

    fun startInteractively()  {
        start()
        println("Hit ENTER to quit Jetty.")
        readLine()
        stop()
        println("Jetty stopped. Bye bye.")
    }

    fun stop() {
        @Suppress("SENSELESS_COMPARISON")
        if (server == null) {
            throw IllegalStateException("Jetty was not yet started!")
        }
        println("Stopping Jetty...")
        server.stop()
        server.join()
    }

}
