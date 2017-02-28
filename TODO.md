
# TODOs

## High

* deploy to official maven repo (see nat pryce's hamkrest for how to do that)
    * http://central.sonatype.org/pages/ossrh-guide.html
    * need to define artifactId, needs to be automated
    * other artifacts need to be able to easily include this
* go through gadsu and harvest stuff

## Med

* better tooling:
    * travis build
    * jacoco, codecov
    * versioneye
* dokka generates nasty warnings in build console: `Can't find node by signature com.github.christophpickl.kpotpourri.common$toPrettyString(kotlin.collections.List((kotlin.Any)))`

## Low

* add README badges:
    * [![Build Status](https://travis-ci.org/npryce/hamkrest.svg?branch=master)](https://travis-ci.org/npryce/hamkrest)
    * [![Maven Central](https://img.shields.io/maven-central/v/com.natpryce/hamkrest.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.natpryce%22%20AND%20a%3A%22hamkrest%22)
* own android lib (harvest yobu)
