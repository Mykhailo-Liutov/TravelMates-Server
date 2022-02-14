package cz.cvut.fit.travelmatesserver.trip.models

import com.fasterxml.jackson.annotation.JsonProperty
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripState
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class DetailedTripDto(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("description")
    val description: String,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty("suggestedDate")
    val suggestedDate: LocalDate,
    @JsonProperty("state")
    val state: TripState,
    @JsonProperty("location")
    val location: Coordinates,
    @JsonProperty("pendingRequirements")
    val pendingRequirements: List<RequirementDto>,
    @JsonProperty("owner")
    val owner: MemberDto,
    @JsonProperty("members")
    val members: List<MemberDto>,
    @JsonProperty("currentUserType")
    val userType: UserType,
    @JsonProperty("requests")
    val requests: List<RequestDto>?
)