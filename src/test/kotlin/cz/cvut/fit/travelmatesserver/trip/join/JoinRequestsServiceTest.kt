package cz.cvut.fit.travelmatesserver.trip.join

import cz.cvut.fit.travelmatesserver.trip.TripsService
import cz.cvut.fit.travelmatesserver.trip.TripsServiceTest
import cz.cvut.fit.travelmatesserver.trip.member.MemberRepository
import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripMember
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripState
import cz.cvut.fit.travelmatesserver.user.models.User
import io.mockk.MockKAnnotations
import io.mockk.MockKMatcherScope
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime


class JoinRequestsServiceTest {

    private lateinit var joinRequestsService: JoinRequestsService

    @MockK
    lateinit var memberRepository: MemberRepository

    @MockK
    lateinit var joinRequestRepository: JoinRequestRepository

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        joinRequestsService = JoinRequestsService(memberRepository, joinRequestRepository)
    }

    private fun MockKMatcherScope.memberEq(member: TripMember) = match<TripMember> {
        member.id == it.id &&
                member.contact == it.contact &&
                member.memberUser.email == it.memberUser.email &&
                member.memberTrip.id == it.memberTrip.id &&
                member.providedEquipment.map { it.id } == it.providedEquipment.map { it.id }
    }

    private fun MockKMatcherScope.requestEq(request: JoinRequest) = match<JoinRequest> {
        request.id == it.id &&
                request.contact == it.contact &&
                request.state == it.state &&
                request.sender.email == it.sender.email &&
                request.providedEquipment.map { it.id } == it.providedEquipment.map { it.id }
    }

    @Test
    fun `when owner accepts join request, member is created, request is deleted`() {
        val expectedMember = TripMember(
            0, LocalDateTime.of(2022, 1, 1, 1, 1), JOIN_REQUEST.contact, JOIN_REQUEST.providedEquipment,
            JOIN_REQUEST.trip, JOIN_REQUEST.sender
        )
        every { memberRepository.save(memberEq(expectedMember)) } returns expectedMember
        every { joinRequestRepository.deleteById(JOIN_REQUEST.id) } returns Unit
        every { joinRequestRepository.getById(JOIN_REQUEST.id) } returns JOIN_REQUEST

        joinRequestsService.acceptRequest(TEST_USER.email, JOIN_REQUEST.id)

        verify(exactly = 1) { memberRepository.save(memberEq(expectedMember)) }
        verify(exactly = 1) { joinRequestRepository.deleteById(JOIN_REQUEST.id) }
    }

    @Test
    fun `when guest tries to accept join request, exception is thrown`() {
        every { joinRequestRepository.getById(JOIN_REQUEST.id) } returns JOIN_REQUEST

        assertThrows<IllegalAccessException> {
            joinRequestsService.acceptRequest(GUEST_USER.email, JOIN_REQUEST.id)
        }
    }

    @Test
    fun `when owner rejects request with allow resend, state of request is changed correctly`() {
        val expectedRequest = with(JOIN_REQUEST) {
            JoinRequest(
                id,
                sentAt,
                message,
                providedEquipment,
                JoinRequestState.REJECTED_ALLOW_RESEND,
                contact,
                sender,
                trip,
                rejectionReason
            )
        }
        every { joinRequestRepository.getById(JOIN_REQUEST.id) } returns JOIN_REQUEST
        every { joinRequestRepository.save(requestEq(expectedRequest)) } returns expectedRequest

        joinRequestsService.rejectRequest(TEST_USER.email, JOIN_REQUEST.id, REJECT_REASON, allowResend = true)

        verify(exactly = 1) { joinRequestRepository.save(requestEq(expectedRequest)) }
    }

    @Test
    fun `when owner rejects request without allow resend, state of request is changed correctly`() {
        val expectedRequest = with(JOIN_REQUEST) {
            JoinRequest(
                id,
                sentAt,
                message,
                providedEquipment,
                JoinRequestState.REJECTED_NO_RESEND,
                contact,
                sender,
                trip,
                rejectionReason
            )
        }
        every { joinRequestRepository.getById(JOIN_REQUEST.id) } returns JOIN_REQUEST
        every { joinRequestRepository.save(requestEq(expectedRequest)) } returns expectedRequest

        joinRequestsService.rejectRequest(TEST_USER.email, JOIN_REQUEST.id, REJECT_REASON, allowResend = false)

        verify(exactly = 1) { joinRequestRepository.save(requestEq(expectedRequest)) }
    }


    @Test
    fun `when guest tries to reject join request, exception is thrown`() {
        every { joinRequestRepository.getById(JOIN_REQUEST.id) } returns JOIN_REQUEST

        assertThrows<IllegalAccessException> {
            joinRequestsService.rejectRequest(GUEST_USER.email, JOIN_REQUEST.id, REJECT_REASON, false)
        }
    }

    companion object {
        val TEST_USER = User("email", "name", "picture")
        val GUEST_USER = User("guest_email", "guest_name", "guest_picture")
        val TEST_DATE = LocalDate.of(2022, 1, 1)
        val TRIP_1 = Trip(
            0,
            "title",
            "description",
            1.0,
            2.0,
            TEST_DATE,
            "ownerContact",
            TripState.GATHERING,
            TEST_USER,
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
        val JOIN_REQUEST = JoinRequest(
            1,
            LocalDateTime.of(2022, 1, 1, 1, 1),
            "message",
            listOf(TripsServiceTest.EQUIPMENT_REQUIREMENT),
            JoinRequestState.PENDING,
            "contact",
            TripsServiceTest.TEST_USER,
            TripsServiceTest.TRIP_1,
            null
        )
        const val REJECT_REASON = "reject_reason"
    }
}