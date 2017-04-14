

* ad release4k: dryRun() option
* ad BUILD: running http4k-tests should have impact on coverage of http4k module itself :-/
* overrideHttp4kImpl can be used to inject custom Adapter
* interceptor for beforeExec/afterExec, change A) in http4k or B) specific adapter
* get rid of guava Bytes stuff (create abstraction layer)
* get rid of: MockRequest
* simple release script for kpot

-------


* module ideas:
    * release scripting infrastructure (use github api module)
    * logback (programmatic appenders)
    * testng (custom listeners)
    * wiremock (reusage)
    * gmail
    * gcal

# Konkurrenzanalyse

* https://github.com/MarioAriasC/KotlinPrimavera/wiki
    * KotlinPrimavera is a set of Kotlin libraries to support Spring portfolio projects
* mooore...

# TODOs

## High

* go through yobu (random stuff)
* go through gadsu (swing as well) and harvest stuff
* dokka generates nasty warnings in build console: `Can't find node by signature com.github.christophpickl.kpotpourri.common$toPrettyString(kotlin.collections.List((kotlin.Any)))`

## Low

* versioneye tracks only deps for reactor project :-/
* own android lib (harvest yobu)
* deploy to official maven repo (see nat pryce's hamkrest for how to do that)
    * http://central.sonatype.org/pages/ossrh-guide.html
    * need to define artifactId, needs to be automated
    * other artifacts need to be able to easily include this
