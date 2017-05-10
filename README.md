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

* Common4k ... Just some common stuff enhancing the standard library.
* Github4k ... SDK for Github's API.
* Http4k ... Easy to use HTTP client abstraction.
* Jackson4k ... Simplify configuration of Jackson Kotlin module.
* Logback4k ... Programmatic Logback configuration.
* Markdown4k ... Automatically check if Kotlin code snippets in Markdown files are actually compileable. ([Read more](markdown4k/README.md))
* Release4k ... High level release scripts in Kotlin.
* Swing4k ... Common Swing extensions.
* Test4k ... Test extensions for TestNG, Hamkrest and Mockito-Kotlin.
* Web4k ... Jetty server using ReST easy for rapid setup.
* Wiremock4k ... Wiremock extensions.


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

For everyone else not using Gradle, please visit the [Maven setup](doc/maven_setup.md) page.

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
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.SC_200_Ok
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get

// setup the http4k instance with some global settings
val http4k = buildHttp4k { 
    baseUrlBy("http://some.server/rest")
    addHeader("someConstant" to "headerValue")
}

// execute a GET /resource request
val response = http4k.get<Response4k>("/resource") {
    addHeader("Accept" to "my/content")
    addQueryParam("sort" to "asc")
}
// process response object
if (response.statusCode != SC_200_Ok) {
    // do something with response.bodyAsString, response.headers, ...
}

// or transform JSON with jackson
data class Dto(val name: String)
val dto = http4k.get<Dto>("/dto")
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
import com.github.christophpickl.kpotpourri.test4k.skip
import org.testng.annotations.Test

// instead of throwing a SkipException which leads to a warning about unreachable code ...
@Test fun `test is still work in progress`() {
    skip("WIP")
    // ... no warning about unreachable code
} 

```

<!---[Read more](test4k/README.md)--->


## Wiremock4k

Simplify the [Wiremock](http://wiremock.org/) API.

```kotlin
TODO()
```

<!---[Read more](wiremock4k/README.md)--->


## Markdown4k

Automatically check if Kotlin code snippets in Markdown files are compilable.

The quickest way to trigger a compilation cycle is by collecting and compiling:

```kotlin
import com.github.christophpickl.kpotpourri.markdown4k.Markdown4k
import com.github.christophpickl.kpotpourri.markdown4k.KompilationResult
import java.io.File

Markdown4k.kollect(File("root_directory")).forEach { 
    val result: KompilationResult = Markdown4k.kompile(it)
    // do something with [ Success, Failure(ScriptException), Ignored ]
}
```

The suggested approach is rather to integrate these steps into a testing framework (TestNG, JUnit).

[Read more](markdown4k/README.md)
