package cz.cvut.fit.travelmatesserver.trip.join

import cz.cvut.fit.travelmatesserver.trip.TripsRepository
import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRepository
import cz.cvut.fit.travelmatesserver.trip.member.MemberRepository
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripMember
import cz.cvut.fit.travelmatesserver.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class JoinRequestsService @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val joinRequestRepository: JoinRequestRepository
) {

    /**
     * Accepts a given join request on behalf of given user.
     */
    @Transactional
    fun acceptRequest(userEmail: String, requestId: Long) {
        ensureUserIsOwner(userEmail, requestId)
        val existingRequest = joinRequestRepository.getById(requestId)
        val member = TripMember(
            0,
            LocalDateTime.now(),
            existingRequest.contact,
            existingRequest.providedEquipment.toList(),
            existingRequest.trip,
            existingRequest.sender
        )
        //Make user a member as result of accepting request
        memberRepository.save(member)
        //Delete the accepted request
        joinRequestRepository.deleteById(requestId)
    }

    /**
     * Rejects a given join request on behalf of given user.
     */
    @Transactional
    fun rejectRequest(userEmail: String, requestId: Long, rejectMessage: String, allowResend: Boolean) {
        ensureUserIsOwner(userEmail, requestId)
        val existingRequest = joinRequestRepository.getById(requestId)
        val newState = if (allowResend) JoinRequestState.REJECTED_ALLOW_RESEND else JoinRequestState.REJECTED_NO_RESEND
        //Create a rejected request entity
        val rejectedRequest = with(existingRequest) {
            JoinRequest(
                id,
                sentAt,
                message,
                providedEquipment.toList(),
                newState,
                contact,
                sender,
                trip,
                rejectMessage
            )
        }
        //Save the updated request
        joinRequestRepository.save(rejectedRequest)
    }

    /**
     * Checks if user is owner of given trip, throws and exception if not
     * @param userEmail Email of user to check
     * @param requestId Id of request to check
     * @throws IllegalAccessException if user is not an owner
     */
    private fun ensureUserIsOwner(userEmail: String, requestId: Long) {
        val request = joinRequestRepository.getById(requestId)
        if (request.trip.owner.email != userEmail) {
            throw IllegalAccessException("Only trip owner is allowed to do this action.")
        }
    }

}