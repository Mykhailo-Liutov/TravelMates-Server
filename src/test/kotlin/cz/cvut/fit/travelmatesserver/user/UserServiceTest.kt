package cz.cvut.fit.travelmatesserver.user

import cz.cvut.fit.travelmatesserver.security.UserDetails
import cz.cvut.fit.travelmatesserver.user.models.User
import io.mockk.MockKAnnotations
import io.mockk.MockKMatcherScope
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class UserServiceTest {

    private lateinit var userService: UserService

    @MockK
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        userService = UserService(userRepository)
    }

    private fun MockKMatcherScope.userEq(user: User) = match<User> {
        areUsersEqual(user, it)
    }

    private fun areUsersEqual(left: User, right: User): Boolean {
        return left.email == right.email &&
                left.name == right.name &&
                left.picture == right.picture
    }

    @Test
    fun `when new user logs in, his details are saved`() {
        every { userRepository.existsById(TEST_USER.email) } returns false
        every { userRepository.save(userEq(TEST_USER)) } returns TEST_USER

        userService.loginUser(UserDetails(TEST_USER.email, TEST_USER.name))

        verify(exactly = 1) { userRepository.save(userEq(TEST_USER)) }
    }

    @Test
    fun `when existing user logs in, his details are not updated`() {
        every { userRepository.existsById(TEST_USER.email) } returns true

        userService.loginUser(UserDetails(TEST_USER.email, TEST_USER.name))

        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `when getting user that doesn't exits, exception is thrown`() {
        every { userRepository.findById(TEST_USER.email) } returns Optional.empty()

        assertThrows<NoSuchElementException> {
            userService.getUser(TEST_USER.email)
        }
    }

    @Test
    fun `when getting user, correct user is returned`() {
        every { userRepository.findById(TEST_USER.email) } returns Optional.of(TEST_USER)

        val actual = userService.getUser(TEST_USER.email)

        assert(areUsersEqual(actual, TEST_USER))
    }

    @Test
    fun `when updating user, correct updated user is saved`() {
        every { userRepository.findById(TEST_USER.email) } returns Optional.of(TEST_USER)
        every { userRepository.save(userEq(NEW_USER)) } returns NEW_USER

        userService.updateUser(NEW_USER.email, NEW_USER.name, NEW_USER.picture)

        verify(exactly = 1) { userRepository.save(userEq(NEW_USER)) }
    }

    companion object {
        val TEST_USER = User("email", "name", null)
        val NEW_USER = User("email", "new_name", "picture")
    }
}