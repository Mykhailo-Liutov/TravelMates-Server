package cz.cvut.fit.travelmatesserver.posts

import cz.cvut.fit.travelmatesserver.posts.models.NewPostDto
import cz.cvut.fit.travelmatesserver.posts.models.Post
import cz.cvut.fit.travelmatesserver.posts.models.PostDto
import cz.cvut.fit.travelmatesserver.trip.models.Coordinates
import cz.cvut.fit.travelmatesserver.trip.models.PublicUserDto
import cz.cvut.fit.travelmatesserver.user.UserRepository
import cz.cvut.fit.travelmatesserver.user.models.User
import io.mockk.MockKAnnotations
import io.mockk.MockKMatcherScope
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class PostsServiceTest {

    private lateinit var postsService: PostsService

    @MockK
    lateinit var postsRepository: PostsRepository

    @MockK
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        postsService = PostsService(postsRepository, userRepository)
    }

    private fun MockKMatcherScope.postEq(post: Post) = match<Post> {
        post.id == it.id &&
                post.description == it.description &&
                post.image == it.image &&
                post.latitude == it.latitude &&
                post.longitude == it.longitude &&
                post.creator.email == it.creator.email
    }

    @Test
    fun `when creating a post, it is saved correctly`() {
        every { userRepository.getById(TEST_USER.email) } returns TEST_USER
        every { postsRepository.save(postEq(TEST_POST)) } returns TEST_POST

        postsService.createPost(TEST_NEW_POST_DTO, TEST_USER.email)

        verify(exactly = 1) { postsRepository.save(postEq(TEST_POST)) }
    }

    @Test
    fun `when getting list of posts, correct list is returned`() {
        every { postsRepository.findByOrderByCreatedAtDesc() } returns listOf(TEST_POST)

        val actual = postsService.getPosts().first()

        assert(actual == TEST_POST_DTO)
    }

    companion object {
        val TEST_NEW_POST_DTO = NewPostDto("description", "image", Coordinates(1.0, 2.0))
        val TEST_USER = User("email", "name", "picture")
        val TEST_POST = Post(
            0,
            TEST_NEW_POST_DTO.description,
            TEST_NEW_POST_DTO.image,
            TEST_NEW_POST_DTO.location.lat,
            TEST_NEW_POST_DTO.location.lon,
            LocalDateTime.now(),
            TEST_USER
        )
        val TEST_POST_DTO = with(TEST_POST) {
            PostDto(
                0, description, Coordinates(
                    latitude,
                    longitude
                ), createdAt, image, PublicUserDto(
                    creator.name,
                    creator.picture
                )
            )
        }
    }
}