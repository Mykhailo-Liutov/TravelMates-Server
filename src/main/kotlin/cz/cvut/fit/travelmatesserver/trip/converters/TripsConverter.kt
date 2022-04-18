package cz.cvut.fit.travelmatesserver.trip.converters

import cz.cvut.fit.travelmatesserver.trip.join.JoinRequest
import cz.cvut.fit.travelmatesserver.trip.join.JoinRequestState
import cz.cvut.fit.travelmatesserver.trip.models.*
import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip

class TripsConverter {

    fun entityToDto(trip: Trip, userEmail: String): TripDto {
        val requirements = trip.requirements.map { requirement ->
            RequirementDto(
                requirement.id,
                requirement.name,
                trip.members.any { it.providedEquipment.contains(requirement) })
        }
        val ownerMember = with(trip.owner) {
            MemberDto(email, picture, name, trip.ownerContact, emptyList())
        }
        val members = trip.members.map {
            val user = it.memberUser
            val equipment = it.providedEquipment.map {
                RequirementDto(it.id, it.name, true)
            }
            MemberDto(user.email, user.picture, user.name, it.contact, equipment)
        }
        val userType = when {
            ownerMember.email == userEmail -> UserType.OWNER
            members.any { it.email == userEmail } -> UserType.MEMBER
            else -> UserType.GUEST
        }
        val currentUserRequest = trip.joinRequests.firstOrNull { it.sender.email == userEmail }
        val otherJoinRequests = trip.joinRequests
            .filterNot { it.sender.email == userEmail }
            .filter { it.state == JoinRequestState.PENDING }

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

    private fun convertRequest(joinRequest: JoinRequest): RequestDto {
        with(joinRequest) {
            return RequestDto(id, PublicUserDto(sender.name, sender.picture), providedEquipment.map {
                RequirementDto(it.id, it.name, true)
            }, contact, message, state, rejectionReason)
        }
    }

}