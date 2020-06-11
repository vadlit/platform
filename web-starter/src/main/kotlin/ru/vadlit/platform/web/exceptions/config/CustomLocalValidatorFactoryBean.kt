package ru.vadlit.platform.web.exceptions.config

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.time.Clock
import javax.validation.ClockProvider
import javax.validation.metadata.ConstraintDescriptor

/**
 * Workarounds for validation error propagation
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CustomLocalValidatorFactoryBean : LocalValidatorFactoryBean() {

    override fun getArgumentsForConstraint(
        objectName: String,
        field: String,
        descriptor: ConstraintDescriptor<*>?
    ): Array<Any> {
        val orig = super.getArgumentsForConstraint(objectName, field, descriptor)
        if (descriptor == null) {
            return orig
        }

        if (orig.isNullOrEmpty()) {
            return arrayOf(descriptor)
        }

        return Array(orig.size + 1) {
            if (it == orig.size) {
                descriptor // add last element for VndUtils.extractConstraintDescriptor
            }
            else {
                orig[it]
            }
        }
    }

    override fun getClockProvider(): ClockProvider {
        return ClockProvider { Clock.systemDefaultZone() }
    }
}