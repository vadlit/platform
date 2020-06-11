package ru.vadlit.platform.web.exceptions.config

import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.validation.Validator
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@AutoConfigureAfter(WebMvcAutoConfiguration::class)
@ConditionalOnClass(RestController::class)
@ComponentScan(value = ["ru.vadlit.platform.web.exceptions"])
class MvcExceptionHandlingAutoConfiguration : WebMvcConfigurerAdapter() {

    private var validator: CustomLocalValidatorFactoryBean? = null

    override fun getValidator(): Validator {
        return validator ?: customLocalValidatorFactoryBean()
    }

    @Bean
    fun customLocalValidatorFactoryBean(): CustomLocalValidatorFactoryBean {
        return CustomLocalValidatorFactoryBean().also { validator = it }
    }
}