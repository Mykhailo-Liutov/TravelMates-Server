package cz.cvut.fit.travelmatesserver.posts.models

import com.fasterxml.jackson.annotation.JsonProperty
import cz.cvut.fit.travelmatesserver.trip.models.Coordinates
import cz.cvut.fit.travelmatesserver.trip.models.PublicUserDto
import java.time.LocalDateTime

/**
 * DTO for post object
 */
data class PostDto(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("location")
    val location: Coordinates,
    @JsonProperty("createdAt")
    val createdAt: LocalDateTime,
    @JsonProperty("image")
    val image: String,
    @JsonProperty("creator")
    val creator: PublicUserDto
)
