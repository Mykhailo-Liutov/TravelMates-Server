package cz.cvut.fit.travelmatesserver.user

import cz.cvut.fit.travelmatesserver.security.UserDetails
import cz.cvut.fit.travelmatesserver.user.models.UserDto
import cz.cvut.fit.travelmatesserver.user.models.UserUpdateDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserRestController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("login")
    fun loginUser(authentication: Authentication): ResponseEntity<Unit> {
        val userDetails = authentication.principal as UserDetails
        userService.loginUser(userDetails)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getUser(authentication: Authentication): ResponseEntity<UserDto> {
        val userDetails = authentication.principal as UserDetails
        val foundUser = userService.getUser(userDetails.email)
        return ResponseEntity.ok(UserDto(foundUser.email, foundUser.name, foundUser.picture))
    }

    @PutMapping
    fun updateUser(authentication: Authentication,@RequestBody newUser: UserUpdateDto): ResponseEntity<UserDto> {
        val userDetails = authentication.principal as UserDetails
        val updatedUser = userService.updateUser(userDetails.email, newUser.name, newUser.picture)
        return ResponseEntity.ok(UserDto(updatedUser.email, updatedUser.name, updatedUser.picture))
    }

}