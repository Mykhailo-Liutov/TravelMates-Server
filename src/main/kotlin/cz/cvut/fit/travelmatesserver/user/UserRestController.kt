package cz.cvut.fit.travelmatesserver.user

import cz.cvut.fit.travelmatesserver.security.UserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserRestController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("login")
    fun loginUser(authentication: Authentication): ResponseEntity<Unit> {
        val userDetails = authentication.principal as UserDetails
        userService.loginUser(userDetails)
        return ResponseEntity.ok().build()
    }

}