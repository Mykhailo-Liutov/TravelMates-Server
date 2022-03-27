package cz.cvut.fit.travelmatesserver.trip

import cz.cvut.fit.travelmatesserver.trip.converters.TripsConverter
import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRepository
import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRequirement
import cz.cvut.fit.travelmatesserver.trip.join.CreateJoinRequestDto
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequest
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequestRepository
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequestState
import cz.cvut.fit.travelmatesserver.trip.models.*
import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripState
import cz.cvut.fit.travelmatesserver.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TripsService {

    @Autowired
    lateinit var tripsRepository: TripsRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var equipmentRepository: EquipmentRepository

    @Autowired
    lateinit var joinRequestRepository: JoinRequestRepository

    @Autowired
    lateinit var tripsConverter: TripsConverter

    fun createTrip(newTrip: NewTripDto, userEmail: String) {
        val ownerRef = userRepository.getById(userEmail)
        val tripEntity = with(newTrip) {
            Trip(
                0,
                title,
                description,
                location.lat,
                location.lon,
                suggestedDate,
                ownerContact,
                TripState.GATHERING,
                ownerRef,
                emptyList(),
                emptyList(),
                emptyList()
            )
        }
        val savedEntity = tripsRepository.save(tripEntity)
        val requirements = newTrip.requirements.map {
            EquipmentRequirement(0, it.name, savedEntity)
        }
        equipmentRepository.saveAll(requirements)
    }

    fun getTrips(userEmail: String, filter: TripsFilter): List<TripDto> {
        return when (filter) {
            TripsFilter.EXPLORE -> getExploreTrips(userEmail)
            TripsFilter.MY_TRIPS -> getMyTrips(userEmail)
            TripsFilter.UNKNOWN -> getAllTrips(userEmail)
        }
    }

    fun getTripDetails(tripId: Long, userEmail: String): TripDto {
        val trip = tripsRepository.findTripById(tripId)
        return tripsConverter.entityToDto(trip, userEmail)
    }

    fun sendJoinRequest(userEmail: String, tripId: Long, requestDto: CreateJoinRequestDto) {
        val equipmentRefs = requestDto.providedEquipmentIds.map {
            equipmentRepository.getById(it)
        }
        val senderRef = userRepository.getById(userEmail)
        val tripRef = tripsRepository.getById(tripId)
        val joinRequest = JoinRequest(
            0,
            LocalDateTime.now(),
            requestDto.message,
            equipmentRefs,
            JoinRequestState.PENDING,
            requestDto.contact,
            senderRef,
            tripRef
        )
        joinRequestRepository.save(joinRequest)
    }


    private fun getAllTrips(userEmail: String): List<TripDto> {
        val trips = tripsRepository.findAll()
        return trips.map {
            tripsConverter.entityToDto(it, userEmail)
        }
    }

    private fun getMyTrips(userEmail: String): List<TripDto> {
        val myTrips = tripsRepository.findMyTrips(userEmail)
        return myTrips.map {
            tripsConverter.entityToDto(it, userEmail)
        }
    }

    private fun getExploreTrips(userEmail: String): List<TripDto> {
        val exploreTrips = tripsRepository.findExploreTrips(userEmail)
        return exploreTrips.map {
            tripsConverter.entityToDto(it, userEmail)
        }
    }

}