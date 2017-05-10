# KPotpourri

[ ![jcenter](https://api.bintray.com/packages/christophpickl/cpickl/kpotpourri/images/download.svg) ](https://bintray.com/christophpickl/cpickl/kpotpourri/_latestVersion)
[![Kotlin](https://img.shields.io/badge/kotlin-1.1.2-blue.svg)](http://kotlinlang.org)
[![Travis](https://img.shields.io/travis/christophpickl/kpotpourri.svg)](https://travis-ci.org/christophpickl/kpotpourri)
[![codecov](https://codecov.io/gh/christophpickl/kpotpourri/branch/master/graph/badge.svg)](https://codecov.io/gh/christophpickl/kpotpourri)
[![Issues](https://img.shields.io/github/issues/christophpickl/kpotpourri.svg)](https://github.com/christophpickl/kpotpourri/issues?q=is%3Aopen)
[![Dependency Versions](https://www.versioneye.com/user/projects/58e51229d6c98d0041747763/badge.svg?style=flat)](https://www.versioneye.com/user/projects/58e51229d6c98d0041747763)

## About

<img style="float: right;" src="https://github.com/christophpickl/kpotpourri/raw/master/doc/images/kotlin.png">
_KPotpourri_ is a collection of several tools and libraries covering different aspects of a programmer's life.
Mostly they introduce a more Kotlin-ish API over existing libraries like Logback, TestNG, Wiremock and others.
It tries to reduce the need of repeating yourself, filling the gaps and simplify usage by taking advantage Kotlin's language features.

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

Maven users please visit the [Maven setup](doc/maven_setup.md) page.
