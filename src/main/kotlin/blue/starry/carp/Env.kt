package blue.starry.carp

import kotlin.properties.ReadOnlyProperty

object Env {
    val HTTP_HOST by string { "0.0.0.0" }
    val HTTP_PORT by int { 20503 }
}

private fun string(default: () -> String): ReadOnlyProperty<Env, String> = ReadOnlyProperty { _, property ->
    System.getenv(property.name) ?: default()
}

private fun int(default: () -> Int): ReadOnlyProperty<Env, Int> = ReadOnlyProperty { _, property ->
    System.getenv(property.name)?.toIntOrNull() ?: default()
}
