
# HTTP4K Todos

## TODOs High

* support cookies for request/response
* make headers (request/response) a multi valued map
* get rid of ByteSource in (public) API
* do not distinguish between bodyfull and bodyless :)

## TODOs Med

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
