package cz.cvut.fit.travelmatesserver.security

import com.auth0.jwk.GuavaCachedJwkProvider
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import cz.cvut.fit.travelmatesserver.Constants
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.security.interfaces.RSAPublicKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtTokenFilter : OncePerRequestFilter() {

    /**
     * Verifies authorization of the request's JWT token
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        // Verifying the format of the header
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response)
            return
        }

        // Validating the signature of the token
        val token = header.split(" ")[1].trim()
        if (!validateToken(token)) {
            chain.doFilter(request, response)
            return
        }

        // Setting user details to security context
        val authentication = UsernamePasswordAuthenticationToken(
            extractDetails(token), null,
            null
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    /**
     * Validates the signature of the JWT token
     * @return If token is valid or not
     */
    private fun validateToken(token: String): Boolean {
        return try {
            val jwt = JWT.decode(token)
            val jwkProvider: JwkProvider = UrlJwkProvider(Constants.JWKS_URL)
            val cachedJwkProvider = GuavaCachedJwkProvider(jwkProvider)
            val jwk = cachedJwkProvider.get(jwt.keyId)
            val algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)
            algorithm.verify(jwt)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Extracts user details from the claims of given token
     * @return details of the user connected to given token
     */
    private fun extractDetails(token: String): UserDetails {
        val jwt = JWT.decode(token)
        return UserDetails(
            jwt.getClaim(CLAIM_KEY_EMAIL).asString(),
            jwt.getClaim(CLAIM_KEY_NAME).asString()
        )
    }

    companion object {
        private const val CLAIM_KEY_EMAIL = "email"
        private const val CLAIM_KEY_NAME = "name"
    }

}