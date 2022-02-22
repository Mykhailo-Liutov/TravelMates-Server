package cz.cvut.fit.travelmatesserver.posts

import cz.cvut.fit.travelmatesserver.posts.models.Post
import org.springframework.data.repository.CrudRepository

interface PostsRepository : CrudRepository<Post, Long> {
    fun findByOrderByCreatedAtDesc():List<Post>
}