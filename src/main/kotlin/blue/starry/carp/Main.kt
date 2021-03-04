package blue.starry.carp

import io.ktor.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

suspend fun main() {
    RepositoryManager.Packages.toList().forEach {
        println(it)
    }

    embeddedServer(CIO, host = Env.HTTP_HOST, port = Env.HTTP_PORT, module = Application::module)
}
