
# Requirements

* request payload: http methods are bodyless (GET) VS bodyfull (POST)
    * what about possibility to enforce body payload even with bodyless http method requests?
    * A. dont allow it all (via type system), B. allow it only via "hidden" API, C. dont disallow it at all (GET always supports request payload)
* response payload
    * by default `Response4k`
    * transform via jackson
    * kotlin primitive types (String, Int, Boolean, ...)
* define request metadata (query params, headers, cookies); global VS request scoped
* base URLs, globally configured, can be disabled by request

# Design

what should the API look like?

## Simple requests

```kotlin
val http4k = buildHttp4k()

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
