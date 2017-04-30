package com.github.christophpickl.kpotpourri.web4k.non_test

import com.github.christophpickl.kpotpourri.common.KPotpourriException
import com.github.christophpickl.kpotpourri.web4k.JettyConfig
import com.github.christophpickl.kpotpourri.web4k.JettyServer
import com.github.christophpickl.kpotpourri.web4k.WebConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

fun main(args: Array<String>) {
    JettyServer(JettyConfig(
            springConfig = DemoSpringConfig::class
    )).startInteractively()
}

@Configuration @Import(WebConfig::class)
class DemoSpringConfig {
    @Bean fun demoResource() = DemoResource()
}

@Path("/")
class DemoResource {

    @GET @Produces("text/plain")
    fun getRoot() = "this is root"

    @GET @Path("/fail")
    fun getFail() {
        throw KPotpourriException("i dont want to work anymore")
    }
}

