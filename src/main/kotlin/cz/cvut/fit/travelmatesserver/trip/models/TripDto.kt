package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripState
import java.time.LocalDate

data class TripDto(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("location")
    val location: Coordinates,
    @JsonProperty("requirements")
    val requirements: List<RequirementDto>,
    @JsonProperty("owner")
    val owner: PublicUserDto,
    @JsonProperty("state")
    val state: TripState,
    @JsonProperty("suggestedTime")
    val suggestedTime: LocalDate
)




