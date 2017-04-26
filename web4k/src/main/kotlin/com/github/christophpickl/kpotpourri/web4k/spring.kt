package com.github.christophpickl.kpotpourri.web4k

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
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
            ObjectMapper().apply {
                registerKotlinModule()
                configure(FAIL_ON_UNKNOWN_PROPERTIES, true)
                // setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                enable(INDENT_OUTPUT)
                // setSerializationInclusion(JsonInclude.Include.ALWAYS)
            }

}
