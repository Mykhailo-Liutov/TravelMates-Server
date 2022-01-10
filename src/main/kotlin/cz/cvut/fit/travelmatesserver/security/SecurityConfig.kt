package cz.cvut.fit.travelmatesserver.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    private val jwtTokenFilter = JwtTokenFilter()

    override fun configure(http: HttpSecurity) {
        // Set session management to stateless
        var config = http
        config = config
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

        config.csrf().disable()

        // Set unauthorized requests exception handler
        config = config
            .exceptionHandling()
            .authenticationEntryPoint { _: HttpServletRequest?, response: HttpServletResponse, ex: AuthenticationException ->
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ex.message
                )
            }
            .and()

        // Set permissions on endpoints
        config.authorizeRequests()
            .anyRequest().fullyAuthenticated()

        // Add JWT token filter
        config.addFilterBefore(
            jwtTokenFilter,
            UsernamePasswordAuthenticationFilter::class.java
        )
    }

}