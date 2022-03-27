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
class JoinRequestsService {

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var equipmentRepository: EquipmentRepository

    @Autowired
    lateinit var joinRequestRepository: JoinRequestRepository

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
        memberRepository.save(member)
        joinRequestRepository.deleteById(requestId)
    }

    @Transactional
    fun rejectRequest(userEmail: String, requestId: Long) {
        ensureUserIsOwner(userEmail, requestId)
        val existingRequest = joinRequestRepository.getById(requestId)
        val rejectedRequest = with(existingRequest) {
            JoinRequest(
                id,
                sentAt,
                message,
                providedEquipment.toList(),
                JoinRequestState.REJECTED,
                contact,
                sender,
                trip
            )
        }
        joinRequestRepository.save(rejectedRequest)
    }

    private fun ensureUserIsOwner(userEmail: String, requestId: Long) {
        val request = joinRequestRepository.getById(requestId)
        if (request.trip.owner.email != userEmail) {
            //TODO Change exception type
            throw IllegalStateException("Only trip owner is allowed to do this action.")
        }
    }

}