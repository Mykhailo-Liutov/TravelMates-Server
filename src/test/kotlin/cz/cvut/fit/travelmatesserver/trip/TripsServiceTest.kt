package cz.cvut.fit.travelmatesserver.trip

import cz.cvut.fit.travelmatesserver.trip.converters.TripsConverter
import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRepository
import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRequirement
import cz.cvut.fit.travelmatesserver.trip.join.CreateJoinRequestDto
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequest
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequestRepository
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequestState
import cz.cvut.fit.travelmatesserver.trip.models.Coordinates
import cz.cvut.fit.travelmatesserver.trip.models.NewRequirement
import cz.cvut.fit.travelmatesserver.trip.models.NewTripDto
import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripState
import cz.cvut.fit.travelmatesserver.user.UserRepository
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

class TripsServiceTest {

    lateinit var tripsService: TripsService

    @MockK
    lateinit var tripsRepository: TripsRepository

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var equipmentRepository: EquipmentRepository

    @MockK
    lateinit var joinRequestRepository: JoinRequestRepository

    //Converter is not mocked, as conversions can not be mocked.
    //Conversion itself is tested separately
    private val tripsConverter = TripsConverter()

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        tripsService =
            TripsService(tripsRepository, userRepository, equipmentRepository, joinRequestRepository, tripsConverter)
    }

    private fun MockKMatcherScope.tripEq(trip: Trip) = match<Trip> {
        trip.id == it.id &&
                trip.description == it.description &&
                trip.title == it.title &&
                trip.latitude == it.latitude &&
                trip.longitude == it.longitude &&
                trip.owner.email == it.owner.email &&
                trip.tripImages == it.tripImages
    }

    private fun MockKMatcherScope.requirementListEq(equipmentRequirements: List<EquipmentRequirement>) =
        match<List<EquipmentRequirement>> {
            equipmentRequirements.zip(it).all { (l, r) -> l.id == r.id && l.name == r.name && l.trip.id == r.trip.id }
        }

    private fun MockKMatcherScope.requestEq(joinRequest: JoinRequest) = match<JoinRequest> {
        joinRequest.id == it.id &&
                joinRequest.state == it.state &&
                joinRequest.message == it.message &&
                joinRequest.contact == it.contact &&
                joinRequest.trip.id == it.trip.id &&
                joinRequest.sender.email == it.sender.email &&
                joinRequest.providedEquipment.map { it.id } == it.providedEquipment.map { it.id }
    }

    @Test
    fun `when creating a trip it is saved correctly`() {
        val requirements = listOf(
            NewRequirement("req1"), NewRequirement("req2")
        )
        val expectedRequirements = requirements.map {
            EquipmentRequirement(0, it.name, TRIP_1)
        }
        val tripDto = with(TRIP_1) {
            NewTripDto(
                title,
                description,
                Coordinates(TRIP_1.latitude, TRIP_1.longitude),
                suggestedDate,
                ownerContact,
                requirements
            )
        }
        every { userRepository.getById(TEST_USER.email) } returns TEST_USER
        every { tripsRepository.save(tripEq(TRIP_1)) } returns TRIP_1
        every { equipmentRepository.saveAll(requirementListEq(expectedRequirements)) } returns emptyList()

        tripsService.createTrip(tripDto, TEST_USER.email)
    }

    @Test
    fun `when getting explore trips, correct list is returned`() {
        every { tripsRepository.findExploreTrips(TEST_USER.email) } returns listOf(TRIP_1)
        val expected = tripsConverter.entityToDto(TRIP_1, TEST_USER.email)

        val actual = tripsService.getTrips(TEST_USER.email, TripsFilter.EXPLORE).first()

        assert(actual == expected)
    }

    @Test
    fun `when getting my trips trips, correct list is returned`() {
        every { tripsRepository.findMyTrips(TEST_USER.email) } returns listOf(TRIP_2)
        val expected = tripsConverter.entityToDto(TRIP_2, TEST_USER.email)

        val actual = tripsService.getTrips(TEST_USER.email, TripsFilter.MY_TRIPS).first()

        assert(actual == expected)
    }

    @Test
    fun `when getting unknown trips, correct list is returned`() {
        every { tripsRepository.findAll() } returns listOf(TRIP_3)
        val expected = tripsConverter.entityToDto(TRIP_3, TEST_USER.email)

        val actual = tripsService.getTrips(TEST_USER.email, TripsFilter.UNKNOWN).first()

        assert(actual == expected)
    }

    @Test
    fun `when getting trip details, correct trip is returned`() {
        every { tripsRepository.findTripById(TRIP_1.id) } returns TRIP_1
        val expected = tripsConverter.entityToDto(TRIP_1, TEST_USER.email)

        val actual = tripsService.getTripDetails(TRIP_1.id, TEST_USER.email)

        assert(actual == expected)
    }

    @Test
    fun `when creating join request, it is saved correctly, existing request is deleted`() {
        val joinRequest = CreateJoinRequestDto(
            JOIN_REQUEST.message,
            JOIN_REQUEST.contact,
            JOIN_REQUEST.providedEquipment.map { it.id })
        val expectedRequest = JoinRequest(
            0,
            LocalDateTime.of(2022, 1, 1, 1, 1),
            joinRequest.message,
            listOf(EQUIPMENT_REQUIREMENT),
            JoinRequestState.PENDING,
            joinRequest.contact,
            TEST_USER,
            TRIP_1,
            null
        )
        every { joinRequestRepository.findRequest(TEST_USER.email, TRIP_1.id) } returns JOIN_REQUEST
        every { joinRequestRepository.deleteById(JOIN_REQUEST.id) } returns Unit
        every { equipmentRepository.getById(EQUIPMENT_REQUIREMENT.id) } returns EQUIPMENT_REQUIREMENT
        every { userRepository.getById(TEST_USER.email) } returns TEST_USER
        every { tripsRepository.getById(TRIP_1.id) } returns TRIP_1
        every { joinRequestRepository.save(requestEq(expectedRequest)) } returns expectedRequest

        tripsService.sendJoinRequest(TEST_USER.email, TRIP_1.id, joinRequest)

        verify(exactly = 1) { joinRequestRepository.deleteById(JOIN_REQUEST.id) }
        verify(exactly = 1) { joinRequestRepository.save(requestEq(expectedRequest)) }
    }

    @Test
    fun `when owner is uploading an image, trip is updated correctly`() {
        val expected = with(TRIP_1) {
            Trip(
                id,
                title,
                description,
                latitude,
                longitude,
                suggestedDate,
                ownerContact,
                state,
                owner,
                members,
                requirements,
                joinRequests,
                listOf(
                    IMAGE_REF
                )
            )
        }
        every { tripsRepository.findTripById(TRIP_1.id) } returns TRIP_1
        every { tripsRepository.save(tripEq(expected)) } returns expected

        tripsService.uploadImage(TEST_USER.email, TRIP_1.id, IMAGE_REF)

        verify(exactly = 1) { tripsRepository.save(tripEq(expected)) }
    }

    @Test
    fun `when guest tries to upload an image, an exception is thrown`() {
        every { tripsRepository.findTripById(TRIP_1.id) } returns TRIP_1

        assertThrows<IllegalAccessException> {
            tripsService.uploadImage(GUEST_USER.email, TRIP_1.id, IMAGE_REF)
        }
    }

    @Test
    fun `when owner stops gathering trip, it's updated correctly`() {
        val expected = with(TRIP_1) {
            Trip(
                id,
                title,
                description,
                latitude,
                longitude,
                suggestedDate,
                ownerContact,
                TripState.GATHERED,
                owner,
                members,
                requirements,
                joinRequests,
                tripImages
            )
        }
        every { tripsRepository.getById(TRIP_1.id) } returns TRIP_1
        every { tripsRepository.save(tripEq(expected)) } returns expected

        tripsService.stopGatheringTrip(TEST_USER.email, TRIP_1.id)

        verify(exactly = 1) { tripsRepository.save(tripEq(expected)) }
    }

    @Test
    fun `when guest tries to stop gathering trip, exception is thrown`() {
        every { tripsRepository.getById(TRIP_1.id) } returns TRIP_1

        assertThrows<IllegalAccessException> {
            tripsService.stopGatheringTrip(GUEST_USER.email, TRIP_1.id)
        }
    }

    @Test
    fun `when owner finishes the trip, it's updated correctly`() {
        val expected = with(TRIP_1) {
            Trip(
                id,
                title,
                description,
                latitude,
                longitude,
                suggestedDate,
                ownerContact,
                TripState.FINISHED,
                owner,
                members,
                requirements,
                joinRequests,
                tripImages
            )
        }
        every { tripsRepository.getById(TRIP_1.id) } returns TRIP_1
        every { tripsRepository.save(tripEq(expected)) } returns expected

        tripsService.finishTrip(TEST_USER.email, TRIP_1.id)

        verify(exactly = 1) { tripsRepository.save(tripEq(expected)) }
    }

    @Test
    fun `when guest tries to finish the trip, exception is thrown`() {
        every { tripsRepository.getById(TRIP_1.id) } returns TRIP_1

        assertThrows<IllegalAccessException> {
            tripsService.finishTrip(GUEST_USER.email, TRIP_1.id)
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
        val TRIP_2 = Trip(
            1,
            "title2",
            "description2",
            2.0,
            3.0,
            TEST_DATE,
            "ownerContact2",
            TripState.GATHERING,
            TEST_USER,
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
        val TRIP_3 = Trip(
            2,
            "title3",
            "description3",
            3.0,
            4.0,
            TEST_DATE,
            "ownerContact3",
            TripState.GATHERING,
            TEST_USER,
            emptyList(),
            emptyList(),
            emptyList(),
            emptyList()
        )
        val EQUIPMENT_REQUIREMENT = EquipmentRequirement(1, "requirement", TRIP_1)
        val JOIN_REQUEST = JoinRequest(
            1,
            LocalDateTime.of(2022, 1, 1, 1, 1),
            "message",
            listOf(EQUIPMENT_REQUIREMENT),
            JoinRequestState.PENDING,
            "contact",
            TEST_USER,
            TRIP_1,
            null
        )
        val IMAGE_REF = "image_reference"
    }
}