package com.github.christophpickl.kpotpourri.web4k

import com.github.christophpickl.kpotpourri.common.logging.LOG
import org.eclipse.jetty.annotations.AnnotationConfiguration
import org.eclipse.jetty.annotations.ClassInheritanceHandler
import org.eclipse.jetty.util.ConcurrentHashSet
import org.eclipse.jetty.webapp.WebAppContext
import org.springframework.web.WebApplicationInitializer
import javax.servlet.ServletContext
import kotlin.reflect.KClass

class WebAppInitializingConfiguration : AnnotationConfiguration() {

    override fun preConfigure(context: WebAppContext) {
        val map = ClassInheritanceMap().addWebAppInit(MyWebAppInitializer::class)
        context.setAttribute(AnnotationConfiguration.CLASS_INHERITANCE_MAP, map)
        _classInheritanceHandler = ClassInheritanceHandler(map)
    }

    private fun ClassInheritanceMap.addWebAppInit(klass: KClass<out WebApplicationInitializer>): ClassInheritanceMap {
        put(WebApplicationInitializer::class.java.name,
                ConcurrentHashSet<String>().addKClass(klass))
        return this
    }

    private fun ConcurrentHashSet<String>.addKClass(klass: KClass<*>): ConcurrentHashSet<String> {
        add(klass.java.name)
        return this
    }
}

class MyWebAppInitializer : WebApplicationInitializer {
    val log = LOG {}
    override fun onStartup(container: ServletContext) {
        log.info("On startup ...")
    }
}

