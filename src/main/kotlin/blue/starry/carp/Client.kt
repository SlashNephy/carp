package blue.starry.carp

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.http.*

val CarpHttpClient by lazy {
    HttpClient {
        install(HttpTimeout)

        defaultRequest {
            userAgent("carp (+https://github.com/SlashNephy/carp)")
        }
    }
}
