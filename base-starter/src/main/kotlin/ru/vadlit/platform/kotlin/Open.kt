package ru.vadlit.platform.kotlin

/**
 * Marker annotation for All-open plugin of Kotlin compiler. Annotated class will be open for inheritance. Necessary for AOP in Spring
 */
@Target(AnnotationTarget.CLASS)
annotation class Open
