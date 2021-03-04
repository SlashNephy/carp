package blue.starry.carp

import blue.starry.carp.parser.AptPackage
import blue.starry.carp.parser.AptPackagesParser
import blue.starry.carp.parser.AptSource
import blue.starry.carp.parser.AptSourceListParser
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mu.KotlinLogging
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.readText
import kotlin.time.minutes

object RepositoryManager {
    private val aptListPath = Paths.get("apt.list")
    private val logger = KotlinLogging.logger("carp.RepositoryManager")

    val Sources = ReadOnlyContainer<AptSource> {
        if (!Files.exists(aptListPath)) {
            return@ReadOnlyContainer
        }

        val content = aptListPath.readText()
        emitAll(
            AptSourceListParser.parse(content)
        )
    }

    val Packages = ReadOnlyContainer<AptPackage> {
        coroutineScope {
            Sources.map { source ->
                launch {
                    val content = try {
                        CarpHttpClient.get<String> {
                            url {
                                takeFrom(source.url)
                                encodedPath += "/Packages"
                            }
                            timeout {
                                socketTimeoutMillis = 10000
                            }
                        }
                    } catch (t: Throwable) {
                        logger.error(t) { "Error in fetch packages for $source" }
                        return@launch
                    }

                    emitAll(
                        AptPackagesParser.parse(content, source.url)
                    )
                }
            }.joinAll()
        }
    }

    class ReadOnlyContainer<T: Any>(private val block: suspend FlowCollector<T>.() -> Unit) {
        private val mutex = Mutex()
        private val collection = mutableListOf<T>()

        private val initialJob = GlobalScope.launch {
            val items = flow(block).toList()
            collection.addAll(items)
        }

        init {
            GlobalScope.launch {
                while (isActive) {
                    delay(Env.UPDATE_INTERVAL_MIN.minutes)
                    update()
                }
            }
        }

        suspend fun update() {
            initialJob.join()

            mutex.withLock {
                val new = flow(block).toList()

                collection.clear()
                collection.addAll(new)
            }
        }

        suspend fun add(new: T) {
            initialJob.join()

            mutex.withLock {
                collection.add(new)
            }
        }

        suspend fun replace(new: T, predicate: (T) -> Boolean) {
            initialJob.join()

            mutex.withLock {
                val index = collection.indexOfFirst(predicate)

                if (index < 0) {
                    add(new)
                } else {
                    collection[index] = new
                }
            }
        }

        suspend fun <R> map(transform: (T) -> R): List<R> {
            initialJob.join()

            return mutex.withLock {
                collection.map(transform)
            }
        }

        suspend fun filter(predicate: (T) -> Boolean): List<T> {
            initialJob.join()

            return mutex.withLock {
                collection.filter(predicate)
            }
        }

        suspend fun toList(): List<T> {
            initialJob.join()

            return mutex.withLock {
                collection.toList()
            }
        }
    }
}
