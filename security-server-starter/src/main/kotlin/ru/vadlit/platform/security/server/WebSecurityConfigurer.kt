package ru.vadlit.platform.security.server

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
abstract class WebSecurityConfigurer : WebSecurityConfigurerAdapter(true) {

    final override fun configure(http: HttpSecurity) {
        applyDefault(http)

        config(http)

        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/management/**").permitAll()
            .antMatchers("/**").denyAll()
    }

    abstract fun config(http: HttpSecurity)

    private fun applyDefault(http: HttpSecurity) {
        http
            .exceptionHandling().and()
            .headers().and()
            .anonymous()
    }
}
