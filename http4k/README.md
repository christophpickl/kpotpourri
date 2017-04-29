# Http4k

Most basic request you can do:

```kotlin
import com.github.christophpickl.kpotpourri.http4k.Response4k
import com.github.christophpickl.kpotpourri.http4k.buildHttp4k
import com.github.christophpickl.kpotpourri.http4k.get

val response = buildHttp4k().get<Response4k>("http://localhost:8042/rest/endpoint")
```
