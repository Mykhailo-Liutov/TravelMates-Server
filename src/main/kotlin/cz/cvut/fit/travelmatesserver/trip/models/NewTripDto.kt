package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class NewTripDto(
    @JsonProperty("title") val title: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("location") val location: Coordinates,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty("suggestedDate")
    val suggestedDate: LocalDate,
    @JsonProperty("ownerContact") val ownerContact: String,
    @JsonProperty("requirements") val requirements: List<NewRequirement>
)