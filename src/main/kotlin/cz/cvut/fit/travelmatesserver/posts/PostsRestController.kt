package cz.cvut.fit.travelmatesserver.posts

import cz.cvut.fit.travelmatesserver.posts.models.NewPostDto
import cz.cvut.fit.travelmatesserver.posts.models.PostDto
import cz.cvut.fit.travelmatesserver.security.UserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
class PostsRestController {

    @Autowired
    lateinit var postsService: PostsService

    @PostMapping
    fun createPost(authentication: Authentication, @RequestBody newPost: NewPostDto): ResponseEntity<Unit> {
        val userDetails = authentication.principal as UserDetails
        postsService.createPost(newPost, userDetails.email)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getPosts(authentication: Authentication): ResponseEntity<List<PostDto>> {
        val posts = postsService.getPosts()
        return ResponseEntity.ok(posts)
    }

}