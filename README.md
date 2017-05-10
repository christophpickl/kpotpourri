# KPotpourri

[ ![jcenter](https://api.bintray.com/packages/christophpickl/cpickl/kpotpourri/images/download.svg) ](https://bintray.com/christophpickl/cpickl/kpotpourri/_latestVersion)
[![Kotlin](https://img.shields.io/badge/kotlin-1.1.2-blue.svg)](http://kotlinlang.org)
[![Travis](https://img.shields.io/travis/christophpickl/kpotpourri.svg)](https://travis-ci.org/christophpickl/kpotpourri)
[![codecov](https://codecov.io/gh/christophpickl/kpotpourri/branch/master/graph/badge.svg)](https://codecov.io/gh/christophpickl/kpotpourri)
[![Issues](https://img.shields.io/github/issues/christophpickl/kpotpourri.svg)](https://github.com/christophpickl/kpotpourri/issues?q=is%3Aopen)
[![Dependency Versions](https://www.versioneye.com/user/projects/58e51229d6c98d0041747763/badge.svg?style=flat)](https://www.versioneye.com/user/projects/58e51229d6c98d0041747763)

## About

_KPotpourri_ is a collection of several tools and libraries covering different aspects of a programmer's life.
Mostly they introduce a more Kotlin-ish API over existing libraries like Logback, TestNG, Wiremock and others.
It tries to reduce the need of repeating yourself, filling the gaps and simplify usage by taking advantage Kotlin's language features.

![Kotlin](https://github.com/christophpickl/kpotpourri/raw/master/doc/images/kotlin.png)

* Common
    * [Common4k](common4k/README.md) ... Common extensions to the standard library (like Guava for Java).
    * [Test4k](test4k/README.md) ... Test extensions for TestNG, Hamkrest and Mockito-Kotlin.
    * [Swing4k](swing4k/README.md) ... Common Swing extensions.
    * [Github4k](github4k/README.md) ... SDK for Github's ReST API.
    * [Logback4k](logback4k/README.md) ... Programmatic [Logback](https://logback.qos.ch/) configuration.
* Tools
    * [Markdown4k](markdown4k/README.md) ... Automatically check if Kotlin code snippets in markdown files are compilable.
    * [Release4k](release4k/README.md) ... High level build/release/deploy script abstraction.
* Web
    * [Jackson4k](jackson4k/README.md) ... Simplify configuration of [Jackson Kotlin](https://github.com/FasterXML/jackson-module-kotlin) module.
    * [Http4k](http4k/README.md) ... HTTP client abstraction.
    * [Web4k](web4k/README.md) ... Ready to use [Jetty](http://www.eclipse.org/jetty/) server using ReST Easy for rapid setup.
    * [Wiremock4k](wiremock4k/README.md) ... [Wiremock](http://wiremock.org/) extensions.


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
  compile 'com.github.christophpickl.kpotpourri:common4k:$versionKPotpourri'
  
  testCompile 'com.github.christophpickl.kpotpourri:test4k:$versionKPotpourri'
}
```

For everyone else not using Gradle, please visit the [Maven setup](doc/maven_setup.md) page.



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
