
* module ideas:
    * release scripting infrastructure (github api module)
    * gmail
    * gcal
    * own http client abstraction like slf4j for logging

# Konkurrenzanalyse

* https://github.com/MarioAriasC/KotlinPrimavera/wiki
    * KotlinPrimavera is a set of Kotlin libraries to support Spring portfolio projects
* mooore...

# TODOs

## High

* go through gadsu (swing as well) and harvest stuff
* dokka generates nasty warnings in build console: `Can't find node by signature com.github.christophpickl.kpotpourri.common$toPrettyString(kotlin.collections.List((kotlin.Any)))`

## Low

* own android lib (harvest yobu)
* deploy to official maven repo (see nat pryce's hamkrest for how to do that)
    * http://central.sonatype.org/pages/ossrh-guide.html
    * need to define artifactId, needs to be automated
    * other artifacts need to be able to easily include this
