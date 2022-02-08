package cz.cvut.fit.travelmatesserver.trip

import cz.cvut.fit.travelmatesserver.security.UserDetails
import cz.cvut.fit.travelmatesserver.trip.join.CreateJoinRequestDto
import cz.cvut.fit.travelmatesserver.trip.models.DetailedTripDto
import cz.cvut.fit.travelmatesserver.trip.models.NewTripDto
import cz.cvut.fit.travelmatesserver.trip.models.TripDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/trips")
class TripsRestController {

    @Autowired
    lateinit var tripsService: TripsService

    @PostMapping
    fun createTrip(authentication: Authentication, @RequestBody newTrip: NewTripDto): ResponseEntity<Unit> {
        val userDetails = authentication.principal as UserDetails
        tripsService.createTrip(newTrip, userDetails.email)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getTrips(
        authentication: Authentication,
        @RequestParam(name = PARAM_FILTER, required = false) filter: TripsFilter?
    ): ResponseEntity<List<TripDto>> {
        val userDetails = authentication.principal as UserDetails
        val trips = tripsService.getTrips(userDetails.email, filter ?: TripsFilter.UNKNOWN)
        return ResponseEntity.ok(trips)
    }

    @GetMapping("{tripId}")
    fun getTripDetails(
        authentication: Authentication,
        @PathVariable("tripId") tripId: Long
    ): ResponseEntity<DetailedTripDto> {
        val detailedTrip = tripsService.getTripDetails(tripId)
        return ResponseEntity.ok(detailedTrip)
    }

    @PostMapping("{tripId}/join")
    fun sendJoinRequest(
        authentication: Authentication,
        @PathVariable("tripId") tripId: Long,
        @RequestBody createTripRequestDto: CreateJoinRequestDto
    ): ResponseEntity<Unit> {
        val userDetails = authentication.principal as UserDetails
        tripsService.sendJoinRequest(userDetails.email, tripId, createTripRequestDto)
        return ResponseEntity.ok().build()
    }

    companion object {
        private const val PARAM_FILTER = "filter"
    }
}