package cz.cvut.fit.travelmatesserver.user

import cz.cvut.fit.travelmatesserver.security.UserDetails
import cz.cvut.fit.travelmatesserver.user.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    /**
     * Ensures that user is present in the server's database
     * @param userDetails details of user performing the request
     */
    fun loginUser(userDetails: UserDetails) {
        if (userRepository.existsById(userDetails.email)) {
            //User already exists, no need to save
            return
        }
        val user = User(userDetails.email, userDetails.name, picture = null)
        userRepository.save(user)
    }

    /**
     * Finds a user based on the email
     * @param email email of user to find
     * @return the object of the found user
     */
    fun getUser(email: String): User {
        val existingUserOptional = userRepository.findById(email)
        if (existingUserOptional.isEmpty) {
            throw NoSuchElementException("User with email $email doesn't exist")
        }
        return existingUserOptional.get()
    }

    /**
     * Updates given user
     * @param email email of user to update
     * @param name the new name of user
     * @param picture the new picture of user
     */
    fun updateUser(email: String, name: String, picture: String?): User {
        val existingUser = getUser(email)
        val newUser = User(existingUser.email, name, picture)
        return userRepository.save(newUser)
    }

}