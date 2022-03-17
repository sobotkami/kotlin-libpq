package org.postgresql.io

/**
 * Closeable resource.
 */
public actual interface Closeable {
    actual fun close()
}

@PublishedApi
internal actual fun Throwable.addSuppressedInternal(other: Throwable) {
}