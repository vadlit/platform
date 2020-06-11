package ru.vadlit.platform.web.exceptions

abstract class DetailedRuntimeException : RuntimeException {

    val details: Map<String, Any>?

    constructor() {
        this.details = null
    }

    constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace) {
        this.details = null
    }

    constructor(message: String, cause: Throwable) : super(message, cause) {
        this.details = null
    }

    constructor(message: String) : super(message) {
        this.details = null
    }

    constructor(message: String, details: Map<String, Any>) : super(message) {
        this.details = details
    }

    constructor(
        message: String,
        details: Map<String, Any>,
        enableSuppression: Boolean = true,
        writableStackTrace: Boolean = true
    ) : super(message, null, enableSuppression, writableStackTrace) {
        this.details = details
    }

    constructor(cause: Throwable) : super("", cause) {
        this.details = null
    }

    constructor(cause: Throwable, details: Map<String, Any>) : super("", cause) {
        this.details = details
    }

    constructor(details: Map<String, Any>) : super("") {
        this.details = details
    }
}
