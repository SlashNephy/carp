package blue.starry.carp.parser

import io.ktor.http.*

data class AptSource(
    val type: Type,
    val url: Url,
    val path: String
) {
    enum class Type {
        deb
    }
}
