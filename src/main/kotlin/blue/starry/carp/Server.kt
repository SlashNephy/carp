package blue.starry.carp

import blue.starry.carp.endpoints.getRelease
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import mu.KotlinLogging

fun Application.module() {
    install(XForwardedHeaderSupport)

    install(CallLogging) {
        logger = KotlinLogging.logger("carp.server")
        format { call ->
            when (val status = call.response.status()) {
                HttpStatusCode.Found -> "$status: ${call.request.toLogString()} -> ${call.response.headers[HttpHeaders.Location]}"
                null -> ""
                else -> "$status: ${call.request.httpMethod.value} ${call.request.uri}"
            }
        }
    }

    routing {
        getRelease()
    }
}
