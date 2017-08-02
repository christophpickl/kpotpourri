# Logback4k

```groovy
compile 'com.github.christophpickl.kpotpourri:logback4k:$versionKPotpourri'
```
Simple way to programmatically configure logback, instead of using plain XML files.

```kotlin
import ch.qos.logback.classic.Level
import com.github.christophpickl.kpotpourri.logback4k.Logback4k

Logback4k.reconfigure { 
    rootLevel = Level.ALL
    packageLevel(Level.WARN, "org.apache", "org.jetty")
    addConsoleAppender { 
        appenderName = "myAppender"
        level = Level.ALL // by default
        pattern = "%-43(%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread]) [%-5level] %logger{42} - %msg%n"
    }
}
```
