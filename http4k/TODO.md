
# TODOs High

* fix GitHub API issues
    * binary upload
* Support POST/PUT/DELETE/PATCH...

# TODOs Med

* support 2nd implementation (fuel, spring rest template)
* cookies for request/response
* custom configs, e.g. for apache: httpRequest.config for timeouts
* make headers (request/response) a multi valued map
* could provide a system property which enforces a specific implementation (useful if there are more than 1)

# TODOs Low

* multi form upload
* lazily store response bodyAsString
* NotFoundException, BadRequestException, ... for most important status codes
* support variable replacement in passed URL
* ad Http4k: couldnt add "returnType: KClass<R> = Response4k::class" ... :(
