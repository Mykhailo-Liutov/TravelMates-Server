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

    /**
     * Creates a new trip
     * @param newTrip a trip to create
     * @param userEmail email of user who is trying to create the trip
     */
    fun createTrip(newTrip: NewTripDto, userEmail: String) {
        val ownerRef = userRepository.getById(userEmail)
        //Create trip entity from given DTO
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
                emptyList(),
                emptyList()
            )
        }
        //Save the new trip
        val savedEntity = tripsRepository.save(tripEntity)
        //Create requirements entities from DTO-s
        val requirements = newTrip.requirements.map {
            EquipmentRequirement(0, it.name, savedEntity)
        }
        //Save the requirements
        equipmentRepository.saveAll(requirements)
    }

    /**
     * @param userEmail email of user, who sent the request
     * @param filter filter, based on which trips are selected
     * @return a list of trips based on given filter
     */
    fun getTrips(userEmail: String, filter: TripsFilter): List<TripDto> {
        return when (filter) {
            TripsFilter.EXPLORE -> getExploreTrips(userEmail)
            TripsFilter.MY_TRIPS -> getMyTrips(userEmail)
            TripsFilter.UNKNOWN -> getAllTrips(userEmail)
        }
    }

    /**
     * Finds a trip by id
     * @param userEmail email of user, who sent the request
     * @param tripId id of trip to find
     */
    fun getTripDetails(tripId: Long, userEmail: String): TripDto {
        val trip = tripsRepository.findTripById(tripId)
        return tripsConverter.entityToDto(trip, userEmail)
    }

    /**
     * Sends a join request for given trip
     * @param userEmail email of sender
     * @param requestDto a request to send
     * @param tripId id of trip, to which the request should be sent
     */
    fun sendJoinRequest(userEmail: String, tripId: Long, requestDto: CreateJoinRequestDto) {
        joinRequestRepository.findRequest(userEmail, tripId)?.let {
            //Delete existing request if present
            joinRequestRepository.deleteById(it.id)
        }
        //Find references to equipment requirements
        val equipmentRefs = requestDto.providedEquipmentIds.map {
            equipmentRepository.getById(it)
        }
        //Find reference to sender
        val senderRef = userRepository.getById(userEmail)
        //Find reference to existing trip
        val tripRef = tripsRepository.getById(tripId)
        //Create the join request entity
        val joinRequest = JoinRequest(
            0,
            LocalDateTime.now(),
            requestDto.message,
            equipmentRefs,
            JoinRequestState.PENDING,
            requestDto.contact,
            senderRef,
            tripRef,
            rejectionReason = null
        )
        //Save the join request
        joinRequestRepository.save(joinRequest)
    }

    /**
     * Upload an image for a trip
     * @param userEmail email of user who sent the request
     * @param imageRef reference to image which should be saved
     * @param tripId id of trip, for which image should be saved
     */
    fun uploadImage(userEmail: String, tripId: Long, imageRef: String) {
        val existingTrip = tripsRepository.findTripById(tripId)
        //Only owner or member is allowed to upload images, ensure that
        if (existingTrip.owner.email != userEmail && existingTrip.members.none { it.memberUser.email == userEmail }) {
            throw IllegalAccessException("User is not a member or owner of this trip.")
        }
        //Append the new image to existing ones
        val newImages = existingTrip.tripImages + imageRef
        //Create and updated entity
        val newTrip = with(existingTrip) {
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
                newImages
            )
        }
        //Save the new trip
        tripsRepository.save(newTrip)
    }

    /**
     * Updated the state of given trip to GATHERED
     * @param tripId id of trip, which should be updated
     * @param userEmail email of user who sent the request
     */
    fun stopGatheringTrip(userEmail: String, tripId: Long) {
        updateTripState(userEmail, tripId, TripState.GATHERED)
    }

    /**
     * Updated the state of given trip to FINISHED
     * @param tripId id of trip, which should be updated
     * @param userEmail email of user who sent the request
     */
    fun finishTrip(userEmail: String, tripId: Long) {
        updateTripState(userEmail, tripId, TripState.FINISHED)
    }

    /**
     * Updates the state of a given trip
     * @param targetState state, to which trip should be updated
     * @param tripId id of trip, which should be updated
     * @param userEmail email of user who sent the request
     */
    private fun updateTripState(userEmail: String, tripId: Long, targetState: TripState) {
        val trip = tripsRepository.getById(tripId)
        //Only owner can update the state of the trip, ensure that
        if (trip.owner.email != userEmail) {
            throw IllegalAccessException("Only trip owner can change trip's state")
        }
        //Create the updated entity
        val newTrip = with(trip) {
            Trip(
                id,
                title,
                description,
                latitude,
                longitude,
                suggestedDate,
                ownerContact,
                targetState,
                owner,
                members,
                requirements,
                joinRequests,
                tripImages
            )
        }
        //Save the entity
        tripsRepository.save(newTrip)
    }

    /**
     * Get all trips without filters
     */
    private fun getAllTrips(userEmail: String): List<TripDto> {
        val trips = tripsRepository.findAll()
        return trips.map {
            tripsConverter.entityToDto(it, userEmail)
        }
    }

    /**
     * Get all trips with "my trips" filter
     */
    private fun getMyTrips(userEmail: String): List<TripDto> {
        val myTrips = tripsRepository.findMyTrips(userEmail)
        return myTrips.map {
            tripsConverter.entityToDto(it, userEmail)
        }
    }

    /**
     * Get all trips with "explore" filter
     */
    private fun getExploreTrips(userEmail: String): List<TripDto> {
        val exploreTrips = tripsRepository.findExploreTrips(userEmail)
        return exploreTrips.map {
            tripsConverter.entityToDto(it, userEmail)
        }
    }

}