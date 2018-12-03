package ru.pocketbyte.hydra.log

import kotlinx.cinterop.*
import platform.Foundation.*
import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze

actual object HydraLog: Logger {

    private var loggerRef: AtomicReference<Logger?> = AtomicReference(null)

    actual fun init(logger: Logger) {
        if (!this.loggerRef.compareAndSet(null, logger.freeze()))
            throw IllegalStateException("HydraLog already initialized")
    }

    actual override fun log(level: LogLevel, tag: String?, message: String, vararg arguments: Any) {
        if (arguments.isEmpty())
            this.getOrInitLogger().log(level, tag, message)
        else
            this.getOrInitLogger().log(level, tag) { format(message, *arguments) }
    }

    actual override fun log(level: LogLevel, tag: String?, exception: Throwable) {
        this.getOrInitLogger().log(level, tag, exception)
    }

    actual override fun log(level: LogLevel, tag: String?, function: () -> String) {
        this.getOrInitLogger().log(level, tag, function)
    }

    private fun getOrInitLogger(): Logger {
        val logger = this.loggerRef.value
        if (logger == null) {
            HydraLog.initDefault()
            return getOrInitLogger()
        }
        return logger
    }
}
