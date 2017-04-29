# Http4k

Most basic request you can do:

```kotlin
val response = buildHttp4k().get<Response4k>("http://localhost:8042/rest/endpoint")
```
