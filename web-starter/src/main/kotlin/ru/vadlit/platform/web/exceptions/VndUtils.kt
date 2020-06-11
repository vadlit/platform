package ru.vadlit.platform.web.exceptions

import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import ru.vadlit.platform.web.exceptions.VndErrors.VndError
import javax.validation.metadata.ConstraintDescriptor

object VndUtils {
    private const val DEFAULT_MESSAGE = "WEB_STARTER_DEFAULT_MESSAGE_TEMPLATE"

    private val IGNORED_JAVAX_VALIDATION_ATTRIBUTES = setOf("groups", "payload", "message")

    fun processBindingResult(objectErrors: Collection<ObjectError>) = VndErrors(
        objectErrors
            .map { error ->
                val constraintDescriptor = extractConstraintDescriptor(error)
                val path = if (error is FieldError) error.field else "*"
                var errorMessage = constraintDescriptor.messageTemplate
                if (errorMessage == DEFAULT_MESSAGE) {
                    errorMessage = error.defaultMessage
                }
                VndError(errorMessage, path, extractErrorDetails(constraintDescriptor))
            }
            .toMutableList()
    )

    /**
     * tightly coupled with the logic of CustomLocalValidatorFactoryBean
     */
    fun extractConstraintDescriptor(objectError: ObjectError) = objectError.arguments[objectError.arguments.size - 1] as ConstraintDescriptor<*>

    fun extractErrorDetails(constraintDescriptor: ConstraintDescriptor<*>) = constraintDescriptor.attributes.entries
        .filter { !IGNORED_JAVAX_VALIDATION_ATTRIBUTES.contains(it.key) }
        .associateBy { it.key }
}