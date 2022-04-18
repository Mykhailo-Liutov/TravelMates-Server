package cz.cvut.fit.travelmatesserver.trip.converters

import cz.cvut.fit.travelmatesserver.trip.join.JoinRequest
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequestState
import cz.cvut.fit.travelmatesserver.trip.models.*
import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip

/**
 * Converter for trips
 */
class TripsConverter {

    /**
     * Converts given Trip entity into TripDto
     */
    fun entityToDto(trip: Trip, userEmail: String): TripDto {
        //Convert requirements into DTO-s
        val requirements = trip.requirements.map { requirement ->
            RequirementDto(
                requirement.id,
                requirement.name,
                trip.members.any { it.providedEquipment.contains(requirement) })
        }
        //Convert owner into DTO
        val ownerMember = with(trip.owner) {
            MemberDto(email, picture, name, trip.ownerContact, emptyList())
        }
        //Convert Members into DTO-s
        val members = trip.members.map {
            val user = it.memberUser
            val equipment = it.providedEquipment.map {
                RequirementDto(it.id, it.name, true)
            }
            MemberDto(user.email, user.picture, user.name, it.contact, equipment)
        }
        //Decide what is the given user type
        val userType = when {
            ownerMember.email == userEmail -> UserType.OWNER
            members.any { it.email == userEmail } -> UserType.MEMBER
            else -> UserType.GUEST
        }
        //Get possible join request of given user
        val currentUserRequest = trip.joinRequests.firstOrNull { it.sender.email == userEmail }
        //Get other pending join requests
        val otherJoinRequests = trip.joinRequests
            .filterNot { it.sender.email == userEmail }
            .filter { it.state == JoinRequestState.PENDING }

        //Assemble and return the final DTO
        return TripDto(
            trip.id,
            trip.title,
            trip.description,
            trip.suggestedDate,
            trip.state,
            Coordinates(trip.latitude, trip.longitude),
            requirements,
            ownerMember,
            members,
            userType,
            otherJoinRequests.map { convertRequest(it) },
            currentUserRequest?.let { convertRequest(it) },
            trip.tripImages
        )
    }

    /**
     * Converts JoinRequest into RequestDto
     */
    private fun convertRequest(joinRequest: JoinRequest): RequestDto {
        with(joinRequest) {
            return RequestDto(id, PublicUserDto(sender.name, sender.picture), providedEquipment.map {
                //User wants to provide the requirement, so setting isFulfilled to true
                RequirementDto(it.id, it.name, isFulfilled = true)
            }, contact, message, state, rejectionReason)
        }
    }

}