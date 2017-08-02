# Web4k

```groovy
compile 'com.github.christophpickl.kpotpourri:web4k:$versionKPotpourri'
```

Preconfigured Jetty using ReST easy in order to rapidly set-up a Java like ReST API.

```kotlin
package com.github.christophpickl.kpotpourri.web4k.non_test

import com.github.christophpickl.kpotpourri.web4k.JettyConfig
import com.github.christophpickl.kpotpourri.web4k.JettyServer
import com.github.christophpickl.kpotpourri.web4k.Web4kSpringConfig
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
    // listens on: http://localhost:8442/rest
}

@Configuration @Import(Web4kSpringConfig::class)
class DemoSpringConfig {
    @Bean fun personResource() = PersonResource()
}

@Path("/")
class PersonResource {
    @GET @Produces("application/json")
    fun getPerson() = Person("anna", 42)
}

data class Person(val name: String, val age: Int)
```

The response of the server will be a properly rendered JSON as expected:

```json
{
  "name": "anna",
  "age": 42
}
```
