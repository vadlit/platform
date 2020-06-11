package ru.vadlit.platform.web.exceptions

import mu.KLogging
import org.springframework.beans.TypeMismatchException
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConversionException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.vadlit.platform.web.exceptions.VndErrors.VndError
import ru.vadlit.platform.web.exceptions.VndUtils.extractErrorDetails
import ru.vadlit.platform.web.exceptions.VndUtils.processBindingResult
import javax.validation.ConstraintViolationException

@ControllerAdvice(annotations = [RestController::class, RequestMapping::class])
internal class ExceptionHandlingControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): VndErrors {
        logger.warn { "${e.bindingResult.allErrors}" }
        return processBindingResult(e.bindingResult.allErrors)
    }

    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBindException(e: BindException): VndErrors {
        logger.warn { "${e.bindingResult.allErrors}" }
        return processBindingResult(e.bindingResult.allErrors)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        HttpMessageNotReadableException::class
    )
    @ResponseBody
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): VndErrors {
        return VndErrors(e.message, "*")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        HttpMessageConversionException::class
    )
    @ResponseBody
    fun handleHttpMessageConversionException(e: HttpMessageConversionException): VndErrors {
        return VndErrors(e.message, "*")
    }

    @ExceptionHandler(TypeMismatchException::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleTypeMismatchException(e: TypeMismatchException): VndErrors {
        return VndErrors(e.message, "*")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        HttpMediaTypeNotSupportedException::class
    )
    @ResponseBody
    fun handleHttpMediaTypeNotSupportedException(e: HttpMediaTypeNotSupportedException): VndErrors {
        return VndErrors(e.message, "*")
    }

    @ExceptionHandler(DetailedRuntimeException::class)
    @ResponseBody
    fun handleDetailedRuntimeException(e: DetailedRuntimeException): ResponseEntity<VndErrors> {
        val status = AnnotationUtils.findAnnotation(
            e.javaClass,
            ResponseStatus::class.java
        )
        val responseStatus = status?.code ?: HttpStatus.INTERNAL_SERVER_ERROR
        if (responseStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            logger.error(e) { "${e.message}, details: ${e.details}" }
        } else {
            logger.warn(e) { "${e.message}, details: ${e.details}" }
        }
        val vndError = VndError(e.message, e.details)
        return ResponseEntity(VndErrors(vndError), responseStatus)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(e: ConstraintViolationException) =
        VndErrors(
            e.constraintViolations
                .map { constraint ->
                    VndError(
                        constraint.message,
                        constraint.propertyPath.toString(),
                        extractErrorDetails(constraint.constraintDescriptor)
                    )
                }
                .toMutableList()
        )

    @ExceptionHandler(Throwable::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleThrowable(e: Throwable): VndErrors {
        logger.error(e.message, e)
        return VndErrors("Operation can not be processed, go to system administrator", "*")
    }

    private companion object : KLogging()
}