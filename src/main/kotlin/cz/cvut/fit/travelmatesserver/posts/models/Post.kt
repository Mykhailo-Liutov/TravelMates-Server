package cz.cvut.fit.travelmatesserver.posts.models

import cz.cvut.fit.travelmatesserver.user.models.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "post")
class Post(
    @GeneratedValue
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "description")
    val description: String,
    @Column(name = "image")
    val image: String,
    @Column(name = "latitude")
    val latitude: Double,
    @Column(name = "longitude")
    val longitude: Double,
    @Column(name = "createdAt")
    val createdAt: LocalDateTime,
    @ManyToOne
    @JoinColumn(name = "creator")
    val creator: User
)