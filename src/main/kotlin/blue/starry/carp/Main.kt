package blue.starry.carp

import io.ktor.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, module = Application::module)
}
