package blue.starry.carp.parser

import io.ktor.http.*

data class AptPackage(
    val Package: String,
    val Version: String,
    val Filename: String,
    val Entries: Map<String, String>,
    val url: Url
)
