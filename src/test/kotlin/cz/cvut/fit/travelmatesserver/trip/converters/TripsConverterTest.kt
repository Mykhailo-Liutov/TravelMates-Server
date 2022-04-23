package cz.cvut.fit.travelmatesserver.trip.converters

import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRequirement
import cz.cvut.fit.travelmatesserver.trip.models.Coordinates
import cz.cvut.fit.travelmatesserver.trip.models.MemberDto
import cz.cvut.fit.travelmatesserver.trip.models.TripDto
import cz.cvut.fit.travelmatesserver.trip.models.UserType
import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripMember
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripState
import cz.cvut.fit.travelmatesserver.user.models.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime


class TripsConverterTest {

    lateinit var converter: TripsConverter

    @BeforeEach
    fun setup() {
        converter = TripsConverter()
    }

    @Test
    fun `trip where user is guest is converted correctly`() {
        val expected = with(TRIP_1) {
            TripDto(
                id, title, description, suggestedDate, state, Coordinates(latitude, longitude), emptyList(), MemberDto(
                    TEST_USER.email, TEST_USER.picture, TEST_USER.name, ownerContact, emptyList()
                ), emptyList(), UserType.GUEST,
                emptyList(), null, emptyList()
            )
        }

        val actual = converter.entityToDto(TRIP_1, GUEST_USER.email)

        assert(expected == actual)
    }

    @Test
    fun `trip where user is member is converted correctly`() {
        val expected = with(TRIP_2) {
            TripDto(
                id,
                title,
                description,
                suggestedDate,
                state,
                Coordinates(latitude, longitude),
                emptyList(),
                MemberDto(
                    GUEST_USER.email, GUEST_USER.picture, GUEST_USER.name, ownerContact, emptyList()
                ),
                listOf(MemberDto(TEST_USER.email, TEST_USER.picture, TEST_USER.name, "contact", emptyList())),
                UserType.MEMBER,
                emptyList(),
                null,
                emptyList()
            )
        }

        val actual = converter.entityToDto(TRIP_2, TEST_USER.email)

        assert(expected == actual)
    }

    @Test
    fun `trip where user is owner is converted correctly`() {
        val expected = with(TRIP_1) {
            TripDto(
                id, title, description, suggestedDate, state, Coordinates(latitude, longitude), emptyList(), MemberDto(
                    TEST_USER.email, TEST_USER.picture, TEST_USER.name, ownerContact, emptyList()
                ), emptyList(), UserType.OWNER,
                emptyList(), null, emptyList()
            )
        }

        val actual = converter.entityToDto(TRIP_1, TEST_USER.email)

        assert(expected == actual)
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
            GUEST_USER,
            listOf(TripMember(1, LocalDateTime.of(2022, 1, 1, 1, 1), "contact", emptyList(), TRIP_1, TEST_USER)),
            emptyList(),
            emptyList(),
            emptyList()
        )
        val EQUIPMENT_REQUIREMENT = EquipmentRequirement(1, "requirement", TRIP_1)
    }
}