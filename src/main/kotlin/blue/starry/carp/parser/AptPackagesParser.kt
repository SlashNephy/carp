package blue.starry.carp.parser

import io.ktor.http.*
import kotlinx.coroutines.flow.flow
import kotlin.reflect.KProperty

object AptPackagesParser {
    private val entryRegex = "^(\\w+): (.+)$".toRegex()

    fun parse(content: String, url: Url) = flow {
        val entries = mutableMapOf<String, String>()
        var lastKey: String? = null

        for (line in content.lineSequence()) {
            // blank like means end of package
            if (line.isBlank()) {
                if (entries.isNotEmpty()) {
                    emit(
                        AptPackage(
                            Package = entries.getValue("Package"),
                            Version = entries.getValue("Version"),
                            Filename = entries.getValue("Filename"),
                            Entries = entries.toMap(),
                            url = url
                        )
                    )

                    entries.clear()
                    lastKey = null
                }

                continue
            }

            val match = entryRegex.matchEntire(line)
            if (match != null) {
                val (key, value) = match.destructured

                entries[key] = value
                lastKey = key
            } else { // continuation previous entry with newline
                val key = lastKey ?: error("Last key is not present. ($content)")
                entries[key] += line.trimStart()
            }
        }
    }

    private infix fun String.eq(propery: KProperty<*>): Boolean {
        return this.equals(propery.name, true)
    }
}
