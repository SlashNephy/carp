package blue.starry.carp.parser

import io.ktor.http.*
import kotlinx.coroutines.flow.flow

object AptSourceListParser {
    private val lineRegex = "^deb (https?://.+) (.+)$".toRegex()

    fun parse(content: String) = flow {
        for (line in content.lineSequence()) {
            val match = lineRegex.matchEntire(line) ?: continue

            emit(
                AptSource(
                    type = AptSource.Type.deb,
                    url = Url(match.groupValues[1]),
                    path = match.groupValues[2]
                )
            )
        }
    }
}
