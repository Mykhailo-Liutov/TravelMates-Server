package cz.cvut.fit.travelmatesserver.posts.models

import com.fasterxml.jackson.annotation.JsonProperty
import cz.cvut.fit.travelmatesserver.trip.models.Coordinates

data class NewPostDto(
    @JsonProperty("description")
    val description: String,
    @JsonProperty("image")
    val image: String,
    @JsonProperty("location")
    val location: Coordinates
)