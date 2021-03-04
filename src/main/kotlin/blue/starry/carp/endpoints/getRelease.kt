package blue.starry.carp.endpoints

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getRelease() {
    get("Release") {
        call.respondTextWriter(ContentType.Application.OctetStream) {
            appendLine("Origin: quiprr")
            appendLine("Label: quiprr")
            appendLine("Suite: stable")
            appendLine("Version: 1.0.0")
            appendLine("Codename: qdev")
            appendLine("Architectures: iphoneos-arm")
            appendLine("Components: main")
            appendLine("Description: APT Repository for myself and others to host software and such.")
        }
    }
}
