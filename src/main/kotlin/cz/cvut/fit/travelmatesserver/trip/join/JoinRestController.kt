package cz.cvut.fit.travelmatesserver.trip.join

import cz.cvut.fit.travelmatesserver.security.UserDetails
import cz.cvut.fit.travelmatesserver.trip.TripsService
import cz.cvut.fit.travelmatesserver.trip.models.NewTripDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/requests")
class JoinRestController {

    @Autowired
    lateinit var joinRequestsService: JoinRequestsService

    @PostMapping("{requestId}/accept")
    fun acceptRequest(
        authentication: Authentication,
        @PathVariable("requestId") requestId: Long
    ): ResponseEntity<Unit> {
        val userDetails = authentication.principal as UserDetails
        joinRequestsService.acceptRequest(userDetails.email, requestId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("{requestId}/reject")
    fun rejectRequest(
        authentication: Authentication,
        @PathVariable("requestId") requestId: Long,
        @RequestBody rejectRequestDto: RejectRequestDto
    ): ResponseEntity<Unit> {
        val userDetails = authentication.principal as UserDetails
        joinRequestsService.rejectRequest(
            userDetails.email,
            requestId,
            rejectRequestDto.reason,
            rejectRequestDto.allowResend
        )
        return ResponseEntity.ok().build()
    }

}