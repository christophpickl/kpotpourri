
# TODOs High

* split into modules!
* fix GitHub API issues
* Support POST/PUT/DELETE/PATCH...

# TODOs Med

* add opt to implicitly check for 2xx, and possibility to inject own behaviour/ (status code) validator
* support 2nd implementation (fuel)
* request/response cookies
* could introduce NotFoundException, BadRequestException, ... for most important status codes

# TODOs Low

* binary upload
* multi form upload
* support variable replacement in passed URL
* lazily store response bodyAsString
* ad Http4k: couldnt add "returnType: KClass<R> = Response4k::class" ... :(

# IDEAs

* split into http4k-api and { http4k-apache, http4k-fuel, http4k-spring }
