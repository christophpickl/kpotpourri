
* module ideas:
    * logback (programmatic appenders)
    * testng (custom listeners)
    * gmail
    * gcal

# What others do

* https://github.com/MarioAriasC/KotlinPrimavera/wiki
    * KotlinPrimavera is a set of Kotlin libraries to support Spring portfolio projects
* https://github.com/kohesive/klutter

# TODOs

## High
* get rid of guava Bytes stuff (create abstraction layer)
* get rid of: MockRequest instance
* automate upload of kdoc to github.io (while releasing)

## Med
* delete `Any.enforceAllBranchesCovered` and `Any.println` as they are too global and not worth it (?)
    * or simply ignore them in kdoc generation ;)
* parallel test builds: maxParallelForks = 2 (need to use random port for wiremock then!)
* could extract module kompile4k (out of markdown4k)
* ad web4k: RootErrorHandler does not catch 404s
* ad markdown4k: get rid of warning about Kotlin runtime library
* go through yobu (random stuff)
* go through gadsu (swing as well) and harvest stuff
    * BaseLogConfigurator
    * LogTestListener
* deploy dokka HTML to github (github.io?)
* dokka generates nasty warnings in build console: `Can't find node by signature com.github.christophpickl.kpotpourri.common$toPrettyString(kotlin.collections.List((kotlin.Any)))`

## Low

* proper bintray upload (for maven central): https://github.com/MicroUtils/kotlin-logging/blob/master/build.gradle
* fine tune dokka output (XyzKt files, code format, ...)
* @BUILD: automatically execute dokka task on package
* @BUILD: creating sources artifact in gradle for MyPublication doesnt work
* own android lib (harvest yobu)
* deploy to official maven repo (see nat pryce's hamkrest for how to do that)
    * http://central.sonatype.org/pages/ossrh-guide.html
    * need to define artifactId, needs to be automated
    * other artifacts need to be able to easily include this
* @release4k: dryRun() option
* enhance kdoc https://github.com/Kotlin/dokka (samples and stuff)
* enable CORS easily 
    * Access-Control-Allow-Origin = "*"
    * Access-Control-Allow-Methods = "POST, GET, PUT, DELETE, OPTIONS"
    * Access-Control-Allow-Credentials = true
    * Access-Control-Max-Age = '86400'; // 24 hours
    * Access-Control-Allow-Headers = "X-Requested-With, Access-Control-Allow-Origin, X-HTTP-Method-Override, Content-Type, Authorization, Accept"


# TODOs Markdown4k

* split into three libs: core, testng, junit (in order to make the base class and stuff visible via main dep, rather test dep)
* the assert Kompile matchers are not visible as they reside in test scope, but should be in main scope (exposing hamcrest dependency as non-test)

# TODOs HTTP4K

## TODOs High

* support cookies for request/response
* make headers (request/response) a multi valued map
* do not distinguish between bodyfull and bodyless :)

## TODOs Med

* overrideHttp4kImpl can be used to inject custom Adapter
* interceptor for beforeExec/afterExec, change A) in http4k or B) specific adapter
* proxy support
* support "validRange: IntRange" in addition to StatusFamily => 200..299
* could provide a system property which enforces a specific implementation (useful if there are more than 1)
* pagination (support multiple flavours, or even semi-customize)
* add error response listener, which (tries to) transforms the response body to a predefined DTO (and throws a custom exception)
* configure timeouts; otherwise: "Apr 13, 2017 12:03:48 AM org.apache.http.impl.execchain.RetryExec execute INFO: I/O exception (org.apache.http.NoHttpResponseException) caught when processing request to {}->http://localhost:8042: The target server failed to respond"
* NotFoundException, BadRequestException, ... for most important status codes
* multi form upload
* support variable replacement in passed URL
* enable/disable log output for (request/response) body
* support spring REST template
* support https://github.com/jkcclemens/khttp

## TODOs Low

* support custom http method (pass through an ordinary string)
* provide 3rd party http client impls
* TEST @StatusCodeCheckIT for SC_100_Continue/SC_301_Moved, as each http impl could behave differently
* @apache impl: when wiremock response with 100 -> .setRetryHandler()
* http4k verwenden um manch andere APIs aufzurufen, und schauen wie die sich verhalten und supporten
* lazily store response bodyAsString
* ad Http4k: couldnt add "returnType: KClass<R> = Response4k::class" ... :(

## From Fuel

- Support both asynchronous and blocking requests
- Download file
- Request timeout
- Special test mode for easier testing
- Support response deserialization into plain old object (both Kotlin & Java)
- Automatically invoke handler on Android Main Thread when using Android Module
- RxJava support out of the box
- Cancel in-flight request

## From khttp

- International domains and URLs
- Sessions with cookie persistence
- Elegant key/value cookies
- Automatic decompression
- Unicode response bodies
- Connection timeouts
