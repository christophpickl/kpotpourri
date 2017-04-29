# KPotpourri

[ ![jcenter](https://api.bintray.com/packages/christophpickl/cpickl/kpotpourri/images/download.svg) ](https://bintray.com/christophpickl/cpickl/kpotpourri/_latestVersion)
[![Kotlin](https://img.shields.io/badge/kotlin-1.1.2-blue.svg)](http://kotlinlang.org)
[![Travis](https://img.shields.io/travis/christophpickl/kpotpourri.svg)](https://travis-ci.org/christophpickl/kpotpourri)
[![codecov](https://codecov.io/gh/christophpickl/kpotpourri/branch/master/graph/badge.svg)](https://codecov.io/gh/christophpickl/kpotpourri)
[![Issues](https://img.shields.io/github/issues/christophpickl/kpotpourri.svg)](https://github.com/christophpickl/kpotpourri/issues?q=is%3Aopen)
[![Dependency Versions](https://www.versioneye.com/user/projects/58e51229d6c98d0041747763/badge.svg?style=flat)](https://www.versioneye.com/user/projects/58e51229d6c98d0041747763)

## About

_KPotpourri_ is a collection of libraries covering many different aspects of day-to-day programming.
Mostly they introduce a more Kotlin-ish API over existing libraries like Logback, TestNG, Wiremock and others.
It tries to reduce the need of repeating yourself, filling the gaps and simplify usage by taking advantage Kotlin's language features. 

Following libraries are currently available:

* Common4k
* Github4k
* Http4k
* Jackson4k
* Logback4k
* Release4k
* Swing4k
* Test4k
* Web4k
* Wiremock4k


## Setup

Register the custom repository in your `build.gradle` file:

```groovy
repositories { 
  maven { 
    url "http://dl.bintray.com/christophpickl/cpickl" 
  }
}
```

Now feel free to add any dependency in its latest version:

```groovy
dependencies {
  compile 'com.github.christophpickl.kpotpourri:common4k:1.x'
}
```

## Common4k

Ordinary extensions for the standard library which I felt like missing.

```kotlin
TODO()
```

<!---[Read more](common4k/README.md)--->

## Http4k

Http4k is an abstraction for writing HTTP clients in a Kotlin-ish fashion, similar to what [Slf4j](https://www.slf4j.org/) is doing for logging in Java.
It supports [Apache's HTTP client](https://hc.apache.org/httpcomponents-client-ga/), [Fuel](https://github.com/kittinunf/Fuel) and maybe will in future support other major HTTP clients like [Spring's ReST template](https://spring.io/guides/gs/consuming-rest/) and others.  

**Sample code:**

```kotlin
// setup the http4k instance with some global settings
val http4k = buildHttp4k { 
    baseUrlBy("http://some.server/rest")
    addHeader("someConstant" to "headerValue")
}

// execute a GET /resource request
val response = http4k.get("/resource") {
    addHeader("Accept" to "my/content")
    addQueryParam("sort" to "asc")
}
// process response object
if (response.statusCode != SC_200_Ok) {
    // do something with response.bodyAsString, response.headers, ...
}

// or transform JSON with jackson
val dto = http4k.get("/dto", Dto::class)
```

[Read more](http4k/README.md)


## Jackson4k

Jackson configured in a nice way using Kotlin language features.

```kotlin
TODO()
```

<!---[Read more](jackson4k/README.md)--->


## Web4k

Preconfigured Jetty using ReST easy in order to rapidly set-up a Java like ReST API.

```kotlin
TODO()
```

<!---[Read more](web4k/README.md)--->


## Github4k

GitHub API abstraction

```kotlin
TODO()
```

<!---[Read more](4k/README.md)--->


### Swing4k

Extensions for the good old GUI framework (or use [TornadoFX](https://github.com/edvin/tornadofx) instead)

```kotlin
TODO()
```

<!---[Read more](swing4k/README.md)--->


### Release4k

Simple replacement for common release shell scripts.

```kotlin
TODO()
```

<!---[Read more](release4k/README.md)--->

## Logback4k

Simple way to programmatically configure logback, instead of using static XML files.

```kotlin
TODO()
```

<!---[Read more](logback4k/README.md)--->


## Github4k

High level SDK for accessing Github features.

```kotlin
TODO()
```

<!---[Read more](github4k/README.md)--->


## Test4k

Test extensions using TestNG, hamkrest and mockito-kotlin.

```kotlin
// instead of throwing a SkipException which leads to a warning about unreachable code ...
@Test fun `test is still work in progress`() {
    skip("WIP")
    // ... no warning about unreachable code
} 

```

<!---[Read more](test4k/README.md)--->


## Wiremock4k

Simplify the [Wirmock](http://wiremock.org/) API.

```kotlin
TODO()
```

<!---[Read more](wiremock4k/README.md)--->
