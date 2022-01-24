package cz.cvut.fit.travelmatesserver.trip.converters

import cz.cvut.fit.travelmatesserver.trip.models.Coordinates
import cz.cvut.fit.travelmatesserver.trip.models.PublicUserDto
import cz.cvut.fit.travelmatesserver.trip.models.RequirementDto
import cz.cvut.fit.travelmatesserver.trip.models.TripDto
import cz.cvut.fit.travelmatesserver.trip.models.entities.Trip

class TripsConverter {

    fun entityToDto(trip: Trip): TripDto {
        with(trip) {
            val location = Coordinates(latitude, longitude)
            return TripDto(
                id,
                title,
                description,
                location,
                requirements.map {
                    RequirementDto(id, it.name, false) //TODO Check fulfillment
                },
                PublicUserDto(owner.name, owner.picture),
                state,
                suggestedDate
            )
        }
    }

}