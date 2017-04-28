package com.github.christophpickl.kpotpourri.web4k

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.christophpickl.kpotpourri.jackson4k.buildJackson4kObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider


@Configuration
class WebConfig {
    @Bean fun kotlinObjectMapperProvider() = KotlinObjectMapperProvider()
//    @Bean fun jsonParseExceptionMapper() = JsonParseExceptionMapper()
    /*

@Provider
class JsonParseExceptionMapper extends ExceptionMapper[JsonParseException] {
  override def toResponse(exception: JsonParseException) =
    Response.status(Response.Status.BAD_REQUEST).`type`(MediaType.APPLICATION_JSON).entity(ErrorResponsePayload("Invalid JSON input!", exception)).build
}
     */
}

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class KotlinObjectMapperProvider : ContextResolver<ObjectMapper> {
    override fun getContext(type: Class<*>?) =
    buildJackson4kObjectMapper(
            failOnUnknownProperties = true
    )
}
