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
class PostsService {

    @Autowired
    lateinit var postsRepository: PostsRepository

    @Autowired
    lateinit var userRepository: UserRepository

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

    fun getPosts(): List<PostDto> {
        return postsRepository.findAll().map {
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