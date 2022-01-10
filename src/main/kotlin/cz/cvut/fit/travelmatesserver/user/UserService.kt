package cz.cvut.fit.travelmatesserver.user

import cz.cvut.fit.travelmatesserver.security.UserDetails
import cz.cvut.fit.travelmatesserver.user.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun loginUser(userDetails: UserDetails) {
        if (userRepository.existsById(userDetails.email)) {
            //User already exists, no need to save
            return
        }
        val user = User(userDetails.email, userDetails.name, picture = null)
        userRepository.save(user)
    }

}