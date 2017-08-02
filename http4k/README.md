# Http4k

Http4k is an abstraction for writing HTTP clients in a Kotlin-ish fashion, similar to what [Slf4j](https://www.slf4j.org/) is doing for logging in Java.
It supports [Apache's HTTP client](https://hc.apache.org/httpcomponents-client-ga/), [Fuel](https://github.com/kittinunf/Fuel) and maybe will in future support other major HTTP clients like [Spring's ReST template](https://spring.io/guides/gs/consuming-rest/) and others.  

Feature list:

* Kotlin-ish idiom using DSLs
* Automatically detect concrete HTTP client implementation during runtime (via reflection)
* Possibility to make use of implementation specific configuration options via Http4k's meta map
* Jackson JSON marshalling built-in
* Global configuration (e.g. to set the base URL)
* Request configuration which takes precedence over global configuration
* Request body can be a File or a usual ByteArray 
* Status code validation (by number, family or custom code)

Roadmap:

* Interceptors
* Pagination support
* Path variable substitution
* Sort/filter support
* Default error type conversion
* Support Spring ReST templates

## Configuration

In order to avoid compile time dependencies on the concrete implementation being used, decalre the HTTP implementation in runtime scope separately:

```groovy
dependencies {
  compile 'com.github.christophpickl.kpotpourri:http4k:$versionKPotpourri'
  runtime 'com.github.christophpickl.kpotpourri:http4k-apache:$versionKPotpourri'
}
```


## Sample code

Most basic GET request you can do:

```kotlin
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get

val response = buildHttp4k().get<Response4k>("http://localhost:8042/rest/endpoint")
```

### Requesting

**Global vs request scoped configuration**:

If there would be conflicting configurations between them, then the request scope always wins.

```kotlin
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get

// configure global settings
val http4k = buildHttp4k { 
    baseUrlBy("http://some.server/rest")
    addHeader("someConstant" to "headerValue")
}

// configure request settings
val responseBody = http4k.get<String>("/resource") {
    addHeader("Accept" to "my/content")
    addQueryParam("sort" to "asc")
}
```


### Process response

You can configure different types as return types, whereas the special type `Response4k` contains detailed information about the actual response,
any other will try to do some conversion.

```kotlin
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get

val http4k = buildHttp4k()

val response = http4k.get<Response4k>("")
if (response.statusCode != SC_200_Ok) {
    println("Body: ${response.bodyAsString}")
    println("Accept Header: ${response.headers["Accept"]}")
}

// or transform JSON with jackson
data class Dto(val name: String)

val dto = http4k.get<Dto>("/dto")
```
