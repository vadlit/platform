package ru.vadlit.platform.kotlin

/**
 * Marker annotation for No-arg plugin of Kotlin compiler. Default no-arg constructor will be generated for annotated class.
 */
@Target(AnnotationTarget.CLASS)
annotation class DefaultConstructor