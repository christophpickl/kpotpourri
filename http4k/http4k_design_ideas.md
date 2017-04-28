
# Requirements

try to get a rough idea on what **http4k** should be about.

## General

* **simple** API, keep it to the bare minimum (concise as kotlin is)
* high level, generic **abstraction** (just as slf4j for logging), with implementations for different http clients (apache, spring rest, fuel) 
* take advantage of **kotlin** specific language features (lambdas/DSL, reified generics, collections/pairs)
* **testability** -functionality should be provided via an interface

## Specific

* **global VS request** scoped configuration
* **configuration** options: base URL, query params, headers, cookies
* **request** payload: http methods are bodyless (GET) VS bodyfull (POST)
    * A. dont allow it all (via type system), B. allow it only via "hidden" API, C. dont disallow it at all (GET always supports request payload)
    * what about possibility to enforce body payload even with bodyless http method requests?
* **response** payload
    * by default `Response4k`
    * transform via jackson
    * kotlin primitive types (String, Int, Boolean, ...)
* **compile** dependency on abstraction, **runtime** dependency on http implementations (with additional configuration option extensions which require compile scope)

# Design

what should the API look like?

## Simple requests

```kotlin
// simplest request ever
val response = http4k.get("http://localhost/string")

// TODO is this even possible, overloading a method just by having an additional type parameter?!
val dto = http4k.get<SomeDto>("http://localhost/dto")
```

## Query params

```kotlin
http4k.get("http://localhost?key=val")

http4k.get("http://localhost") {
    queryParams += "key" to "val"
}

http4k.get("http://localhost?key1=val1") {
    queryParams += "key2" to "val2"
}
```

## Sending Payload

```kotlin
// send simple string
http4k.post("http://localhost/string", "some data")

// automatic JSON conversion
http4k.post("http://localhost/dto", dto)

// or dont send any payload at all
http4k.post("http://localhost/string")

// get more fine-grained control
http4k.post("http://localhost/dto") {
    body = byString("asdf")
    // body = byJson(dto)
    // body = byBinary(byteArrayOf(0, 1, 1, 0))
}
```

## Setting Up

```kotlin
val http4k = buildHttp4k {
    baseUrl = "http://some.server/rest"
    queryParams += "q" to "value"
    headers += "Accept" to "application/json"
    enforceStatusFamily(StatusFamily.Success_2)
    basicAuth("user", "pass")
}
```
