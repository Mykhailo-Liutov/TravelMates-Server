package cz.cvut.fit.travelmatesserver.posts

import cz.cvut.fit.travelmatesserver.posts.models.NewPostDto
import cz.cvut.fit.travelmatesserver.posts.models.Post
import cz.cvut.fit.travelmatesserver.posts.models.PostDto
import cz.cvut.fit.travelmatesserver.trip.models.Coordinates
import cz.cvut.fit.travelmatesserver.trip.models.PublicUserDto
import cz.cvut.fit.travelmatesserver.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostsService @Autowired constructor(
    private val postsRepository: PostsRepository,
    private val userRepository: UserRepository
) {


    /**
     * Creates a new post
     * @param newPostDto post to create
     * @param userEmail email of creator
     */
    fun createPost(newPostDto: NewPostDto, userEmail: String) {
        val ownerRef = userRepository.getById(userEmail)
        val postEntity = with(newPostDto) {
            Post(
                0,
                description,
                image,
                location.lat,
                location.lon,
                LocalDateTime.now(),
                ownerRef
            )
        }
        postsRepository.save(postEntity)
    }

    /**
     * @return List of posts, sorted by creation date
     */
    fun getPosts(): List<PostDto> {
        return postsRepository.findByOrderByCreatedAtDesc().map {
            PostDto(
                it.id,
                it.description,
                Coordinates(it.latitude, it.longitude),
                it.createdAt,
                it.image,
                PublicUserDto(it.creator.name, it.creator.picture)
            )
        }
    }

}