package cz.cvut.fit.travelmatesserver.trip

import cz.cvut.fit.travelmatesserver.trip.converters.TripsConverter
import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRepository
import cz.cvut.fit.travelmatesserver.trip.equipment.EquipmentRequirement
import cz.cvut.fit.travelmatesserver.trip.models.*
import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip
import cz.cvut.fit.travelmatesserver.trip.models.entities.TripState
import cz.cvut.fit.travelmatesserver.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TripsService {

    @Autowired
    lateinit var tripsRepository: TripsRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var equipmentRepository: EquipmentRepository

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
            TripsFilter.UNKNOWN -> getAllTrips()
        }
    }

    private fun getAllTrips(): List<TripDto> {
        val trips = tripsRepository.findAll()
        return trips.map {
            tripsConverter.entityToDto(it)
        }
    }

    private fun getMyTrips(userEmail: String): List<TripDto> {
        val myTrips = tripsRepository.findMyTrips(userEmail)
        return myTrips.map {
            tripsConverter.entityToDto(it)
        }
    }

    private fun getExploreTrips(userEmail: String): List<TripDto> {
        val exploreTrips = tripsRepository.findExploreTrips(userEmail)
        return exploreTrips.map {
            tripsConverter.entityToDto(it)
        }
    }

}