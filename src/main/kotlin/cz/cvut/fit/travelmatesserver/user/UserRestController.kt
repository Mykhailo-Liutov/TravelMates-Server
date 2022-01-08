package cz.cvut.fit.travelmatesserver.user

import cz.cvut.fit.travelmatesserver.security.UserDetails
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserRestController {

    @GetMapping
    fun test(authentication: Authentication): String {
        val userDetails = authentication.principal as UserDetails
        return userDetails.email
    }

}